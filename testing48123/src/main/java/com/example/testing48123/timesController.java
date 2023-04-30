package com.example.testing48123;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class timesController {

    @FXML
    private TextField runnerTxt;

    @FXML
    private VBox timesVBox;

    @FXML
    private Button searchBtn;

    @FXML
    private void handleBackButtonAction() {
        SceneControl.primaryStage.setScene(SceneControl.mainMenuScene);
    }


    @FXML
    private void searchButtonAction() {
        String runnerId = runnerTxt.getText().trim();
        timesVBox.getChildren().clear();

        if (!runnerId.isEmpty()) {
            try {
                BufferedReader reader = new BufferedReader(new FileReader("times.csv"));
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (fields[0].equals(runnerId)) {
                        for (int i = 1; i < fields.length; i++) {
                            Label timeLabel = new Label(fields[i]);
                            timesVBox.getChildren().add(timeLabel);
                        }
                        break;
                    }
                }
                reader.close();
            } catch (IOException e) {
                System.err.println("Error reading CSV file: " + e.getMessage());
            }
        }
    }

}