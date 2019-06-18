package com.example.patrick.myapplication;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ShoppingItemAdapter extends ArrayAdapter {
    public ShoppingItemAdapter(Context context, ArrayList<ShoppingItem> shoppingItems) {
        super(context, 0, shoppingItems);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ShoppingItem shoppingItem = (ShoppingItem) getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.shopping_item, parent, false);
        }

        TextView itemName = convertView.findViewById(R.id.shopping_list_item_name);

        itemName.setText(shoppingItem.getName());

        if(position % 2 == 0){
            int convertViewBackgroundColor = Color.argb(120,213,213,213);
            convertView.setBackgroundColor(convertViewBackgroundColor);
        }

        return convertView;
    }
}
