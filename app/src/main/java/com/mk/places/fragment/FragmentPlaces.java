package com.mk.places.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.mk.places.R;
import com.mk.places.activities.MainActivity;
import com.mk.places.activities.PlaceView;
import com.mk.places.adapters.PlaceAdapter;
import com.mk.places.models.Place;
import com.mk.places.models.Places;
import com.mk.places.threads.ParsePlacesJSON;
import com.mk.places.utilities.Preferences;

import java.util.ArrayList;

public class FragmentPlaces extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FragmentPlaces";
    public static SwipeRefreshLayout mRefreshLayout;
    public static ArrayList<Place> filter = new ArrayList<>();
    public static SearchView sv;
    public static RecyclerView mRecyclerView;
    private static PlaceAdapter mAdapter;
    private static Activity context;

    public static void updateLayout(final boolean filtering, final ArrayList<Place> searchFiltering) {

        if (Places.getPlacesList() != null && Places.getPlacesList().size() > 0) {


            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    mAdapter = new PlaceAdapter(context, new PlaceAdapter.ClickListener() {

                        @Override
                        public void onClick(PlaceAdapter.PlacesViewHolder v, final int position) {

                            Intent intent = new Intent(context, PlaceView.class);
                            if (filtering) intent.putExtra("item", filter.get(position));
                            else intent.putExtra("item", Places.getPlacesList().get(position));
                            intent.putExtra("pos", position);
                            context.startActivity(intent);

                        }
                    });

                    Preferences mPref = new Preferences(context);
                    mPref.setPlacesSize(Places.getPlacesList().size());

                    if (filtering) {

                        if (searchFiltering != null) {
                            mAdapter.setData(searchFiltering);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            mAdapter.setData(filter);
                            mRecyclerView.setAdapter(mAdapter);
                        }

                    } else {
                        mAdapter.setData(Places.getPlacesList());
                        mRecyclerView.setAdapter(mAdapter);
                    }

                }
            });
        }

    }

    public static void loadPlacesList(Activity context) {

        new ParsePlacesJSON(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public static void searchFilter(String key) {

        filter.clear();

        for (int j = 0; j < Places.getPlacesList().size(); j++) {

            if (Places.getPlacesList().get(j).getPlace().toLowerCase().contains(key.toLowerCase())) {
                filter.add(Places.getPlacesList().get(j));
            }
        }
        updateLayout(true, null);

        MainActivity.updateTabTexts(filter.size(), FragmentBookmarks.bookmarks.size());
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

        View view = inflater.inflate(R.layout.fragment_places, null);

        setHasOptionsMenu(true);

        context = getActivity();

        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.placeRefresh);
        mRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
        mRefreshLayout.setOnRefreshListener(this);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.placesRecyclerView);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        updateLayout(false, null);
        return view;
    }

    @Override
    public void onRefresh() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        loadPlacesList(context);
        MainActivity.drawerFilter.setSelection(0);
    }

}
