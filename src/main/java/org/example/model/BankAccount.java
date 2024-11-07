package org.example.model;

import com.sun.istack.internal.NotNull;

import java.util.UUID;

public class BankAccount {

    private String accountNumber;
    private String dni;
    private double balance;
    private AccountType accountType;

    public enum AccountType{
        AHORROS,
        CORRIENTE
    }

    public BankAccount(String accountNumber, String dni, AccountType accountType){
        this.accountNumber= accountNumber;
        this.dni = dni;
        this.balance= 0.00;
        this.accountType=accountType;
    }

    // lógica condicinal para el saldo en los tipos de cuenta
    public boolean discountCharge(double amount){
        if(accountType == AccountType.AHORROS && balance - amount < 0){
            return false;
        }
        if(accountType == AccountType.CORRIENTE && balance-amount < -500 ){
            return false;
        }
        return true;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getBalance() {
        return balance;
    }
    public String getDni(){return dni;}

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }
}
