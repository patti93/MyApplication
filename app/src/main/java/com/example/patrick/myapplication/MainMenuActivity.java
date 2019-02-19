package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    String wgName;
    ActiveResident activeResident;
    WG4U_DataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        dataSource = new WG4U_DataSource(this);
        dataSource.open();
        activeResident = new ActiveResident(this);

        wgName = dataSource.findResidentsWg(activeResident.getActiveResident());

        if(wgName.equals(null)){
            dataSource.close();
            Intent intent = new Intent(this,NoWGActivity.class);
            startActivity(intent);
            finish();
        }
        TextView wgTextView = findViewById(R.id.wg_name);
        wgTextView.setText(wgName);
        wgTextView.setTextSize(24);
    }
}
