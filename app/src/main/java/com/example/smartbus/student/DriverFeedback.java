package com.example.smartbus.student;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.widget.Toast;

import com.example.smartbus.R;
import com.example.smartbus.server.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class DriverFeedback extends AppCompatActivity {
    private RecyclerView recyclerView;
String studentName;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setTitle("Driver feedback");

        //---------------------recyclerView-----------------------
        setContentView(R.layout.fragment_driver_feedback);
        recyclerView = findViewById(R.id.diver_feedback);

        Intent intent = getIntent();
        studentName = intent.getStringExtra("name");
        //----------------------------------
        recyclerView.setLayoutManager(new GridLayoutManager(DriverFeedback.this, 1));
        //----------------------------------

        DataParser dataParser = new DataParser(DriverFeedback.this, Constants.feedbackURL, recyclerView, "student_name",studentName);
        dataParser.execute();
    }

    public static class DataParser extends AsyncTask<Void, Void, Integer> {
        Context c;
        String url,name;
        RecyclerView recyclerView;
        DriverFeedbackAdapter adapter;

        ProgressDialog progressDialog;
        ArrayList<String> comments,star,names,date;
        String userid;
        DataParser(Context c, String dataUrl, RecyclerView lv, String user, String userName) {
            this.c = c;
            this.url = dataUrl;
            this.recyclerView = lv;
            userid = user;
            name = userName;
            //------------------------
            comments = new ArrayList<>();
            star= new ArrayList<>();
            names = new ArrayList<>();
            date = new ArrayList<>();
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
            return this.getData(url, userid);
        }

        @Override
        protected void onPostExecute(Integer b) {
            super.onPostExecute(b);
            if (b == 1) {

                adapter = new DriverFeedbackAdapter(c, comments,names,star,date);
                recyclerView.setAdapter(adapter);

            } else {
                Toast.makeText(c, "not good", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }

        private String downloadData(String urlAddres, String userID) {
            InputStream is = null;
            String line = null;


            try {

                URL url = new URL(urlAddres);

                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoInput(true);
                httpURLConnection.setDoOutput(true);

                String studentName = name;


                Uri.Builder builder = new Uri.Builder()
                        .appendQueryParameter(userID, studentName);

                String query = builder.build().getEncodedQuery();
                OutputStream os = httpURLConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));

                writer.write(query);
                writer.flush();
                writer.close();
                os.close();
                httpURLConnection.connect();
                is = new BufferedInputStream(httpURLConnection.getInputStream());

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));


                StringBuilder stringBuilder = new StringBuilder();
                if (stringBuilder != null) {


                    while ((line = bufferedReader.readLine()) != null) {

                        stringBuilder.append(line + "\n");

                    }
                } else {
                    return null;
                }

                return stringBuilder.toString().trim();


            } catch (MalformedURLException e) {
                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();

            } finally {
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }


            return null;
        }

        private int getData(String urlAddres, String userID) {
            String jsonData = downloadData(urlAddres, userID);
            try {

                JSONArray ja = new JSONArray(jsonData);

                JSONObject jo = null;

                for (int i = 0; i < ja.length(); i++) {

                    jo = ja.getJSONObject(i);


                    names.add(jo.getString("driver_name"));
                    star.add( jo.getString("stars"));
                    comments.add(jo.getString("comment"));
                    date.add(jo.getString("time"));
                }

                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return 0;
        }
    }

}
