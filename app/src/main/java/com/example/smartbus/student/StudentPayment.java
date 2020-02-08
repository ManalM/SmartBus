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

public class StudentPayment extends Fragment {

    private EditText cardNumber , cvc, holder,date;
    private Button save;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_student_payment, container, false);


        cardNumber = v.findViewById(R.id.card_number);
        cvc = v.findViewById(R.id.cvc);
        date = v.findViewById(R.id.exp_date);
        holder = v.findViewById(R.id.card_holder);
        save = v.findViewById(R.id.save_payment);
        return v;
    }

}
