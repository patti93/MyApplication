package com.example.patrick.myapplication;


import android.content.ContentValues;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.patrick.myapplication.Appointment;
import com.example.patrick.myapplication.R;

import java.util.ArrayList;

public class AppointmentAdapter extends ArrayAdapter {

    public AppointmentAdapter(Context context, ArrayList<Appointment> appointments){
        super(context,0,appointments);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        Appointment appointment = (Appointment) getItem(position);
        String time;


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_appointment, parent, false);
        }


        String hour = Integer.toString(appointment.getHour());
        String minute = Integer.toString(appointment.getMinute());

        if(appointment.getHour()<10) hour = "0" + hour;

        if(appointment.getMinute()<10) minute = "0" + minute;

        time =  hour + ":" + minute;

        TextView appointmentName = convertView.findViewById(R.id.appointmentListViewItemName);
        appointmentName.setText(appointment.getName());

        TextView appointmentDescription = convertView.findViewById(R.id.appointmentListViewItemDescription);
        appointmentDescription.setText(appointment.getDescription());

        TextView appointmentTime = convertView.findViewById(R.id.timeTextView);
        appointmentTime.setText(time);


        if(position % 2 == 0){
            int convertViewBackgroundColor = Color.argb(120,213,213,213);
            convertView.setBackgroundColor(convertViewBackgroundColor);
        }


        return convertView;

    }



}
