package com.example.smartbus.driver;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.smartbus.R;
import com.example.smartbus.server.Constants;
import com.example.smartbus.server.https;
import com.example.smartbus.student.EditStudentProfile;

import java.util.zip.Inflater;

import static com.example.smartbus.server.Constants.infoDriverTag;


public class EditDriverProfile extends AppCompatActivity {

    private EditText phone,email;
    Button save;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_edit_driver_profile);
        phone = findViewById(R.id.driver_phone);

        email = findViewById(R.id.driver_email);
        save = findViewById(R.id.save_driver_info);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateDB();
                MediaPlayer m = MediaPlayer.create(EditDriverProfile.this, R.raw.correct);
                m.start();
            }
        });
    }

    private void updateDB() {
        String nameOfDriver = getIntent().getStringExtra("name");
        https https = new https(EditDriverProfile.this, Constants.updateDriverProfile, "id_driver", infoDriverTag);
        https.execute(phone.getText().toString(), email.getText().toString(),nameOfDriver);
    }



}
