package com.example.suresh.mychattapplication.Models;

import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    @Test
    public void getUserID() {
        User user=new User();
        //for newly created user using default constructor
        //string attributes shoulld be null

        assertNull(user.getUserID());

    }






    @Test
    public void isOnline() {
        User user=new User();
        //for newly created user using default constructor
        //string attributes shoulld be null
        boolean expected=false;

        boolean actual=user.isOnline();

        assertEquals(expected,actual);

    }




    @Test
    public void insertUser() {
    }

    @Test
    public void searchFriend() {
    }
}