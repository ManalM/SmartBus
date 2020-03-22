package com.example.smartbus.server;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.Toast;


import androidx.recyclerview.widget.RecyclerView;

import com.example.smartbus.driver.DriverPage;
import com.example.smartbus.student.StudentList;

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

import static com.example.smartbus.server.Constants.infoDriverTag;
import static com.example.smartbus.server.Constants.infoTag;
import static com.example.smartbus.server.Constants.rateTag;

public class https extends AsyncTask<String, Void, String> {

    Context c;

    String urlAddres;


    String userID;

    String tag;

    public https(Context c, String url, String userId, String tag) {
        this.c = c;
        this.urlAddres = url;
        userID = userId;
        this.tag = tag;
    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();

    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream is = null;
        String line = null;


        try {

            String param0, param1, param2;

            URL url = new URL(urlAddres);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            Uri.Builder builder = new Uri.Builder();
            if (tag.equals(infoTag)) {
                // sending data
                param0 = strings[0];
                param1 = strings[1];

                param2 = strings[2];
                String userId = SharedPrefManager.getInstance(c).getUsername();
                builder.appendQueryParameter(userID, userId)
                        .appendQueryParameter("health", param0)
                        .appendQueryParameter("phone", param1)
                        .appendQueryParameter("first_name", param2);
            } else if (tag.equals(rateTag)) {
                param0 = strings[0];
                param1 = strings[1];

                param2 = strings[2];
                builder.appendQueryParameter("student_name", userID)
                        .appendQueryParameter("comment", param0)
                        .appendQueryParameter("stars", param1)
                        .appendQueryParameter("driver_name", param2);
            } else if (tag.equals(infoDriverTag)) {
                param0 = strings[0];
                param1 = strings[1];
                String userId = SharedPrefManager.getInstance(c).getUsername();
                builder.appendQueryParameter(userID, userId)
                        .appendQueryParameter("phone", param1)
                        .appendQueryParameter("email", param0);
            }

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

    @Override
    protected void onPostExecute(String aVoid) {
        //  super.onPostExecute(aVoid);
        // progressDialog.dismiss();
        if (aVoid != null) {

            Toast.makeText(c, aVoid, Toast.LENGTH_SHORT).show();
        } else {
/*
          ////  progressDialog.setMessage(aVoid);
*/

            Toast.makeText(c, aVoid, Toast.LENGTH_LONG).show();

        }

    }



}
