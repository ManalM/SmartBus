package com.example.smartbus.student;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.example.smartbus.R;
import com.example.smartbus.server.Constants;
import com.example.smartbus.server.RecyclerViewHttps;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DriverFeedback extends AppCompatActivity {
    private RecyclerView recyclerView;
    private int rating;
   public static DriverFeedbackAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //---------------------recyclerView-----------------------
//todo:test
        setContentView(R.layout.fragment_driver_feedback);
        recyclerView = findViewById(R.id.diver_feedback);
/*        ArrayList<String> names  = new ArrayList<>();
        ArrayList<String> comments  = new ArrayList<>();

        names.add("ahmed");
        comments.add("Good boy");
        rating = 1;*/
        recyclerView.setLayoutManager(new GridLayoutManager(DriverFeedback.this, 1));
        RecyclerViewHttps https =  new RecyclerViewHttps(DriverFeedback.this, Constants.insertStudentRate,recyclerView,"student_name");
        https.execute();
    }
    public static class DataParser extends AsyncTask<Void, Void, Integer> {
        Context c;
        String jsonData;
        RecyclerView recyclerView;

        ProgressDialog progressDialog;
        ArrayList<String> spase = new ArrayList<>();
        String[] array = new String[4];
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

                adapter= new DriverFeedbackAdapter(c, array);
                recyclerView.setAdapter(adapter);
//here adapter

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

                    String name = jo.getString("student_name");
                    String stars = jo.getString("stars");
                    String comment = jo.getString("comment");
                    String date = jo.getString("time");
                    SimpleDateFormat format = new SimpleDateFormat();
                    String formattedDate =format.format(date);
                   // spase.add(name);
                    array[0] = name ;
                    array[1] = stars;
                    array[2] = comment;
                    array[3] = formattedDate;
                }

                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return 0;
        }
    }

        }
