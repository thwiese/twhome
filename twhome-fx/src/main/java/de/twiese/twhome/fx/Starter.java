package de.twiese.twhome.fx;

import de.twiese.twhome.fx.config.Config;
import de.twiese.twhome.fx.labels.Label;
import de.twiese.twhome.fx.panels.DatePane;
import de.twiese.twhome.fx.support.ExecutorManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Starter extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        getParameters().getRaw().forEach(System.out::println);
        Config.init(getParameters().getNamed().get("configLocation"));
        Config.onConfigChangeAndNow(() -> stage.setTitle(Config.getProperty("title")));
        stage.setMaximized(Config.getBooleanProperty("maximizeOnStartup"));
        GridPane pane = new GridPane();
        pane.setStyle("-fx-background-color: #000000; -fx-hgap: 10px; -fx-vgap: 10px");
        pane.add(Label.build("message", "frontColor1", "fontSizeMedium"), 0, 2);
        pane.add(DatePane.create(), 0, 0);
        Scene scene = new Scene(pane);
        stage.setScene(scene);
        stage.show();


    }

    @Override
    public void stop() throws Exception {
        ExecutorManager.shutDownAllExecutors();
        super.stop();
    }

}
