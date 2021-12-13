package de.twiese.twhome.fx.panels;

import de.twiese.twhome.fx.labels.Label;
import javafx.application.Platform;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DatePane extends GridPane {

    private static final ScheduledExecutorService SCHEDULER = Executors.newSingleThreadScheduledExecutor();
    private final Label dateLabel = Label.build("...", Color.WHITE, 20);
    private final Label timeLabel = Label.build("...", Color.WHITE, 40);

    public static DatePane build() {
        DatePane pane = new DatePane();
        pane.dateLabel.setStyle("-fx-font-size: 20px;");
        pane.timeLabel.setStyle("-fx-font-size: 36px;");
        pane.add(pane.dateLabel, 0, 1);
        pane.add(pane.timeLabel, 0, 2);
        pane.refresh();
        SCHEDULER.scheduleAtFixedRate(pane::refresh, 0, 1, TimeUnit.SECONDS);
        return pane;
    }

    public void refresh() {
        Platform.runLater(() -> {
            LocalDateTime datetime = LocalDateTime.now();
            dateLabel.setText(datetime.format(DateTimeFormatter.ofPattern("'Heute ist 'EEEE', der 'dd. MMMM yyyy")));
            timeLabel.setText(datetime.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        });
    }
}
