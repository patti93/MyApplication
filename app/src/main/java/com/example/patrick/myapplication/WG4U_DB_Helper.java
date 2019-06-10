package com.example.patrick.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class WG4U_DB_Helper extends SQLiteOpenHelper {

    private static final String LOG_TAG = WG4U_DB_Helper.class.getSimpleName();
    private static final int DB_VERSION = 1;

    private static final String SQL_CREATE_TABLE_USERS = "CREATE TABLE residents (id INTEGER PRIMARY KEY AUTOINCREMENT,firstName VARCHAR(32), lastName VARCHAR(32), bday VARCHAR(32), email VARCHAR(32), password VARCHAR(32))";

    private static final String SQL_CREATE_TABLE_WGS = "CREATE TABLE wgs (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(32), street VARCHAR(32), hnr VARCHAR(32),zip VARCHAR(32),town VARCHAR(32), country VHCHAR, description TEXT, password VARCHAR(32))";
    private static final String SQL_CREATE_TABLE_LIVES_IN = "CREATE TABLE lives_in (wg_id INTEGER, resident_id INTEGER PRIMARY KEY)";

    private static final String SQL_CREATE_TABLE_TASK = "CREATE TABLE tasks (id INTEGER PRIMARY KEY AUTOINCREMENT,title VARCHAR(32), description TEXT)";
    private static final String SQL_CREATE_TABLE_HAS_TASK = "CREATE TABLE has_task (resident_id INTEGER, task_id INTEGER)";

    private static final String SQL_CREATE_TABLE_APPOINTMENT = "CREATE TABLE appointment (id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR(32), date VARCHAR(32), description TEXT,hour INT(2),minute INT(2))";
    private static final String SQL_CREATE_TABLE_HAS_APPOINTMENT = "CREATE TABLE has_appointment (wg_id INTEGER, appointment_id INTEGER)";

    private static final String SQL_CREATE_TABLE_SHOPPING_ITEM = "CREATE TABLE shopping_item (id INTEGER PRIMARY KEY AUTOINCREMENT, item_name TEXT)";
    private static final String SQL_CREATE_TABLE_HAS_SHOPPING_ITEM = "CREATE TABLE has_shopping_item (wg_id INTEGER, shopping_item_id INTEGER)";

    private static final String SQL_CREATE_TABLE_TODO = "CREATE TABLE todo (id INTEGER PRIMARY KEY AUTOINCREMENT, todo_name TEXT)";
    private static final String SQL_CREATE_TABLE_HAS_TODO = "CREATE TABLE has_todo (wg_id INTEGER, todo_id INTEGER)";


    private static final String SQL_CREATE_TABLE_JOURNAL_ENTRY = "CREATE TABLE journal_entry (id INTEGER PRIMARY KEY AUTOINCREMENT,sent_by VARCHAR(32), message TEXT, date VARCHAR(32), hour INT(2), minute INT(2))";
    private static final String SQL_CREATE_TABLE_WG_JOURNAL = "CREATE TABLE wg_journal (wg_id INTEGER, journal_entry_id INTEGER)";

    private static  final String DB_NAME = "wg4u.db";



    WG4U_DB_Helper(Context context) {
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
            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_LIVES_IN + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_LIVES_IN);

            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_TASK + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_TASK);
            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_HAS_TASK + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_HAS_TASK);

            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_APPOINTMENT + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_APPOINTMENT);
            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_HAS_APPOINTMENT + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_HAS_APPOINTMENT);

            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_SHOPPING_ITEM + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_SHOPPING_ITEM);
            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_HAS_SHOPPING_ITEM + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_HAS_SHOPPING_ITEM);

            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_TODO + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_TODO);
            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_HAS_TODO + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_HAS_TODO);

            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_JOURNAL_ENTRY + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_JOURNAL_ENTRY);
            Log.d(LOG_TAG,"Die Tabelle wird mit SQL-Befehl: " + SQL_CREATE_TABLE_WG_JOURNAL + " angelegt.");
            db.execSQL(SQL_CREATE_TABLE_WG_JOURNAL);

        }
        catch ( Exception e){
            Log.e(LOG_TAG,"Fehler beim anlegen einer Tabelle");
        }

    }

    //@Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}


