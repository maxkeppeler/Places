package com.mk.places.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.mk.places.fragment.DrawerBookmarks;
import com.mk.places.fragment.DrawerPlaces;

public class PagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private int amount = 0;

    public PagerAdapter(FragmentManager fm, int amount) {
        super(fm);
        this.amount = amount;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {

            case 0:
                return DrawerBookmarks.newInstance(position);

            case 1:
                return DrawerPlaces.newInstance(position);

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return amount;
    }
}
