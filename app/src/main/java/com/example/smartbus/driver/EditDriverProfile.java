package com.example.smartbus.driver;

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

import java.util.zip.Inflater;


public class EditDriverProfile extends AppCompatActivity {

    private EditText name, phone,email;
    Button save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_driver_profile);
        name = findViewById(R.id.driver_name);
        phone = findViewById(R.id.driver_phone);

        email = findViewById(R.id.driver_email);
        save = findViewById(R.id.save_driver_info);
    }



}
