package com.mk.places.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mk.places.R;
import com.mk.places.utilities.Utils;

import butterknife.Bind;
import butterknife.BindString;
import butterknife.ButterKnife;

public class DrawerUpload extends Fragment {

    final private String TAG = "DrawerUpload";
    private Activity context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.drawer_upload, container, false);

        context = getActivity();
        ButterKnife.bind(context);

        TextView rulesTitle = (TextView) view.findViewById(R.id.infoTitle);
        TextView rulesText = (TextView) view.findViewById(R.id.infoText);

        TextView miscTitle = (TextView) view.findViewById(R.id.miscTitle);
        TextView miscText = (TextView) view.findViewById(R.id.miscText);

        TextView rewardTitle = (TextView) view.findViewById(R.id.rewardTitle);
        TextView rewardText = (TextView) view.findViewById(R.id.rewardText);

        rulesTitle.setTypeface(Utils.customTypeface(context, 1));
        rulesText.setTypeface(Utils.customTypeface(context, 2));

        miscTitle.setTypeface(Utils.customTypeface(context, 1));
        miscText.setTypeface(Utils.customTypeface(context, 2));

        rewardTitle.setTypeface(Utils.customTypeface(context, 1));
        rewardText.setTypeface(Utils.customTypeface(context, 2));

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

}