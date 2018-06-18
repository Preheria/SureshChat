package com.example.suresh.mychattapplication.Models;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DAOClass{

    private FirebaseDatabase connectionInstance;
    private DatabaseReference myref;
    public DAOClass() {

        connectionInstance=FirebaseDatabase.getInstance();
        DatabaseReference myref=connectionInstance.getReference("users");

    }

    public void insertData(){
        myref.setValue("hello World");

    }
}
