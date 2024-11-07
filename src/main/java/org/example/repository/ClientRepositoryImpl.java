package org.example.repository;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.example.model.*;
public class ClientRepositoryImpl implements ClientRepositoryy {

    private Connection connection;

    public ClientRepositoryImpl(Connection connection) {
        this.connection = connection;
    }

    //verifica si el dni ya se encuentra registrado en la bd
    public boolean dniExists(String dni) {
        String sql = "SELECT COUNT(*) FROM Client WHERE dni = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Retorna true si el conteo es mayor a 0

            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el DNI: " + e.getMessage());
        }
        return false; // Retorna false si ocurre un error
    }

//añade un nuevo cliente a la bd
    public void addClient(Client client) {
        String sql = "INSERT INTO Client (dni, firstName, lastName, email) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, client.getDni());
            stmt.setString(2, client.getFirstName());
            stmt.setString(3, client.getLastName());
            stmt.setString(4, client.getEmail());
            stmt.executeUpdate();
            System.out.println("Cliente agregado a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al agregar cliente: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
