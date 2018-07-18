package com.example.suresh.mychattapplication.Controllers;

import org.junit.Test;

import static org.junit.Assert.*;

public class HomeActivityTest {

    @Test
    public void onCreate() {
        HomeActivity homeActivity=new HomeActivity();
        homeActivity.onCreate(null);

    }

    @Test
    public void onNewIntent() {
        HomeActivity homeActivity=new HomeActivity();
        homeActivity.onNewIntent(null);
    }


    @Test
    public void initializeViews() {
    }

    @Test
    public void validateFields() {
    }

    @Test
    public void onCreateOptionsMenu() {
        HomeActivity homeActivity=new HomeActivity();
        boolean result= homeActivity.onCreateOptionsMenu(null);
        assertFalse(result);
    }





    @Test
    public void onStart() {
    }

    @Test
    public void onSearchRequested() {
    }

    @Test
    public void onOptionsItemSelected() {
        HomeActivity homeActivity=new HomeActivity();
        boolean result=homeActivity.onOptionsItemSelected(null);
        assertFalse(result);
    }







}