package controller;

import controller.HomeController;
import model.Account;
import model.Client;
import service.ClientService;

public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    // Helper method to get client first found account
    public Account getPrimaryAccount() {
        Client currentClient = (Client) HomeController.getCurrentUser();
        return currentClient.getAccounts().get(0);
    }
}