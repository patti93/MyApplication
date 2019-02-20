package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowCalenderActivity extends AppCompatActivity {

    CalendarView calendarView;
    WG4U_DataSource dataSource;
    ActiveResident activeResident;
    ListView listView;
    AppointmentAdapter appointmentAdapter;
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    String currentDateandTime;
    String task_on;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_calender);
        currentDateandTime = sdf.format(new Date());

        textView = findViewById(R.id.calenderTextview);
        task_on = this.getString(R.string.task_on);

        textView.setText(task_on + currentDateandTime);

        updateListView(currentDateandTime);

        calendarView = findViewById(R.id.calendarView);

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView calendarView, int year, int month, int dayOfMonth) {

                String yearString = Integer.toString(year);
                String monthString = Integer.toString(month + 1);
                String dayString = Integer.toString(dayOfMonth);

                if(month < 10)monthString = "0" + monthString;
                if(dayOfMonth < 10)dayString = "0" + dayString;

                String searchString = dayString + "." + monthString + "." + yearString;


                updateListView(searchString);

            }
        });
    }

    public void updateListView(String date){


        textView.setText(task_on + date);

        dataSource = new WG4U_DataSource(this);

        dataSource.open();

        List<Appointment> appointmentList = new ArrayList<>();
/*
        appointmentList = dataSource.getAppointmentsSearch("date = '" + date + "'");
*/
        ActiveResident activeResident = new ActiveResident(this);
        Wg wg = dataSource.findResidentsWg(activeResident.getActiveResident());

        appointmentList = dataSource.getWgAppointments(wg,date);
        appointmentAdapter = new AppointmentAdapter(this, (ArrayList) appointmentList);

        listView = findViewById(R.id.appointmentListView);
        listView.setAdapter(appointmentAdapter);

        dataSource.close();
    }

    public void redirectCreateAppointment(View view){

        Intent intent = new Intent(this,CreateAppointmentActivity.class);
        startActivity(intent);

    }


 @Override
    protected void onResume(){
        super.onResume();
        updateListView(currentDateandTime);

 }



}
