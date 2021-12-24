package de.twiese.twhome.fx.panels;

import de.twiese.twhome.fx.config.Config;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ImagePane extends Pane {

    private static final Logger log = LoggerFactory.getLogger(Config.class);

    public static ImagePane create(String cfgKey) {
        String src = Config.getProperty(cfgKey);
        ImagePane pane = new ImagePane();
        Config.onConfigChangeAndNow(() -> {
            if (src != null && src.length() > 0) {
                pane.setBackground(new Background(new BackgroundImage(new Image(src), BackgroundRepeat.NO_REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                        new BackgroundSize(pane.getWidth(), pane.getHeight(), false, false, true, true))));
            } else {
                log.warn("ImagePane without image?");
            }
        });
        return pane;
    }
}
