package com.example.patrick.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import javax.sql.DataSource;


public class SearchWGActivity extends AppCompatActivity {

    WG4U_DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_wg);
    }

    public void redirect(View view){

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


    }

}
