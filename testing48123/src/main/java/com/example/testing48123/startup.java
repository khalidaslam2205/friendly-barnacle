package com.example.testing48123;
import com.example.testing48123.SceneControl;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

import static com.example.testing48123.SceneControl.primaryStage;

public class startup extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SceneControl.initialize();
        primaryStage = primaryStage;
    }

    public static void main(String[] args) throws IOException {
//        GreetClient client = new GreetClient();
//        client.startConnection("127.0.0.1", 5555);
//        String response = client.sendMessage("hello server");
//        System.out.println(response);
//        EchoClient client = new EchoClient();
//        client.startConnection("127.0.0.1", 5555);
//
//        while (true) {
//            String response = client.sendMessage("get_data");
//            // Process the response data
//            if (response.equals("done")) {
//                client.sendMessage(".");
//                client.stopConnection();
//                break;
//            }
//        }

        launch(args);


    }
}