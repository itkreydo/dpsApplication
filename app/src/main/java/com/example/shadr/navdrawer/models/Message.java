package com.example.shadr.navdrawer.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.example.shadr.navdrawer.DownloadImageTask;
import com.example.shadr.navdrawer.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class Message {
    public static final int TYPE_MESSAGE_SENT = 1;
    public static final int TYPE_MESSAGE_RECEIVED = 2;
    int type;
    String message;
    Bitmap bitmap;
    User sender;
    Date date;
    String dateString;



    public Message(String message, User sender, Date date) {
        this.message = message;
        this.sender = sender;
        this.date = date;
        this.bitmap = null;
    }
    public Message(String message, User sender,int type, String date) {
        this.message = message;
        this.sender = sender;
        this.type = type;
        this.dateString = date;
        this.date = new Date();
        this.bitmap = null;
    }

    public Message(String message, User sender,int type) {
        this.message = message;
        this.sender = sender;
        this.type = type;
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.dateString = sdf.format(new Date());
        this.bitmap = null;
    }

    public Message(String message, User sender,int type, Bitmap bitmap) {
        this.message = message;
        this.sender = sender;
        this.type = type;
        java.text.SimpleDateFormat sdf =
                new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        this.dateString = sdf.format(new Date());
        this.bitmap = bitmap;
    }
    public Message(String message, User sender,int type, Bitmap bitmap, String date) {
        this.message = message;
        this.sender = sender;
        this.type = type;
        this.dateString = date;
        this.bitmap = bitmap;
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
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date dateloc = null;
        try {
            dateloc = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatForDate_time = new SimpleDateFormat("HH:mm");//2:27
        return formatForDate_time.format(dateloc);
    }
    public String getFilename() {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date dateloc = null;
        try {
            dateloc = format.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat formatForDate_time = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-");
        Random rnd = new Random(System.currentTimeMillis());
        return formatForDate_time.format(dateloc)+String.valueOf(rnd.nextInt(100000))+".jpg";
    }
    public void setDate(Date date) {
        this.date = date;
    }
    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
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
    public static ArrayList<Message> convertJson(JSONArray jsonArrayMessage, Bitmap bitmap){
        ArrayList<Message> messageList = new ArrayList<Message>();
        User u;
        Message m;
        try {
            for (int i=0;i<jsonArrayMessage.length();i++){
                u = new User();
                u.setNickname(jsonArrayMessage.getJSONObject(i).getString("username"));
                int type = Message.TYPE_MESSAGE_RECEIVED;
                if(jsonArrayMessage.getJSONObject(i).has("message")==true) {
                    m = new Message(jsonArrayMessage.getJSONObject(i).getString("message"), u, type,
                            jsonArrayMessage.getJSONObject(i).getString("date"));
                }
                else {
                    m = new Message(jsonArrayMessage.getJSONObject(i).getString("file").split("/")[1],
                            u, type, bitmap,
                            jsonArrayMessage.getJSONObject(i).getString("date"));
                }
                messageList.add(m);
            }
        }catch (JSONException e) {
            e.printStackTrace();
        }
        return messageList;
    }

}
