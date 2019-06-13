package com.example.patrick.myapplication;

public class Resident {

    private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String bday;
    private String password;


    public Resident (long id,String firstName, String lastName, String bday, String email, String password){

        this.id = id;
        this.bday = bday;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getbday() {
        return bday;
    }

    public void setbday(String bday) {
        this.bday = bday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String toString() {
        return "Resident{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                ", bday='" + bday + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
