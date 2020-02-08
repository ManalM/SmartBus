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
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.smartbus.R;

import org.w3c.dom.Text;


public class RateDriver extends Fragment {

    Button save;
    EditText comment;
    RatingBar ratingBar;
    TextView name;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_rate_driver, container, false);

        save = v.findViewById(R.id.send_driver_rate);
        comment = v.findViewById(R.id.driver_comment);
        ratingBar = v.findViewById(R.id.ratingBar_driver);
        name = v.findViewById(R.id.rating_name_driver);
        return v;
    }

}
