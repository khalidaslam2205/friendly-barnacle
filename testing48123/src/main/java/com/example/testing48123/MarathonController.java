//package com.example.testing48123;
//
//import javafx.fxml.FXML;
//import javafx.scene.control.Label;
//import javafx.scene.control.TextField;
//import javafx.util.Duration;
//import javafx.animation.Timeline;
//import javafx.animation.KeyFrame;
//import java.time.LocalTime;
//import java.time.format.DateTimeFormatter;
//
//public class MarathonController {
//
//    private LocalTime startTime;
//    private LocalTime checkpointTime;
//
//    @FXML
//    private TextField runnerIdField;
//
//    @FXML
//    private Label checkpointTimeLabel;
//
//    @FXML
//    private Label marathonTimeLabel;
//
//    private Timeline timeline;
//
//    public MarathonController() {
//        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> updateTime()));
//        timeline.setCycleCount(Timeline.INDEFINITE);
//        timeline.play();
//    }
//
//    public void submitCheckpoint() {
//        String runnerId = runnerIdField.getText();
//        if (runnerId != null && !runnerId.trim().isEmpty()) {
//            checkpointTime = LocalTime.now();
//            checkpointTimeLabel.setText(checkpointTime.format(DateTimeFormatter.ISO_TIME));
//            if (startTime == null) {
//                startTime = checkpointTime;
//            }
//        }
//    }
//
//    private void updateTime() {
//        if (startTime != null) {
//            LocalTime currentTime = LocalTime.now();
//            java.time.Duration duration = java.time.Duration.between(startTime, currentTime);
//            marathonTimeLabel.setText(formatDuration(duration));
//        }
//    }
//
//
//    private String formatDuration(Duration duration) {
//        long seconds = (long) duration.toSeconds();
//        long minutes = seconds / 60;
//        long hours = minutes / 60;
//        minutes %= 60;
//        seconds %= 60;
//        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
//    }
//
//}
