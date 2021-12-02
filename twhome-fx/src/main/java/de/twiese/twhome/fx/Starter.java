package de.twiese.twhome.fx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Starter extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        log.info("Starting FX app ...");
        stage.setTitle("TW Home");
        Pane pane = new StackPane();
        Label label = new Label("Guten Morgen,  Familie Wiese");
        pane.getChildren().add(label);
        Scene scene = new Scene(pane,800, 600);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        log.info("Stopping FX app ...");
        super.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
