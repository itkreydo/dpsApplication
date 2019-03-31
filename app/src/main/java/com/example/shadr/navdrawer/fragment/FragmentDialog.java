package com.example.shadr.navdrawer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.shadr.navdrawer.MessageListAdapter;
import com.example.shadr.navdrawer.R;
import com.example.shadr.navdrawer.models.Message;
import com.example.shadr.navdrawer.models.User;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class FragmentDialog extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // имена атрибутов для Map

    ArrayList<Message> messagesData;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    Message m;
    private OnFragmentInteractionListener mListener;

    private Socket socket;
    private String Nickname ;
    private EditText messagetxt ;

    public FragmentDialog() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static FragmentDialog newInstance(String param1, String param2) {
        FragmentDialog fragment = new FragmentDialog();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        Nickname = "dimon";

        try {
            socket = IO.socket("http://192.168.0.180:3000");
            socket.connect();
            socket.emit("join", Nickname);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }

        messagesData = new ArrayList<Message>();
        mMessageAdapter = new MessageListAdapter(getContext(), messagesData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_recycler_view, container, false);
        mMessageRecycler = v.findViewById(R.id.reyclerview_message_list);
        final LinearLayoutManager linlayoutManager = new LinearLayoutManager(getContext());
        linlayoutManager.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(linlayoutManager);
        mMessageRecycler.setAdapter(mMessageAdapter);
        ImageButton sendMessage = v.findViewById(R.id.button_chatbox_send);
        messagetxt = v.findViewById(R.id.edittext_chatbox);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User u = new User();
                u.setNickname(Nickname);
                m = new Message(messagetxt.getText().toString(), u, Message.TYPE_MESSAGE_SENT);
                socket.emit("messagedetection",Nickname,m.getMessage(), m.getDate_time());



                messagesData.add(m);
                mMessageAdapter.notifyDataSetChanged();
                if (linlayoutManager.findLastVisibleItemPosition()>=mMessageAdapter.getItemCount() - 3) {
                    mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                }
                messagetxt.setText("");
            }
        });

        socket.on("message", new Emitter.Listener() {
            @Override public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        JSONObject data = (JSONObject) args[0];
                        try {
                            User u = new User();
                            u.setNickname(data.getString("senderNickname"));
                            int type = Message.TYPE_MESSAGE_RECEIVED;
                            m = new Message(data.getString("message"), u, type);
                            messagesData.add(m);mMessageAdapter.notifyDataSetChanged();
                            mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });
        socket.on("updateDialog", new Emitter.Listener() {
            @Override public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        String datastr = args[0].toString();
                        try {
                            JSONObject data = (JSONObject) new JSONObject(datastr);
                           messagesData.addAll(Message.convertJson(data.getJSONArray("messages")));
                           mMessageAdapter.notifyDataSetChanged();
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

//        socket.on("userjoinedthechat", new Emitter.Listener() {
//            @Override public void call(final Object... args) {
//                getActivity().runOnUiThread(new Runnable() {
//                    @Override public void run() {
//                        String data = (String) args[0];
//                    }
//                });
//            }
//        });
        return v;

    }

    //socket place



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        socket.disconnect();
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
