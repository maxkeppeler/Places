package com.mk.places.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mk.places.R;
import com.mk.places.adapters.AboutMemberAdapter;
import com.mk.places.adapters.GalleryViewAdapter;

import butterknife.ButterKnife;

public class DrawerAbout extends Fragment {

    private static ViewGroup layout;
    private static Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();
        ButterKnife.bind(context);

        setHasOptionsMenu(true);

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) parent.removeView(layout);
        }

        try {
            layout = (ViewGroup) inflater.inflate(R.layout.drawer_about, container, false);

        } catch (InflateException e) {
        }


        String[] profileImage = getResources().getStringArray(R.array.profileImage);
        String[] profileName = getResources().getStringArray(R.array.profileName);
        String[] profileDescription = getResources().getStringArray(R.array.profileDescription);

        ViewPager mPager = (ViewPager) layout.findViewById(R.id.viewPagerMember);
        mPager.setAdapter(new AboutMemberAdapter(profileImage, profileName, profileDescription, context.getApplicationContext()));
        mPager.setCurrentItem(0);


        return layout;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
//        inflater.inflate(R.menu.toolbar_places_details, menu);
    }
}