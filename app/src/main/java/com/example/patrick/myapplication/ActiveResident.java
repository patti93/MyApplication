package com.example.patrick.myapplication;



import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;


public class ActiveResident {

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "ActiveResident";
    // All Shared Preferences Keys
    private static final String IS_LOGIN = "IsLoggedIn";
    // User name (make variable public to access from outside)
    public static final String KEY_ID = "id";
    public static final String KEY_FNAME = "firstName";
    public static final String KEY_LNAME = "lastName";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_BDAY = "birthday";
    public static final String KEY_PASSWORD = "password";

    public ActiveResident(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setActiveResident(Resident resident){

        editor.putLong(KEY_ID,resident.getId());
        editor.putString(KEY_FNAME,resident.getFirstName());
        editor.putString(KEY_LNAME,resident.getLastName());
        editor.putString(KEY_BDAY,resident.getBirthday());
        editor.putString(KEY_EMAIL,resident.getEmail());
        editor.putString(KEY_PASSWORD,resident.getPassword());

        editor.commit();
    }

    public Resident getActiveResident(){

        Resident resident = new Resident(pref.getLong(KEY_ID,0),pref.getString(KEY_FNAME,null),pref.getString(KEY_LNAME,null),pref.getString(KEY_BDAY,null),pref.getString(KEY_EMAIL,null),pref.getString(KEY_PASSWORD,null));

        return resident;


    }



}
