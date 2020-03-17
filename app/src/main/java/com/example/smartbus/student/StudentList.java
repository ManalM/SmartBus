package com.example.smartbus.student;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbus.R;
import com.example.smartbus.SigninActivity;
import com.example.smartbus.driver.DriverPage;
import com.example.smartbus.driver.ListAdapter;
import com.example.smartbus.server.Constants;
import com.example.smartbus.server.RecyclerViewHttps;
import com.example.smartbus.server.https;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class StudentList extends AppCompatActivity {
    private RecyclerView recyclerView;
    public static StudentListAdapter studentListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_list);
        //---------------------recyclerView-----------------------
        recyclerView = findViewById(R.id.recycler_view_student);

        recyclerView.setLayoutManager(new GridLayoutManager(StudentList.this, 1));


        RecyclerViewHttps https = new RecyclerViewHttps(StudentList.this, Constants.getChildrenUrl, recyclerView, "id_parent");
        https.execute();

    }

    public static class DataParser extends AsyncTask<Void, Void, Integer> {
        Context c;
        String jsonData;
        RecyclerView recyclerView;

        ProgressDialog progressDialog;
        ArrayList<String> spase = new ArrayList<>();

        public DataParser(Context c, String jsonData, RecyclerView lv) {
            this.c = c;
            this.jsonData = jsonData;
            this.recyclerView = lv;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(c);
            progressDialog.setTitle("Retrieving data");
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        @Override
        protected Integer doInBackground(Void... voids) {
            return this.getData();
        }

        @Override
        protected void onPostExecute(Integer b) {
            super.onPostExecute(b);
            if (b == 1) {

                studentListAdapter = new StudentListAdapter(c, spase);
                recyclerView.setAdapter(studentListAdapter);
                studentListAdapter.setOnItemClickListener(new StudentListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(int position) {
                        Intent intent = new Intent(c, StudentPage.class);
                        intent.putExtra("name", spase.get(position));
                        c.startActivity(intent);

                    }
                });

            } else {
                Toast.makeText(c, "not good", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }

        private int getData() {

            try {

                JSONArray ja = new JSONArray(jsonData);

                JSONObject jo = null;

                spase.clear();
                for (int i = 0; i < ja.length(); i++) {

                    jo = ja.getJSONObject(i);

                    String name = jo.getString("child_name");

                    spase.add(name);
                }

                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return 0;
        }
    }

}