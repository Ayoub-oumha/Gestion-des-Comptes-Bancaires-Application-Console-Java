package repository.InMemoryImpl;

import model.Client;
import repository.ClientRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class InMemoryClientRepository implements ClientRepository {
    List<Client> clients = new ArrayList<>();

    @Override
    public void save(Client client){
        clients.add(client);
    }

    @Override
    public Optional<Client> findById(UUID id){
        for (Client client : clients){
            if (client.getClientId().equals(id)){
                return Optional.of(client);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByEmail(String email){
        for (Client client : clients){
            if (client.getEmail().equals(email)){
                return  Optional.of(client);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Client> findAll(){
        return  clients;
    }

    // delete client by id using removeIf(): lambda expression
    @Override
    public void delete(UUID id) {
        clients.removeIf(client -> client.getClientId().equals(id));
    }
}
