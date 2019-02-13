package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public boolean checkuser(String user, String password){

        // TBD: check for real users
        return user.equals("patrick") && password.equals("12345");

    }

    //called when the user taps the login button
    public void clickLogin(View view){

        EditText inputUser = findViewById(R.id.editTextUser);
        EditText inputPassword = findViewById(R.id.editTextPassword);

        String user = inputUser.getText().toString();
        String password = inputPassword.getText().toString();

        if(checkuser(user, password)) {

            Intent intent = new Intent(this, NoWGActivity.class);
            startActivity(intent);
        }
        else Toast.makeText(MainActivity.this, "Wrong user or password!",Toast.LENGTH_LONG).show();
    }

    public void signUpRedirect(View view){

        Intent intent = new Intent(this,CreateUserActivity.class);
        startActivity(intent);
    }





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
