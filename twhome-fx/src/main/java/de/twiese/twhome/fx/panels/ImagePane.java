package de.twiese.twhome.fx.panels;

import de.twiese.twhome.fx.config.Config;
import de.twiese.twhome.fx.support.ExecutorManager;
import de.twiese.twhome.fx.support.ImageTool;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class ImagePane extends Pane {

    private static final Logger log = LoggerFactory.getLogger(ImagePane.class);

    private List<URI> imageUris;
    private int index = 0;
    private ScheduledExecutorService scheduler = ExecutorManager.createScheduler();
    private int imageSwitchInterval = 10;

    public static ImagePane createFromConfig(String cfgKeyImagePath, String cfgKeySwitchPeriod) {
        String src = Config.getProperty(cfgKeyImagePath);
        ImagePane pane = createFromFile(src);
        pane.imageSwitchInterval = Config.getIntProperty(cfgKeySwitchPeriod);
        Config.onConfigChange(() -> pane.refresh(src));
        return pane;
    }

    public static ImagePane createFromFile(String fileName) {
        ImagePane pane = new ImagePane();
        pane.refresh(fileName);
        return pane;
    }

    private void refresh(String fileName) {
        resolveImages(fileName);
        scheduler.shutdownNow();
        scheduler = ExecutorManager.createScheduler();
        scheduler.scheduleWithFixedDelay(() -> {
            index = index >= imageUris.size() ? 0 : index;
            URI imageUri = imageUris.get(index);
            //Platform.runLater(() -> {
            try {
                this.setBackground(new Background(
                        new BackgroundImage(ImageTool.convertImage(ImageTool.getScaledImage(imageUri, (int) this.getWidth())),
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(this.getWidth(), this.getHeight(), false,
                                        false, true, true))));
            } catch (IOException e) {
                log.error("", e);
            }
            //});
            index++;
        }, 10, imageSwitchInterval, TimeUnit.SECONDS);

    }

    private void resolveImages(String fileName) {
        Path filePath = Paths.get(fileName);
        if (Files.isDirectory(filePath)) {
            try {
                imageUris = Files.list(filePath).map(Path::toUri).collect(Collectors.toList());
            } catch (IOException e) {
                log.error("", e);
            }
        } else {
            imageUris = List.of(filePath.toUri());
        }
    }


}
