package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class show_appointment extends AppCompatActivity {

    WG4U_DataSource dataSource;
    Appointment appointment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_appointment);

        Intent intent = getIntent();

        long id = intent.getLongExtra("id",0);

        String time,hour,minute;

        dataSource = new WG4U_DataSource(this);

        dataSource.open();

        appointment = dataSource.getAppointmentsSearch("id =" + Long.toString(id)).get(0);

        dataSource.close();

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

    }

    public void onClickDelete(View view){

        dataSource.open();

        long check = dataSource.deleteAppointment(appointment.getId());

        dataSource.close();

        finish();

    }








}
