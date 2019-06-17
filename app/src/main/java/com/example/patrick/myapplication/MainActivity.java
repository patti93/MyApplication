package com.example.patrick.myapplication;

import android.content.Context;
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
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

public class MainActivity extends AppCompatActivity {

    private WG4U_DataSource dataSource;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.getDelegate().onDestroy();
    }
    public boolean checkuser(String user, String password){

        dataSource = new WG4U_DataSource(this);

        dataSource.open();

        List<Resident> residentList = new ArrayList<>();

        residentList = dataSource.getResidentsSearch("email = " + "'" + user + "'");
        //found a resident
        if(residentList.size() != 0) {
            Resident resident = residentList.get(0);
            //check the password
            if (resident.getPassword().equals(password)) {
                dataSource.close();
                ActiveResident activeResident = new ActiveResident(getApplicationContext());
                activeResident.setActiveResident(resident);
                Log.d(LOG_TAG,"aktiver User:" + activeResident.getActiveResident().getEmail());
                return true;
            }
        }
        //login failed
        dataSource.close();
        return false;

    }

    //called when the user taps the login button
    public void clickLogin(View view){

        EditText inputUser = findViewById(R.id.editTextUser);
        EditText inputPassword = findViewById(R.id.editTextPassword);
        final ActiveResident activeResident = new ActiveResident(getApplicationContext());
        final ActiveWG activeWG = new ActiveWG((getApplicationContext()));
        String email = inputUser.getText().toString();
        String password = inputPassword.getText().toString();


        Map<String,String> params = new HashMap<>();
        params.put("email",email);
        params.put("password",password);

        VolleyHelper volleyHelper = new VolleyHelper();

        VolleyHelper.makeStringRequestPOST(this, "https://wg4u.dnsuser.de/login.php", params, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Log.d(LOG_TAG,message);
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                Log.d(LOG_TAG,response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    /*
                    Response is a JSONArray with 2 Objects. at 0 an object which contains the status and at 1 a resident to set the active resident
                    O = wrong password
                    1 = succes, no WG membership
                    2 = unknown user
                    3 = success and WG member
                    */
                    if(jsonArray.getJSONObject(0).getInt("status") == 1) {
                        Gson gson = new Gson();
                        Resident resident = gson.fromJson(jsonArray.getString(1),Resident.class);
                        activeResident.setActiveResident(resident);
                        activeResident.setLoggedIn();
                        activeWG.unsetActiveWG();
                        Intent intent = new Intent(getApplicationContext(), NoWGActivity.class);
                        startActivity(intent);

                    } else if(jsonArray.getJSONObject(0).getInt("status") == 0){
                        Toast.makeText(getApplicationContext(),R.string.wrong_login,Toast.LENGTH_LONG).show();
                    }
                    else if(jsonArray.getJSONObject(0).getInt("status") == 2){
                        Toast.makeText(getApplicationContext(),R.string.wrong_login,Toast.LENGTH_LONG).show();
                    }
                    else if(jsonArray.getJSONObject(0).getInt("status") == 3) {
                        Gson gson = new Gson();
                        Resident resident = gson.fromJson(jsonArray.getString(1),Resident.class);
                        Wg wg = gson.fromJson(jsonArray.getString(2),Wg.class);
                        activeWG.setActiveWG(wg);
                        activeResident.setLoggedIn();
                        activeResident.setActiveResident(resident);
                        Intent intent = new Intent(getApplicationContext(), MainMenuActivity.class);
                        startActivity(intent);

                    }

                } catch (JSONException e){
                    Log.d(LOG_TAG,e.getMessage());
                }
            }
        });



        /* Login with local Database
        if (checkuser(email, password)) {

            dataSource = new WG4U_DataSource(this);
            dataSource.open();
            if(dataSource.findResidentsWg(activeResident.getActiveResident()) == null){
                Intent intent = new Intent(this, NoWGActivity.class);
                startActivity(intent);
                dataSource.close();
                finish();
            }
            else {
                Intent intent = new Intent(this, MainMenuActivity.class);
                startActivity(intent);
            }

        }
        else Toast.makeText(MainActivity.this, R.string.wrong_login,Toast.LENGTH_LONG).show();
        */
    }

    public void signUpRedirect(View view){
        Intent intent = new Intent(this,CreateUserActivity.class);
        startActivity(intent);
    }

}
