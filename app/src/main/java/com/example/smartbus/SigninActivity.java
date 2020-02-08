package com.example.smartbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.smartbus.driver.DriverPage;

public class SigninActivity extends AppCompatActivity {

    private EditText username,pass;
    private Button login;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        username = (EditText) findViewById(R.id.username);
        pass = (EditText) findViewById(R.id.password);
        login = (Button) findViewById(R.id.login);

        getSupportActionBar().setTitle("Log In");

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(SigninActivity.this, "login", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(SigninActivity.this, com.example.smartbus.driver.DriverPage.class));



            }
        });
    }
}
