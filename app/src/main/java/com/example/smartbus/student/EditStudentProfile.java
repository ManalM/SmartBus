package com.example.smartbus.student;

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


public class EditStudentProfile extends Fragment {

    private EditText name, phone,email;
    Button save;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =  inflater.inflate(R.layout.fragment_edit_student_profile, container, false);

        name = v.findViewById(R.id.student_name);
        phone = v.findViewById(R.id.student_phone);

        email = v.findViewById(R.id.student_email);
        save = v.findViewById(R.id.save_student_info);
        return v;
    }

}
