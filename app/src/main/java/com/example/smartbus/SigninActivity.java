package com.example.smartbus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.smartbus.driver.DriverPage;
import com.example.smartbus.server.Constants;
import com.example.smartbus.server.RequestHandler;
import com.example.smartbus.server.SharedPrefManager;
import com.example.smartbus.student.StudentList;
import com.example.smartbus.student.StudentPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText username, pass;
    private Button login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        // connect with xml
        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        getSupportActionBar().setTitle("Log In");


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                login();
        /*        if (username.getText().toString().equals("D")) {
                    progressBar.setVisibility(View.VISIBLE);
                    startActivity(new Intent(SigninActivity.this, DriverPage.class));
                } else if (username.getText().toString().equals("S")) {
                    progressBar.setVisibility(View.VISIBLE);
                    startActivity(new Intent(SigninActivity.this, StudentList.class));
                } else {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(SigninActivity.this, "press D for Driver and S for student", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
    }

    private void login() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.loginUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if(!obj.getBoolean("error")){
                        SharedPrefManager.getInstance(getApplicationContext())
                                .userLogin(
                                        obj.getInt("id"),
                                        obj.getString("username"),
                                       null
                                );
                        startActivity(new Intent(getApplicationContext(), DriverPage.class));
                        finish();
                    }else{
                        Toast.makeText(
                                getApplicationContext(),
                                obj.getString("message"),
                                Toast.LENGTH_LONG
                        ).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(
                        getApplicationContext(),
                        error.getMessage()+"hh",
                        Toast.LENGTH_LONG
                ).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parms = new HashMap<>();
                parms.put("username", username.getText().toString());
                parms.put("password", pass.getText().toString());
                return parms;
            }
        };

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
