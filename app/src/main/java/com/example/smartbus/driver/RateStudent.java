package com.example.smartbus.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartbus.R;
import com.example.smartbus.server.Constants;
import com.example.smartbus.server.SharedPrefManager;
import com.example.smartbus.server.https;
import com.example.smartbus.student.RateDriver;

import org.json.JSONObject;

import static com.example.smartbus.server.Constants.rateDriverTag;
import static com.example.smartbus.server.Constants.rateTag;

public class RateStudent extends AppCompatActivity {

    RatingBar ratingBar;
    EditText comment;
 TextView name;
    Button send;
        String driverName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_student);
        ratingBar = findViewById(R.id.ratingBar);
        comment= findViewById(R.id.student_comment);
        name=findViewById(R.id.rating_name);
        send =findViewById(R.id.send_student_rate);
        getSupportActionBar().setTitle("Rating Student");

        Intent intent = getIntent();
        driverName = SharedPrefManager.getInstance(this).getUsername().substring(1);
        name.setText(intent.getStringExtra("nameOfStudent"));
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MediaPlayer.create(RateStudent.this, R.raw.correct).start();
                updateDB();
            }
        });
    }

    private void updateDB() {
        https https = new https(RateStudent.this, Constants.insertStudentRate, name.getText().toString(),rateDriverTag);
        https.execute(comment.getText().toString(), String.valueOf(ratingBar.getRating()),driverName);

    }

}
