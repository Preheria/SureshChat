package com.example.suresh.mychattapplication.Models;

import android.content.Context;
import android.support.annotation.NonNull;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class User implements Serializable {

    private String userID;
    private String firstName, lastName;
    private String country, state, homeAddress;
    private String phone;
    private String DOB;
    private String email;
    private String username;
    private String pp_path = "";
    private String password;
    private String status = "";
    private FirebaseDAO firebaseDAO;

    private boolean flag;
    private DataSnapshot ds;

    //default constructor is required
    public User() {
    }

    public User(String userID,String fName, String lname, String country, String state, String pp_path) {
        this.userID=userID;
        firstName = fName;
        lastName = lname;
        this.country = country;
        this.state = state;
        this.pp_path = pp_path;
    }

    public User(String uid) {
        this.userID = uid;
    }

    public void setUserdata(HashMap<String, String> userdata) {
        this.firstName = userdata.get("fname");
        this.lastName = userdata.get("lname");
        this.DOB = userdata.get("DOB");
        this.country = userdata.get("country");
        this.state = userdata.get("state");
        this.homeAddress = userdata.get("homeAddress");
        this.phone = userdata.get("phone");
        this.email = userdata.get("email");
        this.username = userdata.get("username");
        this.password = userdata.get("password");


    }

    public String getPp_path() {
        return pp_path;
    }

    public void setPp_path(String pp_path) {
        this.pp_path = pp_path;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDOB() {
        return DOB;
    }

    public void setDOB(String DOB) {
        this.DOB = DOB;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    //end of getter and setter for the attributes from User class

    public User getUserInstance() {
        return this; //returns the instance of class itself
    }

    public void insertUser() {

    }


    public ArrayList<User> searchFriend(final String name, final ArrayList<User> userList) {


        firebaseDAO = FirebaseDAO.getFirebaseDAOObject();

        firebaseDAO.getDbReference()
                    .child("users")
                    .orderByChild("fName")
                    .startAt(name)
                    .endAt(name+"\uf8ff")
                    .addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            userList.clear();
                            for (DataSnapshot aChild:dataSnapshot.getChildren()
                                 ) {
                                if ((aChild.child("fName").getValue(String.class)).contains(name)) {


                                User user = new User(
                                        aChild.getKey(),
                                        aChild.child("fName").getValue(String.class),
                                        aChild.child("lName").getValue(String.class),
                                        aChild.child("country").getValue(String.class),
                                        aChild.child("state").getValue(String.class),
                                        aChild.child("pp_path").getValue(String.class)

                                );
                                userList.add(user);
                            }


                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
        return userList;
    }

}