package com.example.shadr.navdrawer.models;

import java.text.SimpleDateFormat;
import java.util.Date;

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
}
