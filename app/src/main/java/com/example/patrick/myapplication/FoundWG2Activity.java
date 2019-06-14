package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class FoundWG2Activity extends AppCompatActivity {

    String[] address;
    String[] wgData;
    private static final String LOG_TAG = FoundWG2Activity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_wg2);
        Intent intent = getIntent();
        address = intent.getStringArrayExtra("wg_data");
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }

    public void redirectMain(View view){

        wgData = getWgDetails();

        //put POST params
if(checkInput()) {


    Map<String, String> params = new HashMap<>();
    //address
    params.put("name", wgData[0]);
    //Log.d(LOG_TAG,wgData[0]);
    params.put("street", address[0]);
    params.put("hnr", address[1]);
    params.put("zip", address[2]);
    params.put("town", address[3]);
    params.put("country", address[4]);
    params.put("description", wgData[2]);
    params.put("password", wgData[1]);

    String url = "https://wg4u.dnsuser.de/insert_wg.php";

    VolleyHelper volleyHelper = new VolleyHelper();

    VolleyHelper.makeStringRequestPOST(this, url, params, new VolleyResponseListener() {
        @Override
        public void onError(String message) {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onResponse(String response) {
            Log.d(LOG_TAG, response);
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getInt("success") == 1) {

                    Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
                    //kills all previous activities
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);

                } else if(jsonObject.getInt("success") == 0){

                    Toast.makeText(FoundWG2Activity.this, R.string.wg_exists, Toast.LENGTH_SHORT).show();

                }

            } catch (JSONException e) {
                Log.d(LOG_TAG, e.getMessage());
            }


        }
    });

}
/* local login
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

        */
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
