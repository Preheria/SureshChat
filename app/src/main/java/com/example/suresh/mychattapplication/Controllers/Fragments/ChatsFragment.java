package com.example.suresh.mychattapplication.Controllers.Fragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.suresh.mychattapplication.Controllers.CommonActivity;
import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatsFragment extends Fragment implements CommonActivity{


    //varibale declarations
    
    private View parentView;
    private ListView chatlistView;
    private FirebaseDAO firebaseDAO;
    private ChatListAdapter chatListAdapter;
    private OptionalAdapter optionalAdapter;
    private ArrayList<HashMap<String,String>> chatItemsList;
    
    
    public ChatsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        try{
        parentView=inflater.inflate(R.layout.fragment_chats, container, false);

        return parentView;}
        catch ( Exception e)
        {
            return null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeViews();
    }

    @Override
    public void onResume() {
        super.onResume();
    }


    @Override
    public void initializeViews() {
        
        firebaseDAO=FirebaseDAO.getFirebaseDAOObject();

        chatlistView=parentView.findViewById(R.id.chatListView);
        chatItemsList=new ArrayList<>();

        chatListAdapter=new ChatListAdapter(parentView.getContext(),R.layout.single_chat_item,chatItemsList);


        String[] strings= new String[]{"No recent chats !"};
        optionalAdapter=new OptionalAdapter(parentView.getContext(),R.layout.no_records_found,strings);

        loadChats();

    }

    public void loadChats() {

        try {
            firebaseDAO.getDbReference().child("ConversationLogs")
                    .child(FirebaseDAO.UID)
                    .orderByChild("lastAccessedOn")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                chatlistView.setAdapter(chatListAdapter);
                                chatItemsList.clear();
                                for (DataSnapshot eachItem : dataSnapshot.getChildren()) {

                                    if (eachItem.getChildrenCount() > 1) {

                                        HashMap<String, String> chatItemData = new HashMap<>();

                                        chatItemData.put("lastAccessedOn", eachItem.child("lastAccessedOn").getValue(String.class));
                                        chatItemData.put("lastMessage", eachItem.child("lastMessage").getValue(String.class));
                                        chatItemData.put("sender", eachItem.child("sender").getValue(String.class));
                                        chatItemData.put("receiver", eachItem.child("receiver").getValue(String.class));
                                        chatItemData.put("targetUserID", eachItem.getKey());

                                        chatItemsList.add(chatItemData);
                                        chatListAdapter.notifyDataSetChanged();
                                    }
                                }

                            } else {
                                chatlistView.setAdapter(optionalAdapter);

                            }

                        }


                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        }
        catch (Exception e)
        {
            Log.e("Exception",e.getMessage());
        }
    }

    @Override
    public boolean validateFields() {
        return false;
    }
}
