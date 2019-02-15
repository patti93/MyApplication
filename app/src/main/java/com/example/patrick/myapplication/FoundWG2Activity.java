package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FoundWG2Activity extends AppCompatActivity {

    String[] adress;
    String[] wgData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_wg2);
        Intent intent = getIntent();
        adress = intent.getStringArrayExtra("wg_data");
    }

    public void redirectMain(View view){
        Intent intent = new Intent(this,MainMenuActivity.class);
        startActivity(intent);
        WG4U_DataSource dataSource = new WG4U_DataSource(this);
        dataSource.open();
        wgData = getWgDetails();
        dataSource.insertWg(wgData[0],adress[0],adress[1],adress[2],adress[3],adress[4],wgData[2],wgData[1]);
        dataSource.getAllWGs();
        dataSource.close();
    }

    private String[] getWgDetails(){
        String[] wgInput = new String[3];

        EditText inputName = findViewById(R.id.input_wg_name);
        EditText inputWgPassword = findViewById(R.id.input_wg_password);
        EditText inputDescription = findViewById(R.id.input_wg_password);

        wgInput[0] = inputName.getText().toString();
        wgInput[1] = inputWgPassword.getText().toString();
        wgInput[2] = inputDescription.getText().toString();

        return wgInput;
    }

}
