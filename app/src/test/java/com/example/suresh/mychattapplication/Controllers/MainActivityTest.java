package com.example.suresh.mychattapplication.Controllers;

import org.junit.Test.*;

import static org.junit.Assert.*;

public class MainActivityTest {

    @Test
    public void initializeViews() {
        MainActivity mainActivity=new MainActivity();

        //initializeVuews() is configured to obtained to handle exceptions
        //therefore it will run when called application context and it will run even for
        //non contextual calls

        mainActivity.initializeViews();
    }

    @Test
    public void validateFields() {
        MainActivity mainActivity=new MainActivity();
        //boolean returned from validateFields() is
        //fals for non contextual calls it will return
        boolean actual=mainActivity.validateFields();
        assertFalse(actual);
    }

    @Test
    public void onCreate() {
        MainActivity mainActivity=new MainActivity();
        mainActivity.onCreate(null);
    }


    @Test
    public void onClick() {

    }
}
