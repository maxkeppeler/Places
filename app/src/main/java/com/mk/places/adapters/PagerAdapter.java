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


    /**
     * Depending on the drawerIndex from the main activity, other tabs will be shown.
     * @param index
     * @return
     */
    @Override
    public Fragment getItem(int index) {

        // Drawer Places
        if (MainActivity.drawerIndex == 0)
            switch (index) {
                case 0: return new FragmentPlaces();
                case 1: return new FragmentBookmarks();
            }


        // Drawer Nature
        if (MainActivity.drawerIndex == 1)
            switch (index) {
                case 0: return new FragmentDisasters();
                case 1: return new FragmentGoodActs();
            }

        // Drawer Hall of Honor
        if (MainActivity.drawerIndex == 2)
            switch (index) {
                case 0: return new DrawerEmpty();
                case 1: return new DrawerEmpty();
            }

        return null;

    }

    @Override
    public int getCount() {
        return tabAmount;
    }

}
