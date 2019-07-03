package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

public class ShowWGActivity extends AppCompatActivity {

    WG4U_DataSource dataSource;
    ActiveResident activeResident;
    ActiveWG activeWG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wg);

        String wgName;
        String address;
        String description;
        String password;

        activeResident = new ActiveResident(this);
        activeWG = new ActiveWG(this);


        TextView textView_wg_name = findViewById(R.id.print_wg_name);
        wgName = activeWG.getActiveWG().getName()+"\n";
        textView_wg_name.setText(wgName);


        TextView textView_address = findViewById(R.id.print_address);
        address =   activeWG.getActiveWG().getStreet() +
                    " " +
                    activeWG.getActiveWG().getHnr() +
                    " \n" +
                    activeWG.getActiveWG().getzip() +
                    " " +
                    activeWG.getActiveWG().getTown() +
                    " \n" +
                    activeWG.getActiveWG().getCountry() +
                    "\n";
        textView_address.setText(address);

        TextView textView_description = findViewById(R.id.print_description);
        description =   activeWG.getActiveWG().getDescription() +
                        "\n";
        textView_description.setText(description);


    }

    public void onClickLeaveWG(View view){
/*
        dataSource = new WG4U_DataSource(this);
        activeResident = new ActiveResident(this);

        dataSource.open();

        dataSource.leaveWG(activeResident.getActiveResident().getId());

        dataSource.close();

        Intent intent = new Intent(this,NoWGActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
  */
        String userid = Long.toString(activeResident.getActiveResident().getId());

        String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/leave_wg.php?userid=" + userid;

        VolleyHelper volleyHelper = new VolleyHelper();

        VolleyHelper.makeStringRequestGET(getApplicationContext(), url, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.getInt("status") == 1){

                        activeWG.unsetActiveWG();
                        Intent intent = new Intent(getApplicationContext(),NoWGActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    } else Toast.makeText(getApplicationContext(),R.string.check_input,Toast.LENGTH_LONG).show();

                } catch (JSONException e){
                    Log.d("INFO",e.getMessage());
                }
            }
        });

    }

    public void onClickEditWG(View view){

        Intent intent = new Intent(this,EditWGActivity.class);

        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setContentView(R.layout.activity_show_wg);

        String wgName;
        String address;
        String description;
        String password;


        activeResident = new ActiveResident(this);
        activeWG = new ActiveWG(this);


        TextView textView_wg_name = findViewById(R.id.print_wg_name);
        wgName = activeWG.getActiveWG().getName()+"\n";
        textView_wg_name.setText(wgName);


        TextView textView_address = findViewById(R.id.print_address);
        address =   activeWG.getActiveWG().getStreet() +
                " " +
                activeWG.getActiveWG().getHnr() +
                " \n" +
                activeWG.getActiveWG().getzip() +
                " " +
                activeWG.getActiveWG().getTown() +
                " \n" +
                activeWG.getActiveWG().getCountry() +
                "\n";
        textView_address.setText(address);

        TextView textView_description = findViewById(R.id.print_description);
        description =   activeWG.getActiveWG().getDescription() +
                "\n";
        textView_description.setText(description);

    }
}
