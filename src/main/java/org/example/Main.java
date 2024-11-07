package org.example;

import org.example.repository.*;
import org.example.controller.*;
import org.example.config.Conection;
import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Connection connection = Conection.getConnection();

        try {
            ClientRepositoryy clientRepository = new ClientRepositoryImpl(connection);
            ClientRepositoryImpl clienteRepository = new ClientRepositoryImpl(connection);
            ClientController clientController = new ClientController(clientRepository);
            AccountRepository accountRepository = new InMemoryAccountRepository(connection);
            AccountController accountController = new AccountController(accountRepository, clienteRepository);

            while (true) {
                System.out.println("\nSeleccione una operación:");
                System.out.println("1. Registrar Cliente");
                System.out.println("2. Apertura de Cuenta");
                System.out.println("3. Realizar Depósito");
                System.out.println("4. Retirar Dinero");
                System.out.println("5. Consultar Saldo");
                System.out.println("6. Salir");

                String option = scanner.nextLine();
                switch (option) {
                    case "1":
                        registerClient(scanner, clientController);
                        break;
                    case "2":
                        openAccount(scanner, accountController, clientRepository);
                        break;
                    case "3":
                        makeDeposit(scanner, accountController, clientRepository);
                        break;
                    case "4":
                        withdrawMoney(scanner, accountController, accountRepository);
                        break;
                    case "5":
                        checkBalance(scanner, accountController, accountRepository);
                        break;
                    case "6":
                        System.out.println("Saliendo del sistema...");
                        return;
                    default:
                        System.out.println("Opción no válida. Intente nuevamente.");
                }
            }
        } finally {
            scanner.close();
            closeConnection(connection);
        }
    }

    //REGISTRAR CLIENTE
    private static void registerClient(Scanner scanner, ClientController clientController) {
        System.out.println("Registro de Cliente");
        try {
            System.out.print("Ingrese el nombre: ");
            String firstName = scanner.nextLine();

            System.out.print("Ingrese el apellido: ");
            String lastName = scanner.nextLine();

            System.out.print("Ingrese el DNI: ");
            String dni = scanner.nextLine();

            System.out.print("Ingrese el correo electrónico: ");
            String email = scanner.nextLine();

            clientController.registerClient(firstName, lastName, dni, email);
            System.out.println("Cliente registrado exitosamente.");
        } catch (Exception e) {
            System.out.println("Error en el registro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //APERTURAR CUENTA
    private static void openAccount(Scanner scanner, AccountController accountController, ClientRepositoryy clientRepository) {
        System.out.println("Apertura de cuenta");
        try {
            System.out.print("Ingrese el DNI: ");
            String dni = scanner.nextLine();

            if (clientRepository.dniExists(dni)) {
                System.out.print("Escoja el tipo de cuenta (AHORROS o CORRIENTE): ");
                String accountType = scanner.nextLine();

                String accountNumber = accountController.openAccount(dni, accountType);
                System.out.println("Cuenta creada exitosamente. Número de cuenta: " + accountNumber);
            } else {
                System.out.println("DNI no encontrado. Registre el cliente primero.");
            }
        } catch (Exception e) {
            System.out.println("Error al abrir cuenta: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //HACER UN DEPÓSITO
    private static void makeDeposit(Scanner scanner, AccountController accountController, ClientRepositoryy clientRepository) {
        System.out.println("Realizar Depósito");
        try {
            System.out.print("Ingrese su DNI: ");
            String dni = scanner.nextLine();

            if (!clientRepository.dniExists(dni)) {
                System.out.println("Cliente no encontrado.");
                return;
            }

            System.out.print("Escoja el tipo de cuenta (AHORROS o CORRIENTE): ");
            String accountType = scanner.nextLine();

            System.out.print("Ingrese el monto a depositar: ");
            double amount = scanner.nextDouble();
            scanner.nextLine();

            accountController.deposit(dni, accountType, amount);
            System.out.println("Depósito realizado con éxito.");
        } catch (Exception e) {
            System.out.println("Error en el depósito: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //RETIRAR DINERO
    private static void withdrawMoney(Scanner scanner, AccountController accountController, AccountRepository accountRepository) {
        System.out.println("Realizar retiro");
        try {
            System.out.print("Ingrese su número de cuenta: ");
            String accountNumber = scanner.nextLine();

            if (!accountRepository.accountExists(accountNumber)) {
                System.out.println("Cuenta no encontrada.");
                return;
            }

            double balance = accountController.checkBalance(accountNumber);
            System.out.println("Su saldo actual es: " + balance);

            System.out.print("Ingrese el monto a retirar: ");
            double amount = Double.parseDouble(scanner.nextLine());

            accountController.withdrawMoney(accountNumber, amount);
            System.out.println("Retiro realizado con éxito.");
        } catch (Exception e) {
            System.out.println("Error en el retiro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //CONSULTAR SALDO
    private static void checkBalance(Scanner scanner, AccountController accountController, AccountRepository accountRepository) {
        System.out.println("Consultar saldo");
        try {
            System.out.print("Ingrese su número de cuenta: ");
            String accountNumber = scanner.nextLine();

            if (accountRepository.accountExists(accountNumber)) {
                double balance = accountController.checkBalance(accountNumber);
                System.out.println("Su saldo actual es: " + balance);
            } else {
                System.out.println("Cuenta no encontrada.");
            }
        } catch (Exception e) {
            System.out.println("Error en la consulta de saldo: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //CERRAR CONEXION
    private static void closeConnection(Connection connection) {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a la base de datos cerrada.");
            }
        } catch (Exception e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
