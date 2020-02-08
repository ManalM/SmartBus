package com.example.smartbus.driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.smartbus.R;

import java.util.ArrayList;
import com.example.smartbus.driver.ListAdapter;
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
        getSupportActionBar().setTitle("Students");

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
