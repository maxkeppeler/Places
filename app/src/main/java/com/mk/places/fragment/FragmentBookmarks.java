package com.mk.places.fragment;


import android.app.Activity;
import android.content.Context;
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
import com.mk.places.activities.Bookmarks;
import com.mk.places.activities.MainActivity;
import com.mk.places.activities.PlaceView;
import com.mk.places.adapters.PlaceAdapter;
import com.mk.places.models.Place;
import com.mk.places.models.Places;
import com.mk.places.utilities.Preferences;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentBookmarks extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FragmentBookmarks";
    public static PlaceAdapter adapter;
    public static RecyclerView recycler;
    public static ArrayList<Place> bookmarks;
    public static SwipeRefreshLayout refreshLayout;
    private static Activity context;
    public static ArrayList<Place> filter;
    public static SearchView sv;

    public static void updateLayout(final boolean filtering, final ArrayList<Place> searchFiltering) {

        if (bookmarks != null && bookmarks.size() > 0) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new PlaceAdapter(context,
                            new PlaceAdapter.ClickListener() {
                                @Override
                                public void onClick(PlaceAdapter.PlacesViewHolder view, int position, boolean longClick) {

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
                            adapter.setData(searchFiltering);
                            recycler.setAdapter(adapter);
                        } else {
                            adapter.setData(filter);
                            recycler.setAdapter(adapter);
                        }

                    } else {
                        adapter.setData(bookmarks);
                        recycler.setAdapter(adapter);
                    }
                }
            });
        }

    }

    public static void searchFilter(String key) {

        filter = new ArrayList<>();
        int x = 0;
        for (int j = 0; j < bookmarks.size(); j++) {

            if (bookmarks.get(j).getLocation().toLowerCase().contains(key.toLowerCase())) {
                filter.add(x, bookmarks.get(j));
                x++;
            }
        }
        updateLayout(true, null);
    }

    public static void loadBookmarks(Context context) {

        bookmarks = new ArrayList<>();

        Bookmarks.init(context);

        if (Bookmarks.getDB() != null) {

            int x = 0;

            for (int j = 0; j < Places.getPlacesList().size(); j++) {

                for (int i = 0; i < Bookmarks.getDB().length; i++) {

                    if (Places.getPlacesList().get(j).getId().equals(Bookmarks.getDB()[i].getID())) {

                        if (Bookmarks.isFavorited(Bookmarks.getDB()[i].getID())) {

                            bookmarks.add(x, Places.getPlacesList().get(j));

                            x++;
                            Log.i(TAG, "Found Favored Item: " + Bookmarks.getDB()[i].getID());
                        }
                    }
                }
            }

        }

        Inquiry.deinit();
        Preferences mPref = new Preferences(context);
        mPref.setFavoSize(bookmarks.size());

        updateLayout(false, null);

        if (refreshLayout != null)
            refreshLayout.setRefreshing(false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.actions_places, menu);

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

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.bookmarksRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);

        recycler = (RecyclerView) view.findViewById(R.id.bookmarksRecyclerView);
        recycler.setLayoutManager(new GridLayoutManager(context, 1));
        recycler.setAdapter(adapter);
        recycler.setHasFixedSize(true);

        loadBookmarks(context);
        Log.d(TAG, "onCreateView: " + bookmarks.size());
        return view;
    }

    @Override
    public void onRefresh() {
        loadBookmarks(context);
        MainActivity.drawerFilter.setSelection(0);
    }
}
