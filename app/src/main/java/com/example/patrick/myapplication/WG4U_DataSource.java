package com.example.patrick.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;

public class WG4U_DataSource {

    private static final String LOG_TAG = WG4U_DataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private WG4U_DB_Helper dbHelper;

    private String [] columns = {"id","firstName","lastName", "bday","email", "password"};


    public WG4U_DataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new WG4U_DB_Helper(context);
    }

    public void open() {
        Log.d(LOG_TAG, "Eine Referenz auf die Datenbank wird jetzt angefragt.");
        database = dbHelper.getWritableDatabase();
        Log.d(LOG_TAG, "Datenbank-Referenz erhalten. Pfad zur Datenbank: " + database.getPath());
    }

    public void close() {
        dbHelper.close();
        Log.d(LOG_TAG, "Datenbank mit Hilfe des DbHelpers geschlossen.");
    }

    public Resident insertResident(String firstname, String lastname, String birthday, String email, String password){
        ContentValues values = new ContentValues();
        values.put("firstName",firstname);
        values.put("lastName",lastname);
        values.put("bday",birthday);
        values.put("email",email);
        values.put("password",password);

        long insertID = database.insert("residents",null,values);

        Cursor cursor = database.query("residents",columns,"id = " + insertID, null,null,null,null);

        cursor.moveToFirst();
        Resident resident = cursorToResident(cursor);

        return resident;
    }

    //returns a list of residents where the searchString applies
    public List<Resident> getResidentsSearch(String searchString){

        List<Resident> residentList = new ArrayList<>();

        Cursor cursor = database.query("residents",columns,searchString,null,null,null, null);

        if(cursor.getCount() == 0) return null;

        cursor.moveToFirst();
        Resident resident;

        while (!cursor.isAfterLast()){

            resident = cursorToResident(cursor);
            residentList.add(resident);
            Log.d(LOG_TAG, "ID: " + resident.getId() + ", Inhalt: " + resident.toString());
            cursor.moveToNext();
        }
        cursor.close();

        return residentList;


    }


    public Resident cursorToResident(Cursor cursor){

        long id = cursor.getLong(cursor.getColumnIndex("id"));
        String firstname = cursor.getString(cursor.getColumnIndex("firstName"));
        String lastname = cursor.getString(cursor.getColumnIndex("lastName"));
        String bday = cursor.getString(cursor.getColumnIndex("bday"));
        String email = cursor.getString(cursor.getColumnIndex("email"));
        String password = cursor.getString(cursor.getColumnIndex("password"));


        Resident resident = new Resident(firstname,lastname,bday,email,password);

        return resident;
    }

    public List<Resident> getAllResidents(){

        List<Resident> residentList = new ArrayList<>();

        Cursor cursor = database.query("residents",columns,null,null,null,null, null);

        cursor.moveToFirst();
        Resident resident;

        while (!cursor.isAfterLast()){

            resident = cursorToResident(cursor);
            residentList.add(resident);
            Log.d(LOG_TAG, "ID: " + resident.getId() + ", Inhalt: " + resident.toString());
            cursor.moveToNext();
        }
        cursor.close();

        return residentList;

    }






}
