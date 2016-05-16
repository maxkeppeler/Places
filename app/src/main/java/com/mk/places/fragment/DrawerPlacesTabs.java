package com.mk.places.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mk.places.R;
import com.mk.places.activities.MainActivity;
import com.mk.places.adapters.FragmentPagerAdapter;
import com.mk.places.models.Places;

import butterknife.ButterKnife;

public class DrawerPlacesTabs extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final Activity context = getActivity();
        ButterKnife.bind(context);

        View view = inflater.inflate(R.layout.places_tab_layout, null);

        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);
        final FragmentPagerAdapter adapter = new FragmentPagerAdapter(getChildFragmentManager(), MainActivity.tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(MainActivity.tabLayout));
        MainActivity.tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());


//                Check if
                if (tab.getPosition() == 1)
                    if (FragmentPlaces.filter != null && !FragmentPlaces.sv.isIconified()) {
                        FragmentPlaces.updateLayout(false, null);
                    }
                if (tab.getPosition() == 0) {
                    if (FragmentBookmarks.filter != null && !FragmentBookmarks.sv.isIconified()) {
                        FragmentBookmarks.updateLayout(false, null);
                    }
                }

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