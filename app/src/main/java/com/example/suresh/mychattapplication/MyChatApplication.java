package com.example.suresh.mychattapplication;

import android.app.Application;

import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.google.firebase.database.FirebaseDatabase;

public class MyChatApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }


}
