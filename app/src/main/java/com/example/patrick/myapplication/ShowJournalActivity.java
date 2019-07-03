package com.example.patrick.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ShowJournalActivity extends AppCompatActivity {

    private static final String LOG_TAG = ShowJournalActivity.class.getSimpleName();
    
    public JournalAdapter journalAdapter;
    public ListView journalListView;
    public ArrayList<JournalEntry> journalEntries;
    
    public ActiveWG activeWG;
    
    public ActiveResident activeResident;

    public EditText messageInput;

    SimpleDateFormat sdf;
    String currentDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        activeWG = new ActiveWG(getApplicationContext());
        activeResident = new ActiveResident(getApplicationContext());
        
        setContentView(R.layout.activity_show_journal);
        journalListView = findViewById(R.id.view_journal_ListView);

        updateListView();
    }
    
    
    public void updateListView(){
        
        String wg_id = Long.toString(activeWG.getActiveWG().getId());
        
        String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/get_wgs_journal.php?wg_id=" + wg_id;
        
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

                    journalEntries = new ArrayList<>();

                    for(int i = 0; i < jsonArray.length(); i++){
                        journalEntries.add(gson.fromJson(jsonArray.getString(i),JournalEntry.class));
                    }

                    journalAdapter = new JournalAdapter(ShowJournalActivity.this,journalEntries);

                    journalListView.setAdapter(journalAdapter);

                }catch (JSONException e){

                    journalEntries = new ArrayList<>();
                    journalAdapter = new JournalAdapter(ShowJournalActivity.this,journalEntries);

                    journalListView.setAdapter(journalAdapter);
                }
                
                
            }
        });

    }
    
    public void onClickAddJournal(View view){


        messageInput = findViewById(R.id.input_message);

        if(!isEmpty(messageInput)) {

            String message = messageInput.getText().toString();

            String wg_id = Long.toString(activeWG.getActiveWG().getId());

            String resident_name = activeResident.getActiveResident().getFirstName();

            sdf = new SimpleDateFormat("dd.MM.yyyy");

            SimpleDateFormat stf = new SimpleDateFormat("HH:mm");

            currentDate = sdf.format(new Date());

            String time = stf.format(new Date());

            String[] timeArray = time.split("\\:");

            Map<String, String> params = new HashMap<>();

            params.put("sent_by", resident_name);
            params.put("wg_id", wg_id);
            params.put("message", message);
            params.put("date", currentDate);
            params.put("hour", timeArray[0]);
            params.put("minute", timeArray[1]);

            String url = "https://sfwgfiuvrt1rmt6g.myfritz.net/insert_journal_entry.php";

            VolleyHelper volleyHelper = new VolleyHelper();

            VolleyHelper.makeStringRequestPOST(getApplicationContext(), url, params, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String response) {
                    messageInput.setText("");
                    updateListView();
                }
            });
        }
    }

    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }

}
