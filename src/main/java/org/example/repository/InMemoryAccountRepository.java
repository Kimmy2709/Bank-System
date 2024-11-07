package org.example.repository;
import org.example.model.BankAccount;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
public class InMemoryAccountRepository implements AccountRepository {

    private Connection connection;
    public InMemoryAccountRepository(Connection connection) {
        this.connection = connection;
    }
    private Map<String, BankAccount> accounts = new HashMap<>();

 // agrega una nueva cuenta bancaria a  la bd
    public void addAccount(BankAccount account) {
        String sql = "INSERT INTO BankAccount (accountNumber, dni,  balance, accountType) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, account.getAccountNumber());
            stmt.setString(2, account.getDni());
            stmt.setDouble(3, account.getBalance());
            stmt.setString(4, account.getAccountType().name());

            stmt.executeUpdate();
            System.out.println("Cuenta agregada a la base de datos.");
        } catch (SQLException e) {
            System.out.println("Error al agregar cuenta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //busca una cuenta segunn el dni y el tipo de cuenta
    @Override
    public BankAccount findAccountByDniAndType(String dni, BankAccount.AccountType accountType) {
        String sql = "SELECT * FROM BankAccount WHERE dni = ? AND accountType = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dni);
            stmt.setString(2, accountType.name());
            ResultSet rs = stmt.executeQuery();

            // Verifica si existe un resultado y recupera el saldo correcto
            if (rs.next()) {
                String accountNumber = rs.getString("accountNumber");
                double balance = rs.getDouble("balance");  // Obtiene el balance actual
                BankAccount account = new BankAccount(accountNumber, dni, accountType);
                account.setBalance(balance);  // Asigna el balance recuperado
                return account;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar la cuenta: " + e.getMessage());
            e.printStackTrace();
        }
        return null; // Retorna null si no se encuentra la cuenta
    }


  // actualiza la bd con el nuevo saldo al realizarse una transaccion bancaria
    @Override
    public void updateBalance(String accountNumber, double newBalance) {
        String sql = "UPDATE BankAccount SET balance = ? WHERE accountNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setDouble(1, newBalance); // Balance debe ser el primer parámetro
            stmt.setString(2, accountNumber); // Número de cuenta es el segundo parámetro
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Saldo actualizado en la base de datos.");
            } else {
                System.out.println("No se encontró la cuenta con accountNumber: " + accountNumber);
            }
        } catch (SQLException e) {
            System.out.println("Error al actualizar el saldo: " + e.getMessage());
        }
    }

  //consulta en la bd si la cuenta existe
    public boolean accountExists(String accountNumber) {
        String sql = "SELECT 1 FROM BankAccount WHERE accountNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            // Si hay un resultado, la cuenta existe
            return rs.next();
        } catch (SQLException e) {
            System.out.println("Error al verificar la existencia de la cuenta: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

     //verfica en la bd si la cuenta existe segun el num de cuenta
    public BankAccount findAccountByNumber(String accountNumber) {
        String sql = "SELECT * FROM BankAccount WHERE accountNumber = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            ResultSet rs = stmt.executeQuery();

            // Verifica si existe un resultado y recupera el saldo correcto
            if (rs.next()) {
                String dni = rs.getString("dni");
                double balance = rs.getDouble("balance");
                String accountTypeString = rs.getString("accountType");

                // Convierte el tipo de cuenta a un enum
                BankAccount.AccountType accountType = BankAccount.AccountType.valueOf(accountTypeString);

                // Crea y retorna el objeto BankAccount
                BankAccount account = new BankAccount(accountNumber, dni, accountType);
                account.setBalance(balance);
                return account;
            }
        } catch (SQLException e) {
            System.out.println("Error al buscar la cuenta: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}

