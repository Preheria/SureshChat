package com.example.suresh.mychattapplication.Controllers.Fragments;

import android.view.View;

import org.junit.Test;

import static org.junit.Assert.*;

public class FriendListFragmentTest {

    @Test
    public void validateFields() {
    }

    @Test
    public void loadSentFriendRequests() {
        FriendListFragment fragment=new FriendListFragment();
        //function will run without error but exceptions will be
        //caught
        fragment.loadSentFriendRequests();

    }

    @Test
    public void loadReceivedRequests() {
        FriendListFragment fragment=new FriendListFragment();
        fragment.loadSentFriendRequests();
    }

    @Test
    public void loadFriendsIntoList() {
        FriendListFragment fragment=new FriendListFragment();
        //function will run without error but exceptions will be
            //caught
        fragment.loadFriendsIntoList();
    }


    @Test
    public void onCreateView() {
        FriendListFragment fragment=new FriendListFragment();
        //for any of the null arguments as null arguments
        //exception will occur and will be caught

        View view=fragment.onCreateView(null,null,null);
        assertNull(view);
    }





}