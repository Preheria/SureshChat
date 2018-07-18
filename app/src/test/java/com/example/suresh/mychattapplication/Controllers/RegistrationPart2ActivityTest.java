package com.example.suresh.mychattapplication.Controllers;

import org.junit.Test;

import static org.junit.Assert.*;

public class RegistrationPart2ActivityTest {

    private RegistrationPart2Activity registrationPart2Activity=new RegistrationPart2Activity();
    @Test
    public void initializeViews() {
        //function will run but will catch null pointer exception

        registrationPart2Activity.initializeViews();

    }

    @Test
    public void validateFields() {
        registrationPart2Activity=new RegistrationPart2Activity();
        //non contextual call will produce false result to occur
        assertFalse(registrationPart2Activity.validateFields());
    }


    @Test
    public void onStart() {
    }

    @Test
    public void onCreate() {
    }

    @Test
    public void onClick() {
    }
}