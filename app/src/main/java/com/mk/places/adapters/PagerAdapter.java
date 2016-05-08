package com.mk.places.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.mk.places.fragment.TabBookmarks;
import com.mk.places.fragment.TabPlaces;

public class PagerAdapter extends FragmentPagerAdapter {

    private int amount = 2;
    private Context context;

    public PagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return new TabPlaces();

            case 1:
                return TabBookmarks.newInstance(position);

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return amount;
    }


}
