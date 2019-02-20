package com.example.patrick.myapplication;

import android.content.Intent;
import android.renderscript.ScriptGroup;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class CreateAppointmentActivity extends AppCompatActivity {

    EditText inputAppointmentName;
    EditText inputAppointmentDate;
    EditText inputAppointmentDescription;

    WG4U_DataSource dataSource;

    ActiveResident activeResident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_appointment_ativity);
    }



    public void onClickSafe(View view){

        inputAppointmentDate = findViewById(R.id.appointmentDateEditText);
        String date = inputAppointmentDate.getText().toString();

        inputAppointmentDescription = findViewById(R.id.appointmentDescriptionEditText);
        String description = inputAppointmentDescription.getText().toString();
        inputAppointmentDescription.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        inputAppointmentName = findViewById(R.id.appointmentNameEditText);
        String name = inputAppointmentName.getText().toString();

        Pattern date_pattern = Pattern.compile("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");

        if(!date_pattern.matcher(date).matches()){
            Toast.makeText(this,R.string.check_date,Toast.LENGTH_SHORT).show();
        }
        else if(isEmpty(inputAppointmentDate)||isEmpty(inputAppointmentDescription)||isEmpty(inputAppointmentName)){
            Toast.makeText(this,R.string.empty_Fields,Toast.LENGTH_SHORT).show();
        }
        else {
            activeResident = new ActiveResident(this);
            dataSource = new WG4U_DataSource(this);
            dataSource.open();

            Appointment appointment = dataSource.insertAppointment(name,date,description);
            Wg wg = dataSource.findResidentsWg(activeResident.getActiveResident());

            dataSource.associateAppointmentToWG(wg,appointment);
            dataSource.close();

            finish();
        }

    }
    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }


}
