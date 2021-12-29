package de.twiese.twhome.fx;

import de.twiese.twhome.fx.config.Config;
import de.twiese.twhome.fx.labels.Label;
import de.twiese.twhome.fx.panels.DatePane;
import de.twiese.twhome.fx.panels.ImagePane;
import de.twiese.twhome.fx.support.ExecutorManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
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
        });
        stage.setMaximized(Config.getBooleanProperty("maximizeOnStartup"));

        pane.add(Label.build("message", "frontColor1", "fontSizeBig"), 0, 2);
        pane.add(DatePane.create(), 0, 0);

        ImagePane backgroundImagePane = ImagePane.createFromConfig("backgroundImage");
        Scene scene = new Scene(stackedLayers(backgroundImagePane, pane));
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void stop() throws Exception {
        ExecutorManager.shutDownAllExecutors();
        super.stop();
    }

    public StackPane stackedLayers(Pane... panes) {
        StackPane stackPane = new StackPane();
        for (Pane p : panes) {
            Config.onConfigChangeAndNow(() -> {
                if (!(p instanceof ImagePane)) {
                    p.setStyle(Config.getProperty("stackedPaneStyle"));
                }
            });
        }
        stackPane.getChildren().addAll(panes);
        return stackPane;
    }

}
