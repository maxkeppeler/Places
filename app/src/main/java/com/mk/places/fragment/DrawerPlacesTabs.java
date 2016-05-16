package com.mk.places.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.places.R;
import com.mk.places.activities.MainActivity;
import com.mk.places.adapters.FragmentPagerAdapter;
import com.mk.places.adapters.MemberAdapter;
import com.mk.places.models.MemberItem;
import com.mk.places.utilities.Utils;
import com.mk.places.views.customButtonLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerPlacesTabs extends Fragment {

    private static View view;
    private static Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        context = getActivity();
        ButterKnife.bind(context);

        view = inflater.inflate(R.layout.places_tab_layout, null);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        final FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager(), MainActivity.tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(MainActivity.tabLayout));
        MainActivity.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

}