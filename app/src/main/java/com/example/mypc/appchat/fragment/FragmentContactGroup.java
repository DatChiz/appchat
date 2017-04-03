package com.example.mypc.appchat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mypc.appchat.R;
import com.example.mypc.appchat.adapter.BaseAdapter;

import java.util.ArrayList;

/**
 * Created by MyPC on 07/03/2017.
 */

public class FragmentContactGroup extends Fragment {
    private ArrayList<String> _Items;
    private RecyclerView _ItemList;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        _ItemList = (RecyclerView) view.findViewById(R.id.item_list);

        if (savedInstanceState != null) {
            _Items = savedInstanceState.getStringArrayList("");
        }

//        BaseAdapter adapter = new BaseAdapter(_Items, container.getContext());
//        _ItemList.setAdapter(adapter);
        return view;
    }
}
