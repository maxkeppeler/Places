package com.mk.places.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mk.places.R;

import io.smooch.core.Smooch;

public class DrawerLiveChat extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.feedback, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        String TOKEN = "4k2knjsi7zbut95wk28dgqtvu";
        Smooch.init(getActivity().getApplication(), TOKEN);

//        ConversationActivity.show(getContext());
//        ConversationFragment.instantiate()
    }

}