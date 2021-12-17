package de.twiese.twhome.fx.panels;

import de.twiese.twhome.fx.config.Config;
import de.twiese.twhome.fx.labels.Label;
import de.twiese.twhome.fx.support.ExecutorManager;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

public class DatePane extends GridPane {

    private final Label dateLabel = Label.build("dummyText", "frontColor1", "fontSizeSmall");
    private final Label timeLabel = Label.build("dummyText", "frontColor1", "fontSizeMedium");
    private String datePattern;
    private String timePattern;

    public static DatePane create() {
        DatePane pane = new DatePane();
        Config.onConfigChangeAndNow(() -> {
            pane.datePattern = Config.getProperty("longDatePattern");
            pane.timePattern = Config.getProperty("timePattern");

        });
        pane.add(pane.dateLabel, 0, 1);
        pane.add(pane.timeLabel, 0, 2);
        pane.refresh();
        ExecutorManager.createSchedulerDaemon().scheduleAtFixedRate(pane::refresh, 1, 1, TimeUnit.SECONDS);
        return pane;
    }

    public void refresh() {
        Platform.runLater(() -> {
            LocalDateTime datetime = LocalDateTime.now();
            dateLabel.setText(datetime.format(DateTimeFormatter.ofPattern(datePattern)));
            timeLabel.setText(datetime.format(DateTimeFormatter.ofPattern(timePattern)));
        });
    }

}
