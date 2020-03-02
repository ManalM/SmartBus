package com.example.smartbus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.textclassifier.TextLinks;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartbus.driver.DriverPage;
import com.example.smartbus.server.Constants;
import com.example.smartbus.server.RequestHandler;
import com.example.smartbus.server.SharedPrefManager;
import com.example.smartbus.student.EditStudentProfile;
import com.example.smartbus.student.StudentList;
import com.example.smartbus.student.StudentPage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.sql.Driver;
import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private EditText username, pass;
    private Button login;

    public static String user_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
/*

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this, DriverPage.class));
            return;
        }
*/

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

/*        SharedPrefManager.getInstance(getApplicationContext())
                .userLogin(
                        11,
                        username.getText().toString(),
                        null
                );


        startActivity(new Intent(getApplicationContext(), DriverPage.class));
        finish();*/

/*
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, "http://172.20.10.11/login.php", null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                  //  JSONObject obj = new JSONObject(response);
                    if(!response.getBoolean("error")){
                        SharedPrefManager.getInstance(getApplicationContext())
                                .userLogin(
                                        response.getInt("id"),
                                        response.getString("username"),
                                        null
                                );

                        startActivity(new Intent(getApplicationContext(), DriverPage.class));
                        finish();
                    }else{
                        Toast.makeText(
                                getApplicationContext(),
                                response.getString("message"),
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
                parms.put("username", username.getText().toString().trim());
                parms.put("password", pass.getText().toString().trim());
                return parms;
            }
        };
*/
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://sql111.eb2a.com/login.php", new Response.Listener<String>() {
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
            /* if(response.contains("1")){
                 startActivity(new Intent(getApplicationContext(), DriverPage.class));
             }else{
                 Toast.makeText(SigninActivity.this, "wrong username or password ", Toast.LENGTH_SHORT).show();
             }*/
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
                parms.put("username", username.getText().toString().trim());
                parms.put("password", pass.getText().toString().trim());
                return parms;
            }
        };

        //  Volley.newRequestQueue(this).add(stringRequest);

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
