package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class ShowWGActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_wg);

        String wgName;
        String address;
        String description;
        String password;
        ActiveResident activeResident;
        WG4U_DataSource dataSource;

        dataSource = new WG4U_DataSource(this);
        dataSource.open();
        activeResident = new ActiveResident(this);



        TextView textView_wg_name = findViewById(R.id.print_wg_name);
        wgName = dataSource.findResidentsWg(activeResident.getActiveResident()).getName()+"\n";
        textView_wg_name.setText(wgName);


        TextView textView_address = findViewById(R.id.print_address);
        address =   dataSource.findResidentsWg(activeResident.getActiveResident()).getStreet() +
                    " " +
                    dataSource.findResidentsWg(activeResident.getActiveResident()).getHnr() +
                    " \n" +
                    dataSource.findResidentsWg(activeResident.getActiveResident()).getZipCode() +
                    " " +
                    dataSource.findResidentsWg(activeResident.getActiveResident()).getTown() +
                    " \n" +
                    dataSource.findResidentsWg(activeResident.getActiveResident()).getCountry() +
                    "\n";
        textView_address.setText(address);

        TextView textView_description = findViewById(R.id.print_description);
        description =   dataSource.findResidentsWg(activeResident.getActiveResident()).getDescription() +
                        "\n";
        textView_description.setText(description);

        TextView textView_wg_password = findViewById(R.id.print_password);
        password = dataSource.findResidentsWg(activeResident.getActiveResident()).getPassword();
        textView_wg_password.setText(password);
    }
}
