package com.example.smartbus.student;

import android.content.Context;
import android.net.Uri;
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

import com.example.smartbus.R;

import org.w3c.dom.Text;


public class RateDriver extends AppCompatActivity {

    Button save;
    EditText comment;
    RatingBar ratingBar;
    TextView name;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_rate_driver);

        save = findViewById(R.id.send_driver_rate);
        comment =findViewById(R.id.driver_comment);
        ratingBar = findViewById(R.id.ratingBar_driver);
        name = findViewById(R.id.rating_name_driver);

    }


}
