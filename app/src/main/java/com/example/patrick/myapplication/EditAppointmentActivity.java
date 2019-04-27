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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.regex.Pattern;

public class EditAppointmentActivity extends AppCompatActivity {

    EditText inputAppointmentName;

    EditText inputAppointmentDescription;

    int hourIn = 0;
    int minutesIn = 0;

    String date;

    WG4U_DataSource dataSource;

    ActiveResident activeResident;

    Appointment appointment;

    long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment_ativity);

        TextView headline = findViewById(R.id.editTaskTextView);

        TextView note = findViewById(R.id.appointmentDescriptionEditText);

        TextView name = findViewById(R.id.appointmentNameEditText);

        headline.setText(R.string.edit_appointment);

        Intent intent = getIntent();

        id = intent.getLongExtra("id",0);

        dataSource = new WG4U_DataSource(this);

        dataSource.open();

        appointment = dataSource.getAppointmentsSearch("id = " + Long.toString(id)).get(0);

        name.setText(appointment.getName());

        note.setText(appointment.getDescription());

        date = appointment.getDate();

        dataSource.close();

    }

    public void onClickSafe(View view){

        inputAppointmentDescription = findViewById(R.id.appointmentDescriptionEditText);
        String description = inputAppointmentDescription.getText().toString();
        inputAppointmentDescription.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        inputAppointmentName = findViewById(R.id.appointmentNameEditText);
        String name = inputAppointmentName.getText().toString();


        if(isEmpty(inputAppointmentName)){
            Toast.makeText(this,R.string.name_required,Toast.LENGTH_SHORT).show();
        }
        else {
            /*
            activeResident = new ActiveResident(this);
            dataSource = new WG4U_DataSource(this);
            dataSource.open();

            Appointment appointment = dataSource.insertAppointment(name,date,description,hourIn,minutesIn);
            Wg wg = dataSource.findResidentsWg(activeResident.getActiveResident());

            dataSource.associateAppointmentToWG(wg,appointment);
            dataSource.close();
            */
            dataSource.open();

            dataSource.updateAppointment(id,name,date,description,hourIn,minutesIn);

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


        /*
        // Get current year, month and day.
        Calendar now = Calendar.getInstance();
        int year = now.get(java.util.Calendar.YEAR);
        int month = now.get(java.util.Calendar.MONTH);
        int day = now.get(java.util.Calendar.DAY_OF_MONTH);
        */
        //current appointment date
        String[] dateString = new String[3];

        dateString = appointment.getDate().split("\\.");

        int day = Integer.parseInt(dateString[0]);
        int month = Integer.parseInt(dateString[1]);
        int year = Integer.parseInt(dateString[2]);

        DatePickerDialog datePickerDialog = new DatePickerDialog(EditAppointmentActivity.this,R.style.Theme_AppCompat_Light_Dialog,onDateSetListener,year,month - 1,day);

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

        TimePickerDialog timePickerDialog = new TimePickerDialog(EditAppointmentActivity.this, R.style.Theme_AppCompat_Light_Dialog, onTimeSetListener, appointment.getHour(), appointment.getMinute(), true);

        timePickerDialog.setTitle(R.string.set_Time);

        timePickerDialog.show();
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }


}
