package com.example.patrick.myapplication;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ShowShoppingListActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShowShoppingListActivity.class.getSimpleName();

    private ArrayList<String> shoppingItems;
    private ShoppingItemAdapter shoppingItemsAdapter;
    private ListView listView;
    private WG4U_DataSource dataSource;
    private ActiveResident activeResident;
    private ActiveWG activeWG;
    private Wg wg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_shopping_list);

        activeResident = new ActiveResident(getApplicationContext());
        activeWG = new ActiveWG(getApplicationContext());

        updateShoppingList();

        setupListViewListener();

        setUpOnClickListener();

        /*
        // Open DB
        dataSource = new WG4U_DataSource(this);
        dataSource.open();

        // Get WG
        activeResident = new ActiveResident(this);
        wg = dataSource.findResidentsWg(activeResident.getActiveResident());

        // Get shopping list from DS
        shoppingItems = dataSource.getShoppingItemNameList(wg);

        // Set Adapter
        shoppingItemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, shoppingItems);

        // get and set listView
        listView = findViewById(R.id.view_shopping_list);
        listView.setAdapter(shoppingItemsAdapter);

        // Close DB
        dataSource.close();
        */

    }



    public void updateShoppingList(){

        String wg_id = Long.toString(activeWG.getActiveWG().getId());

        String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/get_wgs_shoppinglist.php?wg_id=" + wg_id;

        VolleyHelper volleyHelper = new VolleyHelper();

        VolleyHelper.makeStringRequestGET(getApplicationContext(), url, new VolleyResponseListener() {

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {

                Log.d(LOG_TAG,response);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Gson gson = new Gson();

                    ArrayList<ShoppingItem> shoppingitems = new ArrayList<>();

                    for(int i = 0; i < jsonArray.length(); i++){

                        shoppingitems.add(gson.fromJson(jsonArray.getString(i),ShoppingItem.class));

                    }
                    shoppingItemsAdapter = new ShoppingItemAdapter(ShowShoppingListActivity.this, shoppingitems);

                    listView = findViewById(R.id.view_shopping_list);
                    listView.setAdapter(shoppingItemsAdapter);


                } catch (JSONException e){

                    Log.d(LOG_TAG,e.getMessage());
                    ArrayList<ShoppingItem> shoppingitems = new ArrayList<>();
                    shoppingItemsAdapter = new ShoppingItemAdapter(ShowShoppingListActivity.this, shoppingitems);

                    listView = findViewById(R.id.view_shopping_list);
                    listView.setAdapter(shoppingItemsAdapter);


                }

            }
        });
    }


    // Called when a new product added
    public void onClick(View v) {
        EditText input_item = findViewById(R.id.input_item);
        String itemText = input_item.getText().toString();


        if(!itemText.isEmpty()){

            String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/insert_shopping_item.php?item_name=" + itemText + "&wg_id=" + activeWG.getActiveWG().getId();

            VolleyHelper volleyHelper = new VolleyHelper();

            VolleyHelper.makeStringRequestGET(getApplicationContext(), url, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String response) {
                    Log.d(LOG_TAG,response);
                    if(response.equals("success")){

                        EditText input_item = findViewById(R.id.input_item);
                        input_item.setText("");
                        updateShoppingList();

                    }
                    else {
                    }

                }
            });
        }

        /*
        if(!itemText.isEmpty()) {
            // Open DB
            dataSource = new WG4U_DataSource(this);
            dataSource.open();

            // Insert
            long itemID = dataSource.insertShoppingItem(itemText, wg);

            if (itemID != 0)
                dataSource.associateShoppingListToWG(wg, itemID);

            // Get new list
            shoppingItems = dataSource.getShoppingItemNameList(wg);

            // Close DB
            dataSource.close();

            // Refresh the adapter
            shoppingItemsAdapter.clear();
            shoppingItemsAdapter.addAll(shoppingItems);
            shoppingItemsAdapter.notifyDataSetChanged();

            // Clear input Text
            input_item.setText("");

        }
        */
    }


    // Called when ListViewItem long pressed
    // Delete that item
    // TBD
    private void setupListViewListener() {
        listView = findViewById(R.id.view_shopping_list);
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        ShoppingItem shoppingItem = (ShoppingItem) adapter.getItemAtPosition(pos);


                        TextView shoppingItemTextView = item.findViewById(R.id.shopping_list_item_name);

                        if(shoppingItemTextView.getPaint().isStrikeThruText()) {

                            String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/delete_shopping_item.php?item_id=" + shoppingItem.getId();

                            VolleyHelper volleyHelper = new VolleyHelper();

                            VolleyHelper.makeStringRequestGET(getApplicationContext(), url, new VolleyResponseListener() {
                                @Override
                                public void onError(String message) {
                                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onResponse(String response) {

                                    if (response.equals("success")) {
                                        updateShoppingList();
                                    }

                                }
                            });
                        }
                    /*
                        // Open DB
                        dataSource.open();

                        // Remove item
                        dataSource.deleteShoppingItem(shoppingItems.get(pos), wg);

                        // Refresh list
                        shoppingItems = dataSource.getShoppingItemNameList(wg);

                        // Close DB
                        dataSource.close();

                        // Refresh the adapter
                        shoppingItemsAdapter.clear();
                        shoppingItemsAdapter.addAll(shoppingItems);
                        shoppingItemsAdapter.notifyDataSetChanged();

                        // Return true consumes the long click event (marks it handled)

                      */
                        return true;
                    }

                });
    }

    public void setUpOnClickListener(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView shoppingItemTextView = view.findViewById(R.id.shopping_list_item_name);

                shoppingItemTextView.setPaintFlags(shoppingItemTextView.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);

            }
        });

    }

}
