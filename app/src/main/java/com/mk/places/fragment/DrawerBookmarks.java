package com.mk.places.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.inquiry.Inquiry;
import com.mk.places.R;
import com.mk.places.activity.FavoriteUtil;
import com.mk.places.activity.PlaceView;
import com.mk.places.adapters.PlaceAdapter;
import com.mk.places.models.Place;
import com.mk.places.models.Places;
import com.mk.places.utilities.Preferences;
import com.mk.places.utilities.Utils;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class DrawerBookmarks extends Fragment {

    private static final String TAG = "DrawerBookmarks";
    private static Activity context;
    private static PlaceAdapter mAdapter;
    private static ViewGroup layout;
    private static RecyclerView mRecycler;
    private Preferences mPrefs;
    private ArrayList<Place> bookmarked = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        context = getActivity();

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) parent.removeView(layout);
        }

        try {
            layout = (ViewGroup) inflater.inflate(R.layout.drawer_bookmarks, container, false);
        } catch (InflateException e) {

        }

        mPrefs = new Preferences(context);
        if (mPrefs.getColumns() == 0) mPrefs.setColumns(1);


        mRecycler = (RecyclerView) layout.findViewById(R.id.bookmarksRecyclerView);
        mRecycler.setLayoutManager(new GridLayoutManager(context, mPrefs.getColumns(), 1, false));
        mRecycler.setAdapter(mAdapter);
        mRecycler.setHasFixedSize(true);

        FavoriteUtil.init(context);

        if (FavoriteUtil.getDB() != null) {

            Log.d(TAG, "  ");

            int x = 0;

            for (int j = 0; j < Places.getPlacesList().size(); j++) {


                for (int i = 0; i < FavoriteUtil.getDB().length; i++) {

                    if (Places.getPlacesList().get(j).getId().equals(FavoriteUtil.getDB()[i].getID())) {


                        if (FavoriteUtil.isFavorited(FavoriteUtil.getDB()[i].getID())) {

                            bookmarked.add(x, Places.getPlacesList().get(j));

                            x++;
                            Log.i(TAG, "Found Favored Item: " + FavoriteUtil.getDB()[i].getID());
                        }
                    }
                }
            }

            Log.d(TAG, "  ");

        }

        Preferences mPref = new Preferences(context);
        mPref.setFavoSize(bookmarked.size());

        Inquiry.deinit();
        setupLayout(true, bookmarked);

        return layout;
    }

    public static void setupLayout(final boolean otherList, final ArrayList<Place> arrayList) {

        if (Places.getPlacesList() != null && Places.getPlacesList().size() > 0) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter = new PlaceAdapter(context,
                            new PlaceAdapter.ClickListener() {
                                @Override
                                public void onClick(PlaceAdapter.PlacesViewHolder view, int position, boolean longClick) {

                                    Intent intent = null;
                                    if (longClick) ;
                                    else intent = new Intent(context, PlaceView.class);

                                    if (intent != null) {
                                        intent.putExtra("item", Places.getPlacesList().get(position));
                                        intent.putExtra("pos", position);
                                        context.startActivity(intent);
                                    }

                                }
                            });

                    if (!otherList) {
                        mAdapter.setData(Places.getPlacesList());
                        mRecycler.setAdapter(mAdapter);

                        Preferences mPref = new Preferences(context);
                        mPref.setPlacesSize(Places.getPlacesList().size());

                    } else {
                        mAdapter.setData(arrayList);
                        mRecycler.setAdapter(mAdapter);
                    }

                    if (Utils.hasNetwork(context))
                        mRecycler.setVisibility(View.VISIBLE);

                }
            });
        }

    }

}
