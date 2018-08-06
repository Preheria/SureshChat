package com.example.suresh.mychattapplication.Controllers.Fragments;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.suresh.mychattapplication.Controllers.ConversationActivity;
import com.example.suresh.mychattapplication.Models.FirebaseDAO;
import com.example.suresh.mychattapplication.Models.User;

import com.example.suresh.mychattapplication.Controllers.CommonActivity;
import com.example.suresh.mychattapplication.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ActiveUsersFragment extends Fragment implements CommonActivity {

    private ListView activeUsersList;
    private ArrayList<User> userList;
    private ActiveUserListAdapter AULAdapter;
    private OptionalAdapter optionalAdapter;
    private View parentView;
    private FirebaseDAO firebaseDAO;

    public ActiveUsersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeViews();
        showActiveUsers();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        parentView=inflater.inflate(R.layout.fragment_active_users, container, false);
        return parentView;
    }

    @Override
    public void initializeViews() {

        activeUsersList=parentView.findViewById(R.id.activeUsersList);
        userList=new ArrayList<>();
        firebaseDAO=FirebaseDAO.getFirebaseDAOObject();
        activeUsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView fullName=view.findViewById(R.id.fullNameActiveUser);
                TextView uid=view.findViewById(R.id.activeUserID);
                TextView imageURI=view.findViewById(R.id.activeUserThumbnailURI);

                Intent i=new Intent(getActivity(),ConversationActivity.class);
                i.putExtra("fullname",fullName.getText());
                i.putExtra("targetUserID",uid.getText());
                i.putExtra("imageURI",imageURI.getText());
                startActivity(i);
            }
        });
    }

    private void showActiveUsers() {
        firebaseDAO.getDbReference().child("FriendLists")
                    .child(FirebaseDAO.UID)
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){

                                AULAdapter=new ActiveUserListAdapter(
                                                                parentView.getContext(),
                                                                R.layout.single_online_user,userList);
                                activeUsersList.setAdapter(AULAdapter);
                                for (DataSnapshot eachSnapshot:dataSnapshot.getChildren()) {

                                    firebaseDAO.getDbReference().child("users")
                                                                .child(eachSnapshot.getKey())
                                                                .addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                    if(dataSnapshot.child("online").getValue(Boolean.class)) {

                                        User user = new User(dataSnapshot.getKey());

                                        user.setFirstName(dataSnapshot.child("fName").getValue(String.class));
                                        user.setLastName(dataSnapshot.child("lName").getValue(String.class));
                                        user.setPp_path(dataSnapshot.child("pp_path").getValue(String.class));

                                                userList.add(user);
                                                AULAdapter.notifyDataSetChanged();
                                            } }
                                                                    @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }
                                                });
                                }
                            }
                            else{
                                String[] strings= new String[]{"No Active Users"};
                optionalAdapter=new OptionalAdapter(parentView.getContext(),R.layout.no_records_found,strings);
                                activeUsersList.setAdapter(optionalAdapter);
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
    }
    @Override
    public boolean validateFields() {
        return false;
    }
}
