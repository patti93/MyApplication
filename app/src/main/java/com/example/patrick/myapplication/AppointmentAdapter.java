package com.example.patrick.myapplication;


import android.content.ContentValues;
import android.content.Context;
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

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_appointment, parent, false);
        }


        TextView appointmentName = convertView.findViewById(R.id.appointmentListViewItemName);
        appointmentName.setText(appointment.getName());

        return convertView;

    }



}
