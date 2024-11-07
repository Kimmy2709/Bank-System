package org.example.repository;
import org.example.model.Client;

public interface ClientRepositoryy {
    void addClient(Client client);

     boolean dniExists(String dni);


}
