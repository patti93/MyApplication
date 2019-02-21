package com.example.patrick.myapplication;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;

public class CreateAppointmentActivity extends AppCompatActivity {

    EditText inputAppointmentName;
    EditText inputAppointmentDate;
    EditText inputAppointmentDescription;

    int hourIn;
    int minutesIn;

    String date;

    WG4U_DataSource dataSource;

    ActiveResident activeResident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment_ativity);
    }



    public void onClickSafe(View view){



        inputAppointmentDescription = findViewById(R.id.appointmentDescriptionEditText);
        String description = inputAppointmentDescription.getText().toString();
        inputAppointmentDescription.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        inputAppointmentName = findViewById(R.id.appointmentNameEditText);
        String name = inputAppointmentName.getText().toString();


        if(isEmpty(inputAppointmentName)){
            Toast.makeText(this,R.string.empty_Fields,Toast.LENGTH_SHORT).show();
        }
        else {
            activeResident = new ActiveResident(this);
            dataSource = new WG4U_DataSource(this);
            dataSource.open();

            Appointment appointment = dataSource.insertAppointment(name,date,description,hourIn,minutesIn);
            Wg wg = dataSource.findResidentsWg(activeResident.getActiveResident());

            dataSource.associateAppointmentToWG(wg,appointment);
            dataSource.close();

            finish();
        }

    }


    public void onClickDate(View view){


        DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                String yearString = Integer.toString(year);
                String monthString = Integer.toString(month + 1);
                String dayString = Integer.toString(dayOfMonth);

                if(month < 10)monthString = "0" + monthString;
                if(dayOfMonth < 10)dayString = "0" + dayString;

                date = dayString + "." + monthString + "." + yearString;

                Log.d("INFO",date);
            }
        };



        // Get current year, month and day.
        Calendar now = Calendar.getInstance();
        int year = now.get(java.util.Calendar.YEAR);
        int month = now.get(java.util.Calendar.MONTH);
        int day = now.get(java.util.Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(CreateAppointmentActivity.this,R.style.Theme_AppCompat_Light_Dialog,onDateSetListener,year,month,day);

        datePickerDialog.setTitle(R.string.set_date);

        datePickerDialog.show();

    }

    public void onClickTime(View view){

        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                hourIn = hour;
                minutesIn = minute;
                Log.d("INFO",Integer.toString(hourIn)+ ":" + Integer.toString(minutesIn));
            }
        };
        /*
        Calendar now = Calendar.getInstance();
        int hour = now.get(java.util.Calendar.HOUR_OF_DAY);
        int minute = now.get(java.util.Calendar.MINUTE);
        */
        TimePickerDialog timePickerDialog = new TimePickerDialog(CreateAppointmentActivity.this, R.style.Theme_AppCompat_Light_Dialog, onTimeSetListener, 0, 0, true);

        timePickerDialog.setTitle(R.string.set_Time);

        timePickerDialog.show();
    }








    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }


}
