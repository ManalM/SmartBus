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

public class StudentPayment extends AppCompatActivity {

    private EditText cardNumber , cvc, holder,date;
    private Button save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_student_payment);

        cardNumber = findViewById(R.id.card_number);
        cvc = findViewById(R.id.cvc);
        date = findViewById(R.id.exp_date);
        holder = findViewById(R.id.card_holder);
        save = findViewById(R.id.save_payment);
    }


}
