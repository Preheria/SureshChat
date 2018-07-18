package com.example.suresh.mychattapplication.Controllers.Fragments;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class ChatListAdapter extends ArrayAdapter<HashMap<String,String>> {

    private Context context;
    private int resource;
    private ArrayList<HashMap<String,String>> chatItemList;

    private FirebaseDAO firebaseDAO;

    public ChatListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<HashMap<String,String>> chatItemList) {
        super(context, resource, chatItemList);
        this.context=context;
        this.resource=resource;
        this.chatItemList=chatItemList;
        firebaseDAO=FirebaseDAO.getFirebaseDAOObject();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(resource,null);

        //accessing static connection object for database connection


        final HashMap<String,String> chatItem=chatItemList.get(position);


        final String lastMessage=chatItem.get("lastMessage");
        final String lastAccessedOn=chatItem.get("lastAccessedOn");
        final String targetUserID=chatItem.get("targetUserID");
        final String senderID=chatItem.get("sender");
        final String receiverID=chatItem.get("receiver");



        final TextView fullName=view.findViewById(R.id.fullNameChatItem);

        final TextView lastMessageChatItem=view.findViewById(R.id.lastMessageChatItem);
        if(receiverID.equals(targetUserID)){
            lastMessageChatItem.setText("You : ".concat(lastMessage));
        }
        else
        {
            lastMessageChatItem.setText(lastMessage);
        }


        final TextView lastAccessedDate=view.findViewById(R.id.lastAccessedDate);

        SimpleDateFormat dateFormat=new SimpleDateFormat("h:mm a");
        Date date=new Date(Long.valueOf(lastAccessedOn));

        lastAccessedDate.setText(dateFormat.format(date));

        //TextView imageURI=view.findViewById(R.id.hiddenImageURI_ChatItem);
       // TextView uid=view.findViewById(R.id.hiddenUserID_ChatItem);
        final CircleImageView thumbnail=view.findViewById(R.id.thumbnailChatItem);
        final ImageView onlineIcon =view.findViewById(R.id.onlineIcon);



        firebaseDAO.getDbReference()
                                    .child("users")
                                    .child(targetUserID)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                            String fullname=dataSnapshot.child("fName").getValue(String.class)
                                                            .concat(" ")
                                                            .concat(dataSnapshot.child("lName").getValue(String.class));
                                            String imageURI=dataSnapshot.child("pp_path").getValue(String.class);

                                            if(dataSnapshot.child("online").getValue(Boolean.class)){
                                                onlineIcon.setVisibility(View.VISIBLE);
                                            }
                                            else
                                            {
                                                onlineIcon.setVisibility(View.INVISIBLE);
                                            }

                                            if(imageURI.equals("")){
                                                thumbnail.setImageResource(R.drawable.ic_profile_male);
                                            }
                                            else{
                                                Picasso.get().load(imageURI).into(thumbnail);
                                            }

                                            fullName.setText(fullname);



                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });

        //each item will be written as a view which needs to be returned



        //ViewGroup parentView=view.findViewById(R.id.friendItem);

        //getting a user at a particular index



        return view;
    }
}
