package com.kencuevas.savepassword.DAO;
import java.sql.*;

import org.mindrot.jbcrypt.BCrypt;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class JDBCPostgreSQLConnection {
    private final String URL = "jdbc:postgresql://localhost/pass_shiel_db";
    private final String USER = "passhield_user";
    private final String PASSWORD = "passhield";
    private final String INSERT_QUERY = "INSERT INTO users (first_name, last_name, email, password) VALUES (?, ?, ?, ?)";
    private final String SELECT_USER = "SELECT (first_name, last_name) FROM users WHERE email VALUES (?, ?, ?, ?)";
    /**
     * Connect to the PostgreSQL database
     *
     * @return a Connection object
     */
    public void insertUser(String name, String lastname, String email, String password) throws SQLException{
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            String hashedPassword = hashPassword(password);
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastname);
            preparedStatement.setString(3, email);
            preparedStatement.setString(4, hashedPassword);

            int rowsAffected = preparedStatement.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Inserción exitosa.");
            } else {
                System.out.println("La inserción no tuvo éxito.");
            }

            // Cerrar la conexión
            connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public boolean login(String email, String password) {
        boolean loggedIn = false;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
                // Manejo de entradas inválidas
                return false;
            }

            String SELECT_PASSWORD_HASH = "SELECT password FROM users WHERE email = ?";
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            preparedStatement = connection.prepareStatement(SELECT_PASSWORD_HASH);
            preparedStatement.setString(1, email);

            resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                // Se encontró un usuario con el correo electrónico proporcionado
                String storedPasswordHash = resultSet.getString("password");

                if (BCrypt.checkpw(password, storedPasswordHash)) {
                    // Las contraseñas coinciden
                    loggedIn = true;
                }
            }
        } catch (SQLException e) {
            // Manejo de excepciones: muestra un mensaje de error o registra el error
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return loggedIn;
    }
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }
}
