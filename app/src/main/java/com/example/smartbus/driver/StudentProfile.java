package com.example.smartbus.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.smartbus.R;
import com.example.smartbus.server.Constants;
import com.example.smartbus.server.HttpsRetrieve;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

public class StudentProfile extends AppCompatActivity {

    ImageView image;
    private static TextView Fname, Lname, phone, address, email, health;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);
        image = findViewById(R.id.retrieve_student_image);
        Fname = findViewById(R.id.retrieve_student_first_name);
        Lname = findViewById(R.id.retrieve_student_last_name);
        phone = findViewById(R.id.retrieve_student_phone);
        health = findViewById(R.id.retrieve_student_health);
        address = findViewById(R.id.retrieve_student_address);
        getSupportActionBar().setTitle("Student Information");
        Intent intent = getIntent();
        String fname = intent.getStringExtra("nameOfStudent");
        new HttpsRetrieve(StudentProfile.this, fname, Constants.getStudentInfoUrl,Constants.profileTag).execute();
    }

    public void callStudent(View view) {

        if (!phone.getText().toString().isEmpty()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(StudentProfile.this);

            builder.setTitle("Call student");

            builder.setMessage("Do want to call " + Fname.getText().toString() + "??");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Uri number = Uri.parse("tel:" + phone.getText().toString());
                    Intent callIntent = new Intent(Intent.ACTION_DIAL, number);
                    PackageManager packageManager = getPackageManager();
                    List activities = packageManager.queryIntentActivities(callIntent,
                            PackageManager.MATCH_DEFAULT_ONLY);
                    boolean isIntentSafe = ((List) activities).size() > 0;

                    if (isIntentSafe)
                        try {
                            startActivity(callIntent);
                        } catch (ActivityNotFoundException e) {
                            String message = e.getMessage();
                            Toast.makeText(StudentProfile.this, "Error:" + message, Toast.LENGTH_LONG).show();
                        }
                }
            });
            builder.setNegativeButton("No", null);
            builder.show();


        } else {
            Toast.makeText(StudentProfile.this, "No added number", Toast.LENGTH_LONG).show();

        }
    }

    public void goToLocation(View view) {
        startActivity(new Intent(StudentProfile.this, StudentLocation.class));
    }

    public static class DataParser extends AsyncTask<Void, Void, Integer> {
        Context c;
        String jsonData;
        ProgressDialog progressDialog;
        String lname,
                phonee,
               name,
                addresss, healthh;

        public DataParser(Context context, String json) {
            c = context;
            jsonData = json;
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
                Fname.setText(name);
                Lname.setText(lname);
                phone.setText(phonee);
                address.setText(addresss);
                health.setText(healthh);
                Toast.makeText(c, "Good", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(c, "not good", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }


        //-- reading json code-----
        private int getData() {

            try {

                JSONObject ja = new JSONObject(jsonData);

                    lname = ja.getString("last_name");
                    phonee = ja.getString("phone");
                    name = ja.getString("first_name");

                    addresss = ja.getString("adress");
                    healthh = ja.getString("health");


                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return 0;
        }
    }
}
