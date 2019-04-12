package com.example.shadr.navdrawer.fragment;

import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.shadr.navdrawer.DownloadImageTask;
import com.example.shadr.navdrawer.FilesUploadingTask;
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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

import static android.app.Activity.RESULT_OK;

public class FragmentDialog extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    static final int GALLERY_REQUEST = 1;

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
    Bitmap imageLoad;
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
            socket = IO.socket("http://192.168.0.144:3000");
            socket.connect();
            socket.emit("join", Nickname);
        }
        catch (URISyntaxException e) {
            e.printStackTrace();
        }

        messagesData = new ArrayList<Message>();
        mMessageAdapter = new MessageListAdapter(getContext(), messagesData);
        imageLoad = BitmapFactory.decodeResource(getResources(), R.raw.image_load);
    }

    @Override
    public void onResume() {
        super.onResume();
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
        ImageButton sendDocument = v.findViewById(R.id.button_doc_send);
        messagetxt = v.findViewById(R.id.edittext_chatbox);
        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                User u = new User();
                u.setNickname(Nickname);
                java.text.SimpleDateFormat sdf =
                        new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                m = new Message(messagetxt.getText().toString(), u, Message.TYPE_MESSAGE_SENT, sdf.format(new Date()));
                socket.emit("messagedetection",Nickname,m.getMessage(), sdf.format(new Date()));

                messagesData.add(m);
                mMessageAdapter.notifyDataSetChanged();
                mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                messagetxt.setText("");

            }
        });

        socket.on("message", new Emitter.Listener() {
            @Override public void call(final Object... args) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override public void run() {
                        final JSONObject data = (JSONObject) args[0];
                        try {
                            final User u = new User();
                            u.setNickname(data.getString("senderNickname"));
                            final int type = Message.TYPE_MESSAGE_RECEIVED;
                            if(data.has("message")==true) {
                                m = new Message(data.getString("message"), u, type, data.getString("messageTime"));
                                messagesData.add(m);
                                mMessageAdapter.notifyDataSetChanged();
                            }
                            else {
                                Toast.makeText(getContext(), "rabotaet", Toast.LENGTH_SHORT).show();
                                m = new Message("", u, type, imageLoad,data.getString("messageTime"));
                                messagesData.add(m);
                                mMessageAdapter.notifyDataSetChanged();
                                new DownloadImageTask(){
                                    @Override
                                    protected void onPostExecute(Bitmap bitmap) {
                                        super.onPostExecute(bitmap);
                                        Message message;
                                        message = m;
                                        int index = messagesData.indexOf(message);
                                        message.setBitmap(bitmap);
                                        messagesData.set(index, message);
                                        mMessageAdapter.notifyDataSetChanged();
                                    }
                                }.execute("http://192.168.0.144:3000/getImage?"+data.getString("file"));
                            }
                            if (linlayoutManager.findLastVisibleItemPosition()>=mMessageAdapter.getItemCount() - 3) {
                                mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                            }
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
                           messagesData.addAll(Message.convertJson(data.getJSONArray("messages"), imageLoad));
                           mMessageAdapter.notifyDataSetChanged();
                           Message message_check;
                           for(int i=0;i<messagesData.size();i++)
                           {
                               message_check = messagesData.get(i);
                               if(message_check.getBitmap()==imageLoad) {
                                   final Message finalMessage_check = message_check;
                                   final int finalI = i;
                                   new DownloadImageTask(){
                                       @Override
                                       protected void onPostExecute(Bitmap bitmap) {
                                           super.onPostExecute(bitmap);
                                           finalMessage_check.setBitmap(bitmap);
                                           messagesData.set(finalI, finalMessage_check);
                                           mMessageAdapter.notifyDataSetChanged();
                                       }
                                   }.execute("http://192.168.0.144:3000/getImage?"+ messagesData.get(finalI).getMessage());
                               }
                           }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        sendDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Picture"), GALLERY_REQUEST);
            }
        });
        return v;

    }

    //socket place


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bitmap bitmap = null;


        switch(requestCode) {
            case GALLERY_REQUEST:
                if(resultCode == RESULT_OK){
                    if(data.getData()!=null){
                        Uri selectedImage = data.getData();
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), selectedImage);
                            User u = new User();
                            u.setNickname(Nickname);
                            m = new Message(messagetxt.getText().toString(), u, Message.TYPE_MESSAGE_SENT, bitmap);
                            messagesData.add(m);
                            mMessageAdapter.notifyDataSetChanged();
                            mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                            messagetxt.setText("");


                            FilesUploadingTask SendToServer = new FilesUploadingTask("http://192.168.0.144:3000/upload"){
                                @Override
                                protected void onPostExecute(String result) {
                                    super.onPostExecute(result);
                                    Log.d("my","in MAIL!!!!!"+result);
                                }
                            };
                            SendToServer.putTextData("username",m.getSender().getNickname());
                            java.text.SimpleDateFormat sdf =
                                    new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            SendToServer.putTextData("date",sdf.format(new Date()));
                            SendToServer.putBitmapData(bitmap,m.getFilename());
                            socket.emit("image");
                            SendToServer.execute();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    else {
                        ClipData mClipData = data.getClipData();
                        ArrayList<Uri> mArrayUri = new ArrayList<Uri>();
                        for (int i = 0; i < mClipData.getItemCount(); i++) {

                            ClipData.Item item = mClipData.getItemAt(i);
                            Uri uri = item.getUri();
                            mArrayUri.add(uri);
                            try {
                                bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                                User u = new User();
                                u.setNickname(Nickname);
                                m = new Message(messagetxt.getText().toString(), u, Message.TYPE_MESSAGE_SENT, bitmap);
                                messagesData.add(m);
                                mMessageAdapter.notifyDataSetChanged();
                                mMessageRecycler.smoothScrollToPosition(mMessageAdapter.getItemCount() - 1);
                                messagetxt.setText("");
                                FilesUploadingTask SendToServer = new FilesUploadingTask("http://192.168.0.144:3000/upload"){
                                    @Override
                                    protected void onPostExecute(String result) {
                                        super.onPostExecute(result);
                                        Log.d("my","in MAIL!!!!!"+result);
                                    }
                                };
                                SendToServer.putTextData("username",m.getSender().getNickname());
                                java.text.SimpleDateFormat sdf =
                                        new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                SendToServer.putTextData("date",sdf.format(new Date()));
                                SendToServer.putBitmapData(bitmap,m.getFilename());
                                socket.emit("image");
                                SendToServer.execute();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }
        }
    }

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
    public void onPause() {
        super.onPause();
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
