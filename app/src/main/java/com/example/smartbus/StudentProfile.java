package com.example.smartbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class StudentProfile extends AppCompatActivity {

    ImageView image;
    TextView Fname ,Lname , phone, address,email, health;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        image = findViewById(R.id.retrieve_student_image);
        Fname = findViewById(R.id.retrieve_student_first_name);
        Lname = findViewById(R.id.retrieve_student_last_name);
        phone = findViewById(R.id.retrieve_student_phone);
        email = findViewById(R.id.retrieve_student_email);
        health = findViewById(R.id.retrieve_student_health);
        address = findViewById(R.id.retrieve_student_address);

    }
}
