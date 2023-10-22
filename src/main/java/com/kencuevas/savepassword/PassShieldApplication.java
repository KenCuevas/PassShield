package com.kencuevas.savepassword;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class PassShieldApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(PassShieldApplication.class.getResource("/com/kencuevas/savepassword/views/login-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1078, 695);
        stage.setTitle("PassShield");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}