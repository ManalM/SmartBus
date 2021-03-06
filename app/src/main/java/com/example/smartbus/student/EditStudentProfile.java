package com.example.smartbus.student;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.smartbus.R;
import com.example.smartbus.driver.Scan;
import com.example.smartbus.server.Constants;
import com.example.smartbus.server.https;

import static com.example.smartbus.server.Constants.infoTag;


public class EditStudentProfile extends AppCompatActivity implements View.OnClickListener {

    private EditText health, phone;
    Button save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_student_profile);
        getSupportActionBar().setTitle("Edit Profile");

        health = findViewById(R.id.student_health);
        phone = findViewById(R.id.student_phone);

        save = findViewById(R.id.save_student_info);
        save.setOnClickListener(this);
    }

    private void updateDB() {
        https https = new https(EditStudentProfile.this, Constants.updateStudentProfile, "id_parent", infoTag);
        https.execute(health.getText().toString(), phone.getText().toString(), getIntent().getStringExtra("name"));
    }

    @Override
    public void onClick(View v) {

        updateDB();
        MediaPlayer m = MediaPlayer.create(EditStudentProfile.this, R.raw.correct);
        m.start();
    }
}
