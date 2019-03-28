package com.example.shadr.navdrawer.fragment;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.example.shadr.navdrawer.MessageListAdapter;
import com.example.shadr.navdrawer.R;
import com.example.shadr.navdrawer.MessageAdapter;
import com.example.shadr.navdrawer.models.Message;
import com.example.shadr.navdrawer.models.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragmentDialog.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragmentDialog#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentDialog extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    // имена атрибутов для Map
    ListView lvSimple;
    SimpleAdapter sAdapter;
    MessageAdapter mAdapter;
    ArrayList<Message> messagesData;
    Message m;
    private OnFragmentInteractionListener mListener;
    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;

    public FragmentDialog() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentDialog.
     */
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
        for (int i = 1; i < 100; i++) {
            User u = new User();
            u.setNickname("Иванов Иван "+i);
            int type = (i%5==2) ? Message.TYPE_MESSAGE_SENT : Message.TYPE_MESSAGE_RECEIVED;
            m = new Message("Ваш текст для теста Проверка текста. но и не только. Всё, хз что писать!", u, type);
            messagesData.add(m);
        }


        //Создаем свой кастомизированный адаптер
        mAdapter = new MessageAdapter(getContext(), messagesData);

        // определяем список и присваиваем ему адаптер

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.chat, container, false);

        mMessageRecycler = (RecyclerView) v.findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(getContext(), messagesData);
        mMessageRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        mMessageRecycler.setAdapter(mMessageAdapter);
        return v;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
//    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
