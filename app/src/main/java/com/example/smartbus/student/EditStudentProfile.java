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

import com.example.smartbus.R;


public class EditStudentProfile extends AppCompatActivity {

    private EditText name, phone,email;
    Button save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_student_profile);
        name =findViewById(R.id.student_name);
        phone = findViewById(R.id.student_phone);

        email = findViewById(R.id.student_email);
        save = findViewById(R.id.save_student_info);
    }

}
