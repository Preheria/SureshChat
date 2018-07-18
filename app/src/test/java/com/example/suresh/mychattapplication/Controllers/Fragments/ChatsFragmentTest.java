package com.example.suresh.mychattapplication.Controllers.Fragments;

import android.view.View;

import org.junit.Test;

import static org.junit.Assert.*;

public class ChatsFragmentTest {


    @Test
    public void onCreateView() {
        ChatsFragment chatsFragment=new ChatsFragment();
        View view;
        // this function is also configured to catch any exception
        //occurring during runtime
        view=chatsFragment.onCreateView(null,null,null);
        assertNull(view);
    }




    @Test
    public void initializeViews() {
    }

    @Test
    public void loadChats() {
        ChatsFragment chatsFragment=new ChatsFragment();

        //exception will ocur for any nontextual call
        // this will be caught by the function

        chatsFragment.loadChats();
    }


}