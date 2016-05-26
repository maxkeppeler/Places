package com.mk.places.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mk.places.activities.MainActivity;
import com.mk.places.fragment.DrawerEmpty;
import com.mk.places.fragment.FragmentBookmarks;
import com.mk.places.fragment.FragmentDisasters;
import com.mk.places.fragment.FragmentGoodActs;
import com.mk.places.fragment.FragmentPlaces;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private int tabAmount;

    public PagerAdapter(FragmentManager fm, int tabAmount) {
        super(fm);
        this.tabAmount = tabAmount;
    }

    @Override
    public Fragment getItem(int position) {

        if (MainActivity.drawerIndex == 0) {

            switch (position) {
                case 0:
                    return new FragmentPlaces();
                case 1:
                    return new FragmentBookmarks();
                default:
                    return null;
            }
        }

        if (MainActivity.drawerIndex == 1) {


            switch (position) {

                case 0:
                    return new FragmentDisasters();
                case 1:
                    return new FragmentGoodActs();
                default:
                    return null;
            }
        }


        if (MainActivity.drawerIndex == 2) {

            switch (position) {
                case 0:
                    return new DrawerEmpty();
                case 1:
                    return new DrawerEmpty();
                default:
                    return null;
            }
        }

        return null;

    }

    @Override
    public int getCount() {
        return tabAmount;
    }

}
