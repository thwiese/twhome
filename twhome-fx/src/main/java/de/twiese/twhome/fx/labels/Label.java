package de.twiese.twhome.fx.labels;

import de.twiese.twhome.fx.config.Config;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Label extends javafx.scene.control.Label {

    private static final Logger log = LoggerFactory.getLogger(Label.class);

    private Label(String text) {
        super(text);
    }

    public static Label build(String text, Color color, int fontSize) {
        Label label = new Label(text);
        label.setTextFill(color);
        label.setStyle("-fx-font-size: " + fontSize + "px");
        return label;
    }

    public static Label build(String textKey, String colorKey, String fontSizeKey) {
        var label = build(Config.getProperty(textKey), Config.getColorProperty(colorKey), Config.getIntProperty(fontSizeKey));
        Config.onConfigChange(() -> {
            label.setText(Config.getProperty(textKey));
            label.setStyle("-fx-font-size: " + Config.getIntProperty(fontSizeKey) + "px");
            label.setTextFill(Config.getColorProperty(colorKey));
        });
        return label;
    }
}
