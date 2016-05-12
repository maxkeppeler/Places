package com.mk.places.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
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
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mk.places.R;
import com.mk.places.activities.MainActivity;
import com.mk.places.activities.PlaceView;
import com.mk.places.adapters.PlaceAdapter;
import com.mk.places.models.Place;
import com.mk.places.models.Places;
import com.mk.places.utilities.JSONParser;
import com.mk.places.utilities.Preferences;
import com.mk.places.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;


public class DrawerPlaces extends Fragment {

    private static final String TAG = "DrawerPlaces";
    public static PlaceAdapter adapter;
    private static View view;
    private static RecyclerView recyclerView;
    private static Activity context;
    private static SwipeRefreshLayout refreshLayout;
    private static Preferences preferences;
    private static int color;

    public DrawerPlaces() {
    }

    public static void setColumns(int amount) {
        recyclerView.setLayoutManager(new GridLayoutManager(context, amount, 1, false));

        preferences = new Preferences(context);
        preferences.setColumns(amount);
    }

    public static void searchFilter(String key) {

        ArrayList<Place> filter = new ArrayList<>();

        int x = 0;

        for (int j = 0; j < Places.getPlacesList().size(); j++) {

            if (
                    Places.getPlacesList().get(j).getLocation().toLowerCase().contains(key.toLowerCase())


                    ) {

                filter.add(x, Places.getPlacesList().get(j));
                x++;
            }

        }

        createLayout(true, filter);
    }

    public static void loadPlacesList(Context context) {

        Places.clearList();

        new DownloadPlacesJSON(new MainActivity.PlacesListInterface() {
            @Override
            public void checkPlacesListCreation(boolean result) {

                if (DrawerPlaces.adapter != null)
                    DrawerPlaces.adapter.notifyDataSetChanged();

                refreshLayout.setRefreshing(false);
            }
        }, context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    public static void createLayout(final boolean otherList, final ArrayList<Place> arrayList) {

        if (Places.getPlacesList() != null && Places.getPlacesList().size() > 0) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter = new PlaceAdapter(context,
                            new PlaceAdapter.ClickListener() {
                                @Override
                                public void onClick(PlaceAdapter.PlacesViewHolder view, final int position, boolean longClick) {

                                    Intent intent = new Intent(context, PlaceView.class);

                                    if (intent != null) {
                                        intent.putExtra("item", Places.getPlacesList().get(position));
                                        intent.putExtra("pos", position);
                                        intent.putExtra("color", color);
                                        Log.d(TAG, "onClick: " + color);
                                        context.startActivity(intent);
                                    }

                                }
                            });

                    if (!otherList) {
                        adapter.setData(Places.getPlacesList());
                        recyclerView.setAdapter(adapter);

                        Preferences mPref = new Preferences(context);
                        mPref.setPlacesSize(Places.getPlacesList().size());

                    } else {
                        adapter.setData(arrayList);
                        recyclerView.setAdapter(adapter);
                    }

                }
            });
        }

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.toolbar_places, menu);

        MenuItem item = menu.findItem(R.id.search);
        SearchView sv = new SearchView(((MainActivity) getActivity()).getSupportActionBar().getThemedContext());
        MenuItemCompat.setActionView(item, sv);
        sv.setQueryHint("Berlin, Germany");
        sv.setImeOptions(EditorInfo.IME_ACTION_DONE);
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
//            case R.id.column:
//                Dialogs.columnsDialog(context);
//                break;

            case R.id.drawer:
                MainActivity.drawerFilter.openDrawer();
                break;
        }

        return super.onOptionsItemSelected(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = inflater.inflate(R.layout.drawer_places, null);

        setHasOptionsMenu(true);
        context = getActivity();

        preferences = new Preferences(context);

        refreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.placeRefresh);
        refreshLayout.setColorSchemeResources(R.color.colorPrimary);
        refreshLayout.setOnRefreshListener(
                new SwipeRefreshLayout.OnRefreshListener() {
                    @Override
                    public void onRefresh() {
                        loadPlacesList(context);
                        MainActivity.drawerFilter.setSelection(0);
                    }
                }
        );

        recyclerView = (RecyclerView) view.findViewById(R.id.placesRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(context, preferences.getColumns(), 1, false));
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        if (Places.getPlacesList() == null)
            DrawerPlaces.loadPlacesList(context);

        createLayout(false, Places.getPlacesList());

        return view;
    }

    public static class DownloadPlacesJSON extends AsyncTask<Void, Void, Void> {

        private final static ArrayList<Place> places = new ArrayList<>();
        static long startTime, endTime;
        private static boolean successful;
        private MainActivity.PlacesListInterface wi;
        private WeakReference<Context> taskContext;
        private WeakReference<Activity> wrActivity;

        public DownloadPlacesJSON(MainActivity.PlacesListInterface wi, Context context) {
            this.wi = wi;
            this.taskContext = new WeakReference<>(context);
        }

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();

            if (wrActivity != null) {
                final Activity a = wrActivity.get();
                if (a != null) {
                    this.taskContext = new WeakReference<>(a.getApplicationContext());
                }
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            JSONObject json = JSONParser.getJSONFromURL(Utils.getRessources(taskContext.get(), R.string.json_file_url));

            if (json != null) {
                try {
                    JSONArray jsonarray = json.getJSONArray("places");

                    for (int i = 0; i < jsonarray.length(); i++) {

                        json = jsonarray.getJSONObject(i);

                        places.add(new Place(
                                        json.getString("id"),
                                        json.getString("location"),
                                        json.getString("sight"),
                                        json.getString("continent"),
                                        json.getString("infoTitle"),
                                        json.getString("info"),
                                        json.getString("description"),
                                        json.getString("url")
                                )
                        );


                    }

                    Places.createPlaceList(places);
                    successful = true;

                } catch (JSONException e) {
                    successful = false;
                }

            } else {
                successful = false;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void args) {
            endTime = System.currentTimeMillis();
            Log.d("Places ", "Task took: " + String.valueOf((endTime - startTime) / 1000) + " seconds");

            if (view != null) createLayout(false, Places.getPlacesList());
            if (wi != null) wi.checkPlacesListCreation(successful);
        }


    }


}
