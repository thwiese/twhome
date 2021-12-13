package de.twiese.twhome.fx;

import de.twiese.twhome.fx.labels.Label;
import de.twiese.twhome.fx.panels.DatePane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.TimeZone;

public class Starter extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
        Locale.setDefault(Locale.GERMANY);

        stage.setTitle("TW Home");
        stage.setMaximized(true);
        GridPane pane = new GridPane();
        pane.setStyle("-fx-background-color: #000000; -fx-hgap: 10px; -fx-vgap: 10px");

        pane.add(Label.build("Ich wünsche einen schönen Tag und", Color.WHITE, 64), 0, 2);
        pane.add(Label.build("viel Erfolg bei der Geo-Arbeit!", Color.WHITE, 64), 0, 3);

        pane.add(DatePane.build(), 0, 0);

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }

}
