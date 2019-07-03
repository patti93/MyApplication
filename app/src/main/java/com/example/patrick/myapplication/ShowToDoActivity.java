package com.example.patrick.myapplication;

import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ShowToDoActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShowToDoActivity.class.getSimpleName();

    public ListView toDoListView;
    public ToDoItemAdapter toDoItemAdapter;
    public ArrayList<ToDoItem> toDoItems;

    public ActiveWG activeWG;

    public EditText todoInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_to_do);

        toDoListView = findViewById(R.id.view_to_do_list);

        updateListView();

        setupListviewListener();

        setUpOnClickListener();
    }


    public void onClickAddShoppingItem(View view){

        todoInput = findViewById(R.id.input_todo);

        String todoName = todoInput.getText().toString();

        activeWG = new ActiveWG(getApplicationContext());

        String wg_id = Long.toString(activeWG.getActiveWG().getId());

        String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/insert_todo_item.php?item_name=" + todoName + "&wg_id=" + wg_id;

        VolleyHelper volleyHelper = new VolleyHelper();

       VolleyHelper.makeStringRequestGET(getApplicationContext(), url, new VolleyResponseListener() {
           @Override
           public void onError(String message) {

           }

           @Override
           public void onResponse(String response) {
               todoInput.setText("");
               updateListView();
           }
       });
    }

    public void updateListView(){

        activeWG = new ActiveWG(getApplicationContext());

        String wg_id = Long.toString(activeWG.getActiveWG().getId());

        String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/get_wgs_todolist.php?wg_id=" + wg_id;

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

                    toDoItems = new ArrayList<>();

                    for(int i = 0; i < jsonArray.length(); i++){
                        toDoItems.add(gson.fromJson(jsonArray.getString(i),ToDoItem.class));
                    }

                    toDoItemAdapter = new ToDoItemAdapter(ShowToDoActivity.this,toDoItems);

                    toDoListView.setAdapter(toDoItemAdapter);

                }catch (JSONException e){

                    toDoItems = new ArrayList<>();
                    toDoItemAdapter = new ToDoItemAdapter(ShowToDoActivity.this,toDoItems);

                    toDoListView.setAdapter(toDoItemAdapter);
                }
            }
        });

    }

    private void setupListviewListener() {


        toDoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


                TextView todoTextview = view.findViewById(R.id.todo_list_item_name);


                if(todoTextview.getPaint().isStrikeThruText()){

                    ToDoItem toDoItem = (ToDoItem) parent.getItemAtPosition(position);

                    VolleyHelper volleyHelper = new VolleyHelper();

                    String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/delete_todo_item.php?item_id=" + toDoItem.getId();


                    VolleyHelper.makeStringRequestGET(getApplicationContext(), url, new VolleyResponseListener() {
                        @Override
                        public void onError(String message) {
                            Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onResponse(String response) {

                            updateListView();

                        }
                    });
                }

                return true;
            }
        });

    }

    public void setUpOnClickListener(){

        toDoListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView todoTextview = view.findViewById(R.id.todo_list_item_name);

                todoTextview.setPaintFlags(todoTextview.getPaintFlags() ^ Paint.STRIKE_THRU_TEXT_FLAG);

            }
        });

    }
}
