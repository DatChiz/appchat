package com.example.mypc.appchat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import com.example.mypc.appchat.ContactActivity;
import com.example.mypc.appchat.ConversationActivity;
import com.example.mypc.appchat.R;
import com.example.mypc.appchat.gson.Contact;
import com.example.mypc.appchat.gson.Member;
import com.example.mypc.appchat.gson.Message;
import com.example.mypc.appchat.utils.Key;

import java.util.ArrayList;

/**
 * Created by MyPC on 07/03/2017.
 */

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Contact _Contact;
    private Context _Context;
    private ArrayList<Message> _Items;
    private ArrayList<Member> _ItemsMember;

    public ChatAdapter(ArrayList<Message> items, ArrayList<Member> itemsMember, Context context) {
        _Context      = context;
        _Items        = items;
        _ItemsMember  = itemsMember;
    }

    @Override
    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_content, parent, false);
        return new ChatAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Message message   = _Items.get(position);
        String messageTXT = message.getBody();
        LinearLayout.LayoutParams params = new LinearLayout
                .LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

//        for (int i = 0; i < _ItemsMember.size(); i++) {
//            if (!ContactActivity._Id.equals(_ItemsMember.get(i).getId())) {
//                messageTXT = _ItemsMember.get(i).getName() + ": " + message.getBody();
//            }
//        }

        if (ContactActivity._Id.equals(message.getId())){
            holder._Name.setBackgroundResource(R.drawable.textview_me);
            params.gravity = Gravity.RIGHT;
        }else{
            holder._Name.setBackgroundResource(R.drawable.textview_friend);
            params.gravity = Gravity.LEFT;
        }
        holder._Name.setText(messageTXT);
        holder._Name.setLayoutParams(params);
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
