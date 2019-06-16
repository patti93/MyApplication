package com.example.patrick.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ActiveWG {

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    // Sharedpref file name
    private static final String PREF_NAME = "ActiveWG";
    //Shared Preferences Keys
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "wgName";
    private static final String KEY_STREET = "wgStreet";
    private static final String KEY_HNR = "hnr";
    private static final String KEY_ZIP = "zip";
    private static final String KEY_TOWN = "town";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PASSWORD = "password";

    public ActiveWG(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void setActiveWG(Wg wg){

        editor.putLong(KEY_ID,wg.getId());
        editor.putString(KEY_NAME,wg.getName());
        editor.putString(KEY_STREET,wg.getStreet());
        editor.putString(KEY_HNR,wg.getHnr());
        editor.putString(KEY_ZIP,wg.getzip());
        editor.putString(KEY_TOWN,wg.getTown());
        editor.putString(KEY_COUNTRY,wg.getCountry());
        editor.putString(KEY_DESCRIPTION,wg.getDescription());
        editor.putString(KEY_PASSWORD,wg.getPassword());

        editor.commit();

    }

    public void unsetActiveWG(){

        editor.clear();
        editor.commit();

    }

    public Wg getActiveWG(){

        Wg wg = new Wg(pref.getLong(KEY_ID,0),pref.getString(KEY_NAME,null),
                pref.getString(KEY_STREET,null),pref.getString(KEY_HNR,null),pref.getString(KEY_ZIP,null),pref.getString(KEY_COUNTRY,null),pref.getString(KEY_TOWN,null),
                pref.getString(KEY_DESCRIPTION,null),pref.getString(KEY_PASSWORD,null));

        return wg;
    }

}
