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

public class https extends AsyncTask<String, Void, String> {

    Context c;

    String urlAddres;

    ProgressDialog progressDialog;

    String userID;

    public https(Context c, String url, String userId) {
        this.c = c;
        this.urlAddres = url;
        userID = userId;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(c);
        progressDialog.setTitle("Updating ... ");
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        InputStream is = null;
        String line = null;


        try {

            String health = strings[0];
            String phone = strings[1];
            URL url = new URL(urlAddres);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            String userId = SharedPrefManager.getInstance(c).getUsername();


            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter(userID, userId)
                    .appendQueryParameter("health", health)
                    .appendQueryParameter("phone", phone);


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
            progressDialog.setMessage(aVoid);

            Toast.makeText(c, "Record updated Failed" + aVoid, Toast.LENGTH_LONG).show();

        }

    }

/*
    private void downloadData() {
        InputStream is = null;
        String line = null;



        try {

            String name =
            URL url = new URL(urlAddres);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

           String userId= SharedPrefManager.getInstance(c).getUsername();


            Uri.Builder builder = new Uri.Builder()
                    .appendQueryParameter(userID, userId);

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
*/

}
