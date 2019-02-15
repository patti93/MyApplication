package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class FoundWGActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_wg);
    }

    public void redirect(View view){
        Intent intent = new Intent(this,FoundWG2Activity.class);
        intent.putExtra("wg_data",getAdressInput());
        startActivity(intent);
    }


    private String[] getAdressInput(){

        String[] adress = new String[5];

        EditText inputStreet = findViewById(R.id.input_wg_Street);
        EditText inputHnr = findViewById(R.id.input_wg_hnr);
        EditText inputTown = findViewById(R.id.input_town);
        EditText inputZip = findViewById(R.id.input_zip_code);
        EditText inputCounty = findViewById(R.id.input_country);

        adress[0] = inputStreet.getText().toString();
        adress[1] =  inputHnr.getText().toString();
        adress[2] =  inputTown.getText().toString();
        adress[3] =  inputZip.getText().toString();
        adress[4] =  inputCounty.getText().toString();

        return adress;

    }

}
