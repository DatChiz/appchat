package com.example.mypc.appchat;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;

import com.example.mypc.appchat.adapter.BaseAdapter;
import com.example.mypc.appchat.adapter.ViewPagerFragmenntAdapter;
import com.example.mypc.appchat.fragment.FragmentContact;
import com.example.mypc.appchat.gson.Contact;
import com.example.mypc.appchat.gson.Conversation;
import com.example.mypc.appchat.utils.Key;
import com.google.gson.Gson;

import java.net.URISyntaxException;
import java.util.ArrayList;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;

public class ContactActivity extends AppCompatActivity implements View.OnClickListener{
    private ArrayList<Contact> _Items;
    private RecyclerView _ItemList;
    private EditText edtName;
    private Button btnSubmit;
    private BaseAdapter _Adapter;
    public static String _Id;
    private Socket mSocket = MyApplication.getInstance().getSocket();

    private Emitter.Listener onAddContact = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            final String data = args[0].toString();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.d("add_contact", data);
                    Gson gson = new Gson();
                    Contact contact = gson.fromJson(data, Contact.class);

                    for (int i = 0; i < contact.getMembers().size(); i++){
                        if(ContactActivity._Id.contains(contact.getMembers().get(i).getId())){
                            _Items.add(contact);
                            _Adapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener onMakeColor = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            final String data = args[0].toString();
            final String id = args[1].toString();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(!_Id.equals(id)){
                        for (int i = 0; i < _Items.size(); i++){
                            if(data.contains(_Items.get(i).getId())){
                                Log.d("onMakeColor", data);
                                _Items.get(i).setColor(true);
                                _Adapter.notifyDataSetChanged();
                            }
                        }
                    }
                }
            });
        }
    };

    private Emitter.Listener onMakeColorDefault = new Emitter.Listener() {

        @Override
        public void call(final Object... args) {
            final String data = args[0].toString();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < _Items.size(); i++){
                        if(data.contains(_Items.get(i).getId())){
                            Log.d("onMakeColor", data);
                            _Items.get(i).setColor(false);
                            _Adapter.notifyDataSetChanged();
                        }
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mSocket.connect();
        mSocket.on("add_contact", onAddContact);
        mSocket.on("make_color", onMakeColor);
        mSocket.on("make_color_default", onMakeColorDefault);

        _ItemList = (RecyclerView) findViewById(R.id.item_list);
        edtName = (EditText) findViewById(R.id.edtName);
        btnSubmit = (Button) findViewById(R.id.btnSubmit);

        Conversation contactOBJ   = (Conversation) getIntent().getSerializableExtra(Key.CONTACT_OBJ);
        _Items = (ArrayList<Contact>) contactOBJ.getContact();
        _Id = contactOBJ.getId();
        Log.d("gson", contactOBJ.getId());

        _Adapter = new BaseAdapter(_Items, ContactActivity.this);
        _ItemList.setAdapter(_Adapter);

        btnSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mSocket.emit("add_contact", edtName.getText().toString(), _Id);
        edtName.setText("");
    }


    //    private ViewPager _PagerContact;
//    private ViewPagerFragmenntAdapter _FragmenntAdapter;
//    private ArrayList<Fragment> _Items;
//    private final int NUM_PAGES = 3;

//        _PagerContact = (ViewPager) findViewById(R.id.pager_contact);
//        _Items = new ArrayList<Fragment>();
//        for (int i = 1; i <= NUM_PAGES; i++){
//            Fragment fragment = new FragmentContact();
//            Bundle bundle = new Bundle();
//            //recieve Array object Conservation
//            bundle.putStringArrayList(Key.CONTACT_LIST, new ArrayList<String>());
//            fragment.setArguments(bundle);
//            _Items.add(fragment);
//        }
//        _FragmenntAdapter = new ViewPagerFragmenntAdapter(getSupportFragmentManager(), _Items);
//        _PagerContact.setAdapter(_FragmenntAdapter);
}
