package com.example.suresh.mychattapplication.Controllers;

import org.junit.Test;

import static org.junit.Assert.*;

public class ConversationActivityTest {

    private ConversationActivity conversationActivity;
    @Test
    public void onCreate() {
        conversationActivity=new ConversationActivity();
        //onCreate method can run even if the savedInstance State
        //is null
        conversationActivity.onCreate(null);
    }




    @Test
    public void onStart() {
    }

    @Test
    public void initializeViews() {
        conversationActivity=new ConversationActivity();

    }

    @Test
    public void validateFields() {
        conversationActivity=new ConversationActivity();

        //the function for non contextual call returns false boolean value
        boolean result=conversationActivity.validateFields();
        assertFalse(result);
    }

    @Test
    public void checkForExistingConversation() {
        conversationActivity=new ConversationActivity();

        //the function will catch null pointer exception for non
        //contextual call
        conversationActivity.checkForExistingConversation();
    }







    @Test
    public void loadMessages() {
        conversationActivity=new ConversationActivity();

        //the function will catch null pointer exception for non
        //contextual call

        conversationActivity.loadMessages();
    }


    @Test
    public void onClick() {
        conversationActivity=new ConversationActivity();
    }
}