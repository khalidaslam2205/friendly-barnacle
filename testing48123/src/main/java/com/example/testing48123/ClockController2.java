package com.example.testing48123;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import javafx.scene.control.Alert.AlertType;
import java.util.Timer;
import java.util.TimerTask;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;



public class ClockController2 {

    @FXML
    private Label clockLabel;

    @FXML
    private TextField runnerIdField;

    @FXML
    private Button saveTimeButton;

    @FXML
    private Button viewRetirementButton;


    @FXML
    private Label checkpointLabel;

    @FXML
    private Button reportRetirementButton;

    private HashMap<String, Integer> runnerCheckpoints = new HashMap<>();
    private HashMap<String, String> expectedCheckpointTimes = new HashMap<>();
    private Set<String> finishedRunners = new HashSet<>();
    private HashMap<String, List<String>> runnerExpectedCheckpointTimes = new HashMap<>();


    public void setClockTime(String time) {
        clockLabel.setText(time);
    }

    @FXML
    public void initialize() throws IOException {

        reportRetirementButton.setOnAction(e -> {
            try {
                showRetirementWindow();
            } catch (IOException ex) {
                System.err.println("Error opening retirement window: " + ex.getMessage());
            }
        });

        viewRetirementButton.setOnAction(e -> showRetirementInfo());


        saveTimeButton.setOnAction(e -> {
            String runnerId = runnerIdField.getText();
            if (finishedRunners.contains(runnerId)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Finished Runner");
                alert.setHeaderText(null);
                alert.setContentText("This runner has already finished the race.");
                alert.showAndWait();
                runnerIdField.setText("");
            } else {
                int checkpoint = runnerCheckpoints.getOrDefault(runnerId, 1);
                checkpointLabel.setText("Checkpoint: " + checkpoint);

                String time = clockLabel.getText();
                try {
                    saveTime(runnerId, time, checkpoint);
                    if (checkpoint == 10) {
                        saveFinalTime(runnerId, time);
                        finishedRunners.add(runnerId);
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Race Finished");
                        alert.setHeaderText(null);
                        alert.setContentText("Runner " + runnerId + " has finished the race.");
                        alert.showAndWait();
                        runnerIdField.setText("");
                    }
                } catch (IOException ex) {
                    System.err.println("Error saving time: " + ex.getMessage());
                }
            }
        });

        try {
            loadExpectedCheckpointTimes();
        } catch (IOException ex) {
            System.err.println("Error loading expected checkpoint times: " + ex.getMessage());
        }

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                checkRunnersExpectedTimes();
            }
        }, 0, 1000); // Check every 1 second

        calculateAndPrintAverageTimes();
    }

    //expected checkpoints only works until checkpoint 1 time is reached and then it stops working

    private void loadExpectedCheckpointTimes() throws IOException {
        Path filePath = Paths.get("times.csv");
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        for (String line : lines) {
            if (line.isEmpty()) {
                continue;
            }
            String[] values = line.split(",");
            if (values.length == 0) {
                continue;
            }

            String runnerId = values[0];
            Map<Integer, List<Long>> checkpointTimes = new HashMap<>();
            for (int i = 1; i < values.length; i++) {
                if (values[i].equals("-")) {
                    continue;
                }

                String[] timeParts = values[i].split(":");
                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);

                long totalSeconds = (hours * 3600) + (minutes * 60) + seconds;

                int checkpoint = (i - 1) % 10;
                checkpointTimes.putIfAbsent(checkpoint, new ArrayList<>());
                checkpointTimes.get(checkpoint).add(totalSeconds);
            }

            List<String> expectedTimes = new ArrayList<>();
            for (int checkpoint = 0; checkpoint < 10; checkpoint++) {
                if (checkpointTimes.containsKey(checkpoint)) {
                    List<Long> times = checkpointTimes.get(checkpoint);
                    if (!times.isEmpty()) {
                        long averageTimeInSeconds = times.stream().mapToLong(Long::longValue).sum() / times.size();

                        int estimatedHours = (int) (averageTimeInSeconds / 3600);
                        int estimatedMinutes = (int) ((averageTimeInSeconds % 3600) / 60);
                        int estimatedSeconds = (int) (averageTimeInSeconds % 60);

                        String expectedTime = String.format("%02d:%02d:%02d", estimatedHours, estimatedMinutes, estimatedSeconds);
                        expectedTimes.add(expectedTime);
                    } else {
                        expectedTimes.add("N/A");
                    }
                } else {
                    expectedTimes.add("N/A");
                }
            }

            runnerExpectedCheckpointTimes.put(runnerId, expectedTimes);
        }
    }

    private void checkRunnersExpectedTimes() {
        LocalTime currentTime = LocalTime.parse(clockLabel.getText(), DateTimeFormatter.ofPattern("HH:mm:ss"));

        for (Map.Entry<String, List<String>> entry : runnerExpectedCheckpointTimes.entrySet()) {
            String runnerId = entry.getKey();
            List<String> expectedTimes = entry.getValue();
            int currentCheckpoint = runnerCheckpoints.getOrDefault(runnerId, 1);

            if (currentCheckpoint <= expectedTimes.size()) {
                LocalTime expectedTime = LocalTime.parse(expectedTimes.get(currentCheckpoint - 1), DateTimeFormatter.ofPattern("HH:mm:ss"));

                // Debug code
                if (runnerId.equals("55")) {
                    System.out.println("Current Time: " + currentTime);
                    System.out.println("Expected Time: " + expectedTime);
                    System.out.println("Checkpoint: " + currentCheckpoint);
                }
                // End of debug code

                if (currentTime.equals(expectedTime)) {
                    Platform.runLater(() -> {
                        showAlert("Alert", "Runner " + runnerId + " hasn't reached checkpoint " + currentCheckpoint + " yet.");
                    });
                }
            }
        }
    }

    private void showRetirementWindow() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Retirement.fxml"));
        Parent root = loader.load();

        Stage retirementStage = new Stage();
        retirementStage.initModality(Modality.APPLICATION_MODAL);
        retirementStage.setTitle("Report Retirement");
        retirementStage.setScene(new Scene(root));
        retirementStage.show();
    }



    private void saveTime(String runnerId, String time, int checkpoint) throws IOException {
        if (isRunnerRetired(runnerId)) {
            showAlert("Error", "This runner has retired and cannot reach more checkpoints.");
            return;
        }

        if (checkpoint >= 10) {
            showAlert("Error", "This runner has already reached the maximum number of checkpoints.");
            return;
        }

        if (!isRunnerRegistered(runnerId)) {
            showAlert("Error", "The runner ID is not registered.");
            return;
        }

        Path filePath = Paths.get("times.csv");
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        // If the time value is empty, store "-" instead
        String timeToStore = time.isEmpty() ? "-" : time;

        // Check if the runner has completed all checkpoints for the current race
        boolean completedRace = checkpoint == 9;

        if (completedRace) {
            // If the race is completed, append the new race times for the runner
            StringBuilder newLine = new StringBuilder(runnerId);
            for (int i = 1; i <= checkpoint + 1; i++) {
                if (i == checkpoint + 1) {
                    newLine.append(",").append(timeToStore);
                } else {
                    newLine.append(",").append(runnerExpectedCheckpointTimes.get(runnerId).get(i - 1));
                }
            }
            lines.add(newLine.toString());
            Files.write(filePath, lines, StandardCharsets.UTF_8);
        } else {
            // If the race is not completed, update the expected time for the next checkpoint
            List<String> expectedTimes = runnerExpectedCheckpointTimes.get(runnerId);
            expectedTimes.set(checkpoint, timeToStore);
        }

        // Update checkpoint only if the time was saved successfully
        if (checkpoint < 10) {
            runnerCheckpoints.put(runnerId, checkpoint + 1);

        }


    }

    private void saveFinalTime(String runnerId, String time) throws IOException {
        if (isRunnerRetired(runnerId)) {
            showAlert("Error", "This runner has retired and cannot finish the race.");
            return;
        }

        Path filePath = Paths.get("final_times.csv");
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        // Add a new line for the runner ID and final time
        lines.add(runnerId + "," + time);

        // Write the updated lines back to the file
        Files.write(filePath, lines, StandardCharsets.UTF_8);

        if (allRunnersFinishedOrRetired()) {
            clearRetirementsFile();
        }
    }

    private boolean isRunnerRetired(String runnerId) throws IOException {
        Path filePath = Paths.get("retirements.csv");
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        for (String line : lines) {
            String[] values = line.split(",");
            if (values.length > 0 && values[0].equals(runnerId)) {
                return true;
            }
        }

        return false;
    }


    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private boolean isRunnerRegistered(String runnerId) throws IOException {
        Path filePath = Paths.get("profiles.csv");
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        for (String line : lines) {
            String[] values = line.split(",");
            if (values.length > 0 && values[0].equals(runnerId)) {
                return true;
            }
        }

        return false;
    }

    private boolean allRunnersFinishedOrRetired() throws IOException {
        Set<String> registeredRunners = new HashSet<>();
        Set<String> completedRunners = new HashSet<>();
        Set<String> retiredRunners = new HashSet<>();

        // Read registered runners
        Path registeredRunnersPath = Paths.get("profiles.csv");
        List<String> registeredRunnersLines = Files.readAllLines(registeredRunnersPath, StandardCharsets.UTF_8);
        for (String line : registeredRunnersLines) {
            String[] values = line.split(",");
            if (values.length > 0) {
                registeredRunners.add(values[0]);
            }
        }

        // Read finished runners
        Path finishTimesPath = Paths.get("finish_times.csv");
        if (Files.exists(finishTimesPath)) {
            List<String> finishTimesLines = Files.readAllLines(finishTimesPath, StandardCharsets.UTF_8);
            for (String line : finishTimesLines) {
                String[] values = line.split(",");
                if (values.length > 0) {
                    completedRunners.add(values[0]);
                }
            }
        }

        // Read retired runners
        Path retiredRunnersPath = Paths.get("retirements.csv");
        if (Files.exists(retiredRunnersPath)) {
            List<String> retiredRunnersLines = Files.readAllLines(retiredRunnersPath, StandardCharsets.UTF_8);
            for (String line : retiredRunnersLines) {
                String[] values = line.split(",");
                if (values.length > 0) {
                    retiredRunners.add(values[0]);
                }
            }
        }

        // Check if all registered runners have either finished or retired
        registeredRunners.removeAll(completedRunners);
        registeredRunners.removeAll(retiredRunners);
        return registeredRunners.isEmpty();
    }


    private void clearRetirementsFile() throws IOException {
        Path retirementsPath = Paths.get("retirements.csv");
        Files.write(retirementsPath, Collections.emptyList(), StandardCharsets.UTF_8);
    }

    private void showRetirementInfo() {
        try {
            Path retirementsPath = Paths.get("retirements.csv");
            List<String> lines = Files.readAllLines(retirementsPath, StandardCharsets.UTF_8);

            StringBuilder retirementInfo = new StringBuilder("Retired Runners:\n\n");

            for (String line : lines) {
                String[] values = line.split(",");
                if (values.length > 2) {
                    retirementInfo.append("Runner ID: ").append(values[0]).append("\n");
                    retirementInfo.append("Reason: ").append(values[1]).append("\n");
                    retirementInfo.append("Responsible Person: ").append(values[2]).append("\n\n");
                }
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Retirement Info");
            alert.setHeaderText(null);
            alert.setContentText(retirementInfo.toString());
            alert.showAndWait();

        } catch (IOException e) {
            System.err.println("Error reading retirements file: " + e.getMessage());
        }
    }

    private void calculateAndPrintAverageTimes() throws IOException {
        Path filePath = Paths.get("times.csv");
        List<String> lines = Files.readAllLines(filePath, StandardCharsets.UTF_8);

        // Key: RunnerId, Value: List of checkpoint times (as seconds) for each race
        Map<String, List<List<Long>>> runnerTimes = new HashMap<>();

        // Read the data from times.csv
        for (String line : lines) {
            String[] values = line.split(",");

            if (values.length == 0) {
                continue;
            }

            String runnerId = values[0];
            List<Long> checkpointTimes = new ArrayList<>();

            for (int i = 1; i < values.length; i++) {
                String[] timeParts = values[i].split(":");

                if (timeParts.length != 3) {
                    continue;
                }

                int hours = Integer.parseInt(timeParts[0]);
                int minutes = Integer.parseInt(timeParts[1]);
                int seconds = Integer.parseInt(timeParts[2]);

                long totalSeconds = (hours * 3600) + (minutes * 60) + seconds;
                checkpointTimes.add(totalSeconds);
            }

            runnerTimes.putIfAbsent(runnerId, new ArrayList<>());
            runnerTimes.get(runnerId).add(checkpointTimes);
        }

        // Calculate and print the average times
        for (Map.Entry<String, List<List<Long>>> entry : runnerTimes.entrySet()) {
            String runnerId = entry.getKey();
            List<List<Long>> races = entry.getValue();

            System.out.println("Runner ID: " + runnerId);

            int checkpointCount = races.get(0).size();
            for (int i = 0; i < checkpointCount; i++) {
                long totalTime = 0;
                int count = 0;

                for (List<Long> race : races) {
                    if (i < race.size()) {
                        totalTime += race.get(i);
                        count++;
                    }
                }

                long averageTimeInSeconds = totalTime / count;
                int averageHours = (int) (averageTimeInSeconds / 3600);
                int averageMinutes = (int) ((averageTimeInSeconds % 3600) / 60);
                int averageSeconds = (int) (averageTimeInSeconds % 60);

                System.out.printf("Checkpoint %d average time: %02d:%02d:%02d%n", i + 1, averageHours, averageMinutes, averageSeconds);
            }
            System.out.println();
        }
    }
}