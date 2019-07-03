package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class show_appointment extends AppCompatActivity {

    WG4U_DataSource dataSource;
    Appointment appointment;
    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appointment);

        Intent intent = getIntent();

        id = intent.getLongExtra("id",0);



        //dataSource = new WG4U_DataSource(this);

        //dataSource.open();

        String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/get_appointment.php?appID=" + id;

        VolleyHelper volleyHelper = new VolleyHelper();

        VolleyHelper.makeStringRequestGET(getApplicationContext(), url, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {


                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Gson gson = new Gson();
                    String time,hour,minute;

                    appointment = gson.fromJson(jsonArray.getString(0),Appointment.class);

                    minute = Integer.toString(appointment.getMinute());
                    hour = Integer.toString(appointment.getHour());

                    if(hour.length() == 1)hour = "0" + hour;
                    if(minute.length() == 1)minute = "0" + minute;
                    time = hour + ":" + minute;

                    TextView appointmentName = findViewById(R.id.appointmentNameTextView);
                    appointmentName.setText(appointment.getName());

                    TextView appointmentDate = findViewById(R.id.appointmentDateTextView);
                    appointmentDate.setText(appointment.getDate());

                    TextView appointmentNote = findViewById(R.id.appointmentNoteTextView);
                    appointmentNote.setText(appointment.getDescription());

                    TextView appointmentTime = findViewById(R.id.appointmentTimeTextView);
                    appointmentTime.setText(time);

                } catch (JSONException e){

                }


            }
        });
        //appointment = dataSource.getAppointmentsSearch("id =" + id).get(0);

        //dataSource.close();

    }

    public void onClickDelete(View view){

        String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/delete_appointment.php?appID=" + appointment.getId();


        VolleyHelper volleyHelper = new VolleyHelper();

        VolleyHelper.makeStringRequestGET(getApplicationContext(), url, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {

                    if(response.equals("success")) finish();
                        else Toast.makeText(getApplicationContext(),R.string.error_delete,Toast.LENGTH_LONG).show();

            }
        });

    }

    public void  onClickEdit(View view){

        Intent intent = new Intent(this,EditAppointmentActivity.class);

        intent.putExtra("id",appointment.getId());
        intent.putExtra("name",appointment.getName());
        intent.putExtra("date",appointment.getDate());
        intent.putExtra("description",appointment.getDescription());
        intent.putExtra("hour",appointment.getHour());
        intent.putExtra("minute",appointment.getMinute());

        startActivity(intent);

        finish();
    }






}
