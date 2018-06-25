package com.example.suresh.mychattapplication.Controllers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity implements CommonActivity{

    private User user;
    private FirebaseDAO firebaseDAO;
    private TextView tvFullname,tvUsername,tvQuote,tvEmail,tvPhone,tvAddress,tvFriendCount;
    private DatabaseReference dbrf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setTitle("My profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initializeControls();
    }

    @Override
    public void initializeControls() {
        firebaseDAO=FirebaseDAO.getFirebaseDAOObject();
        user=new User(firebaseDAO.getFirebaseUser().getUid());

        //holds reference to the database path
        //i.e. /users/(Userid)

        tvFullname=findViewById(R.id.p_fullName);
        tvUsername=findViewById(R.id.p_username);
        tvQuote=findViewById(R.id.p_motto);
        tvEmail=findViewById(R.id.p_email);
        tvPhone=findViewById(R.id.p_phone);
        tvAddress=findViewById(R.id.p_address);
        tvFriendCount=findViewById(R.id.p_friendCount);

        dbrf=firebaseDAO.getDbReference().child("users").child(user.getUserID());

        dbrf.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    tvFullname.setText(dataSnapshot.child("fName").getValue().toString()
                            +" "
                            +dataSnapshot.child("lName").getValue().toString()
                    );

                    tvUsername.setText(dataSnapshot.child("username").getValue().toString());

                    tvQuote.setText("Empty");

                    tvEmail.setText(dataSnapshot.child("email").getValue().toString());

                    tvPhone.setText(dataSnapshot.child("phone").getValue().toString());

                    tvAddress.setText(dataSnapshot.child("country").getValue().toString()
                            +" "+dataSnapshot.child("state").getValue().toString()
                            +" "+dataSnapshot.child("homeAddress").getValue().toString()

                    );

                    tvFriendCount.setText(tvFriendCount.getText()+" : "+ 0);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void loadDataIntoControls(){

    }

    @Override
    public boolean validateFields() {
        return false;
    }
}
