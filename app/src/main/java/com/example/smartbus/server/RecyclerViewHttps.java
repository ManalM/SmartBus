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

public class RecyclerViewHttps extends AsyncTask<Void, Integer, String> {

    Context c;

    String urlAddres;
    RecyclerView recyclerView;
    ProgressDialog progressDialog;

    String userID;

    public RecyclerViewHttps(Context c, String url, RecyclerView recyclerView, String userId) {
        this.c = c;
        this.recyclerView = recyclerView;
        this.urlAddres = url;
        userID = userId;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog = new ProgressDialog(c);
        progressDialog.setTitle("Retrieving");
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(Void... voids) {
        String data = downloadData();
        return data;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        super.onPostExecute(aVoid);
        progressDialog.dismiss();
        if (aVoid != null) {
            DriverPage.DataParser p = new DriverPage.DataParser(c, aVoid, recyclerView);
            p.execute();

            StudentList.DataParser s = new StudentList.DataParser(c, aVoid, recyclerView);
            s.execute();
        } else {
            Toast.makeText(c, "not good" + aVoid, Toast.LENGTH_LONG).show();

        }

    }

    private String downloadData() {
        InputStream is = null;
        String line = null;


        try {

            URL url = new URL(urlAddres);

            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);

            String userId = SharedPrefManager.getInstance(c).getUsername();


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
}
