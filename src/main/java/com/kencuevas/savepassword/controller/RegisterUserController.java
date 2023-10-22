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
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class RegisterUserController {
    @FXML
    private TextField firstnameField;
    @FXML
    private TextField lastnameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button registerBTN;

    @FXML
    public void register(ActionEvent event) throws SQLException{
        String firstname = firstnameField.getText();
        String lastname = lastnameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        Window owner = registerBTN.getScene().getWindow();

        if (firstnameField.getText().isEmpty() || lastnameField.getText().isEmpty() || emailField.getText().isEmpty() || passwordField.getText().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, owner, "Form Error!", "Please fill in all fields");
            return;
        }

        try {
            JDBCPostgreSQLConnection connection = new JDBCPostgreSQLConnection();
            connection.insertUser(firstname, lastname, email, password);

            showAlert(Alert.AlertType.CONFIRMATION, owner, "Registration Successful!", "Welcome " + firstname + " " + lastname);

            // Cargar la vista main-menu.fxml
            FXMLLoader loader = new FXMLLoader(getClass().getResource("main-menu.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (SQLIntegrityConstraintViolationException e){
            // Esta excepción se lanza cuando hay una violación de restricción UNIQUE
            showAlert(Alert.AlertType.ERROR, owner, "Database Error!", "El correo que estas ingresando ya esta registrado en la base de datos");
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, owner, "Database Error!", "Ocurrio un error al momento de registrar el usuario");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showAlert(Alert.AlertType alertType, Window owner, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.initOwner(owner);
        alert.show();
    }
}
