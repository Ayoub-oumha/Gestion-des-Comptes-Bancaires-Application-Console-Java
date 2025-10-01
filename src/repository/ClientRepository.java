package repository;

import model.Client;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ClientRepository {

    // save new client
    void save(Client client);

    Optional<Client> findById(UUID id);

    Optional<Client> findByEmail(String email);

    List<Client> findAll();

    void delete(UUID id);
}
