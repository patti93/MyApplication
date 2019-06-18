package com.example.patrick.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ShowToDoActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShowToDoActivity.class.getSimpleName();

    private ArrayList<String> todoItems;
    private ToDoItemAdapter todoItemsAdapter;
    private ListView listView;
    private WG4U_DataSource dataSource;
    private ActiveResident activeResident;
    private ActiveWG activeWG;
    private Wg wg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_to_do);

        activeResident = new ActiveResident(getApplicationContext());
        activeWG = new ActiveWG(getApplicationContext());

        updateToDoList();

        setupListViewListener();

        /*
        // Open DB
        dataSource = new WG4U_DataSource(this);
        dataSource.open();

        // Get WG
        activeResident = new ActiveResident(this);
        wg = dataSource.findResidentsWg(activeResident.getActiveResident());

        // Get todo list from DS
        todoItems = dataSource.getToDoItemNameList(wg);

        // Set Adapter
        todoItemsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todoItems);

        // get and set listView
        listView = findViewById(R.id.view_todo_list);
        listView.setAdapter(todoItemsAdapter);

        // Close DB
        dataSource.close();
        */

    }



    public void updateToDoList(){

        String wg_id = Long.toString(activeWG.getActiveWG().getId());

        String url = "https://wg4u.dnsuser.de/get_wgs_todolist.php?wg_id=" + wg_id;

        VolleyHelper volleyHelper = new VolleyHelper();

        VolleyHelper.makeStringRequestGET(getApplicationContext(), url, new VolleyResponseListener() {

            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    Gson gson = new Gson();

                    ArrayList<ToDoItem> todoitems = new ArrayList<>();

                    for(int i = 0; i < jsonArray.length(); i++){

                        todoitems.add(gson.fromJson(jsonArray.getString(i),ToDoItem.class));

                    }
                    todoItemsAdapter = new ToDoItemAdapter(getApplicationContext(), todoitems);

                    listView = findViewById(R.id.view_to_do_list);
                    listView.setAdapter(todoItemsAdapter);


                } catch (JSONException e){

                    Log.d(LOG_TAG,e.getMessage());
                    ArrayList<ToDoItem> todoitems = new ArrayList<>();
                    todoItemsAdapter = new ToDoItemAdapter(getApplicationContext(), todoitems);

                    listView = findViewById(R.id.view_to_do_list);
                    listView.setAdapter(todoItemsAdapter);
                }

            }
        });
    }


    // Called when a new product added
    public void onClickAddShoppingItem(View v) {
        EditText input_item = findViewById(R.id.input_todo);
        String itemText = input_item.getText().toString();

        if(!itemText.isEmpty()){

            String url = "https://wg4u.dnsuser.de/insert_todo_item.php?item_name=" + itemText + "&wg_id=" + activeWG.getActiveWG().getId();

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

                        updateToDoList();
                        EditText input_item = findViewById(R.id.input_todo);
                        input_item.setText("");

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
            long itemID = dataSource.insertToDoItem(itemText, wg);

            if (itemID != 0)
                dataSource.associateToDoListToWG(wg, itemID);

            // Get new list
            todoItems = dataSource.getToDoItemNameList(wg);

            // Close DB
            dataSource.close();

            // Refresh the adapter
            todoItemsAdapter.clear();
            todoItemsAdapter.addAll(todoItems);
            todoItemsAdapter.notifyDataSetChanged();

            // Clear input Text
            input_item.setText("");

        }
        */
    }


    // Called when ListViewItem long pressed
    // Delete that item
    // TBD
    private void setupListViewListener() {
        listView = findViewById(R.id.view_to_do_list);
        listView.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener() {
                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter,
                                                   View item, int pos, long id) {
                        ToDoItem todoItem = (ToDoItem) adapter.getItemAtPosition(pos);

                        String url = "https://wg4u.dnsuser.de/delete_todo_item.php?item_id=" + todoItem.getId();

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
                                    updateToDoList();
                                }

                            }
                        });
                    /*
                        // Open DB
                        dataSource.open();

                        // Remove item
                        dataSource.deleteToDoItem(todoItems.get(pos), wg);

                        // Refresh list
                        todoItems = dataSource.getToDoItemNameList(wg);

                        // Close DB
                        dataSource.close();

                        // Refresh the adapter
                        todoItemsAdapter.clear();
                        todoItemsAdapter.addAll(todoItems);
                        todoItemsAdapter.notifyDataSetChanged();

                        // Return true consumes the long click event (marks it handled)

                      */
                        return true;
                    }

                });
    }

}
