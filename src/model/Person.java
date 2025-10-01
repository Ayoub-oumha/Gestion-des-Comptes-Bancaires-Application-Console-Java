package model;

import model.enums.Role;

public abstract class Person {
    String firstName;
    String lastName;
    String email;
    String password;
    Role role;

    public Person(String firstName, String lastName, String email, String password, Role role) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    //Getters
    public String getFirstName() {return firstName;}
    public String getLastName() {return lastName;}
    public String getEmail() {return email;}
    public String getPassword() {return password;}
    public Role getRole() {return role;}

    //Setters
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setEmail(String email) {this.email = email;}
    public void setPassword(String password) {this.password = password;}
    public void setRole(Role role) {this.role = role;}
}
