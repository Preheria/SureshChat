package com.example.suresh.mychattapplication.Controllers;

import org.junit.Test;

import static org.junit.Assert.*;

public class ProfileTest {

    private Profile profile;
    @Test
    public void onCreate() {
        profile=new Profile();
        //in case null savedInstanceState is sent, exception will occur
        //but function will run
        profile.onCreate(null);
    }






    @Test
    public void initializeViews() {
    }

    @Test
    public void loadDataIntoControls() {
    }

    @Test
    public void validateFields() {
        profile=new Profile();

        boolean actual=profile.validateFields();
        assertFalse(actual);

    }





    @Test
    public void onClick() {
    }

    @Test
    public void onActivityResult() {
        profile=new Profile();
        //dummy data as parameters are sent to the function
        //this will cause exception to occur since the data are non
        //contextual. therefore exception will occur.

        profile.onActivityResult(0,0,null);

    }



}