package com.example.patrick.myapplication;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class JournalAdapter extends ArrayAdapter {


    public JournalAdapter(Context context, ArrayList<JournalEntry> journal){
        super(context,0,journal);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        JournalEntry entry = (JournalEntry) getItem(position);


        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.journal_item, parent, false);
        }


        TextView sent_by = convertView.findViewById(R.id.journalListViewItemSentBy);
        TextView message = convertView.findViewById(R.id.journalListViewItemMessage);
        TextView date = convertView.findViewById(R.id.journalListViewItemDate);
        TextView time = convertView.findViewById(R.id.journalListViewItemTime);


        String hour = Integer.toString(entry.getHour());
        if(hour.length()==1) hour = "0" + hour;
        String minute = Integer.toString(entry.getMinute());
        if(minute.length()==1) minute = "0" + minute;
        String t =  hour + ":" + minute;

        sent_by.setText(entry.getSent_by());
        message.setText(entry.getMessage());
        date.setText(entry.getDate());
        time.setText(t);
        Log.d("INFO",t);

        return convertView;
    }
}
