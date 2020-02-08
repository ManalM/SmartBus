package com.example.smartbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class RateStudent extends AppCompatActivity {

    RatingBar ratingBar;
    EditText comment;
    TextView name;
    Button send;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_student);
        ratingBar = findViewById(R.id.ratingBar);
        comment= findViewById(R.id.student_comment);
        name=findViewById(R.id.rating_name);
        send =findViewById(R.id.send_student_rate);
        getActionBar().setTitle("Rating Student");

    }
}
