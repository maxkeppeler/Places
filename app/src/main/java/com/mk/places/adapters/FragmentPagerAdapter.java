package com.mk.places.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mk.places.fragment.FragmentBookmarks;
import com.mk.places.fragment.FragmentPlaces;

/**
 * Created by max on 15.05.16.
 */
public class FragmentPagerAdapter extends FragmentStatePagerAdapter {

    private int mNumOfTabs;

    public FragmentPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new FragmentPlaces();
            case 1:
                return new FragmentBookmarks();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

}
