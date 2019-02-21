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

    private String [] residentColumns = {"id","firstName","lastName", "bday","email", "password"};
    private String [] wgColumns = {"id","name","street","hnr","zip","town","country","description","password"};
    private String [] lives_inColumns = {"wg_id","resident_id"};
    private String [] appointmentCollumns = {"id","name","date","description"};
    private String [] has_appointmentColumns = {"wg_id","appointment_id"};

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

        Cursor cursor = database.query("residents",residentColumns,"id = " + insertID, null,null,null,null);

        cursor.moveToFirst();
        Resident resident = cursorToResident(cursor);

        return resident;
    }


    //Resident operations

    //returns a list of residents where the searchString applies
    public List<Resident> getResidentsSearch(String searchString){

        List<Resident> residentList = new ArrayList<>();

        Cursor cursor = database.query("residents",residentColumns,searchString,null,null,null, null);

        cursor.moveToFirst();
        Resident resident;

        while (!cursor.isAfterLast()){

            resident = cursorToResident(cursor);
            residentList.add(resident);
            Log.d(LOG_TAG, "ID: " + resident.getId() + ", Inhalt: " + resident.toString());
            cursor.moveToNext();
        }
        cursor.close();
        Log.d(LOG_TAG,Integer.toString(residentList.size()));

        return residentList;


    }

    public Resident cursorToResident(Cursor cursor){

        long id = cursor.getLong(cursor.getColumnIndex("id"));
        String firstname = cursor.getString(cursor.getColumnIndex("firstName"));
        String lastname = cursor.getString(cursor.getColumnIndex("lastName"));
        String bday = cursor.getString(cursor.getColumnIndex("bday"));
        String email = cursor.getString(cursor.getColumnIndex("email"));
        String password = cursor.getString(cursor.getColumnIndex("password"));


        Resident resident = new Resident(id,firstname,lastname,bday,email,password);

        return resident;
    }

    public List<Resident> getAllResidents(){

        List<Resident> residentList = new ArrayList<>();

        Cursor cursor = database.query("residents",residentColumns,null,null,null,null, null);

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


    //WG Operations
    public Wg insertWg(String name, String street, String hnr,String zip,String town, String country, String description, String password){

        Wg wg;

        ContentValues values = new ContentValues();

        values.put("name",name);
        values.put("street",street);
        values.put("hnr",hnr);
        values.put("zip",zip);
        values.put("town",town);
        values.put("country",country);
        values.put("description",description);
        values.put("password",password);

        long insertID = database.insert("wgs",null,values);

        Cursor cursor = database.query("wgs",wgColumns,"id = " + insertID, null,null,null,null);

        cursor.moveToFirst();

        wg = cursorToWg(cursor);
        return wg;
    }

    public Wg cursorToWg(Cursor cursor){

        long id = cursor.getLong(cursor.getColumnIndex("id"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String street = cursor.getString(cursor.getColumnIndex("street"));
        String hnr = cursor.getString(cursor.getColumnIndex("hnr"));
        String password = cursor.getString(cursor.getColumnIndex("password"));
        String zip = cursor.getString(cursor.getColumnIndex("zip"));
        String town = cursor.getString(cursor.getColumnIndex("town"));
        String description = cursor.getString(cursor.getColumnIndex("description"));
        String country = cursor.getString(cursor.getColumnIndex("country"));

        Wg wg = new Wg(id,name,street,hnr,zip,country,town,description,password);

        return wg;
    }

    public List<Wg> getAllWGs(){

        List<Wg> wgList = new ArrayList<>();

        Cursor cursor = database.query("wgs",wgColumns,null,null,null,null, null);

        cursor.moveToFirst();
        Wg wg;

        while (!cursor.isAfterLast()){

            wg = cursorToWg(cursor);
            wgList.add(wg);
            Log.d(LOG_TAG, "ID: " + wg.getId() + ", Inhalt: " + wg.toString());
            cursor.moveToNext();
        }
        cursor.close();

        return wgList;

    }

    public List<Wg> getWGsSearch(String searchString){


        Wg wg;
        List<Wg> wgList = new ArrayList<>();

        Cursor cursor = database.query("wgs",wgColumns,searchString,null,null,null, null);

        cursor.moveToFirst();

        while (!cursor.isAfterLast()){

            wg = cursorToWg(cursor);
            wgList.add(wg);
            Log.d(LOG_TAG, "ID: " + wg.getId() + ", Inhalt: " + wg.toString());
            cursor.moveToNext();

        }

        cursor.close();

        return wgList;

    }
    //lives in
    public long associateWgToResident(Wg wg, Resident resident){


        ContentValues values = new ContentValues();

        values.put("wg_id",wg.getId());
        values.put("resident_id",resident.getId());

        return database.insert("lives_in",null,values);

    }

    public Wg findResidentsWg(Resident resident){

        List<Wg> wgList = new ArrayList<>();
        Cursor cursor = database.query("lives_in",lives_inColumns,"resident_id = " + resident.getId(),null,null,null,null,null);

        cursor.moveToFirst();

        if(cursor.getCount() == 0){
            cursor.close();
            return null;
        }
        else{

            long wg_id = cursor.getLong(cursor.getColumnIndex("wg_id"));

            if (getWGsSearch("id = " +wg_id).size() != 0){
                wgList = getWGsSearch("id = " +wg_id);
                cursor.close();
                return wgList.get(0);
            }
        }
        cursor.close();

        return null;
    }

    public List<Resident> findWgResidents(Wg wg){

        List<Resident> residentListTemp = new ArrayList<>();
        List<Resident> residentListResult = new ArrayList<>();
        List<Long> residentIDs = new ArrayList<>();
        //returns a cursor containing all resident IDs for a certain WG
        Cursor cursor = database.query("lives_in",lives_inColumns,"wg_id = " + wg.getId(),null,null,null,null,null);

        cursor.moveToFirst();


        //create an ArrayList with all resident IDs
        while (!cursor.isAfterLast()){
            residentIDs.add(cursor.getLong(cursor.getColumnIndex("resident_id")));
            cursor.moveToNext();
        }

        //add one resident for each ID to the result
        for(Long i: residentIDs){
            residentListTemp = getResidentsSearch("id = " + i);
            if(residentListTemp.size()>0)
            residentListResult.add(residentListTemp.get(0));
        }

        cursor.close();

        Log.d(LOG_TAG,"Bewohner von WG" + wg.getName());
        for(Resident resident: residentListResult){

            Log.d(LOG_TAG,resident.toString());

        }
        return residentListResult;

    }



    //appointment operations
    public Appointment insertAppointment(String name, String date, String description){

        long insertID;

        ContentValues values = new ContentValues();

        values.put("name",name);
        values.put("date",date);
        values.put("description",description);

        insertID = database.insert("appointment",null,values);

        Cursor cursor = database.query("appointment",appointmentCollumns,"id = " + insertID,null,null,null,null,null);

        cursor.moveToFirst();
        Appointment appointment = cursorToAppointment(cursor);

        Log.d(LOG_TAG,"Termin hinzugefügt:" + appointment.toString());
        cursor.close();
        return appointment;
    }



    public Appointment cursorToAppointment(Cursor cursor){

        long id = cursor.getLong(cursor.getColumnIndex("id"));
        String name = cursor.getString(cursor.getColumnIndex("name"));
        String date = cursor.getString(cursor.getColumnIndex("date"));
        String description = cursor.getString(cursor.getColumnIndex("description"));

        Appointment appointment = new Appointment(id, name, date, description);

        return appointment;
    }

    public List<Appointment> getAppointmentsSearch(String searchstring){

        List<Appointment> appointmentList = new ArrayList<>();

        Cursor cursor;

        cursor = database.query("appointment",appointmentCollumns,searchstring,null,null,null,null,null);

        cursor.moveToFirst();

        //if(cursor.getCount() == 0) return null;


        while (!cursor.isAfterLast()){

            appointmentList.add(cursorToAppointment(cursor));
            cursor.moveToNext();
        }
        cursor.close();

        return appointmentList;
    }

//has appointment operations
    public long associateAppointmentToWG(Wg wg, Appointment appointment){


        ContentValues values = new ContentValues();

        values.put("wg_id",wg.getId());
        values.put("appointment_id",appointment.getId());

        return database.insert("has_appointment",null,values);

    }


        public List<Appointment> getWgAppointments(Wg wg,String date){

        List<Appointment> appointmentListTemp = new ArrayList<>();
        List<Appointment> appointmentListResult = new ArrayList<>();
        List<Long> appointmenIDs = new ArrayList<>();
        //returns a cursor containing all appointment IDs for a certain WG
        Cursor cursor = database.query("has_appointment",has_appointmentColumns,"wg_id = " + wg.getId() ,null,null,null,null,null);

        cursor.moveToFirst();

        //create an ArrayList with all appointment IDs
        while (!cursor.isAfterLast()){
            appointmenIDs.add(cursor.getLong(cursor.getColumnIndex("appointment_id")));
            cursor.moveToNext();
        }

        //add one appointment for each ID with matching date to the result
        for(Long i: appointmenIDs){
            appointmentListTemp = getAppointmentsSearch("id = " + i + " AND date = '" + date + "'");
            if(appointmentListTemp.size()>0)
            appointmentListResult.add(appointmentListTemp.get(0));
        }

        cursor.close();

        Log.d(LOG_TAG,"Termine von WG" + wg.getName());
        for(Appointment appointment: appointmentListResult){

            Log.d(LOG_TAG,appointment.toString());

        }

        return appointmentListResult;

    }





}
