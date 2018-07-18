package com.example.suresh.mychattapplication.Controllers;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserProfileActivityTest {

    private UserProfileActivity profileActivity;
    @Test
    public void onCreate() {
    }

    @Test
    public void validateFields() {
        profileActivity=new UserProfileActivity();

        //function is supposed to return false value
        assertFalse(profileActivity.validateFields());
    }








    @Test
    public void sendFriendRequest() {

        profileActivity=new UserProfileActivity();

        //function will work for even for non contextual call
        //NPE will occur but will be handled by the function

        profileActivity.sendFriendRequest();
    }


    @Test
    public void checkFriendshipWithTagetUser() {
        profileActivity=new UserProfileActivity();

        //function will work but is ready to catch any exception
        profileActivity.checkFriendshipWithTagetUser();
    }







    @Test
    public void unFriend() {
        profileActivity=new UserProfileActivity();

        //function will work but is ready to catch any exception
        profileActivity.unFriend();
    }



}