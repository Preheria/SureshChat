package com.example.suresh.mychattapplication.Models;

import co.intentservice.chatui.models.ChatMessage;

public class Message extends ChatMessage{

    private String sender,receiver;


   public  Message(String message, long timestamp, Type type){
        super(message,timestamp,type);
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    @Override
    public long getTimestamp() {
        return super.getTimestamp();
    }

    @Override
    public void setTimestamp(long timestamp) {
        super.setTimestamp(timestamp);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public void setMessage(String message) {
        super.setMessage(message);
    }

    @Override
    public Type getType() {
        return super.getType();
    }

    @Override
    public void setType(Type type) {
        super.setType(type);
    }

    @Override
    public String getFormattedTime() {
        return super.getFormattedTime();
    }
}
