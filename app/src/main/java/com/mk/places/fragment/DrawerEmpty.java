package com.mk.places.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mk.places.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerEmpty extends Fragment {


    public DrawerEmpty() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_drawer_empty, container, false);
    }

}