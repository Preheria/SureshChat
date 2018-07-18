package com.example.suresh.mychattapplication.Controllers.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;


public class ReceivedRequestListAdapter extends ArrayAdapter<User>{


    private  Context context;
    private int resource;
    private FirebaseDAO firebaseDAO;

    private ArrayList<User> userlist;

     ReceivedRequestListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> userList) {
        super(context, resource, userList);

        this.context=context;
        this.resource=resource;
        this.userlist=userList;
         firebaseDAO=FirebaseDAO.getFirebaseDAOObject();

    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {


        final int pos=position;
         LayoutInflater inflater=LayoutInflater.from(context);

        //each item will be written as a view which needs to be returned
        final View view=inflater.inflate(resource,null);

        TextView fullName=view.findViewById(R.id.fullName1);
        TextView location=view.findViewById(R.id.location1);
        TextView imageURI=view.findViewById(R.id.IMAGE_URI1);
        final TextView uid=view.findViewById(R.id.uid1);
        CircleImageView thumbnail=view.findViewById(R.id.thumbnail1);

        Button btnAccept=view.findViewById(R.id.btnAccept);
        Button btnDecline=view.findViewById(R.id.btnDecline);

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptRequest(uid.getText().toString(),pos);
            }
        });
        btnDecline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             declineRequest(uid.getText().toString(),pos);
            }
        });


        //getting a user at a particular index
        User user=userlist.get(position);

        //binding model data with view data
        fullName.setText(user.getFirstName()+" "+user.getLastName());
        location.setText(user.getState()+" , "+user.getCountry());
        uid.setText(user.getUserID());
        imageURI.setText(user.getPp_path());

       // if(user.getPp_path().equals("")){
            thumbnail.setImageResource(R.drawable.ic_profile_male);
        //}
        //else{
          //  Picasso.get().load(user.getPp_path()).into(thumbnail);
        //}


        return view;
    }


    //function for accepting friend reqest
    private void acceptRequest(final String targetUSerId,final int position) {

         //checking if request exists as received request first
        firebaseDAO.getDbReference()
                .child("Requests")
                .child(FirebaseDAO.UID)
                .child("receivedFrom")
                .child(targetUSerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {

                    //now checking if there is request in senders log as well
                    firebaseDAO.getDbReference()
                            .child("Requests")
                            .child(targetUSerId)
                            .child("sentTo")
                            .child(FirebaseDAO.UID)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    //removing request from receiver's account
                                    if(dataSnapshot.hasChildren())
                                    {

                                        //coming up to this point, it is confirmed that request data exists in both sender's log and receiver's log
                                        //therefore now registering as friends to eachother
                                        firebaseDAO.getDbReference()
                                                .child("FriendLists")
                                                .child(FirebaseDAO.UID)
                                                .child(targetUSerId)
                                                .child("status")
                                                .setValue("friend")
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {

                                                        if(task.isSuccessful()){

                                                            firebaseDAO.getDbReference()
                                                                    .child("FriendLists")
                                                                    .child(targetUSerId)
                                                                    .child(FirebaseDAO.UID)
                                                                    .child("status")
                                                                    .setValue("friend")
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                       if(task.isSuccessful()){
                                                                           userlist.remove(position);
                                                                       }

                                                                        }
                                                                    });
                                                        }

                                                    }
                                                });
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(context,"Request could not be canceled right now",Toast.LENGTH_SHORT).show();
                                }
                            });

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Request could not be canceled right now",Toast.LENGTH_SHORT).show();
            }
        });

        updateRequestList(targetUSerId);

    }

    //function for removing requests from request log after request is accepted or declined
    private void updateRequestList(final String targetUSerId) {
        firebaseDAO.getDbReference()
                .child("Requests")
                .child(FirebaseDAO.UID)
                .child("receivedFrom")
                .child(targetUSerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {
                    dataSnapshot.getRef().removeValue();



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //now removing request from receiver's account
        firebaseDAO.getDbReference()
                .child("Requests")
                .child(targetUSerId)
                .child("sentTo")
                .child(FirebaseDAO.UID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        //removing request from receiver's account
                        if(dataSnapshot.hasChildren())
                        {
                            dataSnapshot.getRef().removeValue();

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }


    //function for declining friend request
    private void declineRequest(final String targetUSerId,final int position) {


        firebaseDAO.getDbReference()
                .child("Requests")
                .child(FirebaseDAO.UID)
                .child("receivedFrom")
                .child(targetUSerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChildren())
                {
                    dataSnapshot.getRef().removeValue();

                    //now removing request from receiver's account
                    firebaseDAO.getDbReference()
                            .child("Requests")
                            .child(targetUSerId)
                            .child("sentTo")
                            .child(FirebaseDAO.UID)
                            .addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                    //removing request from receiver's account
                                    if(dataSnapshot.hasChildren())
                                    {
                                        dataSnapshot.getRef().removeValue();
                                        userlist.remove(position);

                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(context,"Request could not be canceled right now",Toast.LENGTH_SHORT).show();
                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(context,"Request could not be canceled right now",Toast.LENGTH_SHORT).show();
            }
        });


    }

}
