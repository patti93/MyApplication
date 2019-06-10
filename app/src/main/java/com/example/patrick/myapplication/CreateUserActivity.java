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


public class CreateUserActivity extends AppCompatActivity {

    private WG4U_DataSource dataSource;
    private static final String LOG_TAG = MainActivity.class.getSimpleName();


    private boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() <= 0;
    }

    private int safeUser() {

        EditText inputFirstName = findViewById(R.id.editTextFirstName);
        EditText inputName = findViewById(R.id.editTextName);
        EditText inputBday = findViewById(R.id.editTextBday);
        EditText inputMail = findViewById(R.id.editTextMail);
        EditText inputPassword = findViewById(R.id.editTextPassword);
        EditText inputPassword2 = findViewById(R.id.editTextConfirmPassword);

        final String firstName = inputFirstName.getText().toString();
        final String name = inputName.getText().toString();
        final String bday = inputBday.getText().toString();
        final String mail = inputMail.getText().toString();
        final String password = inputPassword.getText().toString();
        final String password2 = inputPassword2.getText().toString();

        Pattern email_pattern = Pattern.compile("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");
        Pattern bday_pattern = Pattern.compile("^(?:(?:31(\\/|-|\\.)(?:0?[13578]|1[02]))\\1|(?:(?:29|30)(\\/|-|\\.)(?:0?[1,3-9]|1[0-2])\\2))(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$|^(?:29(\\/|-|\\.)0?2\\3(?:(?:(?:1[6-9]|[2-9]\\d)?(?:0[48]|[2468][048]|[13579][26])|(?:(?:16|[2468][048]|[3579][26])00))))$|^(?:0?[1-9]|1\\d|2[0-8])(\\/|-|\\.)(?:(?:0?[1-9])|(?:1[0-2]))\\4(?:(?:1[6-9]|[2-9]\\d)?\\d{2})$");

        if (isEmpty(inputBday) || isEmpty(inputFirstName) || isEmpty(inputMail) || isEmpty(inputName) || isEmpty(inputPassword) || isEmpty(inputPassword2)) {
            return 4;
        } else if (!email_pattern.matcher(mail).matches()) return 2;

        else if (password.length() < 8) return 3;

        else if (!password.equals(password2)) return 1;

        else if (!bday_pattern.matcher(bday).matches()) return 5;


        dataSource = new WG4U_DataSource(this);

        dataSource.open();

        if (dataSource.getResidentsSearch("email = " + "'" + mail + "'").size() != 0) return 6;

        dataSource.insertResident(firstName,name,bday,mail,password);

        dataSource.close();

        return 0;


    }


    public void clickSignUp(View view) {

        int check = safeUser();

        if (check == 0) {
            successRedirect(view);
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


    public void successRedirect(View view) {
        Intent intent = new Intent(this, CreateUserSuccess.class);
        startActivity(intent);
    }
}
