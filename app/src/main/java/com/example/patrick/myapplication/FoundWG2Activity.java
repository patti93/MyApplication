package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class FoundWG2Activity extends AppCompatActivity {

    String[] adress;
    String[] wgData;
    private static final String LOG_TAG = FoundWG2Activity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_wg2);
        Intent intent = getIntent();
        adress = intent.getStringArrayExtra("wg_data");
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }

    public void redirectMain(View view){


        ActiveResident resident = new ActiveResident(this);
        WG4U_DataSource dataSource = new WG4U_DataSource(this);
        dataSource.open();
        wgData = getWgDetails();


        if(checkInput()){
            //check if there is already a wg with that name
            if (dataSource.getWGsSearch("name = '" + wgData[0] + "'").size() == 0) {
                Wg wg = dataSource.insertWg(wgData[0], adress[0], adress[1], adress[2], adress[3], adress[4], wgData[2], wgData[1]);
                dataSource.associateWgToResident(wg,resident.getActiveResident());
                Log.d(LOG_TAG,"Bewohner:" + resident.getActiveResident().getEmail() + " ist in: " + wg.getName() + " eingezogen!");
                Intent intent = new Intent(this,MainMenuActivity.class);
                //kills all previous activities
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

            } else {
                Toast.makeText(FoundWG2Activity.this, R.string.wg_exists, Toast.LENGTH_SHORT).show();
            }
        }

        dataSource.getAllWGs();
        dataSource.close();
    }

    private String[] getWgDetails(){
        String[] wgInput = new String[3];

        EditText inputName = findViewById(R.id.input_wg_name);
        EditText inputWgPassword = findViewById(R.id.input_wg_Login);
        EditText inputDescription = findViewById(R.id.input_description);

        wgInput[0] = inputName.getText().toString();
        wgInput[1] = inputWgPassword.getText().toString();
        wgInput[2] = inputDescription.getText().toString();

        return wgInput;
    }

    private boolean checkInput(){

        EditText inputName = findViewById(R.id.input_wg_name);
        EditText inputWgPassword = findViewById(R.id.input_wg_Login);
        EditText inputWgPassword2 = findViewById(R.id.input_wg_password2);
        EditText inputDescription = findViewById(R.id.input_description);

        String password1 = inputWgPassword.getText().toString();
        String password2 = inputWgPassword2.getText().toString();


        if(isEmpty(inputName)||isEmpty(inputDescription)||isEmpty(inputWgPassword)||isEmpty(inputWgPassword2)){
            Toast.makeText(FoundWG2Activity.this,R.string.empty_Fields,Toast.LENGTH_SHORT).show();
            return false;
        }
        else if (!(password1.equals(password2))){
            Toast.makeText(FoundWG2Activity.this,R.string.no_password_match,Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;

    }




}
