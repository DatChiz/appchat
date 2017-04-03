package com.example.mypc.appchat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.mypc.appchat.adapter.ChatAdapter;
import com.example.mypc.appchat.gson.Contact;
import com.example.mypc.appchat.gson.Member;
import com.example.mypc.appchat.gson.Message;
import com.example.mypc.appchat.utils.Key;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ConversationActivity extends AppCompatActivity implements View.OnClickListener {
    private String contacID;
    private Button btnSubmit;
    private EditText edtChat;
    private RecyclerView item_list;
    private ArrayList<Message> items;
    private ChatAdapter chatAdapter;
    private Socket mSocket = MyApplication.getInstance().getSocket();

    private Emitter.Listener onConversation = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            final String data = args[0].toString();
            final String id = args[1].toString();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                if (contacID.equals(id)){
                    Gson gson = new Gson();
                    Message message = gson.fromJson(data, Message.class);

                    items.add(message);
                    chatAdapter.notifyDataSetChanged();
                    item_list.post(new Runnable() {
                        @Override
                        public void run() {
                            // Call smooth scroll
                            item_list.smoothScrollToPosition(chatAdapter.getItemCount());
                        }
                    });
                }
                }
            });
        }
    };

    private Emitter.Listener onUpdateMessage = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            final String data = args[0].toString();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Gson gson = new Gson();
                    Contact contact = gson.fromJson(data, Contact.class);

                    items = (ArrayList<Message>) contact.getMessage();
                    ArrayList<Member> member = (ArrayList<Member>) contact.getMembers();

                    chatAdapter = new ChatAdapter(items, member,getApplicationContext());
                    item_list.setAdapter(chatAdapter);
                    final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
                    layoutManager.setStackFromEnd(true);
                    item_list.setLayoutManager(layoutManager);
                    Log.d("onUpdateMessage", "Update Message Succes");
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mSocket.connect();
        findViewByIds();

        mSocket.on("conversation", onConversation);
        mSocket.on("update_message", onUpdateMessage);

        contacID =  getIntent().getStringExtra(Key.CONVERSATION);
        Log.d("getIntent",contacID);
        mSocket.emit("update_message", contacID, ContactActivity._Id);

        btnSubmit.setOnClickListener(this);
    }

    private void findViewByIds(){
        btnSubmit = (Button) findViewById(R.id.btnSubmit);
        edtChat   = (EditText) findViewById(R.id.edtChat);
        item_list = (RecyclerView) findViewById(R.id.item_list);
    }

    @Override
    public void onClick(View v) {
        mSocket.emit("conversation", ContactActivity._Id, edtChat.getText().toString(), contacID);
        mSocket.emit("make_color", contacID, ContactActivity._Id);
        edtChat.setText("");
        Log.d("conversation", edtChat.getText().toString());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        mSocket.emit("make_color_default", contacID);
    }
}
