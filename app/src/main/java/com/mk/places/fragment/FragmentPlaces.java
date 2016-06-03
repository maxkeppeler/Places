package com.mk.places.fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mk.places.R;
import com.mk.places.activities.MainActivity;
import com.mk.places.activities.PlaceView;
import com.mk.places.adapters.PlaceAdapter;
import com.mk.places.models.Place;
import com.mk.places.models.Places;
import com.mk.places.threads.PlacesJSON;
import com.mk.places.utilities.Constants;

import java.util.ArrayList;

public class FragmentPlaces extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "FragmentPlaces";
    public static SwipeRefreshLayout refreshLayout;
    public static ArrayList<Place> filter = new ArrayList<>();
    public static SearchView searchView;
    public static RecyclerView recyclerView;
    private static PlaceAdapter placeAdapter;
    private static Activity context;
    private Menu placesMenu;

    public static void updateLayout(final boolean filtering, final ArrayList<Place> searchFiltering) {

        if (Places.getPlacesList() != null && Places.getPlacesList().size() > 0) {

            context.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    placeAdapter = new PlaceAdapter(context, new PlaceAdapter.ClickListener() {

                        @Override
                        public void onClick(PlaceAdapter.PlacesViewHolder v, final int position) {

                            Intent intent = new Intent(context, PlaceView.class);
                            if (filtering) intent.putExtra("item", searchFiltering.get(position));
                            else intent.putExtra("item", Places.getPlacesList().get(position));
                            intent.putExtra("pos", position);
                            intent.putExtra("color", Places.getPlacesList().get(position).getColor());
                            context.startActivity(intent);

                        }
                    });

                    if (filtering) {

                        if (searchFiltering != null) {
                            placeAdapter.setData(searchFiltering);
                            recyclerView.setAdapter(placeAdapter);
                        } else {
                            placeAdapter.setData(filter);
                            recyclerView.setAdapter(placeAdapter);
                        }

                    } else {
                        placeAdapter.setData(Places.getPlacesList());
                        recyclerView.setAdapter(placeAdapter);
                    }

                }
            });
        }

    }

    public static void loadPlacesList(Activity context) {

        new PlacesJSON(context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public static void searchFilter(String key) {

        filter.clear();

        for (int j = 0; j < Places.getPlacesList().size(); j++) {

            if (Places.getPlacesList().get(j).getPlace().toLowerCase().contains(key.toLowerCase())) {
                filter.add(Places.getPlacesList().get(j));
            }
        }
        updateLayout(true, filter);

        MainActivity.updateTabTexts(0, filter.size(), FragmentBookmarks.getBookmarks().size());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        placesMenu = menu;

        inflater.inflate(R.menu.actions_places, menu);

        menu.findItem(R.id.drawer).setIcon(new IconicsDrawable(context, GoogleMaterial.Icon.gmd_filter_list).color(Color.WHITE).sizeDp(20));

        MenuItem item = menu.findItem(R.id.search);
        searchView = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setActionView(item, searchView);
        searchView.setQueryHint(getResources().getString(R.string.searchTextHint));
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                updateLayout(false, null);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String key) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String key) {
                searchFilter(key);
                MainActivity.drawerFilter.setSelection(Constants.NO_SELECTION);
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

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.placeRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(this);

        recyclerView = (RecyclerView) view.findViewById(R.id.placesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        recyclerView.setAdapter(placeAdapter);
        recyclerView.setHasFixedSize(true);

        updateLayout(false, null);
        return view;
    }

    @Override
    public void onRefresh() {
        placeAdapter.notifyDataSetChanged();
        recyclerView.setVisibility(View.INVISIBLE);
        loadPlacesList(context);
        MainActivity.drawerFilter.setSelection(Constants.NO_SELECTION);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (placesMenu != null)
        placesMenu.clear();
    }
}
