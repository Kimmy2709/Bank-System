package org.example.repository;
import java.util.HashMap;
import java.util.Map;
import org.example.model.*;

public interface AccountRepository {
    void addAccount(BankAccount account);
    BankAccount findAccountByNumber(String accountNumber);
    boolean accountExists(String accountNumber);
    BankAccount findAccountByDniAndType(String dni, BankAccount.AccountType accountType);
    void updateBalance(String accountNumber, double newBalance);

}

