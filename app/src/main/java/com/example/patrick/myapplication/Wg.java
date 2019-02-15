package com.example.patrick.myapplication;

public class Wg {

    private long id;

    private String street;
    private String  hnr;
    private String  name;
    private String password;


    public Wg(long id, String street, String hnr, String name, String password) {
        this.id = id;
        this.street = street;
        this.hnr = hnr;
        this.name = name;
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHnr() {
        return hnr;
    }

    public void setHnr(String hnr) {
        this.hnr = hnr;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String toString(){
        return Long.toString(id) + " | " +  name + " | " + street + " | " + hnr  ;
    }

}
