package com.example.patrick.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ShowToDoActivity extends AppCompatActivity {

    private static final String LOG_TAG = WG4U_DataSource.class.getSimpleName();

    private ArrayList<String> todos;
    private ArrayAdapter<String> todoAdapter;
    private ListView listView;
    private WG4U_DataSource dataSource;
    private ActiveResident activeResident;
    private Wg wg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_to_do);

        // Open DB
        dataSource = new WG4U_DataSource(this);
        dataSource.open();

        // Get WG
        activeResident = new ActiveResident(this);
        wg = dataSource.findResidentsWg(activeResident.getActiveResident());

        // Get shopping list from DS
        todos = dataSource.getTodoNameList(wg);

        // Set Adapter
        todoAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, todos);

        // get and set listView
        listView = findViewById(R.id.view_to_do_list);
        listView.setAdapter(todoAdapter);

        // Close DB
        dataSource.close();

        setupListViewListener();
    }

    // Called when a new todo added
    public void onClick(View v) {
        EditText input_todo = findViewById(R.id.input_todo);
        String todoText = input_todo.getText().toString();

        if(!todoText.isEmpty()) {
            // Open DB
            dataSource = new WG4U_DataSource(this);
            dataSource.open();

            // Insert
            long todoID = dataSource.insertTodo(todoText, wg);

            // Associate if entered todo_name is new
            if (todoID != 0)
                dataSource.associateTodoToWG(wg, todoID);

            // Get new list
            todos = dataSource.getTodoNameList(wg);

            // Close DB
            dataSource.close();

            // Refresh the adapter
            todoAdapter.clear();
            todoAdapter.addAll(todos);
            todoAdapter.notifyDataSetChanged();

            // Clear input Text
            input_todo.setText("");

        }

    }


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
                        dataSource.deleteTodo(todos.get(pos), wg);

                        // Refresh list
                        todos = dataSource.getTodoNameList(wg);

                        // Close DB
                        dataSource.close();

                        // Refresh the adapter
                        todoAdapter.clear();
                        todoAdapter.addAll(todos);
                        todoAdapter.notifyDataSetChanged();

                        // Return true consumes the long click event (marks it handled)
                        return true;
                    }

                });
    }
}
