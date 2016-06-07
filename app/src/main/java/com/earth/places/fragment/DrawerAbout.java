package com.earth.places.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.earth.places.R;
import com.earth.places.adapters.MemberAdapter;
import com.earth.places.models.MemberItem;
import com.earth.places.utilities.Utils;
import com.earth.places.views.ButtonLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerAbout extends Fragment implements View.OnClickListener {


    private static String TAG = "DrawerAbout";
    private static Activity context;

    @Bind(R.id.recyclerViewMember) RecyclerView recyclerViewMember;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.drawer_about, container, false);

        context = getActivity();
        setHasOptionsMenu(true);

        TextView aboutTitle = ButterKnife.findById(view, R.id.aboutTitle);
        TextView aboutDesc = ButterKnife.findById(view, R.id.aboutDesc);

        aboutTitle.setTypeface(Utils.customTypeface(context, 1));
        aboutDesc.setTypeface(Utils.customTypeface(context, 2));

        final String[] aBtnNames = getResources().getStringArray(R.array.appButtonNames);
        final String[] aBtnLinks = getResources().getStringArray(R.array.appButtonLinks);

        final ButtonLayout buttonLayout = ButterKnife.findById(view, R.id.buttonLayoutApp);

        buttonLayout.setAmount(aBtnNames.length);

        for (int j = 0; j < aBtnNames.length; j++)
            buttonLayout.addButton(aBtnNames[j], aBtnLinks[j], true);

        for (int i = 0; i < buttonLayout.getChildCount(); i++)
            buttonLayout.getChildAt(i).setOnClickListener(this);

        final String[] mImage = getResources().getStringArray(R.array.mImage);
        final String[] mName = getResources().getStringArray(R.array.mName);
        final String[] mTitle = getResources().getStringArray(R.array.mTitle);
        final String[] mDesc = getResources().getStringArray(R.array.mDesc);
        final String[] mBtnNames = getResources().getStringArray(R.array.profileButtonNames);
        final String[] mBtnLinks = getResources().getStringArray(R.array.profileButtonLinks);

        final String[][] mBtnNamesN = new String[mBtnNames.length][];
        for (int i = 0; i < mBtnNames.length; i++)
            mBtnNamesN[i] = mBtnNames[i].split("\\|");

        final String[][] mBtnLinksN = new String[mBtnLinks.length][];
        for (int i = 0; i < mBtnLinks.length; i++)
            mBtnLinksN[i] = mBtnLinks[i].split("\\|");

        final MemberItem[] membersData = new MemberItem[mName.length];

        for (int i = 0; i < mName.length; i++)
            membersData[i] = new MemberItem(mImage[i], mName[i], mTitle[i], mDesc[i], mBtnNamesN[i], mBtnLinksN[i]);

        recyclerViewMember = (RecyclerView) view.findViewById(R.id.recyclerViewMember);
        recyclerViewMember.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        recyclerViewMember.setClipToPadding(false);
        recyclerViewMember.setAdapter(new MemberAdapter(membersData, context));
        recyclerViewMember.setHasFixedSize(true);

        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
    }

    @Override
    public void onClick(View view) {

        Utils.openChromeTab(context, (String) view.getTag(), 0);
    }
}