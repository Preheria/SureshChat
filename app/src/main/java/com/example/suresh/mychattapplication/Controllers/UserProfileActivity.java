package com.example.suresh.mychattapplication.Controllers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

public class UserProfileActivity extends AppCompatActivity implements CommonActivity,View.OnClickListener{

    private TextView tvStatus;
    private Button btnSendFriendRequest, btnSendMessage,btnUnfriend;
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
        //setting app bar's title
        if(getSupportActionBar()!=null){
            getSupportActionBar().setTitle(extraBundle.getString("fullname")+"'s Profile");
        }
        initializeViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void initializeViews() {
        user=new User(extraBundle.getString("loggedInUserID"));
        ImageView imgProfileView = findViewById(R.id.imageViewOnProfile);
        TextView tvfullName = findViewById(R.id.NameOnProfile);
        TextView tvfullAddress = findViewById(R.id.AddressOnProfile);
        tvStatus=findViewById(R.id.StatusOnProfile);
        btnSendFriendRequest=findViewById(R.id.btnSendRequest);
        btnSendFriendRequest.setOnClickListener(this);
        btnSendMessage=findViewById(R.id.btnSendMessage);
        btnSendMessage.setOnClickListener(this);

        btnUnfriend=findViewById(R.id.btnUnFriend);
        btnUnfriend.setOnClickListener(this);

        //fullname address imageURI ,userID
        targetUserID=extraBundle.getString("targetUserID");
        tvfullName.setText(extraBundle.getString("fullname"));
        tvfullAddress.setText(extraBundle.getString("address"));
        if(extraBundle.getString("imageURI").equals("")){
            imgProfileView.setImageResource(R.drawable.ic_profile_male);
        }
        else{
            Picasso.get().load(extraBundle.getString("imageURI"))
                    .placeholder(R.drawable.ic_profile_male).into(imgProfileView);
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

        checkFriendshipWithTagetUser();
    }

    @Override
    public boolean validateFields() {
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnSendRequest:

                    sendFriendRequest();
                    break;

        case R.id.btnUnFriend:

            unFriend();
                break;

            case R.id.btnSendMessage:
                Intent i=new Intent(UserProfileActivity.this,ConversationActivity.class);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    public void sendFriendRequest(){
        try {
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
                            if (task.isComplete()) {
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
                                                if (task.isComplete()) {
                                                    btnSendFriendRequest.setText("Request Sent");
                                                    btnSendFriendRequest.setEnabled(false);
                                                } else {
                                                    Toast.makeText(
                                                            UserProfileActivity.this,
                                                            "Request could not be sent, please try again later",
                                                            Toast.LENGTH_SHORT
                                                    ).show();
                                                }

                                            }
                                        });


                            } else {
                                Toast.makeText(
                                        UserProfileActivity.this,
                                        "Request could not be sent, please try again later",
                                        Toast.LENGTH_SHORT
                                ).show();
                            }

                        }
                    });
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage()+ " Exception handled");
        }
    }

    public void checkFriendshipWithTagetUser(){
        try {
            //checking if the targetuser is already friend with the current user
            firebaseDAO.getDbReference()
                    .child("FriendLists")
                    .child(user.getUserID())
                    .child(targetUserID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChildren()) {
                                btnSendMessage.setText("Send Message");
                                btnSendMessage.setVisibility(View.VISIBLE);
                                btnUnfriend.setText("Unfriend");
                                btnUnfriend.setVisibility(View.VISIBLE);
                                btnSendFriendRequest.setVisibility(View.INVISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

            //checking if request to the user is already sent
            firebaseDAO.getDbReference()
                    .child("Requests")
                    .child(user.getUserID())
                    .child("sentTo")
                    .child(targetUserID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild("status")) {
                                btnSendFriendRequest.setText("Request Sent");
                                btnSendFriendRequest.setVisibility(View.VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

            //checking if the request is already received by the user

            firebaseDAO.getDbReference()
                    .child("Requests")
                    .child(user.getUserID())
                    .child("receivedFrom")
                    .child(targetUserID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.hasChild("status")) {
                                btnSendFriendRequest.setText("Request Received");
                                btnSendFriendRequest.setVisibility(View.VISIBLE);
                                btnSendFriendRequest.setEnabled(false);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

            if (TextUtils.isEmpty(btnSendFriendRequest.getText())) {
                btnSendFriendRequest.setText("Send Request");
                btnSendFriendRequest.setVisibility(View.VISIBLE);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage() +" Exception handled");
        }
    }

    public void unFriend(){
        try {
            //removing friend from current user's list
            firebaseDAO.getDbReference()
                    .child("FriendLists")
                    .child(user.getUserID())
                    .child(targetUserID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChildren()) {
                                dataSnapshot.getRef().removeValue();

                                //removing current user from target user's list
                                firebaseDAO.getDbReference()
                                        .child("FriendLists")
                                        .child(targetUserID)
                                        .child(user.getUserID())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                if (dataSnapshot.hasChildren()) {
                                                    dataSnapshot.getRef().removeValue();
                                                    btnSendMessage.setVisibility(View.GONE);
                                                    btnUnfriend.setVisibility(View.GONE);
                                                    btnSendFriendRequest.setText("Send Request");
                                                    btnSendFriendRequest.setVisibility(View.VISIBLE);
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
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }
}
