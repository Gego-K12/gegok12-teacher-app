package com.gegosoft.schoolteacherapp.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.gegosoft.schoolteacherapp.R;

import java.util.ArrayList;
import java.util.List;

import co.intentservice.chatui.ChatView;
import co.intentservice.chatui.adapters.ChatViewListAdapter;
import co.intentservice.chatui.models.ChatMessage;

public class ChatFragment  extends DialogFragment {

    ChatView chatView;
    TextView room_name;
    ImageView back;
    List<ChatMessage> chatMessages = new ArrayList<>();
    OnFragmentCommunicationListener mListener;
    String name;
    private ListView listView;
    ChatViewListAdapter chatViewListAdapter;
    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_fragment, container, false);
        chatView = view.findViewById(R.id.chat_view);
        room_name = view.findViewById(R.id.room_name);
        back = view.findViewById(R.id.back);
        room_name.setText(getArguments().getString("room_name"));
        chatMessages = (List<ChatMessage>) getArguments().getSerializable("messages");
        for (ChatMessage chatMessage : chatMessages){
            chatView.addMessage(chatMessage);
        }
        name = getArguments().getString("name");
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        chatView.setOnSentMessageListener(new ChatView.OnSentMessageListener() {
            @Override
            public boolean sendMessage(ChatMessage chatMessage) {
                chatMessage.setSender(name);
                mListener.sendMessage(chatMessage);
                return true;
            }
        });


        listView = (ListView) chatView.getChildAt(0);
        chatViewListAdapter = (ChatViewListAdapter) listView.getAdapter();
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                if (chatMessages.get(position).getType().toString().equalsIgnoreCase(ChatMessage.Type.SENT.toString())){



                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setMessage("Do you want to delete message ?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {

                                            mListener.deleteMessage(chatMessages.get(position));
                                            chatView.removeMessage(position);

                                            chatViewListAdapter.notifyDataSetChanged();

                                        }
                                    });

                                    dialog.dismiss();

                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });

                    AlertDialog alert = builder.create();
                    alert.show();
                }
                return false;
            }
        });


        chatView.setTypingListener(new ChatView.TypingListener() {
            @Override
            public void userStartedTyping() {


            }

            @Override
            public void userStoppedTyping() {

            }
        });

        return view;

    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentCommunicationListener) {
            mListener = (OnFragmentCommunicationListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentCommunicationListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public void addMessage(ChatMessage message) {


        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {
                chatView.addMessage(message);
            }
        });



    }

    public void removeMessage(ChatMessage chatMessage) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (int i =0 ; i <chatMessages.size();i++){
                    if (chatMessage.getTimestamp()==(chatMessages.get(i).getTimestamp())){
                        chatView.removeMessage(i);
                        chatMessages.remove(i);
                        break;
                    }
                }
                chatViewListAdapter.notifyDataSetChanged();
            }
        });
    }

    public interface OnFragmentCommunicationListener {
        void sendMessage(ChatMessage message);
        void deleteMessage(ChatMessage message);

    }

}
