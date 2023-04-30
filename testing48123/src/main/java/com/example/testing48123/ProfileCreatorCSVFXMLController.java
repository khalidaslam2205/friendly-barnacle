package com.example.testing48123;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;

import java.io.FileWriter;
import java.io.IOException;

public class ProfileCreatorCSVFXMLController {

    @FXML
    private TextField idField, firstNameField, lastNameField, ageField, heightField, weightField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private ComboBox<String> genderComboBox;

    @FXML
    private void handleSubmitButtonAction() {
        // Validate input
        String errorMessage = "";
        int id = 0, age = 0, height = 0, weight = 0;
        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String gender = genderComboBox.getValue();
        String password = passwordField.getText();

        if (firstName.isEmpty()) {
            errorMessage += "First name is required.\n";
        }

        if (lastName.isEmpty()) {
            errorMessage += "Last name is required.\n";
        }

        if (password.isEmpty()) {
            errorMessage += "Password is required.\n";
        }

        try {
            id = Integer.parseInt(idField.getText());
            if (id <= 0) {
                errorMessage += "ID must be a positive integer.\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "ID must be a positive integer.\n";
        }

        try {
            age = Integer.parseInt(ageField.getText());
            if (age <= 0 || age > 150) {
                errorMessage += "Age must be between 1 and 150.\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Age must be a positive integer.\n";
        }

        try {
            height = Integer.parseInt(heightField.getText());
            if (height <= 0 || height > 300) {
                errorMessage += "Height must be between 1 and 300.\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Height must be a positive integer.\n";
        }

        try {
            weight = Integer.parseInt(weightField.getText());
            if (weight <= 0 || weight > 1000) {
                errorMessage += "Weight must be between 1 and 1000.\n";
            }
        } catch (NumberFormatException e) {
            errorMessage += "Weight must be a positive integer.\n";
        }

        if (gender == null) {
            errorMessage += "Gender is required.\n";
        }

        if (!errorMessage.isEmpty()) {
            // Show error message if there are validation errors
            SceneControl.showErrorMessage(errorMessage);
            return;
        }

        // Save profile to CSV file
        try {
            FileWriter writer = new FileWriter("profiles.csv", true);
            writer.write(id + "," + firstName + "," + lastName + "," + age + "," + height + "," + gender + "," + weight + "," + password + "\n");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Clear input fields
        idField.setText("");
        firstNameField.setText("");
        lastNameField.setText("");
        ageField.setText("");
        heightField.setText("");
        genderComboBox.setValue(null);
        weightField.setText("");
        passwordField.setText("");
    }

    public void handleBackButtonAction() {
        SceneControl.primaryStage.setScene(SceneControl.mainMenuScene);
    }
}
