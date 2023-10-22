package com.kencuevas.savepassword.controller;

import com.kencuevas.savepassword.DAO.JDBCPostgreSQLConnection;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    @FXML
    private Button loginbtn;

    @FXML
    private Button registerBTN;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private StackPane menuContainer;

    private ActionEvent storedEvent;

    @FXML
    void initialize() {
        //registerBTN.setOnAction(this::handleRegisterButtonClick);
        loginbtn.setOnAction(event -> {
            String email = emailField.getText();
            String password = passwordField.getText();

            if (email.isEmpty() || password.isEmpty()) {
                showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter your email and password.");
                return;
            }

            if (attemptLogin(email, password)) {
                // Autenticación exitosa, redirige al menú principal
                try {
                    loadPrincipalMenu();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                // Muestra un mensaje de error si la autenticación falla
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Please enter correct email and password.");
            }
        });
    }

    private boolean attemptLogin(String email, String password) {
        JDBCPostgreSQLConnection connection = new JDBCPostgreSQLConnection();
        return connection.login(email, password);
    }

    private static void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.show();
    }
    public static void infoBox(String infoMessage, String headerText, String title){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }
    private void loadPrincipalMenu() throws IOException {
        // Cargar la vista main-menu.fxml
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kencuevas/savepassword/views/main-menu.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root);

        Stage stage = (Stage) ((Node) storedEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
    private void handleRegisterButtonClick(ActionEvent event) {
        try {
            // Cargar la vista de registro de usuario desde un archivo FXML
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/kencuevas/savepassword/views/register-user.fxml"));
            Parent root = loader.load();

            // Crear una nueva escena
            Scene scene = new Scene(root);

            // Obtener el escenario actual (ventana)
            Stage stage = (Stage) registerBTN.getScene().getWindow();

            // Establecer la nueva escena en el escenario
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}