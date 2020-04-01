package com.example.smartbus;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Intent;
import android.media.MediaPlayer;
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
import com.example.smartbus.driver.EditDriverProfile;
import com.example.smartbus.driver.StudentLocation;
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
                        if (SharedPrefManager.getInstance(SigninActivity.this).getUsername().startsWith("d")) {
                            MediaPlayer.create(SigninActivity.this, R.raw.correct).start();
                            startActivity(new Intent(getApplicationContext(), DriverPage.class));
                            finish();
                        } else if (SharedPrefManager.getInstance(SigninActivity.this).getUsername().startsWith("p")) {
                            MediaPlayer.create(SigninActivity.this, R.raw.correct).start();
                            startActivity(new Intent(getApplicationContext(), StudentList.class));
                            finish();
                        } else {

                            Toast.makeText(SigninActivity.this, "Error in username", Toast.LENGTH_SHORT).show();
                        }
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
                        error.getMessage(),
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

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
