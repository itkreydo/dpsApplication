package com.example.shadr.navdrawer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.shadr.navdrawer.models.Message;

import java.util.ArrayList;
import java.util.Map;
import java.util.zip.Inflater;

public class MessageAdapter extends BaseAdapter {
    // кол-во элементов
    Context ctx;
    ArrayList<Message> messages;
    LayoutInflater lInflater;
    public MessageAdapter(Context context, ArrayList<Message> _messages){
        messages = _messages;
        ctx=context;
        lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return messages.size();
    }

    // элемент по позиции
    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    // id по позиции
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        //View view = null;
        Message message = (Message) getItem(position);
        //Если нет свободный вью, то создаём новый
        if (view == null) {
            if (message.getType()==Message.TYPE_MESSAGE_SENT){
                view = lInflater.inflate(R.layout.item_message_sent, parent, false);
            }else {
                view = lInflater.inflate(R.layout.item_message_received, parent, false);
            }
        }

        if (message.getType()==Message.TYPE_MESSAGE_RECEIVED){
        ((TextView) view.findViewById(R.id.text_message_name)).setText(message.getSender().getNickname());
        }
        ((TextView) view.findViewById(R.id.text_message_body)).setText(message.getMessage());
        ((TextView) view.findViewById(R.id.text_message_time)).setText(message.getDateCreatedAt_time());
        return view;

    }

}
