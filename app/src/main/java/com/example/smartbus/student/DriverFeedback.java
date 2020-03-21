package com.example.smartbus.student;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartbus.R;

import java.util.ArrayList;

public class DriverFeedback extends AppCompatActivity {
    private RecyclerView recyclerView;
    private int rating;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---------------------recyclerView-----------------------
//todo:take driver feedback from database
        setContentView(R.layout.fragment_driver_feedback);
        recyclerView = findViewById(R.id.diver_feedback);
        ArrayList<String> names  = new ArrayList<>();
        ArrayList<String> comments  = new ArrayList<>();

        names.add("ahmed");
        comments.add("Good boy");
        rating = 1;
        recyclerView.setLayoutManager(new GridLayoutManager(DriverFeedback.this, 1));
//todo:why it is list ???? for one student only
        DriverFeedbackAdapter studentListAdapter = new DriverFeedbackAdapter(DriverFeedback.this, names, comments, rating);
        recyclerView.setAdapter(studentListAdapter);
    }

        }
