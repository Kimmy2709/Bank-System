package org.example.model;

import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class Client {

    private String firstName;
    private String lastName;

    private String dni;

    private String email;
    private List<BankAccount> accounts;



    public Client(String firstName, String lastName, String dni, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dni = dni;
        this.email = email;
        this.accounts = new ArrayList<>();
    }

    public void addAccount(BankAccount account) {
        accounts.add(account);
    }
    public List<BankAccount> getAccounts() {
        return accounts;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
