package Repositories;

import Model.Personne;

public interface PersonRepository {
    Personne findUserByEmail(String email );

}
