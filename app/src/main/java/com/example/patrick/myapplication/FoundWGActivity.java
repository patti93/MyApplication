package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FoundWGActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_wg);
    }

    public void redirect(View view){

        EditText inputStreet = findViewById(R.id.input_wg_Street);
        EditText inputHnr = findViewById(R.id.input_wg_hnr);
        EditText inputTown = findViewById(R.id.input_town);
        EditText inputZip = findViewById(R.id.input_zip_code);
        EditText inputCounty = findViewById(R.id.input_country);


        if(isEmpty(inputCounty) || isEmpty(inputHnr) || isEmpty(inputTown) || isEmpty(inputZip) || isEmpty(inputStreet)){

            Toast.makeText(FoundWGActivity.this,R.string.empty_Fields,Toast.LENGTH_SHORT).show();
        }
        else{
            Intent intent = new Intent(this,FoundWG2Activity.class);
            intent.putExtra("wg_data",getAddressInput());
            startActivity(intent);
        }

    }


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }

    private String[] getAddressInput(){

        String[] address = new String[5];

        EditText inputStreet = findViewById(R.id.input_wg_Street);
        EditText inputHnr = findViewById(R.id.input_wg_hnr);
        EditText inputTown = findViewById(R.id.input_town);
        EditText inputZip = findViewById(R.id.input_zip_code);
        EditText inputCounty = findViewById(R.id.input_country);



        address[0] = inputStreet.getText().toString();
        address[1] =  inputHnr.getText().toString();
        address[2] =  inputTown.getText().toString();
        address[3] =  inputZip.getText().toString();
        address[4] =  inputCounty.getText().toString();

        return address;

    }

}
