package com.example.patrick.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ToDoItemAdapter extends ArrayAdapter {

    public ToDoItemAdapter(Context context, ArrayList<ToDoItem> todoItems) {
        super(context, R.layout.todo_item, todoItems);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ToDoItem todoItem = (ToDoItem) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.todo_item, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.todo_list_item_name);

        itemName.setText(todoItem.getName());

        if(position % 2 == 0){
            int convertViewBackgroundColor = Color.argb(120,213,213,213);
            convertView.setBackgroundColor(convertViewBackgroundColor);
        }

        return convertView;
    }
}