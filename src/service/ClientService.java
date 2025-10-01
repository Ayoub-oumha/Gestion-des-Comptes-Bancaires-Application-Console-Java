package service;

import model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientService {
    Client createClient(String firstName, String lastName, String email, String password);

    List<Client> listClients();

    void deleteClient(UUID clientId);
//
//    void updateClient(UUID clientId, String firstName, String lastName, String email, String password);
//
//    Optional<Client> findClientById(UUID clientId);
//
//    Optional<Client> findClientByEmail(String email);
}
