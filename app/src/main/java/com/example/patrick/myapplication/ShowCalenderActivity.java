package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowCalenderActivity extends AppCompatActivity {

    String LOGTAG = ShowCalenderActivity.class.getSimpleName();

    CalendarView calendarView;
    //WG4U_DataSource dataSource;
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
/*
        //listView.invalidate();

        textView.setText(task_on + date);

        //dataSource = new WG4U_DataSource(this);

        //dataSource.open();

        List<Appointment> appointmentList = new ArrayList<>();

        appointmentList = dataSource.getAppointmentsSearch("date = '" + date + "'");

        ActiveResident activeResident = new ActiveResident(this);
        Wg wg = dataSource.findResidentsWg(activeResident.getActiveResident());

        appointmentList = dataSource.getWgAppointments(wg,date);
        appointmentAdapter = new AppointmentAdapter(this, (ArrayList) appointmentList);

        */
        textView.setText(task_on + date);

        ActiveWG activeWG = new ActiveWG(getApplicationContext());

        String url = "https://wg4u.dnsuser.de/get_wg_appointments.php?wg_id=" + activeWG.getActiveWG().getId() + "&date=" + date;

        VolleyHelper volleyHelper = new VolleyHelper();


        VolleyHelper.makeStringRequestGET(getApplicationContext(), url, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                Log.d(LOGTAG,response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    List<Appointment> appointmentList = new ArrayList<>();
                    if(jsonArray.getJSONObject(0).getInt("status") == 1){
                        Gson gson = new Gson();

                        for(int i = 1; i < jsonArray.length(); i++){
                            appointmentList.add(gson.fromJson(jsonArray.getString(i),Appointment.class));
                        }
                        appointmentAdapter = new AppointmentAdapter(ShowCalenderActivity.this, (ArrayList) appointmentList);
                        listView = findViewById(R.id.appointmentListView);
                        listView.setAdapter(appointmentAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                Appointment appointment = (Appointment) parent.getItemAtPosition(position);

                                Intent intent = new Intent(getApplicationContext(),show_appointment.class);
                                intent.putExtra("id",appointment.getId());

                                startActivity(intent);

                            }
                        });
                    }

                }catch (JSONException e){
                    Log.d(LOGTAG,e.getMessage());
                }

            }
        });






        //dataSource.close();


    }

    public void redirectShowAppointment(View view, int id){

        Intent intent = new Intent(this,show_appointment.class);
        startActivity(intent);

    }

    public void redirectCreateAppointment(View view){

        Intent intent = new Intent(this,CreateAppointmentActivity.class);
        startActivity(intent);

    }


 @Override
    protected void onResume(){
        super.onResume();
        try {
            calendarView.setDate(new SimpleDateFormat("dd.MM.yyyy").parse(currentDateandTime).getTime(), true, true);
        } catch (ParseException e){
            Log.d("INFO",e.getMessage());
        }
        updateListView(currentDateandTime);

 }

}
