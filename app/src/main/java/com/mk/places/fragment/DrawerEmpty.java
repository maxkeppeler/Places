package com.mk.places.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mk.places.R;
import com.mk.places.utilities.Utils;

import butterknife.ButterKnife;

public class DrawerEmpty extends Fragment {

    final private String TAG = "DrawerUpload";
    private Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_empty, container, false);

        context = getActivity();
        ButterKnife.bind(context);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

}