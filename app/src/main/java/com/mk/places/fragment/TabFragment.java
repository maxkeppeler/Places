package com.mk.places.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mk.places.R;
import com.mk.places.adapters.PagerAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment {

    public static int int_items = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        View view = inflater.inflate(R.layout.activity_main, container, false);

//        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);
        final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);

//        viewPager.setAdapter(new PagerAdapter(getChildFragmentManager(), int_items));

//        tabLayout.setupWithViewPager(viewPager);

        viewPager.setAdapter(buildAdapter());

//        tabLayout.post(new Runnable() {
//            @Override
//            public void run() {
//                tabLayout.setupWithViewPager(viewPager);
//            }
//        });

//        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//                viewPager.setCurrentItem(tab.getPosition());
//                Log.d("MainActivity", "ViewPager - position: " + tab.getPosition() + " TabLayout - position: " + tabLayout.getSelectedTabPosition());
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//            }
//        });




        return view;

    }

    private PagerAdapter buildAdapter() {
        return(new PagerAdapter(getActivity(), getChildFragmentManager()));
    }

}
