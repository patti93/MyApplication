package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WgLoginActivity extends AppCompatActivity {

    private static final String LOG_TAG = WgLoginActivity.class.getSimpleName();
    WG4U_DataSource dataSource;
    String wgName;
    ActiveResident activeResident;
    ActiveWG activeWG;
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

        EditText inputPassword = findViewById(R.id.input_wg_Login);
        String password = inputPassword.getText().toString();

        VolleyHelper volleyHelper = new VolleyHelper();

        ActiveResident activeResident = new ActiveResident(getApplicationContext());
        Resident resident = activeResident.getActiveResident();
        int id = (int)resident.getId();

        String idString = Integer.toString(id);

        String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/wglogin.php?wgname=" + wgName + "&password=" + password + "&userid=" + idString;


        VolleyHelper.makeStringRequestGET(getApplicationContext(), url, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                Log.d(LOG_TAG,response);
                try {
                    JSONArray jsonArray = new JSONArray(response);

                    if(jsonArray.getJSONObject(0).getInt("status") == 1){

                        Gson gson = new Gson();
                        Wg wg = gson.fromJson(jsonArray.getString(1),Wg.class);
                        activeWG = new ActiveWG(getApplicationContext());
                        activeWG.setActiveWG(wg);
                        Intent intent = new Intent(getApplicationContext(),MainMenuActivity.class);
                        //kills all previous activities and direct to main menu
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }

                    else if(jsonArray.getJSONObject(0).getInt("status") == -1){
                        Toast.makeText(getApplicationContext(),R.string.check_password,Toast.LENGTH_LONG).show();
                    }


                } catch (JSONException e){
                    Log.d(LOG_TAG,e.getMessage());
                }

            }
        });


/*
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
            intent.putExtra("wgName",wgList.get(0).getName());
            startActivity(intent);

        }
        else {
            Toast.makeText(WgLoginActivity.this,R.string.check_password,Toast.LENGTH_SHORT).show();
            dataSource.close();
        }
    */
    }

}
