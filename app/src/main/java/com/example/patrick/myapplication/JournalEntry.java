package com.example.patrick.myapplication;

public class JournalEntry {


    private long id;
    private String sent_by;
    private String date;
    private String message;
    private int hour;
    private int minute;


    public JournalEntry(long id, String sent_by, String date, String message, int hour, int minute) {
        this.id = id;
        this.sent_by = sent_by;
        this.date = date;
        this.message = message;
        this.hour = hour;
        this.minute = minute;
    }

    public String getSent_by() {
        return sent_by;
    }

    public void setSent_by(String sent_by) {
        this.sent_by = sent_by;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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


    public String toString() {
        return "id = " + this.id + ", Message: " + this.message;
    }
}





