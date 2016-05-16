package com.mk.places.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mk.places.fragment.DrawerBookmarks;
import com.mk.places.fragment.DrawerPlaces;
import com.mk.places.fragment.DrawerPlacesNew;

/**
 * Created by max on 15.05.16.
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;

    public FragmentPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new DrawerPlaces();
            case 1:
                return new DrawerBookmarks();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
