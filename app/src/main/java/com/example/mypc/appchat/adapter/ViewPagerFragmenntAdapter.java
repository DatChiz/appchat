package com.example.mypc.appchat.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by MyPC on 08/03/2017.
 */

public class ViewPagerFragmenntAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> _items;

    public ViewPagerFragmenntAdapter(FragmentManager fm, ArrayList<Fragment> items) {
        super(fm);
        _items = items;
    }

    @Override
    public Fragment getItem(int position) {
        return _items.get(position);
    }

    @Override
    public int getCount() {
        return _items.size();
    }
}
