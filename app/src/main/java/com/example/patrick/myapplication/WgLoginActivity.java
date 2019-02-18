package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class WgLoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = WgLoginActivity.class.getSimpleName();
    WG4U_DataSource dataSource;
    String wgName;
    ActiveResident activeResident;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wg_login);
        Intent intent = getIntent();
        wgName = intent.getStringExtra("wgName");
        TextView wgTextView = findViewById(R.id.wgTextView);
        wgTextView.setText(wgName);
        wgTextView.setTextSize(24);
    }

    public void onClickJoin(View view){

        dataSource = new WG4U_DataSource(this);
        dataSource.open();

        EditText inputPassword = findViewById(R.id.input_wg_Login);
        String password = inputPassword.getText().toString();


        List<Wg> wgList = dataSource.getWGsSearch("name = '" + wgName + "'");
        if(wgList.get(0).getPassword().equals(password)){

            activeResident = new ActiveResident(this);
            Log.d(LOG_TAG,"Bewohner:" + activeResident.getActiveResident().getEmail() + " ist in: " + wgList.get(0).getName() + " eingezogen!");
            dataSource.associateWgToResident(wgList.get(0),activeResident.getActiveResident());
            dataSource.close();
            Intent intent = new Intent(this,MainMenuActivity.class);
            //kills all previous activities and direct to main menu
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

        }
        else {
            Toast.makeText(WgLoginActivity.this,R.string.check_password,Toast.LENGTH_SHORT).show();
            dataSource.close();
        }

    }

}
