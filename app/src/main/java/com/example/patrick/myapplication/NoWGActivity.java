package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class NoWGActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_wg);
    }

    public void found_new_WG_button(View view){
        Intent intent = new Intent(this,FoundWGActivity.class);
        startActivity(intent);
    }

    public void join_WG_button(View view){
        Intent intent = new Intent(this,SearchWGActivity.class);
        startActivity(intent);
    }
}
