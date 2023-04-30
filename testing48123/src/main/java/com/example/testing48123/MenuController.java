package com.example.testing48123;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.time.LocalTime;
import java.time.Duration;
import java.time.format.DateTimeFormatter;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;



public class MenuController {

    @FXML
    public void createProfile() throws IOException {
        SceneControl.primaryStage.setScene(SceneControl.profileMenu);
        SceneControl.primaryStage.setTitle("Profile Creation");
    }

    public void viewProfile() throws IOException {
        SceneControl.primaryStage.setScene(SceneControl.viewProfile);
        SceneControl.primaryStage.setTitle("View Profile");
    }

    public void viewTimes() throws IOException {
        SceneControl.primaryStage.setScene(SceneControl.viewTimes);
        SceneControl.primaryStage.setTitle("View Times");
    }

    private void startTimer(ClockController clockController) {
        LocalTime startTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        Timeline timeline = new Timeline(new KeyFrame(javafx.util.Duration.ZERO, e -> {
            LocalTime currentTime = LocalTime.now();
            LocalTime elapsedTime = LocalTime.ofNanoOfDay(Duration.between(startTime, currentTime).toNanos());
            clockController.setClockTime(elapsedTime.format(formatter));
        }), new KeyFrame(javafx.util.Duration.seconds(1)));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    @FXML
    void startClock(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Clock.fxml"));
        Parent root = loader.load();
        ClockController clockController = loader.getController();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();

        startTimer(clockController);
    }
}

