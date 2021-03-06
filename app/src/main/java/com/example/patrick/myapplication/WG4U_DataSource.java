package com.example.patrick.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.content.ContentValues;
import android.database.Cursor;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;




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

    private String [] journal_entryColumns = {"id","sent_by","message","date","hour","minute"};
    private String [] wg_journalColumns = {"wg_id","journal_entry_id"};

    private Context appcontext;

    public WG4U_DataSource(Context context) {
        Log.d(LOG_TAG, "Unsere DataSource erzeugt jetzt den dbHelper.");
        dbHelper = new WG4U_DB_Helper(context);
        appcontext = context;
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

    public boolean insertResident(String firstName, String lastName, String bday, String mail, String password){

        final JSONObject jsonResponse = new JSONObject();

        boolean check = false;
        final String paramfName = firstName;
        final String paramlName = lastName;
        final String paramBday = bday;
        final String paramMail = mail;
        final String paramPassword = password;

        /*Resident resident;
        long insertID = database.insert("residents",null,values);
        Cursor cursor = database.query("residents",residentColumns,"id = " + insertID, null,null,null,null);
        cursor.moveToFirst();
        Resident resident = cursorToResident(cursor);
        */

        final RequestQueue queue = Volley.newRequestQueue(appcontext);
        String url_create_usr = "https://sfwgfiuvrt1rmt6g.myfritz.net/create_user.php";

        StringRequest strRequest = new StringRequest(Request.Method.POST, url_create_usr,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                         try {
                             JSONObject message = new JSONObject(response);
                             Log.d(LOG_TAG,response);
                             jsonResponse.put("status",message.getInt("success"));
                             if(message.getInt("success") == 1) jsonResponse.put("id",message.getInt("id"));
                             Toast.makeText(appcontext, message.getString("message"), Toast.LENGTH_LONG).show();
                             queue.stop();
                         } catch (JSONException e){
                             Toast.makeText(appcontext, "json fehler", Toast.LENGTH_LONG).show();
                             Log.d(LOG_TAG,response);
                             queue.stop();
                         }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(appcontext, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String> values = new HashMap<>();
                values.put("firstName", paramfName);
                values.put("lastName", paramlName);
                values.put("bday", paramBday);
                values.put("email", paramMail);
                values.put("password", paramPassword);
                return values;
            }
        };
        queue.add(strRequest);
        //evaluate the response
        try {
            if(jsonResponse.getInt("status") == 1){
                check = true;
                Log.d(LOG_TAG,"user mit der ID:" + jsonResponse.getInt("id") + " angelegt");
            }
        } catch (JSONException e){
            check = false;
        }
        return check;
    }


    public JSONArray postRequest(String url, final Map<String,String> params){

        RequestQueue queue = Volley.newRequestQueue(appcontext);
        final JSONArray ret = new JSONArray();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d(LOG_TAG,response);
                        //try to create a JSONArray from the response and store them in return JSONArray
                        try {
                            JSONArray temp = new JSONArray(response);
                            for(int i = 0; i < temp.length(); i++){
                                ret.put(temp.getJSONObject(i));
                            }

                        }catch (JSONException e){
                            Toast.makeText(appcontext, e.getMessage(), Toast.LENGTH_LONG).show();
                            Log.d(LOG_TAG,e.getMessage());
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(appcontext,error.getMessage(),Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams()
            {

                return params;
            }

        };


        queue.add(stringRequest);


        return ret;

    }

    //Resident operations

    //returns a list of residents where the searchString applies
    public List<Resident> getResidentsSearch(String searchString){

        final List<Resident> residentList = new ArrayList<>();
        String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/get_user.php";
        Map<String,String> params = new HashMap<>();

        params.put("searchString",searchString);

        VolleyHelper volleyHelper = new VolleyHelper();

        VolleyHelper.makeStringRequestPOST(appcontext, url, params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d(LOG_TAG,message);
            }

            @Override
            public void onResponse(String response) {
                Log.d(LOG_TAG,response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Gson gson = new Gson();
                    Resident temp;
                    for(int i = 0; i < jsonArray.length(); i++){
                        temp = gson.fromJson(jsonArray.getString(i),Resident.class);
                        residentList.add(temp);
                    }
                }catch (JSONException e){

                }

            }
        });

        /*
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
*/
        Log.d(LOG_TAG,residentList.toString());


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

    public long leaveWG(long id){

        return database.delete("lives_in","resident_id=" + id,null);

    }

    public long updateWG(long wg_id,String street, String hnr,String zip,String town, String country, String description){

        ContentValues values = new ContentValues();

        values.put("street",street);
        values.put("hnr",hnr);
        values.put("zip",zip);
        values.put("town",town);
        values.put("country",country);
        values.put("description",description);

        return database.update("wgs",values,"id = " + wg_id,null);
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

        return database.delete("appointment","id=" + id,null);
    }


    public int updateAppointment(long id, String name,String date, String description, int hour, int minute){

        ContentValues values = new ContentValues();

        values.put("name",name);
        values.put("date",date);
        values.put("description",description);
        values.put("hour",hour);
        values.put("minute",minute);

        return database.update("appointment",values,"id = " + id,null);

    }



//has appointment operations
    public long associateAppointmentToWG(Wg wg, Appointment appointment){


        ContentValues values = new ContentValues();

        values.put("wg_id",wg.getId());
        values.put("appointment_id",appointment.getId());

        return database.insert("has_appointment",null,values);

    }

    public long deleteHasAppointment(Long appointment_id){

        return database.delete("has_appointment","id = " + appointment_id,null);
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

            if(i==0) sqlcond = "id = " + appointmenIDs.get(i);
            else sqlcond = sqlcond + " OR id = " + appointmenIDs.get(i);

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

    //Journal Operations
    public JournalEntry insertJournalEntry(String sent_by,String message, String date, int hour, int minute){

        long insertID;

        ContentValues values = new ContentValues();

        values.put("sent_by",sent_by);
        values.put("message",message);
        values.put("date",date);
        values.put("hour",hour);
        values.put("minute",minute);

        insertID = database.insert("journal_entry",null,values);

        Cursor cursor = database.query("journal_entry",journal_entryColumns,"id = " + insertID,null,null,null,null);

        cursor.moveToFirst();
        JournalEntry journalEntry= cursorToJournalEntry(cursor);

        Log.d(LOG_TAG,"Journaleintrag erstellt: " + journalEntry.toString() );

        return journalEntry;
    }

    public JournalEntry cursorToJournalEntry(Cursor cursor){

        long id = cursor.getLong(cursor.getColumnIndex("id"));
        String sent_by = cursor.getString(cursor.getColumnIndex("sent_by"));
        String message = cursor.getString(cursor.getColumnIndex("message"));
        String date = cursor.getString(cursor.getColumnIndex("date"));
        int hour = cursor.getInt(cursor.getColumnIndex("hour"));
        int minute = cursor.getInt(cursor.getColumnIndex("minute"));

        return new JournalEntry(id,sent_by,date,message,hour,minute);
    }


    public List<JournalEntry> getWGJournalEntries(Wg wg){

        List<JournalEntry> journalEntryListResult = new ArrayList<>();
        List<Long> journalEntryIDs = new ArrayList<>();

        //returns a cursor containing all JournalEntry IDs for a certain WG
        Cursor cursor = database.query("wg_journal",wg_journalColumns,"wg_id = " + wg.getId() ,null,null,null,null,null);

        cursor.moveToFirst();

        //create an ArrayList with all journalEntry IDs
        while (!cursor.isAfterLast()){
            journalEntryIDs.add(cursor.getLong(cursor.getColumnIndex("journal_entry_id")));
            cursor.moveToNext();
        }


        return journalEntryListResult;


    }
/*
    public ArrayList<JournalEntry> getJournalEntrySearch(String searchstring){








    }*/




    public long associateJournalEntryToWG(Wg wg, JournalEntry journalEntry){

        ContentValues values = new ContentValues();

        values.put("wg_id", wg.getId());
        values.put("journal_entry_id", journalEntry.getId());

        return database.insert("wg_journal",null,values);

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
