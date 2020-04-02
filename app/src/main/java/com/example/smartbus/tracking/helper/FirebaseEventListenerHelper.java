package com.example.smartbus.tracking.helper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.smartbus.student.StudentPage;
import com.example.smartbus.tracking.FirebaseDriverListener;
import com.example.smartbus.tracking.model.Driver;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

public class FirebaseEventListenerHelper  implements ChildEventListener {
    FirebaseDriverListener firebaseDriverListener;

    public FirebaseEventListenerHelper(StudentPage studentPage) {
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Driver driver = dataSnapshot.getValue(Driver.class);
        firebaseDriverListener.onDriverAdded(driver);
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        Driver driver = dataSnapshot.getValue(Driver.class);
        firebaseDriverListener.onDriverUpdated(driver);
    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        Driver driver = dataSnapshot.getValue(Driver.class);
        firebaseDriverListener.onDriverRemoved(driver);
    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }
}
