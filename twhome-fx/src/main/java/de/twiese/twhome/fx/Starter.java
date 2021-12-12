package de.twiese.twhome.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class Starter extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("TW Home");
        stage.setMaximized(true);
        Pane pane = new StackPane();
        pane.setStyle("-fx-background-color: #000000;");

        Label label = new Label("Guten Morgen,  Familie Wiese");
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 64px");

        pane.getChildren().add(label);
        Scene scene = new Scene(pane,800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
