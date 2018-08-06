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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SentRequestListAdapter extends ArrayAdapter<User>{

    private  Context context;
    private int resource;
    private ArrayList<User> userlist;
    private FirebaseDAO firebaseDAO;

     SentRequestListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> userList) {
        super(context, resource, userList);
        this.context=context;
        this.resource=resource;
        this.userlist=userList;
        firebaseDAO=FirebaseDAO.getFirebaseDAOObject();
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull final ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(context);
        //each item will be written as a view which needs to be returned
        final View view=inflater.inflate(resource,null);
        Button cancelButton=view.findViewById(R.id.btnCancel);
        TextView fullName=view.findViewById(R.id.fullName2);
        TextView location=view.findViewById(R.id.location2);
        TextView imageURI=view.findViewById(R.id.IMAGE_URI2);
        final TextView uid=view.findViewById(R.id.uid2);
        CircleImageView thumbnail=view.findViewById(R.id.thumbnail2);
        ViewGroup parentView=view.findViewById(R.id.anItem2);

        //getting a user at a particular index
        User user=userlist.get(position);

        //binding model data with view data
        fullName.setText(user.getFirstName()+" "+user.getLastName());
        location.setText(user.getState()+" , "+user.getCountry());
        uid.setText(user.getUserID());
        imageURI.setText(user.getPp_path());

        if(user.getPp_path().equals("")){
            thumbnail.setImageResource(R.drawable.ic_profile_male);
        }
        else{
            Picasso.get().load(user.getPp_path()).into(thumbnail);
        }
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.setVisibility(View.GONE);
                removeFriendRequest(uid.getText().toString());
                userlist.remove(position);
            }
        });
        return view;
    }


    void removeFriendRequest(final String targetUSerId){

         //removing friend request from sender
         firebaseDAO.getDbReference()
                 .child("Requests")
                 .child(FirebaseDAO.UID)
                 .child("sentTo")
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
                             .child("receivedFrom")
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
                Toast.makeText(context,"Request could not be deleted right now",Toast.LENGTH_SHORT).show();
                                 }
                             });
                 }
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
                 Toast.makeText(context,"Request could not be deleted right now",Toast.LENGTH_SHORT).show();
             }
         });


    }
}











