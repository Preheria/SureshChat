package com.example.suresh.mychattapplication.Controllers.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suresh.mychattapplication.Controllers.CommonActivity;
import com.example.suresh.mychattapplication.Controllers.Conversation;
import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.Models.User;
import com.example.suresh.mychattapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class FriendList extends Fragment implements CommonActivity{

    private View parentView;
    private ListView friendList,receivedRequestList,sentRequestList;

    private ArrayList<User> friendsArray,friendRequestsArray,sentRequestsArray;
    private String[] strings;
    private String userID;
    private User user;
    private FirebaseDAO firebaseDAO;
    private FriendListAdapter FLAdapter;
    private ReceivedRequestListAdapter RRLAdapter;
    private SentRequestListAdapter SRLAdapter;
    private OptionalAdapter optionalAdapter;


    public FriendList() {
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView =inflater.inflate(R.layout.fragment_friend_list, container, false);

        //initialized parentView so that findViewByID could be used later
        return parentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeControls();
        loadReceivedRequests();
        loadSentFriendRequests();
        loadFriendsIntoList();

    }

    @Override
    public void initializeControls() {


        firebaseDAO=FirebaseDAO.getFirebaseDAOObject();
        friendList= parentView.findViewById(R.id.friendLists);
        receivedRequestList= parentView.findViewById(R.id.receivedRequestLists);
        sentRequestList= parentView.findViewById(R.id.sentRequestLists);

        friendsArray=new ArrayList<>();
        friendRequestsArray=new ArrayList<>();
        sentRequestsArray=new ArrayList<>();

            FLAdapter = new FriendListAdapter(parentView.getContext(), R.layout.friend_item, friendsArray);
            SRLAdapter=new SentRequestListAdapter(parentView.getContext(),R.layout.sent_request_item,sentRequestsArray);
            RRLAdapter=new ReceivedRequestListAdapter(parentView.getContext(),R.layout.received_request_item,friendRequestsArray);



            friendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView fullName=view.findViewById(R.id.fullNameFriendItem);
                TextView uid=view.findViewById(R.id.uidFriendItem);
                TextView imageURI=view.findViewById(R.id.IMAGE_URI_FriendItem);

                Intent i=new Intent(getActivity(),Conversation.class);
                i.putExtra("fullname",fullName.getText());
                i.putExtra("targetUserID",uid.getText());
                i.putExtra("imageURI",imageURI.getText());
                startActivity(i);
            }
        });

    }

    @Override
    public boolean validateFields() {
        return false;
    }

    public void loadSentFriendRequests(){

        sentRequestsArray.clear();

        firebaseDAO.getDbReference()
                .child("Requests")
                .child(FirebaseDAO.UID)
                .child("sentTo")
                .orderByKey()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChildren()) {

                            sentRequestList.setAdapter(SRLAdapter);
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {

                                firebaseDAO.getDbReference()
                                        .child("users")
                                        .child(snap.getKey())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                User user = new User(
                                                        dataSnapshot.getKey(),
                                                        dataSnapshot.child("fName").getValue(String.class),
                                                        dataSnapshot.child("lName").getValue(String.class),
                                                        dataSnapshot.child("country").getValue(String.class),
                                                        dataSnapshot.child("state").getValue(String.class),
                                                        dataSnapshot.child("pp_path").getValue(String.class)

                                                );
                                                sentRequestsArray.add(user);
                                                SRLAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                           }
                        }

                        else{

                            strings= new String[]{"No sent requests found"};
                            optionalAdapter=new OptionalAdapter(parentView.getContext(),R.layout.no_records_found,strings);
                            sentRequestList.setAdapter(optionalAdapter);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });



    }

    public void loadReceivedRequests(){

        friendRequestsArray.clear();

        firebaseDAO.getDbReference()
                .child("Requests")
                .child(FirebaseDAO.UID)
                .child("receivedFrom")
                .orderByKey()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        if (dataSnapshot.hasChildren()) {

                            receivedRequestList.setAdapter(RRLAdapter);
                            for (DataSnapshot snap : dataSnapshot.getChildren()) {

                                firebaseDAO.getDbReference()
                                        .child("users")
                                        .child(snap.getKey())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                User user = new User(
                                                        dataSnapshot.getKey(),
                                                        dataSnapshot.child("fName").getValue(String.class),
                                                        dataSnapshot.child("lName").getValue(String.class),
                                                        dataSnapshot.child("country").getValue(String.class),
                                                        dataSnapshot.child("state").getValue(String.class),
                                                        dataSnapshot.child("pp_path").getValue(String.class)

                                                );
                                                friendRequestsArray.add(user);
                                                RRLAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });

                            }
                        }

                        else{

                            strings= new String[]{"No Friend requests"};
                            optionalAdapter=new OptionalAdapter(parentView.getContext(),R.layout.no_records_found,strings);
                            receivedRequestList.setAdapter(optionalAdapter);

                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    public void loadFriendsIntoList() {

        friendsArray.clear();

        firebaseDAO.getDbReference()
                .child("FriendLists")
                .child(FirebaseDAO.UID)
                .orderByKey()
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChildren()){

                            friendList.setAdapter(FLAdapter);
                            for (DataSnapshot achild:dataSnapshot.getChildren()) {

                                firebaseDAO.getDbReference()
                                        .child("users")
                                        .child(achild.getKey())
                                        .addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                User user = new User(
                                                        dataSnapshot.getKey(),
                                                        dataSnapshot.child("fName").getValue(String.class),
                                                        dataSnapshot.child("lName").getValue(String.class),
                                                        dataSnapshot.child("country").getValue(String.class),
                                                        dataSnapshot.child("state").getValue(String.class),
                                                        dataSnapshot.child("pp_path").getValue(String.class)

                                                );
                                                friendsArray.add(user);
                                                FLAdapter.notifyDataSetChanged();
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                            }
                                        });
                            }
                        }
                        else{
                            strings= new String[]{"No Friends found"};
                            optionalAdapter=new OptionalAdapter(parentView.getContext(),R.layout.no_records_found,strings);
                            friendList.setAdapter(optionalAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }




}
