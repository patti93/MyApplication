package com.example.patrick.myapplication;

import android.content.Context;
import android.content.Intent;
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
        //found a resident
        if(residentList != null) {
            Resident resident = residentList.get(0);
            //check the password
            if (resident.getPassword().equals(password)) {
                dataSource.close();
                ActiveResident activeResident = new ActiveResident(getApplicationContext());
                activeResident.setActiveResident(resident);
                Log.d(LOG_TAG,"aktiver User:" + activeResident.getActiveResident().getEmail());
                return true;
            }
        }
        //login failed
        dataSource.close();
        return false;

    }

    //called when the user taps the login button
    public void clickLogin(View view){

        EditText inputUser = findViewById(R.id.editTextUser);
        EditText inputPassword = findViewById(R.id.editTextPassword);

        String user = inputUser.getText().toString();
        String password = inputPassword.getText().toString();

        if (checkuser(user, password)) {

            Intent intent = new Intent(this, NoWGActivity.class);
            startActivity(intent);
            finish();
        }
        else Toast.makeText(MainActivity.this, R.string.wrong_login,Toast.LENGTH_LONG).show();
    }

    public void signUpRedirect(View view){
        Intent intent = new Intent(this,CreateUserActivity.class);
        startActivity(intent);
    }

}
