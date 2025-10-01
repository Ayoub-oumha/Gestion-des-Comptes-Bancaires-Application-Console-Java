package service.serviceImpl;

import model.Client;
import model.enums.Role;
import repository.ClientRepository;
import service.AuthService;
import service.ClientService;
import util.ValidatorUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ClientServiceImpl implements ClientService {

    private final ClientRepository clientRepository;
    private final AuthService authService;

    public ClientServiceImpl(ClientRepository clientRepository, AuthService authService) {
        this.clientRepository = clientRepository;
        this.authService = authService;
    }

    @Override
    public Client createClient(String firstName, String lastName, String email, String password) {
        if (!ValidatorUtil.isValidName(firstName)) {
            throw new IllegalArgumentException("Invalid first name");
        }

        if (!ValidatorUtil.isValidName(lastName)) {
            throw new IllegalArgumentException("Invalid last name");
        }

        if (!ValidatorUtil.isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!ValidatorUtil.isValidPassword(password)) {
            throw new IllegalArgumentException("Invalid password - must be at least 6 characters");
        }

        if (authService.emailExists(email, Role.CLIENT)) {
            throw new IllegalArgumentException("Email already exists");
        }

        Client newClient = new Client(
            firstName,
            lastName,
            email,
            password,
            UUID.randomUUID(),
            new ArrayList<>()
        );

        clientRepository.save(newClient);

        return newClient;
    }

    @Override
    public List<Client> listClients(){
        return clientRepository.findAll();
    }

    @Override
    public void deleteClient(UUID clientId) {
        clientRepository.delete(clientId);
    }
}
