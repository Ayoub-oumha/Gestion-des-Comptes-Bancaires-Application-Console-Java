package View;



import Model.Personne;
import Service.AuthService;

import java.util.Scanner;

public class MenuPrincipal {
    private static   AuthService authservice ;
    static Scanner sc = new Scanner(System.in);
    public MenuPrincipal(AuthService authservice){
        MenuPrincipal.authservice = authservice;
    }

    public static   void showMenuPrincipal(){
        //Scanner sc = new Scanner(System.in);
        System.out.println("MenuPrincipal");
        while(true){
            System.out.println("=====================================");
            System.out.println("MenuPrincipal");
            System.out.println("=====================================");
            System.out.println("1 -Connexion");
            System.out.println("1 -Quitter");
            System.out.println("=====================================");
            System.out.print("entre vote choir :");
            int choice = sc.nextInt();

            switch (choice){
                case 1:
                    showMenuLogin();
                    break;
                case 2:
                    showMenuPrincipal();
                    break;
                default:
                    System.out.println("Veuillez choisir votre choix");
            }

        }
    }
     public static void showMenuLogin(){

        System.out.println("============================ Login ============================");
        System.out.println("Email :");
        String email = sc.next();
        System.out.println("Password :");
        String password = sc.next();
        Personne loggeIn = authservice.login(email , password);
        if(loggeIn){
            System.out.println("Login successful");
            showMenuClient() ;
        }
        else {
            System.out.println("Login failed");
            showMenuPrincipal() ;
        }

    }
     static void showMenuClient(){
        System.out.println("================================ Cliente ============================");
        System.out.println("1 Nom :");
        String nom = sc.next();
    }
}
