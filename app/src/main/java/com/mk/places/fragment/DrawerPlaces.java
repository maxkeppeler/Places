package com.mk.places.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mk.places.R;
import com.mk.places.activity.DetailView;
import com.mk.places.activity.MainActivity;
import com.mk.places.adapters.PlaceAdapter;
import com.mk.places.models.Place;
import com.mk.places.models.Places;
import com.mk.places.utilities.Dialogs;
import com.mk.places.utilities.JSONParser;
import com.mk.places.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DrawerPlaces extends Fragment implements SearchView.OnQueryTextListener {

    public static PlaceAdapter mAdapter;
    private static ViewGroup layout;
    private static RecyclerView mRecycler;
    private static Activity context;
    private static boolean successful;
    private static int columns = 1;
    private String filterKey = "All";
    private static ViewGroup layoutTest;
    private static final String TAG = "DrawerPlaces";

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_places, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.column:
                Dialogs.columnsDialog(context);
                break;
            case R.id.changelog:
                Dialogs.showChangelog(context);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void filterFavorites() {

        ArrayList<Place> filteredList = Places.getPlacesList();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        for (int i = 0; i < Places.getPlacesList().size(); i++) {

            if (Places.getPlacesList().get(i).getFavorite() == 0 || preferences.getInt("item", 0) == 0) {
                filteredList.remove(i);
                Log.d(TAG, "Delete Item, because it's not marked as Favorite: " + i);
            }
        }

        DrawerPlaces.mAdapter.notifyDataSetChanged();
        mAdapter.setData(filteredList);
        mRecycler.setAdapter(mAdapter);

    }

    public void setFilterKey(String string) {
        this.filterKey = string;
        loadPlacesList(context);
    }

    public void loadPlacesList(Context context) {
        Places.clearList();

        new DrawerPlaces.DownloadJSON(new MainActivity.PlacesListInterface() {
            @Override
            public void checkPlacesListCreation(boolean result) {
                if (DrawerPlaces.mAdapter != null) {
                    DrawerPlaces.mAdapter.notifyDataSetChanged();
                }
            }
        }, context, filterKey).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);

//        Places.getPlacesList().clear();
//        Places.clearList();

        context = getActivity();

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) parent.removeView(layout);
        }

        try {
            layout = (ViewGroup) inflater.inflate(R.layout.drawer_places, container, false);
            layoutTest = (ViewGroup) inflater.inflate(R.layout.activity_detail_view_preview, container, false);
        } catch (InflateException e) {
        }

        if (layout != null)
        mRecycler = (RecyclerView) layout.findViewById(R.id.placecRecyclerView);

        mRecycler.setLayoutManager(new GridLayoutManager(context, columns, 1, false));
        mRecycler.setHasFixedSize(true);
        mRecycler.setAdapter(mAdapter);
        mRecycler.setVisibility(View.VISIBLE);
        setupLayout(false);
        return layout;
    }

    private static void setupLayout(final boolean fromTask) {

        if (Places.getPlacesList() != null && Places.getPlacesList().size() > 0) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mAdapter = new PlaceAdapter(context,
                            new PlaceAdapter.ClickListener() {
                                @Override
                                public void onClick(PlaceAdapter.PlacesViewHolder view,
                                                    int position, boolean longClick) {

                                    Intent intent = null;

                                    if (longClick) {


                                        final ImageView mainImage = (ImageView) layoutTest.findViewById(R.id.mainImage);
                                        Place item = Places.getPlacesList().get(position);

//                                        new MaterialDialog.Builder(context)
//                                                .customView(R.layout.activity_detail_view_preview, true)
//                                                .show();
//
//
//                                       Glide.with(context).load(item.getUrl()).asBitmap().into(mainImage);



                                    }

                                    else intent = new Intent(context, DetailView.class);

                                    if (intent != null) {

                                    intent.putExtra("item", Places.getPlacesList().get(position));
                                    intent.putExtra("pos", position);
                                    context.startActivity(intent);
                                    }
                                }
                            });

                    mAdapter.setData(Places.getPlacesList());
                    mRecycler.setAdapter(mAdapter);
                    if (Utils.hasNetwork(context)) {
                        mRecycler.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        else {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (layout != null) {

                        if (fromTask) {
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    context.runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                        }
                                    });
                                }
                            }, 10000);
                        }
                    }
                }
            });
        }
    }

    public static void changeColumns(int i) {
        columns = i;
        mRecycler.setLayoutManager(new GridLayoutManager(context, columns, 1, false));
    }


    public static class DownloadJSON extends AsyncTask<Void, Void, Void> {

        private final static ArrayList<Place> places = new ArrayList<>();
        static long startTime, endTime;
        private MainActivity.PlacesListInterface wi;
        private WeakReference<Context> taskContext;
        private WeakReference<Activity> wrActivity;
        private String keyWord;
        private boolean favored;

        public DownloadJSON(MainActivity.PlacesListInterface wi, Context context, String keyWord) {
            this.keyWord = keyWord;
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

            JSONObject json = JSONParser.getJSONFromURL(Utils.getStringFromResources(taskContext.get(), R.string.json_file_url));
            if (json != null) {
                try {
                    JSONArray jsonarray = json.getJSONArray("places");

                    for (int i = 0; i < jsonarray.length(); i++) {

                        json = jsonarray.getJSONObject(i);

                        if (!keyWord.equals("All") && (

                                json.getString("sight").equals(keyWord)
                                        || json.getString("continent").equals(keyWord)

                        )) {
                            addJsonObject(jsonarray, i);
                        } else if (keyWord.equals("All")) addJsonObject(jsonarray, i);
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

        private void addJsonObject(JSONArray jsonarray, int i) throws JSONException {
            JSONObject json = jsonarray.getJSONObject(i);
            int favorite = 0;
            places.add(new Place(

                            json.getString("location"),
                            json.getString("sight"),
                            json.getString("continent"),
                            json.getString("religion"),
                            json.getString("description"),

                            json.getString("url"),
                            json.getString("urla"),
                            json.getString("urlb"),
                            json.getString("urlc"),
                            json.getString("urld"),
                            json.getString("urle"),
                            json.getString("urlf"),
                            json.getString("urlg"),
                            json.getString("urlh"),
                            json.getString("urli"),
                            json.getString("urlj"),
                            json.getString("urlk"),
                            json.getString("urll"),
                            json.getString("urlm"),
                            json.getString("urln"),
                            json.getString("urlo"),
                            json.getString("urlp"),
                            json.getString("urlq"),
                            json.getString("urlr"),
                            json.getString("urls"),
                            json.getString("urlt"),

                            favorite
                    )
            );
        }

        //   TODO Methods for filtering out the favored items/ json objects

        public void filterFavored() {

        }


        @Override
        protected void onPostExecute(Void args) {
            endTime = System.currentTimeMillis();
            Log.d("Places ", "Places Task completed in: " + String.valueOf((endTime - startTime) / 1000) + " secs.");

            if (layout != null) setupLayout(true);
            if (wi != null) wi.checkPlacesListCreation(successful);
        }


    }


}
