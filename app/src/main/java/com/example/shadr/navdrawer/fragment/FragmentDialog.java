package com.example.shadr.navdrawer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shadr.navdrawer.MessageListAdapter;
import com.example.shadr.navdrawer.R;
import com.example.shadr.navdrawer.models.Message;
import com.example.shadr.navdrawer.models.User;

import java.util.ArrayList;

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

        // упаковываем данные в понятную для адаптера структуру сообщений
        messagesData = new ArrayList<Message>();

        //Генерируем сообщения, потом будем брать из бд
        for (int i = 1; i < 1000; i++) {
            User u = new User();
            u.setNickname("Иванов Иван "+i);
            int type = (i%5==2) ? Message.TYPE_MESSAGE_SENT : Message.TYPE_MESSAGE_RECEIVED;
            m = new Message("Ваш текст для теста Проверка текста. но и не только. Всё, хз что писать!", u, type);
            messagesData.add(m);
        }

        //Создаем свой кастомизированный адаптер
        mMessageAdapter = new MessageListAdapter(getContext(), messagesData);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat_recycler_view, container, false);
        mMessageRecycler = (RecyclerView) v.findViewById(R.id.reyclerview_message_list);
        LinearLayoutManager linlayoutManager = new LinearLayoutManager(getContext());
        linlayoutManager.setStackFromEnd(true);
        mMessageRecycler.setLayoutManager(linlayoutManager);
        mMessageRecycler.setAdapter(mMessageAdapter);
        return v;

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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
