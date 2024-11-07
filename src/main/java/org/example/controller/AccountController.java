package org.example.controller;
import org.example.model.*;
import org.example.repository.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AccountController {
    private AccountRepository accountRepository;
    private ClientRepositoryImpl clientRepository;

    //Inicializar controlador
    public AccountController(AccountRepository accountRepository, ClientRepositoryImpl clientRepository) {
        this.accountRepository = accountRepository;
        this.clientRepository = clientRepository;
    }

    // generar un numero de cuenta al azar
    private String generateAccountNum(){
        return UUID.randomUUID().toString().substring(0,10);
    }

    //crea  cuenta verificando que el cliente se encuentre previamente registrado
    public String openAccount(String dni, String accountType) {
        if (!clientRepository.dniExists(dni)) {
            System.out.println("Cliente no encontrado. Regístrese antes de abrir una cuenta.");
        }
        BankAccount.AccountType type;
        if (accountType.equalsIgnoreCase("AHORROS")) {
            type = BankAccount.AccountType.AHORROS;
        } else if (accountType.equalsIgnoreCase("CORRIENTE")) {
            type = BankAccount.AccountType.CORRIENTE;
        } else {
            throw new IllegalArgumentException("Tipo de cuenta inválido. Debe ser 'AHORROS' o 'CORRIENTE'.");
        }
        //agrega la cuenta
        String accountNumber = generateAccountNum();
        BankAccount newAccount = new BankAccount(accountNumber, dni,type);
        accountRepository.addAccount(newAccount);
        return newAccount.getAccountNumber();
    }

    // realiza deposto en la cuenta verificando que el cliente y el tipo de cuenta existan y se actualiza el saldo
    public void deposit(String dni, String accountType, double amount) {
        if (!clientRepository.dniExists(dni)) {
            System.out.println("El cliente con DNI " + dni + " no existe.");
            return;
        }
        BankAccount account = accountRepository.findAccountByDniAndType(dni, BankAccount.AccountType.valueOf(accountType.toUpperCase()));
        if (account == null) {
            System.out.println("No se encontró una cuenta de tipo " + accountType + " para este cliente.");
            return;
        }
        // Realiza el depósito
        double newBalance = account.getBalance() + amount;
        account.setBalance(newBalance);
        // Actualiza el saldo en la base de datos
        accountRepository.updateBalance(account.getAccountNumber(), newBalance);
        System.out.println( "Su nuevo saldo: " + newBalance);
    }

   // retiro de dinero verificando que el cliente exista, actualiza saldo
    public void withdrawMoney(String accountNumber, double amount) {
        BankAccount account = accountRepository.findAccountByNumber(accountNumber);
        if (account != null && account.discountCharge(amount)) {
            double newBalance = account.getBalance() - amount;
            accountRepository.updateBalance(accountNumber, newBalance);
            System.out.println("Retiro realizado con éxito. Nuevo saldo: " + newBalance);
        } else {
            System.out.println("No cuenta con el monto suficiente. Ingrese otro monto");
        }
    }

    //consulta saldo con el numero de cuenta
    public double  checkBalance(String accountNumber)  {
        BankAccount account = accountRepository.findAccountByNumber(accountNumber);
        if (account != null) {
            return account.getBalance();
        } else {
            throw new IllegalArgumentException("Esta cuenta no existe");
        }
    }
}
