package Repositories;

import Model.Personne;

import java.util.ArrayList;

public class ImpPreson  implements PersonRepository {
    private ArrayList<Personne> list = new ArrayList<>();

   public ImpPreson(){
        boolean add;
        list.add(new Personne("admin", "admin", "admin@gmail.com", "123456"));

    }


    @Override
    public Personne findUserByEmail(String email ){
        for(Personne per : list){
            if(email.equalsIgnoreCase(per.getEmail())){
                return per;
            }
        }
        return null;
    }
}
