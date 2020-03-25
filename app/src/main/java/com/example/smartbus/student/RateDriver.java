package com.example.smartbus.student;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartbus.R;
import com.example.smartbus.server.Constants;
import com.example.smartbus.server.SharedPrefManager;
import com.example.smartbus.server.https;

import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static com.example.smartbus.server.Constants.rateTag;


public class RateDriver extends AppCompatActivity {

    Button save;
    EditText comment;
    RatingBar ratingBar;
    TextView name;
    String studentName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rate_driver);

        save = findViewById(R.id.send_driver_rate);
        comment = findViewById(R.id.driver_comment);
        ratingBar = findViewById(R.id.ratingBar_driver);
        name = findViewById(R.id.rating_name_driver);
        Intent intent = getIntent();
        studentName = intent.getStringExtra("name");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                updateDB();
            }
        });
    }

    private void updateDB() {
        https https = new https(RateDriver.this, Constants.driverRateUrl, studentName, rateTag);
        https.execute(comment.getText().toString(), String.valueOf(ratingBar.getRating()), name.getText().toString());

    }

}

