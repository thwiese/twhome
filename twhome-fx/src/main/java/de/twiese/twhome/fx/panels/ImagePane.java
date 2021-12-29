package de.twiese.twhome.fx.panels;

import de.twiese.twhome.fx.config.Config;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

public class ImagePane extends Pane {

    private static final Logger log = LoggerFactory.getLogger(ImagePane.class);

    private List<URI> imageUris;

    public static ImagePane createFromConfig(String cfgKey) {
        String src = Config.getProperty(cfgKey);
        ImagePane pane = createFromFile(src);
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
        if (fileName != null && fileName.length() > 0) {
            URI imageUri = imageUris.get(0);
            log.info("Loading image {}", imageUri);
            this.setBackground(new Background(
                    new BackgroundImage(new Image(imageUri.toString()),
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundRepeat.NO_REPEAT,
                            BackgroundPosition.CENTER,
                            new BackgroundSize(this.getWidth(), this.getHeight(), false,
                                    false, true, true))));
        } else {
            log.warn("ImagePane without image?");
        }
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
