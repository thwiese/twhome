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
        ImagePane pane = new ImagePane();
        pane.imageSwitchInterval = Config.getIntProperty(cfgKeySwitchPeriod);
        Config.onConfigChangeAndNow(() -> pane.refreshConfig(cfgKeyImagePath, cfgKeySwitchPeriod));
        return pane;
    }

    private void refreshConfig(String cfgKeyImagePath, String cfgKeySwitchPeriod) {
        this.imageSwitchInterval = Config.getIntProperty(cfgKeySwitchPeriod);
        this.refresh(Config.getProperty(cfgKeyImagePath));
    }

    private void refresh(String fileName) {
        resolveImages(fileName);
        scheduler.shutdownNow();
        scheduler = ExecutorManager.createScheduler();
        scheduler.scheduleWithFixedDelay(() -> {
            index = index >= imageUris.size() ? 0 : index;
            URI imageUri = imageUris.get(index);
            log.debug("setting background image {}:{}", index, imageUri);
            try {
                this.setBackground(new Background(
                        new BackgroundImage(ImageTool.convertImage(ImageTool.getScaledImage(imageUri, (int) this.getWidth())),
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundRepeat.NO_REPEAT,
                                BackgroundPosition.CENTER,
                                new BackgroundSize(this.getWidth(), this.getHeight(), false,
                                        false, true, true))));
            } catch (IOException e) {
                log.error("setting background for image {}: failed: {}", imageUri, e.getMessage());
            }
            index++;
        }, 10, imageSwitchInterval, TimeUnit.SECONDS);
        log.info("created image pane with refresh rate {} secs", imageSwitchInterval);
    }

    private void resolveImages(String fileName) {
        Path filePath = Paths.get(fileName);
        if (Files.isDirectory(filePath)) {
            try {
                imageUris = Files.list(filePath).map(Path::toUri).collect(Collectors.toList());
                log.info("found {} files in directory {}", imageUris.size(), filePath);
            } catch (IOException e) {
                log.error("reading directory " + filePath + " failed", e);
            }
        } else {
            imageUris = List.of(filePath.toUri());
        }
    }


}
