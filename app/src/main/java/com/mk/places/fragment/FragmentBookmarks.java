package com.mk.places.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.afollestad.inquiry.Inquiry;
import com.mk.places.R;
import com.mk.places.utilities.Bookmarks;
import com.mk.places.activities.MainActivity;
import com.mk.places.activities.PlaceView;
import com.mk.places.adapters.PlaceAdapter;
import com.mk.places.models.Place;
import com.mk.places.models.Places;
import com.mk.places.utilities.Constants;

import java.util.ArrayList;

public class FragmentBookmarks extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FragmentBookmarks";
    public static ArrayList<Place> bookmarks = new ArrayList<>();
    public static SwipeRefreshLayout mRefreshLayout;
    public static ArrayList<Place> filter;
    public static SearchView sv;
    private static PlaceAdapter mAdapter;
    private static RecyclerView mRecyclerView;
    private static Activity context;
    private Menu bookmarksMenu;

    public static void updateLayout(final boolean filtering, final ArrayList<Place> searchFiltering) {

        if (bookmarks != null && bookmarks.size() > 0) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter = new PlaceAdapter(context,
                            new PlaceAdapter.ClickListener() {
                                @Override
                                public void onClick(PlaceAdapter.PlacesViewHolder v, int position) {

                                    Intent intent = new Intent(context, PlaceView.class);
                                    if (filtering)
                                        intent.putExtra("item", filter.get(position));
                                    else intent.putExtra("item", bookmarks.get(position));
                                    intent.putExtra("pos", position);
                                    context.startActivity(intent);

                                }
                            });

                    if (filtering) {

                        if (searchFiltering != null) {
                            mAdapter.setData(searchFiltering);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.setData(filter);
                            mRecyclerView.setAdapter(mAdapter);
                        }

                    } else {
                        mAdapter.setData(bookmarks);
                        mRecyclerView.setAdapter(mAdapter);
                    }
                }
            });
        }

        mRecyclerView.setVisibility(View.VISIBLE);
    }

    public static void searchFilter(String key) {

        filter = new ArrayList<>();
        int x = 0;
        for (int j = 0; j < bookmarks.size(); j++) {

            if (bookmarks.get(j).getPlace().toLowerCase().contains(key.toLowerCase())) {
                filter.add(x, bookmarks.get(j));
                x++;
            }
        }
        updateLayout(true, null);

        MainActivity.updateTabTexts(0, Places.getPlacesList().size(), filter.size());
    }

    public static void loadBookmarks(final Activity context) {

        bookmarks.clear();

        Bookmarks.init(context);

        if (Bookmarks.getDB() != null) {

            for (int j = 0; j < Places.getPlacesList().size(); j++) {

                for (int i = 0; i < Bookmarks.getDB().length; i++) {

                    if (Places.getPlacesList().get(j).getId().equals(Bookmarks.getDB()[i].getID())) {

                        if (Bookmarks.isFavorited(Bookmarks.getDB()[i].getID())) {

                            bookmarks.add(Places.getPlacesList().get(j));
                            Log.i(TAG, "Bookmarked Place: " + Bookmarks.getDB()[i].getID());
                        }
                    }
                }
            }


        } else mRecyclerView.setAdapter(mAdapter);

        Inquiry.deinit();

        MainActivity.updateTabTexts(0, Places.getPlacesList().size(), bookmarks.size());

        updateLayout(false, null);

        if (mRefreshLayout != null) mRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.actions_places, menu);

        bookmarksMenu = menu;

        MenuItem item = menu.findItem(R.id.search);
        sv = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setActionView(item, sv);
        sv.setQueryHint("Berlin, Germany");
        sv.setImeOptions(EditorInfo.IME_ACTION_DONE);
        sv.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                updateLayout(false, null);
                return false;
            }
        });
        sv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String key) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String key) {
                searchFilter(key);
                MainActivity.drawerFilter.setSelection(0);
                return false;
            }

        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {

        int id = menu.getItemId();
        switch (id) {
            case R.id.drawer:
                MainActivity.drawerFilter.openDrawer();
                break;
        }

        return super.onOptionsItemSelected(menu);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
        context = getActivity();

        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.bookmarksRefresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.bookmarksRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        return view;
    }

    @Override
    public void onRefresh() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        loadBookmarks(context);
        MainActivity.drawerFilter.setSelection(Constants.NO_SELECTION);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (bookmarksMenu != null)
        bookmarksMenu.clear();
    }

}
