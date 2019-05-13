package com.example.patrick.myapplication;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import java.util.ArrayList;

public class JournalAdapter extends ArrayAdapter {


    public JournalAdapter(Context context, ArrayList<JournalEntry> journal){
        super(context,0,journal);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {












        return super.getView(position, convertView, parent);
    }
}
