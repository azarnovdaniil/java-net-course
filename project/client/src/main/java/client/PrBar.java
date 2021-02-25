package client;

import javafx.concurrent.Task;
import javafx.stage.Stage;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;

public class PrBar extends Stage {

    public static int countProgress = 0;
    public static int countParts = 1;


    public PrBar() {
        Label label = new Label("Load file");

        Label statusLabel = new Label();
        statusLabel.setMinWidth(250);
        statusLabel.setTextFill(Color.BLUE);

        ProgressBar progressBar = new ProgressBar(0);
        progressBar.setProgress(0);
        progressBar.setMinWidth(270);
        progressBar.progressProperty().unbind();
        Task<Void> task = new Task<>() {
            @Override
            public Void call() {
                do {
                    updateProgress(countProgress, countParts);
                }
                while (countProgress / countParts != 1);
                updateProgress(countProgress, countParts);
                countProgress = 0;
                countParts = 1;
                return null;
            }
        };
        progressBar.progressProperty().bind(task.progressProperty());

        ProgressIndicator progressIndicator = new ProgressIndicator(0);
        progressIndicator.setProgress(0);
        progressIndicator.progressProperty().unbind();
        progressIndicator.progressProperty().bind(task.progressProperty());

        statusLabel.setText("Loading... ");
        // When completed tasks
        task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                new EventHandler<WorkerStateEvent>() {

                    @Override
                    public void handle(WorkerStateEvent t) {

                        statusLabel.setText("Loading complete!!! ");
                    }
                });

        new Thread(task).start();

        FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);

        root.getChildren().addAll(label, progressBar, progressIndicator, statusLabel);

        Scene scene = new Scene(root, 400, 80, Color.WHITE);
        setTitle("The progress of the copy");
        setScene(scene);
    }
}
