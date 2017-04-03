package com.example.mypc.appchat.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mypc.appchat.ContactActivity;
import com.example.mypc.appchat.ConversationActivity;
import com.example.mypc.appchat.MyApplication;
import com.example.mypc.appchat.R;
import com.example.mypc.appchat.gson.Contact;
import com.example.mypc.appchat.gson.Member;
import com.example.mypc.appchat.utils.Key;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

/**
 * Created by MyPC on 07/03/2017.
 */

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder>  {
    private ArrayList<Contact> _Items;
    private Activity _Activity;
    private Socket mSocket = MyApplication.getInstance().getSocket();

    private Emitter.Listener onDeleteContact = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            final String data = args[0].toString();
            _Activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < _Items.size(); i++){
                        if(data.contains(_Items.get(i).getId())){
                            Log.d("DELETE_CONTACT", data);
                            _Items.remove(_Items.get(i));
                            notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    };

    public BaseAdapter(ArrayList<Contact> items,Activity activity) {
        _Items      = items;
        _Activity = activity;
        mSocket.connect();
        mSocket.on("delete_contact",onDeleteContact);
    }

    @Override
    public BaseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user, parent, false);
        return new BaseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ArrayList<Member> members = (ArrayList<Member>) _Items.get(position).getMembers();

        for (int i = 0; i < members.size(); i++){
            if(!ContactActivity._Id.equals(members.get(i).getId())){
                holder._Name.setText(members.get(i).getName());
            }
        }

        if (_Items.get(position).getColor()){
            holder._View.setBackgroundResource(R.drawable.background_user_buzz);
        }else{
            holder._View.setBackgroundResource(R.drawable.background_user);
        }
        holder._View.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(_Activity, ConversationActivity.class);
                intent.putExtra(Key.CONVERSATION, _Items.get(position).getId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                _Activity.startActivity(intent);
            }
        });

        holder._View.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(_Activity);

                builder.setTitle("Bạn có chắc muốn xóa cuộc trò chuyện ?");
                builder.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mSocket.emit("delete_contact", _Items.get(position).getId());
                    }
                });
                builder.show();

                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return _Items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        final View _View;
        final TextView _Name;

        public ViewHolder(View itemView) {
            super(itemView);
            _View  = itemView;
            _Name  = (TextView) _View.findViewById(R.id.tv_name);
        }
    }
}
