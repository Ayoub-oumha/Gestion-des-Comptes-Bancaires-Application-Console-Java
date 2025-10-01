package controller;

import exceptions.AccountNotFoundException;
import model.Client;
import model.Person;
import model.enums.Role;
import repository.InMemoryImpl.InMemoryAccountRepository;
import repository.InMemoryImpl.InMemoryClientRepository;
import repository.InMemoryImpl.InMemoryTransactionRepository;
import service.serviceImpl.AuthServiceImpl;
import service.serviceImpl.ClientServiceImpl;
import service.serviceImpl.AccountServiceImpl;
import service.serviceImpl.TransactionServiceImpl;
import util.ValidatorUtil;
import view.BankerView;
import view.ClientView;

import java.util.List;
import java.util.Optional;

public class HomeController {
    private static final InMemoryClientRepository clientRepository = new InMemoryClientRepository();
    private static final InMemoryAccountRepository accountRepository = new InMemoryAccountRepository();
    private static final InMemoryTransactionRepository transactionRepository = new InMemoryTransactionRepository();
    private static final AuthServiceImpl authService = new AuthServiceImpl(clientRepository);
    private static final ClientServiceImpl clientService = new ClientServiceImpl(clientRepository, authService);
    private static final AccountServiceImpl accountService = new AccountServiceImpl(accountRepository);
    private static final TransactionServiceImpl transactionService = new TransactionServiceImpl(transactionRepository, accountRepository);
    private static final BankerController bankerController = new BankerController(clientService, accountService, transactionService);
    private static final ClientController clientController = new ClientController(clientService);
    private static final TransactionController transactionController = new TransactionController(transactionService, clientService);
    private static final BankerView bankerView = new BankerView(bankerController);
    private static final ClientView clientView = new ClientView(clientController, transactionController);

    public static Person currentUser = null; //track the logged-in user

    public static boolean authenticateUser(String email, String password, int roleChoice) {
        // Validate inputs
        if (!ValidatorUtil.isValidEmail(email)) {
            System.out.println("Invalid email format. Try again.");
            return false;
        }

        if (!ValidatorUtil.isValidPassword(password)) {
            System.out.println("Invalid password format. Try again.");
            return false;
        }

        try {
            Optional<Person> loggedUser = authService.login(email, password);
            if (loggedUser.isPresent()) {
                currentUser = loggedUser.get();
                Role expectedRole = (roleChoice == 1) ? Role.CLIENT : Role.BANKER;

                if (currentUser.getRole() == expectedRole) {
                    System.out.println("Welcome " + currentUser.getFirstName() + " to the " +
                        (expectedRole == Role.CLIENT ? "Client" : "Banker") + " Dashboard!");

                    // navigate to appropriate view
                    if (expectedRole == Role.CLIENT) {
                        List<Client> allClients = clientService.listClients();
                        for (Client repoClient : allClients) {
                            if (repoClient.getClientId().equals(((Client) currentUser).getClientId())) {
                                currentUser = repoClient; // Use repository reference
                                break;
                            }
                        }
                        clientView.clientView();
                    } else {
                        bankerView.bankerView();
                    }
                    return true;
                } else {
                    System.out.println("Access denied.");
                    currentUser = null;
                    return false;
                }
            }
            return false;
        } catch (AccountNotFoundException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Person getCurrentUser() {
        return currentUser;
    }

    // logout the current user and clear session
    public static void logout() {
        if (currentUser != null) {
            System.out.println("User " + currentUser.getFirstName() + " logged out successfully.");
            currentUser = null;
        }
    }
}
