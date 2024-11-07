package org.example.controller;

import org.example.model.*;
import org.example.repository.ClientRepositoryy;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class ClientController {
    private ClientRepositoryy clientRepositoryy;
    private Client client;
    private Map<String, Client> clients = new HashMap<>();

    //establece un patron para email valido
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    //Constructor de clientController
    public ClientController(ClientRepositoryy clientRepositoryy) {
        this.clientRepositoryy = clientRepositoryy;
    }

    //validaciones para que no existan campos nulos
    private void validateFields(String firstName, String lastName, String dni, String email){
        if(firstName==null || lastName==null || dni==null || email==null){
            System.out.println("Todos los campos son obligatorios");
        }
    }
    //validar formato de email cumpla el patron establecido
    private void validateEmailFormat(String email) {
        if (!EMAIL_PATTERN.matcher(email).matches()) {
            System.out.println("Formato de email no válido");
        }
    }

    //validar que el dni no se repita
    private void uniqueId(String dni){
        if(clientRepositoryy.dniExists(dni)){
            System.out.println("El cliente ya se encuentra registrado.");
        }
    }

    //registrar cliente validando campos y formatos
    public void registerClient(String firstName, String lastName, String dni, String email) {
        try {

            Client client = new Client(firstName, lastName, dni, email);
            uniqueId(dni);
            validateEmailFormat(email);
            validateFields(firstName, lastName, dni, email);
            clientRepositoryy.addClient(client);
            System.out.println("Cliente registrado correctamente.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error al registrar cliente: " + e.getMessage());
        }
    }
}
