package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import javax.sql.DataSource;


public class SearchWGActivity extends AppCompatActivity {

    WG4U_DataSource dataSource;
    private static final String LOG_TAG = SearchWGActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_wg);
    }

    public void redirect(View view){

        EditText inputSearch = findViewById(R.id.input_search);

        final String wgSearchString = inputSearch.getText().toString();

        VolleyHelper volleyHelper = new VolleyHelper();

        VolleyHelper.makeStringRequestGET(this, "https://sfwgfiuvrt1rmt6g.myfritz.net/find_wg.php?wgname=" + wgSearchString, new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                Log.d(LOG_TAG,response);
                try {
                    JSONObject jsonObject = new JSONObject(response);

                    if(jsonObject.getInt("status") == 1){

                        Intent intent = new Intent(SearchWGActivity.this,WgLoginActivity.class);
                        intent.putExtra("wgName", wgSearchString);
                        startActivity(intent);

                    }
                    else if (jsonObject.getInt("status") == 0) {

                        Toast.makeText(getApplicationContext(),R.string.no_result,Toast.LENGTH_LONG).show();

                    }

                } catch (JSONException e){

                    Log.d(LOG_TAG,e.getMessage());
                }

            }
        });









    /*
    EditText inputSearch = findViewById(R.id.input_search);

    String wgSearchString = inputSearch.getText().toString();

    dataSource = new WG4U_DataSource(this);
    dataSource.open();

    List<Wg> wgList = dataSource.getWGsSearch("name = '" + wgSearchString + "'");

    if(wgList.size() == 0){
        Toast.makeText(SearchWGActivity.this,R.string.no_result,Toast.LENGTH_SHORT).show();
        dataSource.close();
    } else{
        dataSource.close();
        Intent intent = new Intent(SearchWGActivity.this,WgLoginActivity.class);
        intent.putExtra("wgName", wgList.get(0).getName());
        startActivity(intent);
    }
*/
    }

}
