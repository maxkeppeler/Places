package com.mk.places.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.mk.places.R;
import com.mk.places.adapters.MemberAdapter;
import com.mk.places.models.MemberItem;
import com.mk.places.utilities.Utils;
import com.mk.places.views.ButtonLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerAbout extends Fragment implements View.OnClickListener {

    private static View view;
    private static Activity context;

    @Bind(R.id.recyclerViewMember)
    RecyclerView recyclerViewMember;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)  {

        context = getActivity();
        ButterKnife.bind(context);

        setHasOptionsMenu(true);

        view = inflater.inflate(R.layout.drawer_about, container, false);

        final TextView appTitle = (TextView) view.findViewById(R.id.appTitle);
        final TextView appDesc = (TextView) view.findViewById(R.id.appDesc);
        appTitle.setTypeface(Utils.customTypeface(context, 1));
        appDesc.setTypeface(Utils.customTypeface(context, 2));

        final String[] aBtnNames = getResources().getStringArray(R.array.appButtonNames);
        final String[] aBtnLinks = getResources().getStringArray(R.array.appButtonLinks);

        final ButtonLayout buttonLayout = (ButtonLayout) view.findViewById(R.id.buttonLayoutApp);

        buttonLayout.setbAmount(aBtnNames.length);

        for (int j = 0; j < aBtnNames.length; j++)
            buttonLayout.addButton(aBtnNames[j], aBtnLinks[j], true);

        for (int i = 0; i < buttonLayout.getChildCount(); i++)
            buttonLayout.getChildAt(i).setOnClickListener(this);

        final String[] mImage = getResources().getStringArray(R.array.mImage);
        final String[] mName = getResources().getStringArray(R.array.mName);
        final String[] mTitle = getResources().getStringArray(R.array.mTitle);
        final String[] mDesc = getResources().getStringArray(R.array.mDesc);
        final String[] mBtnNames = context.getResources().getStringArray(R.array.profileButtonNames);
        final String[] mBtnLinks = getResources().getStringArray(R.array.profileButtonLinks);

        Bitmap bitmap = BitmapFactory.decodeResource( getResources(), R.drawable.ic_places );

        Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated( Palette palette ) {
                buttonLayout.setBackgroundColor(Utils.colorFromPalette(context, palette));
            }
        });

        final String[][] mBtnNamesN = new String[mBtnNames.length][];
        for (int i = 0; i < mBtnNames.length; i++)
            mBtnNamesN[i] = mBtnNames[i].split("\\|");

        final String[][] mBtnLinksN = new String[mBtnLinks.length][];
        for (int i = 0; i < mBtnLinks.length; i++)
            mBtnLinksN[i] = mBtnLinks[i].split("\\|");


        final MemberItem[] membersData = new MemberItem[mName.length];

        for (int i = 0; i < mName.length; i++)
        membersData[i] = new MemberItem(mImage[i], mName[i], mTitle[i], mDesc[i], mBtnNamesN[i], mBtnLinksN[i]);

        MemberAdapter memberAdapter = new MemberAdapter(membersData, context);

        recyclerViewMember = (RecyclerView) view.findViewById(R.id.recyclerViewMember);
        recyclerViewMember.setLayoutManager(new LinearLayoutManager(context) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        recyclerViewMember.setClipToPadding(false);
        recyclerViewMember.setAdapter(memberAdapter);
        recyclerViewMember.setHasFixedSize(true);


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

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof String) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse((String) view.getTag())));
            } catch (Exception e) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
}