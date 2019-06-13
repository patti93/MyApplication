package com.example.patrick.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowUserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_user);


        ActiveResident activeResident = new ActiveResident(this);

        TextView firstname = findViewById(R.id.firstNameTextView);
        firstname.setText(activeResident.getActiveResident().getFirstName());

        TextView name = findViewById(R.id.nameTextView);
        name.setText(activeResident.getActiveResident().getLastName());

        TextView bday = findViewById(R.id.bdayTextView);
        bday.setText(activeResident.getActiveResident().getbday());

        TextView email = findViewById(R.id.mailTextView);
        email.setText(activeResident.getActiveResident().getEmail());


    }
}
