package com.example.smartbus.driver;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.smartbus.R;

import java.util.zip.Inflater;


public class EditDriverProfile extends Fragment {

    private EditText name, phone,email;
    Button save;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_driver_profile, container, false);

        name = v.findViewById(R.id.driver_name);
        phone = v.findViewById(R.id.driver_phone);

        email = v.findViewById(R.id.driver_email);
        save = v.findViewById(R.id.save_driver_info);
        return v;
    }

}
