package com.example.patrick.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;


public class ShowShoppingListActivity extends AppCompatActivity {

    private ArrayList<String> shoppingItems;
    private ArrayAdapter<String> shoppingItemsAdapter;
    private ListView listView;
    private WG4U_DataSource dataSource;
    private ActiveResident activeResident;
    private Wg wg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shopping_list);

        // Open DB
        dataSource = new WG4U_DataSource(this);
        dataSource.open();

        // Get WG
        activeResident = new ActiveResident(this);
        wg = dataSource.findResidentsWg(activeResident.getActiveResident());

        // Get shopping list from DS
        shoppingItems = dataSource.getWgShoppingList(wg);

        // Set Adapter
        shoppingItemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shoppingItems);

        // get and set listView
        listView = findViewById(R.id.view_shopping_list);
        listView.setAdapter(shoppingItemsAdapter);

        // Close DB
        dataSource.close();

        //setupListViewListener();
    }

    // Called when a new product added
    public void onClick(View v) {
        EditText input_item = findViewById(R.id.input_item);
        String itemText = input_item.getText().toString();
        //shoppingItemsAdapter.add(itemText);

        // Open DB
        dataSource = new WG4U_DataSource(this);
        dataSource.open();

        // Insert
        long itemID = dataSource.insertShoppingItem(itemText);
        dataSource.associateShoppingListToWG(wg, itemID);

        // Get new list
        shoppingItems = dataSource.getWgShoppingList(wg);

        // Close DB
        dataSource.close();

        // Refresh the adapter
        shoppingItemsAdapter.clear();
        shoppingItemsAdapter.addAll(shoppingItems);
        shoppingItemsAdapter.notifyDataSetChanged();

        // Clear input Text
        input_item.setText("");

    }

    /*
    // Called when ListViewItem long pressed
    // Delete that item
    // TBD
    private void setupListViewListener() {
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {

                        // Open DB
                        dataSource.open();

                        // Remove item
                        shoppingItems = dataSource.deleteShoppingItem(shoppingItems.get(pos));

                        // Close DB
                        dataSource.close();

                        // Refresh the adapter
                        shoppingItemsAdapter.clear();
                        shoppingItemsAdapter.addAll(shoppingItems);
                        shoppingItemsAdapter.notifyDataSetChanged();

                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }

                });
    }*/

}
