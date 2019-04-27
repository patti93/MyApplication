package com.example.patrick.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class WG4U_DataSource {

    private static final String LOG_TAG = WG4U_DataSource.class.getSimpleName();

    private SQLiteDatabase database;
    private WG4U_DB_Helper dbHelper;

    private String [] residentColumns = {"id","firstName","lastName", "bday","email", "password"};

    private String [] wgColumns = {"id","name","street","hnr","zip","town","country","description","password"};
    private String [] lives_inColumns = {"wg_id","resident_id"};

    private String [] appointmentCollumns = {"id","name","date","description","hour","minute"};
    private String [] has_appointmentColumns = {"wg_id","appointment_id"};

    private String [] shoppingItemColumns = {"id", "item_name"};
    private String [] has_shopping_itemColumns = {"wg_id", "shopping_item_id"};

    private String [] todoColumns = {"id", "todo_name"};
    private String [] has_todoColumns = {"wg_id", "todo_id"};

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

        Cursor cursor = database.query("wgs",wgColumns,null,null,null,null, "name ASC");

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
    public Appointment insertAppointment(String name, String date, String description,int hour, int minute){

        long insertID;

        ContentValues values = new ContentValues();

        values.put("name",name);
        values.put("date",date);
        values.put("description",description);
        values.put("hour",hour);
        values.put("minute",minute);

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
        int hour = cursor.getInt(cursor.getColumnIndex("hour"));
        int minute = cursor.getInt(cursor.getColumnIndex("minute"));

        Appointment appointment = new Appointment(id, name, date, description,hour,minute);

        return appointment;
    }

    public List<Appointment> getAppointmentsSearch(String searchstring){

        List<Appointment> appointmentList = new ArrayList<>();

        Cursor cursor;

        cursor = database.query("appointment",appointmentCollumns,searchstring,null,null,null,"hour ASC",null);

        cursor.moveToFirst();

        Log.d(LOG_TAG,"liste:");
        while (!cursor.isAfterLast()){

            appointmentList.add(cursorToAppointment(cursor));
            cursor.moveToNext();
        }
        cursor.close();


        //Log.d(LOG_TAG,"Termine von WG" );
        for(Appointment appointment: appointmentList){

            Log.d(LOG_TAG,appointment.toString());

        }

        return appointmentList;
    }

    public int deleteAppointment(long id){

        return database.delete("appointment","id=" + Long.toString(id),null);
    }


    public int updateAppointment(long id, String name,String date, String description, int hour, int minute){

        ContentValues values = new ContentValues();

        values.put("name",name);
        values.put("date",date);
        values.put("description",description);
        values.put("hour",hour);
        values.put("minute",minute);

        return database.update("appointment",values,"id = " + Long.toString(id),null);

    }



//has appointment operations
    public long associateAppointmentToWG(Wg wg, Appointment appointment){


        ContentValues values = new ContentValues();

        values.put("wg_id",wg.getId());
        values.put("appointment_id",appointment.getId());

        return database.insert("has_appointment",null,values);

    }


        public List<Appointment> getWgAppointments(Wg wg,String date){

            String sqlcond = "";

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

        //Append IDs for SQL Statement
        for (int i = 0; i < appointmenIDs.size(); i++){

            if(i==0) sqlcond = "id = " + Long.toString(appointmenIDs.get(i));
            else sqlcond = sqlcond + " OR id = " + Long.toString(appointmenIDs.get(i));

        }

        sqlcond = "(" + sqlcond + ") AND date = '" + date + "'";
        //only execute if appointments exist
        if(appointmenIDs.size()>0) appointmentListResult = getAppointmentsSearch(sqlcond);

        cursor.close();

        Log.d(LOG_TAG,"Termine von WG" + wg.getName());
        for(Appointment appointment: appointmentListResult){

            Log.d(LOG_TAG,appointment.toString());

        }

        return appointmentListResult;

    }


    // Shopping List Operations

    // Insert item_name name in table shopping_item.
    // Returns itemID
    public long insertShoppingItem(String shoppingItem, Wg wg){

        if (getShoppingItemIDsForThisWG(shoppingItem, wg).size() == 0) {
            ContentValues values = new ContentValues();
            values.put("item_name", shoppingItem);

            return database.insert("shopping_item", null, values);
        }
        else
            return 0;
    }

    // Insert wg_id and shopping_item_id in table has_shopping_item
    // Returns itemID
    public long associateShoppingListToWG(Wg wg, long itemId){


        ContentValues values = new ContentValues();

        values.put("wg_id",wg.getId());
        values.put("shopping_item_id",itemId);

        return database.insert("has_shopping_item",null,values);

    }

    // Get item_name (String) from table shopping_item.
    // Requires Wg Object
    public ArrayList<String> getShoppingItemNameList(Wg wg) {

        ArrayList<Long> shoppingItemIDs = new ArrayList<>();

        //returns a cursor containing all shopping item IDs for a certain WG
        Cursor cursor = database.query("has_shopping_item",has_shopping_itemColumns,
                "wg_id = " + wg.getId() ,null,null,null,null,null);

        cursor.moveToFirst();

        //create an ArrayList with all shoppingItem IDs
        while (!cursor.isAfterLast()){
            shoppingItemIDs.add(cursor.getLong(cursor.getColumnIndex("shopping_item_id")));
            cursor.moveToNext();
        }
        cursor.close();

        return getShoppingItemNamesSearch(shoppingItemIDs);

    }

    // Helper method for getWgShoppingList.
    // Get item_name (String) from table shopping_item.
    // Requires item id
    private ArrayList<String> getShoppingItemNamesSearch(ArrayList<Long> shoppingItemIDs) {

        ArrayList<String> shoppingListResult = new ArrayList<>();

        for (int i = 0; i < shoppingItemIDs.size(); i++) {
            Cursor cursor = database.query("shopping_item", shoppingItemColumns,
                    "id = " + shoppingItemIDs.get(i), null, null, null, null, null);

            cursor.moveToFirst();

            //create an ArrayList with all shoppingItem IDs
            while (!cursor.isAfterLast()) {
                shoppingListResult.add(cursor.getString(cursor.getColumnIndex("item_name")));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return shoppingListResult;
    }


    // deletes all  Shopping Items with that item_name in this wg
    public void deleteShoppingItem(String item_name, Wg wg) {

        ArrayList<String> itemIDs = this.getShoppingItemIDsForThisWG(item_name, wg);

        for (int i = 0; i < itemIDs.size(); i++) {
            database.delete("shopping_item", "id=?", new String[]{itemIDs.get(i)});
            database.delete("has_shopping_item", "shopping_item_id=? AND wg_id=?", new String[]{itemIDs.get(i), String.valueOf(wg.getId())} );
        }
    }

    public ArrayList<String> getShoppingItemIDsForThisWG(String item_name, Wg wg) {

        ArrayList<String> shoppingItemIDs = new ArrayList<>();
        ArrayList<String> resultIDs = new ArrayList<>();

        // Get List of IDs with item_name
        Cursor cursor =     this.database.query("shopping_item", shoppingItemColumns, "item_name='"+item_name+"'",null, null
                            ,null, null, null);

        if (cursor.moveToFirst()) {

            //create an ArrayList with all shoppingItem IDs
            while (!cursor.isAfterLast()) {
                shoppingItemIDs.add(cursor.getString(cursor.getColumnIndex("id")));
                cursor.moveToNext();
            }
        }
        cursor.close();

        // Lookup the right item_id for this WG
        for (int i = 0; i < shoppingItemIDs.size(); i++) {
            cursor =    this.database.query("has_shopping_item", has_shopping_itemColumns, "shopping_item_id='" + shoppingItemIDs.get(i) + "' and wg_id='"+wg.getId()+"'", null, null
                        , null, null, null);

            if(cursor.moveToFirst()) {

                //create an ArrayList with all shoppingItem IDs
                while (!cursor.isAfterLast()) {
                    resultIDs.add(cursor.getString(cursor.getColumnIndex("shopping_item_id")));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }

        return resultIDs;
    }


    // ToDo List Operations

    // Insert todo_name name in table todo.
    // Returns itemID
    public long insertTodo(String todo_name, Wg wg){

        if (getTodoIDsForThisWG(todo_name, wg).size() == 0) {
            ContentValues values = new ContentValues();
            values.put("todo_name", todo_name);

            return database.insert("todo", null, values);
        }
        else
            return 0;
    }


    // Insert wg_id and todo_id in table has_todo
    // Returns itemID
    public long associateTodoToWG(Wg wg, long todoId){


        ContentValues values = new ContentValues();

        values.put("wg_id",wg.getId());
        values.put("todo_id",todoId);

        return database.insert("has_todo",null,values);

    }

    // Get todo_name (String) from table todo.
    // Requires Wg Object
    public ArrayList<String> getTodoNameList(Wg wg) {

        ArrayList<Long> todoIDs = new ArrayList<>();

        //returns a cursor containing all todo IDs for a certain WG
        Cursor cursor = database.query("has_todo",has_todoColumns,
                "wg_id = " + wg.getId() ,null,null,null,null,null);

        cursor.moveToFirst();

        //create an ArrayList with all ToDo IDs
        while (!cursor.isAfterLast()){
            todoIDs.add(cursor.getLong(cursor.getColumnIndex("todo_id")));
            cursor.moveToNext();
        }
        cursor.close();

        return getTodoNamesSearch(todoIDs);

    }

    // Helper method for getWgShoppingList.
    // Get item_name (String) from table shopping_item.
    // Requires item id
    private ArrayList<String> getTodoNamesSearch(ArrayList<Long> todoIDs) {

        ArrayList<String> todoListResult = new ArrayList<>();

        for (int i = 0; i < todoIDs.size(); i++) {
            Cursor cursor = database.query("todo", todoColumns,
                    "id = " + todoIDs.get(i), null, null, null, null, null);

            cursor.moveToFirst();

            //create an ArrayList with all shoppingItem IDs
            while (!cursor.isAfterLast()) {
                todoListResult.add(cursor.getString(cursor.getColumnIndex("todo_name")));
                cursor.moveToNext();
            }
            cursor.close();
        }

        return todoListResult;
    }


    // deletes all  Shopping Items with that item_name in this wg
    public void deleteTodo(String todo_name, Wg wg) {

        ArrayList<String> todoIDs = this.getTodoIDsForThisWG(todo_name, wg);

        for (int i = 0; i < todoIDs.size(); i++) {
            database.delete("todo", "id=?", new String[]{todoIDs.get(i)});
            database.delete("has_todo", "todo_id=? AND wg_id=?", new String[]{todoIDs.get(i), String.valueOf(wg.getId())} );
        }
    }

    public ArrayList<String> getTodoIDsForThisWG(String todo_name, Wg wg) {

        ArrayList<String> todoIDs = new ArrayList<>();
        ArrayList<String> resultIDs = new ArrayList<>();

        // Get List of IDs with todo_name
        Cursor cursor =     this.database.query("todo", todoColumns, "todo_name='"+todo_name+"'",null, null
                ,null, null, null);

        if (cursor.moveToFirst()) {

            //create an ArrayList with all shoppingItem IDs
            while (!cursor.isAfterLast()) {
                todoIDs.add(cursor.getString(cursor.getColumnIndex("id")));
                cursor.moveToNext();
            }
        }
        cursor.close();

        // Lookup the right item_id for this WG
        for (int i = 0; i < todoIDs.size(); i++) {
            cursor =    this.database.query("has_todo", has_todoColumns, "todo_id='" + todoIDs.get(i) + "' and wg_id='"+wg.getId()+"'", null, null
                    , null, null, null);

            if(cursor.moveToFirst()) {

                //create an ArrayList with all shoppingItem IDs
                while (!cursor.isAfterLast()) {
                    resultIDs.add(cursor.getString(cursor.getColumnIndex("todo_id")));
                    cursor.moveToNext();
                }
            }
            cursor.close();
        }

        return resultIDs;
    }

}
