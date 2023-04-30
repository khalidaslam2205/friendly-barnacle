package com.example.testing48123;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class ProfileViewerController {

    public void handleBackButtonAction() {
        SceneControl.primaryStage.setScene(SceneControl.mainMenuScene);
    }

    @FXML
    private TextField runnerTxt;

    @FXML
    private TextField firstTxt;

    @FXML
    private TextField lastTxt;

    @FXML
    private TextField ageTxt;

    @FXML
    private TextField genderTxt;

    @FXML
    private TextField heightTxt;

    @FXML
    private TextField weightTxt;

    @FXML
    private Button searchBtn;

    @FXML
    private Button clearBtn;

    @FXML
    private void searchButtonAction() {
        String runnerId = runnerTxt.getText().trim();
        if (!runnerId.isEmpty()) {
            try {
                int id = Integer.parseInt(runnerId);
                BufferedReader reader = new BufferedReader(new FileReader("profiles.csv"));
                String line;
                boolean found = false;
                while ((line = reader.readLine()) != null) {
                    String[] fields = line.split(",");
                    if (fields[0].equals(runnerId)) {
                        firstTxt.setText(fields[1]);
                        lastTxt.setText(fields[2]);
                        ageTxt.setText(fields[3]);
                        heightTxt.setText(fields[4]);
                        genderTxt.setText(fields[5]);
                        weightTxt.setText(fields[6]);
                        found = true;
                        break;
                    }
                }
                reader.close();
                if (!found) {
                    SceneControl.showErrorMessage("Runner ID not found.");
                }
            } catch (NumberFormatException e) {
                SceneControl.showErrorMessage("ID must be a positive integer.");
            } catch (IOException e) {
                System.err.println("Error reading CSV file: " + e.getMessage());
            }
        } else {
            SceneControl.showErrorMessage("ID is required.");
        }
    }


    @FXML
    private void clearButtonAction() {
        runnerTxt.setText("");
        firstTxt.setText("");
        lastTxt.setText("");
        ageTxt.setText("");
        genderTxt.setText("");
        heightTxt.setText("");
        weightTxt.setText("");
    }
}