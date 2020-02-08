package com.example.smartbus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class DriverPage extends AppCompatActivity {


    private  RecyclerView recyclerView;
    private ArrayList students;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driver_page);
        recyclerView = findViewById(R.id.recycler_view);
        students = new ArrayList();
        students.add("Manal");
        getActionBar().setTitle("Students");

        recyclerView.setLayoutManager(new GridLayoutManager(DriverPage.this, 1));
        ListAdapter listAdapter = new ListAdapter(DriverPage.this, students);
        recyclerView.setAdapter(listAdapter);


        listAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }
        });

    }
}
