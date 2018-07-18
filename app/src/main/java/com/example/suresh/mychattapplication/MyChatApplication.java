package com.example.suresh.mychattapplication;

import android.app.Application;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MyChatApplication extends Application {

    private FirebaseDAO firebaseDAO;
    @Override
    public void onCreate() {
        super.onCreate();


    }


}
