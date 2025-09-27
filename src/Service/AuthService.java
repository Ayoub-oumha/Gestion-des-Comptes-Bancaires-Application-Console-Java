package Service;

import Model.Personne;
import Repositories.ImpPreson;

public class AuthService {
        public ImpPreson imppreson ;
        public AuthService(ImpPreson imppreson){
                this.imppreson = imppreson;
        }

        public  Personne login(String email , String password){
                Personne persson = imppreson.findUserByEmail(email) ;
                if(persson == null) {
                        System.out.println("Invalid email or password");
                }
                if(!password.equals(persson.getPassword())){
                        System.out.println("Invalid password");
                }
                return persson;
        }
}
