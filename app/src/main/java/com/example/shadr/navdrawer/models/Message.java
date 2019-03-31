package com.example.shadr.navdrawer.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Message {
    public static final int TYPE_MESSAGE_SENT = 1;
    public static final int TYPE_MESSAGE_RECEIVED = 2;
    int type;
    String message;
    User sender;
    Date date;
    Date dateCreatedAt;



    public Message(String message, User sender, Date date, Date createdAt) {
        this.message = message;
        this.sender = sender;
        this.date = date;
        this.dateCreatedAt = createdAt;
    }
    public Message(String message, User sender,int type) {
        this.message = message;
        this.sender = sender;
        this.type = type;
        this.date = new Date();
        this.dateCreatedAt = new Date();
    }


    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public User getSender() {
        return sender;
    }
    public void setSender(User sender) {
        this.sender = sender;
    }
    public Date getDate() {
        return date;
    }
    public String getDate_time() {
        SimpleDateFormat formatForDate_time = new SimpleDateFormat("H:m");//2:27
        return formatForDate_time.format(date);
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Date getDateCreatedAt() {
        return dateCreatedAt;
    }
    public void setDateCreatedAt(Date createdAt) {
        this.dateCreatedAt = createdAt;
    }
    public String getDateCreatedAt_time() {
        SimpleDateFormat formatForDate_time = new SimpleDateFormat("H:m");//2:27
        return formatForDate_time.format(dateCreatedAt);
    }
    public int getType() {
        return type;
    }
    public void setType(int type) {
        this.type = type;
    }

    public static ArrayList<Message> convertJson(JSONObject jsonArrayMessage){
        ArrayList<Message> messageList = new ArrayList<Message>();
        User u;
        Message m;
        try {
            for (int i=0;i<jsonArrayMessage.getJSONArray("messages").length();i++){
                u = new User();
                u.setNickname(jsonArrayMessage.getJSONArray("messages").getJSONObject(i).getString("username"));
                int type = Message.TYPE_MESSAGE_RECEIVED;
                m = new Message(jsonArrayMessage.getJSONArray("messages").getJSONObject(i).getString("message"), u, type);
                messageList.add(m);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return messageList;
    }
    public static ArrayList<Message> convertJson(JSONArray jsonArrayMessage){
        ArrayList<Message> messageList = new ArrayList<Message>();
        User u;
        Message m;
        try {
            for (int i=0;i<jsonArrayMessage.length();i++){
                u = new User();
                u.setNickname(jsonArrayMessage.getJSONObject(i).getString("username"));
                int type = Message.TYPE_MESSAGE_RECEIVED;
                m = new Message(jsonArrayMessage.getJSONObject(i).getString("message"), u, type);
                messageList.add(m);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return messageList;
    }

}
