package com.example.suresh.mychattapplication.Controllers;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.suresh.mychattapplication.R;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.models.ChatMessage;

public class Conversation extends AppCompatActivity implements CommonActivity, View.OnClickListener{

    private ChatView chatView ;
    private ActionBar actionBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
    }


    @Override
    protected void onStart() {
        super.onStart();

        actionBar=getSupportActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        actionBar.setCustomView(R.layout.actionbar_for_chat_activity);
        initializeControls();
    }

    @Override
    public void initializeControls() {

        ChatMessage msg=new ChatMessage("this is too hot",System.currentTimeMillis()/1000, ChatMessage.Type.RECEIVED);

        chatView=findViewById(R.id.chat_view);

        chatView.addMessage(msg);
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener(){
            @Override
            public boolean sendMessage(ChatMessage chatMessage){
                Toast.makeText(Conversation.this,chatMessage.toString(),Toast.LENGTH_SHORT).show();
                // perform actual message sending
                return true;
            }
        });

        chatView.setTypingListener(new ChatView.TypingListener(){
            @Override
            public void userStartedTyping(){
                // will be called when the user starts typing
            }

            @Override
            public void userStoppedTyping(){
                // will be called when the user stops typing
            }
        });
    }

    @Override
    public boolean validateFields() {
        return false;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){


        }

    }
}
