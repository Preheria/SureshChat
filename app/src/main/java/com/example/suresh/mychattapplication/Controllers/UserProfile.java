package com.example.suresh.mychattapplication.Controllers;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class UserProfile extends AppCompatActivity implements CommonActivity,View.OnClickListener{

    private TextView tvfullName,tvfullAddress,tvStatus;
    private Button btnRequest1, btnRequest2;
    private ImageView imgProfileView;
    private User user;
    private String targetUserID;
    private Bundle extraBundle;
    FirebaseDAO firebaseDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        extraBundle=getIntent().getExtras();

        //setting app bar's titl
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(extraBundle.getString("fullname")+"'s Profile");
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        initializeControls();

    }

    @Override
    public void initializeControls() {

        user=new User(extraBundle.getString("loggedInUserID"));

        imgProfileView=findViewById(R.id.imageViewOnProfile);
        tvfullName=findViewById(R.id.NameOnProfile);
        tvfullAddress=findViewById(R.id.AddressOnProfile);
        tvStatus=findViewById(R.id.StatusOnProfile);
        btnRequest1 =findViewById(R.id.btnOnProfile);
        btnRequest1.setOnClickListener(this);

        btnRequest2 =findViewById(R.id.btnOnProfile1);
        btnRequest2.setOnClickListener(this);

        //fullname address imageURI ,userID
        targetUserID=extraBundle.getString("targetUserID");
        tvfullName.setText(extraBundle.getString("fullname"));
        tvfullAddress.setText(extraBundle.getString("address"));
        if(extraBundle.getString("imageURI").equals("")){
            imgProfileView.setImageResource(R.drawable.ic_profile_male);
        }
        else{
            Picasso.get().load(extraBundle.getString("imageURI")).placeholder(R.drawable.ic_profile_male).into(imgProfileView);
        }

        firebaseDAO=FirebaseDAO.getFirebaseDAOObject();

        //query to read status of the target user
        firebaseDAO.getDbReference()
                .child("users")
                .child(targetUserID)
                .child("status")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                   tvStatus.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
        checkIfRequestAlreadySentOrAlreadyReceived();

    }

    @Override
    public boolean validateFields() {
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnOnProfile:

                if(btnRequest1.getText().equals("Send Friend Request")){
                    sendFriendRequest();
                }
                break;

            case R.id.btnOnProfile1:

                break;
            default:
                break;
        }


    }

    private void sendFriendRequest(){

        //this references inserts requests into target user's table
        firebaseDAO.getDbReference()
                .child("Requests")
                .child(targetUserID)
                .child("receivedFrom")
                .child(user.getUserID())
                .child("status")
                .setValue("pending")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            //now insertion to request sender's table begins
                            firebaseDAO
                                    .getDbReference()
                                    .child("Requests")
                                    .child(user.getUserID())
                                    .child("sentTo")
                                    .child(targetUserID)
                                    .child("status")
                                    .setValue("pending")
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isComplete()&&task.isSuccessful())
                                            {
                                                btnRequest1.setText("Request Sent");
                                            }
                                            else{
                                                Toast.makeText(
                                                        UserProfile.this,
                                                        "Request could not be sent, please try again later",
                                                        Toast.LENGTH_SHORT
                                                ).show();
                                            }

                                        }
                                    });


                        }
                        else
                        {
                            Toast.makeText(
                                    UserProfile.this,
                                    "Request could not be sent, please try again later",
                                    Toast.LENGTH_SHORT
                            ).show();
                        }

                    }
                });


    }

    private void checkIfRequestAlreadySentOrAlreadyReceived(){

        //checking if request to the user is already sent

        firebaseDAO.getDbReference()
                .child("Requests")
                .child(user.getUserID())
                .child("sentTo")
                .child(targetUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        //if(dataSnapshot.getKey()==null)
                        if(dataSnapshot.hasChild("status")){
                            btnRequest1.setText("Request Already Sent");
                            btnRequest1.setEnabled(false);
                        }
                        else
                        {

                            //checking if the request is already received by the user

                            firebaseDAO.getDbReference()
                                    .child("Requests")
                                    .child(user.getUserID())
                                    .child("receivedFrom")
                                    .child(targetUserID)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //if(dataSnapshot.getKey()==null)
                                            if(dataSnapshot.hasChild("status")){
                                                btnRequest1.setText("Accept Request");
                                                btnRequest2.setText("Decline Request");
                                                btnRequest2.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                btnRequest1.setText("Send Friend Request");
                                            }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

                        }

                          }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }


}
