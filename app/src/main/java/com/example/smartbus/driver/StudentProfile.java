package com.example.smartbus.driver;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
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
        email = findViewById(R.id.retrieve_student_email);
        health = findViewById(R.id.retrieve_student_health);
        address = findViewById(R.id.retrieve_student_address);
        getSupportActionBar().setTitle("Student Information");
        Intent intent = getIntent();
        //todo:implement calling
        String fname = intent.getStringExtra("nameOfStudent");
        new HttpsRetrieve(StudentProfile.this, fname, Constants.getStudentInfoUrl).execute();
    }

    public static class DataParser extends AsyncTask<Void, Void, Integer> {
        Context c;
        String jsonData;
        ProgressDialog progressDialog;
        HashMap<String, String> hashMap = new HashMap<>();

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
                Toast.makeText(c, "Good", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(c, "not good", Toast.LENGTH_LONG).show();
            }
            progressDialog.dismiss();
        }


        //-- reading json code-----
        private int getData() {

            try {

                JSONArray ja = new JSONArray(jsonData);

                JSONObject jo = null;

                for (int i = 0; i < ja.length(); i++) {
//todo:error here
                    jo = ja.getJSONObject(i);

                    String lname = jo.getString("last_name");
                    String phonee = jo.getString("phone");
                    String emaill = jo.getString("email");
                    String name = jo.getString("first_name");

                    String addresss = jo.getString("adress");
                    String healthh = jo.getString("health");

                    Fname.setText(name);
                    Lname.setText(lname);
                    phone.setText(phonee);
                    address.setText(addresss);
                    email.setText(emaill);
                    health.setText(healthh);
                }

                return 1;
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return 0;
        }
    }
}
