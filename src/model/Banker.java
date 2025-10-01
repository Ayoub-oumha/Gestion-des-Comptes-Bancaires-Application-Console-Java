package model;

import java.util.List;
import java.util.UUID;

public class Banker extends  Person {
    UUID managerId;
    String department;
    List<Client> clients;

    public Banker(String firstName, String lastName, String email, String password, UUID managerId, String department, List<Client> clients) {
        super(firstName, lastName, email, password, model.enums.Role.BANKER);
        this.managerId = managerId;
        this.department = department;
        this.clients = clients;
    }

    //Getters
    public UUID getManagerId() {return managerId;}
    public String getDepartment() {return department;}
    public List<Client> getClients() {return clients;}

    //Setters
    public void setManagerId(UUID managerId) {this.managerId = managerId;}
    public void setDepartment(String department) {this.department = department;}
    public void setClients(List<Client> clients) {this.clients = clients;}
}
