package com.example.patrick.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WG4U_DB_Helper extends SQLiteOpenHelper {

    private static final String LOG_TAG = WG4U_DB_Helper.class.getSimpleName();
    public static final int DB_VERSION = 1;
    public static final String SQL_CREATE_TABLE_USERS = "CREATE TABLE residents (id INTEGER PRIMARY KEY AUTOINCREMENT,firstName VCHAR, lastName VCHAR, bday VCHAR, email VCHAR, password VCHAR)";
    public static final String SQL_CREATE_TABLE_WGS = "CREATE TABLE wgs (id INTEGER PRIMARY KEY AUTOINCREMENT, name VCHAR, street VCHAR, hnr VCHAR,zip VCHAR,town VCHAR, country VHCHAR, description TEXT, password VCHAR)";
    public static  final String DB_NAME = "wg4u.db";



    public WG4U_DB_Helper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        Log.d(LOG_TAG, "DbHelper hat die Datenbank: " + getDatabaseName() + " erzeugt.");
    }

    //@Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_USERS + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_USERS);
            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_WGS + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_WGS);
        }
        catch ( Exception e){
            Log.e(LOG_TAG,"Fehler beim anlegen einer Tabelle");
        }

    }

    //@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


