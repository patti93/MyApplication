package com.example.patrick.myapplication;

public class Appointment {

    private long id;
    private String name;
    private String date;
    private String description;
    private int hour;
    private int minute;

    public Appointment(long id, String name, String date, String description, int hour, int minute) {
        this.id = id;
        this.name = name;
        this.date = date;
        this.description = description;
        this.hour = hour;
        this.minute = minute;
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", hour=" + hour +
                ", minute=" + minute +
                '}';
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
