package de.twiese.twhome.fx;

import de.twiese.twhome.fx.config.Config;
import de.twiese.twhome.fx.labels.Label;
import de.twiese.twhome.fx.panels.DatePane;
import de.twiese.twhome.fx.support.ExecutorManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class Starter extends Application {


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        GridPane pane = new GridPane();
        Config.init(getParameters().getNamed().get("configLocation"));
        Config.onConfigChangeAndNow(() -> {
            stage.setTitle(Config.getProperty("title"));
            pane.setMinWidth(Config.getIntProperty("windowWidth"));
            pane.setMinHeight(Config.getIntProperty("windowHeight"));
            //pane.setStyle("-fx-background-color: " + Config.getProperty("backColor1") + ";");
            String bckgrnd = Config.getProperty("backgroundImage");
            if (bckgrnd != null && bckgrnd.length() > 0) {
                pane.setBackground(new Background(new BackgroundImage(new Image(bckgrnd), BackgroundRepeat.REPEAT,
                        BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT,
                        BackgroundSize.DEFAULT )));
            }
        });
        stage.setMaximized(Config.getBooleanProperty("maximizeOnStartup"));

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
