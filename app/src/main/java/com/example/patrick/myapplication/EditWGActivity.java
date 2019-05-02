package com.example.patrick.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditWGActivity extends AppCompatActivity {

    WG4U_DataSource dataSource;
    ActiveResident activeResident;
    Wg wg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_wg);

        EditText inputStreet = findViewById(R.id.input_wg_Street);
        EditText inputHnr = findViewById(R.id.input_wg_hnr);
        EditText inputTown = findViewById(R.id.input_town);
        EditText inputZip = findViewById(R.id.input_zip_code);
        EditText inputCountry = findViewById(R.id.input_country);
        EditText inputDescription = findViewById(R.id.input_description);

        dataSource = new WG4U_DataSource(this);
        activeResident = new ActiveResident(this);

        Resident resident = activeResident.getActiveResident();

        dataSource.open();

        wg = dataSource.findResidentsWg(resident);

        dataSource.close();

        TextView headline = findViewById(R.id.textView);
        headline.setText(wg.getName());

        inputStreet.setText(wg.getStreet());

        inputHnr.setText(wg.getHnr());

        inputTown.setText(wg.getTown());

        inputZip.setText(wg.getZipCode());

        inputCountry.setText(wg.getCountry());

        inputDescription.setText(wg.getDescription());

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
        EditText inputCountry = findViewById(R.id.input_country);
        EditText inputDescription = findViewById(R.id.input_description);

        address[0] = inputStreet.getText().toString();
        address[1] =  inputHnr.getText().toString();
        address[2] =  inputTown.getText().toString();
        address[3] =  inputZip.getText().toString();
        address[4] =  inputCountry.getText().toString();

        return address;

    }


    public void onClickSafe(View view){


        EditText inputStreet = findViewById(R.id.input_wg_Street);
        EditText inputHnr = findViewById(R.id.input_wg_hnr);
        EditText inputTown = findViewById(R.id.input_town);
        EditText inputZip = findViewById(R.id.input_zip_code);
        EditText inputCountry = findViewById(R.id.input_country);
        EditText inputDescription = findViewById(R.id.input_description);

        if(isEmpty(inputCountry) || isEmpty(inputHnr) || isEmpty(inputTown) || isEmpty(inputZip) || isEmpty(inputStreet)){

            Toast.makeText(this,R.string.empty_Fields,Toast.LENGTH_SHORT).show();
        }
        else{
            dataSource = new WG4U_DataSource(this);
            dataSource.open();
            String[] address = getAddressInput();
            dataSource.updateWG(wg.getId(),address[0],address[1],address[3],address[2],address[4],inputDescription.getText().toString());
            finish();
        }

    }



}
