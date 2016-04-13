package com.mk.placesdrawer.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.mk.placesdrawer.fragment.DrawerAbout;

/**
 * Created by max on 13.04.16.
 */
public class TabsAdapter extends FragmentStatePagerAdapter {

    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        Fragment frag=null;
        switch (position){
            case 0:
                frag=new DrawerAbout();
                break;
            case 1:
                frag=new DrawerAbout();
                break;
            case 2:
                frag=new DrawerAbout();
                break;
        }
        return frag;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        String title=" ";
        switch (position){
            case 0:
                title="Game";
                break;
            case 1:
                title="Movie";
                break;
            case 2:
                title="Study";
                break;
        }

        return title;
    }
}