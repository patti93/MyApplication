package com.example.patrick.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditWGActivity extends AppCompatActivity {

    //WG4U_DataSource dataSource;
    ActiveWG activeWG;
    ActiveResident activeResident;
    Resident resident;
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

        //dataSource = new WG4U_DataSource(this);
        activeResident = new ActiveResident(this);
        activeWG = new ActiveWG(this);
        resident = activeResident.getActiveResident();
        wg = activeWG.getActiveWG();

        /*dataSource.open();

        wg = dataSource.findResidentsWg(resident);

        dataSource.close();
        */
        TextView headline = findViewById(R.id.textView);
        headline.setText(wg.getName());

        inputStreet.setText(wg.getStreet());

        inputHnr.setText(wg.getHnr());

        inputTown.setText(wg.getTown());

        inputZip.setText(wg.getzip());

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
        final EditText inputDescription = findViewById(R.id.input_description);

        if(isEmpty(inputCountry) || isEmpty(inputHnr) || isEmpty(inputTown) || isEmpty(inputZip) || isEmpty(inputStreet)|isEmpty(inputDescription)){

            Toast.makeText(this,R.string.empty_Fields,Toast.LENGTH_SHORT).show();
        }
        else{
            /*
            dataSource = new WG4U_DataSource(this);
            dataSource.open();
            String[] address = getAddressInput();
            dataSource.updateWG(wg.getId(),address[0],address[1],address[3],address[2],address[4],inputDescription.getText().toString());
            finish();
            */
            final String[] address = getAddressInput();

            VolleyHelper volleyHelper = new VolleyHelper();

            String url = "https://wg4u.dnsuser.de/update_wg.php";

            Map<String,String> params = new HashMap<>();

            params.put("wgid",Long.toString(wg.getId()));
            params.put("street",address[0]);
            params.put("hnr",address[1]);
            params.put("town",address[2]);
            params.put("zip",address[3]);
            params.put("country",address[4]);
            params.put("description",inputDescription.getText().toString());

            VolleyHelper.makeStringRequestPOST(getApplicationContext(), url, params, new VolleyResponseListener() {

                @Override
                public void onError(String message) {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if(jsonObject.getInt("status")==1){

                            wg.setStreet(address[0]);
                            wg.setHnr(address[1]);
                            wg.setTown(address[2]);
                            wg.setzip(address[3]);
                            wg.setCountry(address[4]);
                            wg.setDescription(inputDescription.getText().toString());
                            activeWG.setActiveWG(wg);

                            finish();

                        } else Toast.makeText(getApplicationContext(),R.string.check_input,Toast.LENGTH_LONG).show();

                    } catch (JSONException e){
                        Log.d("INFO",e.getMessage());
                    }

                }
            });

        }

    }

}
