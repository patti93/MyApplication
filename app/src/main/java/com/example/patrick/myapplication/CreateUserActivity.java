package com.example.patrick.myapplication;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.*;

import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonObject;


public class CreateUserActivity extends AppCompatActivity {

    private WG4U_DataSource dataSource;
    private static final String LOG_TAG = CreateUserActivity.class.getSimpleName();


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }

    private int checkinput() {

        EditText inputFirstName = findViewById(R.id.editTextFirstName);
        EditText inputName = findViewById(R.id.editTextName);
        EditText inputBday = findViewById(R.id.editTextBday);
        EditText inputMail = findViewById(R.id.editTextMail);
        EditText inputPassword = findViewById(R.id.editTextPassword);
        EditText inputPassword2 = findViewById(R.id.editTextConfirmPassword);

        String firstName = inputFirstName.getText().toString();
        String name = inputName.getText().toString();
        String bday = inputBday.getText().toString();
        String mail = inputMail.getText().toString();
        String password = inputPassword.getText().toString();
        String password2 = inputPassword2.getText().toString();

        Pattern email_pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
        Pattern bday_pattern = Pattern.compile("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");

        if (isEmpty(inputBday) || isEmpty(inputFirstName) || isEmpty(inputMail) || isEmpty(inputName) || isEmpty(inputPassword) || isEmpty(inputPassword2)) {
            return 4;
        } else if (!email_pattern.matcher(mail).matches()) return 2;

        else if (password.length() < 8) return 3;

        else if (!password.equals(password2)) return 1;

        else if (!bday_pattern.matcher(bday).matches()) return 5;

        /*insert User, local DB
        dataSource = new WG4U_DataSource(this);

        dataSource.open();

        if (dataSource.getResidentsSearch("email = " + "'" + mail + "'").size() != 0) return 6;

        dataSource.insertResident(firstName,name,bday,mail,password);

        dataSource.close();
        */
        //Log.d(LOG_TAG,test.toString());
        return 0;


    }


    public void clickSignUp(View view) {

        int check = checkinput();

        if (check == 0) {

            EditText inputFirstName = findViewById(R.id.editTextFirstName);
            EditText inputName = findViewById(R.id.editTextName);
            EditText inputBday = findViewById(R.id.editTextBday);
            EditText inputMail = findViewById(R.id.editTextMail);
            EditText inputPassword = findViewById(R.id.editTextPassword);


            String firstName = inputFirstName.getText().toString();
            String name = inputName.getText().toString();
            String bday = inputBday.getText().toString();
            String mail = inputMail.getText().toString();
            String password = inputPassword.getText().toString();


            Map<String, String> values = new HashMap<>();
            values.put("firstName", firstName);
            values.put("lastName", name);
            values.put("bday", bday);
            values.put("email", mail);
            values.put("password", password);

            VolleyHelper volleyHelper = new VolleyHelper();

            VolleyHelper.makeStringRequestPOST(getApplicationContext(), "https://sfwgfiuvrt1rmt6g.myfritz.net/create_user.php", values, new VolleyResponseListener() {
                @Override
                public void onError(String message) {
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject message = new JSONObject(response);
                        //if insertion was successful open next activity
                        if(message.getInt("success") == 1){
                            Intent intent = new Intent(getApplicationContext(), CreateUserSuccess.class);
                            startActivity(intent);
                        }
                        Toast.makeText(getApplicationContext(), message.getString("message"), Toast.LENGTH_LONG).show();

                    } catch (JSONException e){

                    }

                }
            });


            //Messages for invalid input
        } else if (check == 1)
            Toast.makeText(CreateUserActivity.this, R.string.no_password_match, Toast.LENGTH_SHORT).show();
        else if (check == 2)
            Toast.makeText(CreateUserActivity.this, R.string.mail_not_valid, Toast.LENGTH_SHORT).show();
        else if (check == 3)
            Toast.makeText(CreateUserActivity.this, R.string.short_password, Toast.LENGTH_SHORT).show();
        else if (check == 4)
            Toast.makeText(CreateUserActivity.this, R.string.empty_Fields, Toast.LENGTH_SHORT).show();
        else if (check == 5)
            Toast.makeText(CreateUserActivity.this, R.string.bday_format_error, Toast.LENGTH_SHORT).show();
        else if (check == 6)
            Toast.makeText(CreateUserActivity.this, R.string.user_exists, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);
    }

}
