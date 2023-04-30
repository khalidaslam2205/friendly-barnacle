package com.example.testing48123;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class SceneControl {
    public static Stage primaryStage;
    public static Scene mainMenuScene;
    public static Scene profileMenu;
    public static Scene viewProfile;
    public static Scene viewTimes;
    public static Scene loginScene;


    public static void initialize() throws IOException {
        primaryStage = new Stage();
        FXMLLoader mainMenuLoader = new FXMLLoader(SceneControl.class.getResource("menu.fxml"));
        mainMenuScene = new Scene(mainMenuLoader.load());

        FXMLLoader loginLoader = new FXMLLoader(SceneControl.class.getResource("Login.fxml"));
        loginScene = new Scene(loginLoader.load());

        primaryStage.setTitle("Runner Profile Manager");
        primaryStage.setScene(loginScene);
        primaryStage.show();



        mainMenuScene = new Scene(FXMLLoader.load(SceneControl.class.getResource("menu.fxml")));
        profileMenu = new Scene(FXMLLoader.load(SceneControl.class.getResource("profile.fxml")));
        viewProfile = new Scene(FXMLLoader.load(SceneControl.class.getResource("view.fxml")));
        viewTimes = new Scene(FXMLLoader.load(SceneControl.class.getResource("times.fxml")));
    }

    public static void showErrorMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}

