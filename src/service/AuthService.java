package service;

import model.Person;
import model.enums.Role;

import java.util.Optional;

public interface AuthService {
    Optional<Person> login(String email, String password);

    boolean emailExists(String email, Role role);
}
