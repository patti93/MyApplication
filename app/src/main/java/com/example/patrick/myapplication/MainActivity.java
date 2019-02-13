package com.example.patrick.myapplication;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.strictmode.SqliteObjectLeakedViolation;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private WG4U_DataSource dataSource;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getDelegate().onDestroy();
    }
    public boolean checkuser(String user, String password){


        dataSource = new WG4U_DataSource(this);

        dataSource.open();

        List<Resident> residentList = new ArrayList<>();

        residentList = dataSource.getResidentsSearch("email = " + "'" + user + "'");

        if(residentList != null) {

            if (residentList.get(0).getPassword().equals(password)) {
                dataSource.close();
                return true;
            }
        }

        dataSource.close();
        return false;

    }

    //called when the user taps the login button
    public void clickLogin(View view){

        EditText inputUser = findViewById(R.id.editTextUser);
        EditText inputPassword = findViewById(R.id.editTextPassword);

        String user = inputUser.getText().toString();
        String password = inputPassword.getText().toString();

        if(user != null && password != null) {

            if (checkuser(user, password)) {

            Intent intent = new Intent(this, NoWGActivity.class);
            startActivity(intent);
            }

        }
        else Toast.makeText(MainActivity.this, "Wrong user or password!",Toast.LENGTH_LONG).show();
    }

    public void signUpRedirect(View view){

        Intent intent = new Intent(this,CreateUserActivity.class);
        startActivity(intent);
    }

}
