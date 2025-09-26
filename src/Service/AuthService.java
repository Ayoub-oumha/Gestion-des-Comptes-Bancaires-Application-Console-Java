package Service;

import Model.Personne;
import Repositories.ImpPreson;

public class AuthService {
        public ImpPreson repoPreson = new ImpPreson() ;
        public boolean login(String email , String password){
                Personne persson = repoPreson.findUserByEmail(email) ;
                if(persson != null){
                       return true;
                }
                else {
                        return  false;
                }
        }
}
