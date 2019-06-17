package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MainMenuActivity extends AppCompatActivity {

    String wgName;
    ActiveResident activeResident;
    ActiveWG activeWG;
    WG4U_DataSource dataSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        /*
        dataSource = new WG4U_DataSource(this);
        dataSource.open();
        activeResident = new ActiveResident(this);

        wgName = dataSource.findResidentsWg(activeResident.getActiveResident()).getName();


        if(wgName.equals(null)){
            //dataSource.close();
            Intent intent = new Intent(this,NoWGActivity.class);
            startActivity(intent);
            finish();
        }
        */
        activeWG = new ActiveWG(getApplicationContext());
        wgName = activeWG.getActiveWG().getName();
        TextView wgTextView = findViewById(R.id.wg_name);
        wgTextView.setText(wgName);
        wgTextView.setTextSize(32);
    }

    public void onClickUser(View view){

        Intent intent = new Intent(this,ShowUserActivity.class);
        startActivity(intent);

    }

    public void onClickWG(View view){

        Intent intent = new Intent(this,ShowWGActivity.class);
        startActivity(intent);

    }

    public void onClickCalender(View view){

        Intent intent = new Intent(this,ShowCalenderActivity.class);
        startActivity(intent);

    }

    public void onClickToDo(View view){

        Intent intent = new Intent(this,ShowToDoActivity.class);
        startActivity(intent);

    }

    public void onClickJournal(View view){

        Intent intent = new Intent(this,ShowJournalActivity.class);
        startActivity(intent);

    }
    public void onClickShoppingList(View view){

        Intent intent = new Intent(this,ShowShoppingListActivity.class);
        startActivity(intent);

    }

    public void onDestroy(){
        super.onDestroy();
        activeWG.unsetActiveWG();
        activeResident.unsetActiveResident();

    }










}
