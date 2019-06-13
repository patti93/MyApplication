package com.example.patrick.myapplication;

public class Wg {

    private long id;

    private String street;
    private String hnr;
    private String name;
    private String password;
    private String zipCode;
    private String country;
    private String town;
    private String description;


    public Wg(long id,String name, String street, String hnr, String zipCode, String country, String town, String description, String password) {
        this.id = id;
        this.street = street;
        this.hnr = hnr;
        this.name = name;
        this.password = password;
        this.zipCode = zipCode;
        this.country = country;
        this.town = town;
        this.description = description;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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


    @Override
    public String toString() {
        return "Wg{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", hnr='" + hnr + '\'' +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", country='" + country + '\'' +
                ", town='" + town + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
