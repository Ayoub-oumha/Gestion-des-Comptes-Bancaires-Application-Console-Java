package service;

import model.Banker;
import model.Client;
import model.Person;
import model.enums.Role;
import repository.ClientRepository;
import exceptions.AccountNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthService {
    private final ClientRepository clientRepo;

    // Dependency Injection via constructor
    public AuthService(ClientRepository clientRepo) {
        this.clientRepo = clientRepo;
    }

    // Hardcoded banker credentials
    private static final Banker BANKER = new Banker(
        "John",
        "Smith",
        "banker@bank.com",
        "password",
        UUID.randomUUID(),
        "kaydir kolchi",
        new ArrayList<>()
    );

    //create banker credentials
    public Banker banker(String username, String password, Role role) {
        return BANKER;
    }

    // check if email exists
    public boolean emailExists(String email, Role role) {
        List<Client> clients = clientRepo.findAll();
        for (Client client : clients) {
            if (client.getEmail().equals(email)) {
                return true;
            }
        }
        return false;
    }

    // login - check credentials for both Client and Banker
    public Optional<Person> login(String email, String password) {
        // check if it's the banker
        if (BANKER.getEmail().equals(email) && BANKER.getPassword().equals(password)) {
            return Optional.of(BANKER);
        }
        // check in clients
        List<Client> clients = clientRepo.findAll();
        for (Client client : clients) {
            if (client.getEmail().equals(email) && client.getPassword().equals(password)) {
                return Optional.of(client);
            }
        }
        // If no match found, throw exception
        throw new AccountNotFoundException("Invalid credentials: Account not found for email " + email);
    }
}
