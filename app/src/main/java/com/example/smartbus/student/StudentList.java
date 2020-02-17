package com.example.smartbus.student;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbus.R;
import com.example.smartbus.SigninActivity;
import com.example.smartbus.driver.DriverPage;

import java.util.ArrayList;

public class StudentList extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list);
        //---------------------recyclerView-----------------------
        recyclerView = findViewById(R.id.recycler_view_student);
        ArrayList<String> arrayList  = new ArrayList<>();
        arrayList.add("ahmed");
        recyclerView.setLayoutManager(new GridLayoutManager(StudentList.this, 1));

        StudentListAdapter studentListAdapter = new StudentListAdapter(StudentList.this, arrayList);
        recyclerView.setAdapter(studentListAdapter);
        studentListAdapter.setOnItemClickListener(new StudentListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                startActivity(new Intent(StudentList.this, StudentPage.class));

            }
        });
    }
}
