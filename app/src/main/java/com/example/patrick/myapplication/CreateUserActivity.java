package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.util.regex.*;
import android.widget.EditText;
import android.widget.Toast;


public class CreateUserActivity extends AppCompatActivity {



    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }

    private int safeUser(){

        EditText inputFirstName = findViewById(R.id.editTextFirstName);
        EditText inputName = findViewById(R.id.editTextName);
        EditText inputBday = findViewById(R.id.editTextBday);
        EditText inputMail = findViewById(R.id.editTextMail);
        EditText inputPassword = findViewById(R.id.editTextPassword);
        EditText inputPassword2 = findViewById(R.id.editTextConfirmPassword);

        String firstName = inputFirstName.getText().toString();
        String name = inputName.getText().toString();
        String bday = inputBday.getText().toString();
        String mail = inputMail.getText().toString();
        String password = inputPassword.getText().toString();
        String password2 = inputPassword2.getText().toString();



        if(isEmpty(inputBday) || isEmpty(inputFirstName) || isEmpty(inputMail) || isEmpty(inputName) || isEmpty(inputPassword) || isEmpty(inputPassword2)){
            return 4;
        }
        if(!password.equals(password2)) return 1;

        if(password.length() < 8) return 3;

        Pattern email_pattern = Pattern.compile("[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+.[a-zA-Z0-9-]+");

        if(!email_pattern.matcher("mail").matches()) return 2;

        return 0;




    }


    public void clickSignUp(View view){

        int check = safeUser();

        if(check == 0) successRedirect(view);
        else if(check == 1)Toast.makeText(CreateUserActivity.this,R.string.no_password_match, Toast.LENGTH_SHORT).show();
        else if(check == 2)Toast.makeText(CreateUserActivity.this,R.string.mail_not_valid, Toast.LENGTH_SHORT).show();
        else if(check == 3)Toast.makeText(CreateUserActivity.this,R.string.short_password, Toast.LENGTH_SHORT).show();
        else if(check == 4)Toast.makeText(CreateUserActivity.this,R.string.empty_Fields, Toast.LENGTH_SHORT).show();
        else if(check == 5)Toast.makeText(CreateUserActivity.this,R.string.bday_format_error, Toast.LENGTH_SHORT).show();
        else Toast.makeText(CreateUserActivity.this,R.string.user_exists, Toast.LENGTH_SHORT).show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }

    public void successRedirect(View view){
        Intent intent = new Intent(this,CreateUserSuccess.class);
        startActivity(intent);
    }
}
