package de.twiese.twhome.fx.labels;

import javafx.scene.paint.Color;

public class Label extends javafx.scene.control.Label {

    private Label(String text) {
        super(text);
    }

    public static Label build(String text, Color color, int fontSize) {
        Label label = new Label(text);
        label.setTextFill(color);
        label.setStyle("-fx-font-size: " + fontSize + "px");
        return label;
    }
}
