package com.example.testing48123;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class RetirementController {

    @FXML
    private TextField runnerIdField;

    @FXML
    private TextField retirementReasonField;

    @FXML
    private TextField responsiblePersonField;

    @FXML
    private Button saveRetirementButton;

    @FXML
    public void initialize() {
        saveRetirementButton.setOnAction(e -> {
            String runnerId = runnerIdField.getText();
            String reason = retirementReasonField.getText();
            String responsiblePerson = responsiblePersonField.getText();
            LocalDateTime retirementTime = LocalDateTime.now();

            try {
                saveRetirement(runnerId, reason, responsiblePerson, retirementTime);
            } catch (IOException ex) {
                System.err.println("Error saving retirement: " + ex.getMessage());
            }
        });
    }

    private void saveRetirement(String runnerId, String reason, String responsiblePerson, LocalDateTime retirementTime) throws IOException {
        Path filePath = Paths.get("retirements.csv");
        String newLine = runnerId + "," + reason + "," + responsiblePerson + "," + retirementTime;

        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
        lines.add(newLine);
        Files.write(filePath, lines, StandardCharsets.UTF_8);
    }
}
