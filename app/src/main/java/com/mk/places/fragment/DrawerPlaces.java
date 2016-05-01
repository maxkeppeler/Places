package com.mk.places.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.inquiry.Inquiry;
import com.mk.places.R;
import com.mk.places.activity.FavoriteUtil;
import com.mk.places.activity.MainActivity;
import com.mk.places.activity.PlaceView;
import com.mk.places.adapters.PlaceAdapter;
import com.mk.places.models.Place;
import com.mk.places.models.Places;
import com.mk.places.utilities.Dialogs;
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
    private static PlaceAdapter mAdapter;
    private static ViewGroup layout;
    private static RecyclerView mRecycler;
    private static Activity context;
    private static String filterKey = "All";
    private Preferences mPrefs;

    public static void filterFavorites() {

        ArrayList<Place> filter = new ArrayList<>();

        FavoriteUtil.init(context);

        if (FavoriteUtil.getDB() != null) {

            Log.d(TAG, "  ");

            int x = 0;

            for (int j = 0; j < Places.getPlacesList().size(); j++) {


                for (int i = 0; i < FavoriteUtil.getDB().length; i++) {

                    if (Places.getPlacesList().get(j).getId().equals(FavoriteUtil.getDB()[i].getID())) {


                        if (FavoriteUtil.isFavorited(FavoriteUtil.getDB()[i].getID())) {

                            filter.add(x, Places.getPlacesList().get(j));
                            mAdapter.notifyItemInserted(x);

                            x++;
                            Log.i(TAG, "Found Favored Item: " + FavoriteUtil.getDB()[i].getID());
                        }
                    }
                }
            }

            Log.d(TAG, "  ");

        }

        Preferences mPref = new Preferences(context);
        mPref.setFavoSize(filter.size());

        Inquiry.deinit();
        setupLayout(true, filter);
    }

    public static void setFilterKey(String string) {
        filterKey = string;
        loadPlacesList(context);
    }

    public static void loadPlacesList(Context context) {

        Places.clearList();

        new DownloadPlacesJSON(new MainActivity.PlacesListInterface() {
            @Override
            public void checkPlacesListCreation(boolean result) {

                if (DrawerPlaces.mAdapter != null)
                    DrawerPlaces.mAdapter.notifyDataSetChanged();

            }
        }, context, filterKey).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

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

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_places, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.column:
                Dialogs.columnsDialog(context);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

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
            layout = (ViewGroup) inflater.inflate(R.layout.drawer_places, container, false);
        } catch (InflateException e) {

        }

        mPrefs = new Preferences(context);
        if (mPrefs.getColumns() == 0) mPrefs.setColumns(1);

        mRecycler = (RecyclerView) layout.findViewById(R.id.placecRecyclerView);
        mRecycler.setLayoutManager(new GridLayoutManager(context, mPrefs.getColumns(), 1, false));
        mRecycler.setAdapter(mAdapter);
        mRecycler.setHasFixedSize(true);
        setupLayout(false, Places.getPlacesList());

        return layout;
    }

    public void setColumns(int amount) {
        int columns = amount;
        mRecycler.setLayoutManager(new GridLayoutManager(context, columns, 1, false));

        mPrefs = new Preferences(context);
        mPrefs.setColumns(amount);
    }

    public static class DownloadPlacesJSON extends AsyncTask<Void, Void, Void> {

        private final static ArrayList<Place> places = new ArrayList<>();
        static long startTime, endTime;
        private static boolean successful;
        private MainActivity.PlacesListInterface wi;
        private WeakReference<Context> taskContext;
        private WeakReference<Activity> wrActivity;
        private String keyWord;

        public DownloadPlacesJSON(MainActivity.PlacesListInterface wi, Context context, String keyWord) {
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

                            json.getString("id"),
                            json.getString("location"),
                            json.getString("sight"),
                            json.getString("continent"),
                            json.getString("religion"),
                            json.getString("description"),

                            json.getString("url"),

                            json.getString("urlaTitle"), json.getString("urlaDesc"), json.getString("urla"),
                            json.getString("urlbTitle"), json.getString("urlbDesc"), json.getString("urlb"),
                            json.getString("urlcTitle"), json.getString("urlcDesc"), json.getString("urlc"),
                            json.getString("urldTitle"), json.getString("urldDesc"), json.getString("urld"),
                            json.getString("urleTitle"), json.getString("urleDesc"), json.getString("urle"),
                            json.getString("urlfTitle"), json.getString("urlfDesc"), json.getString("urlf"),
                            json.getString("urlgTitle"), json.getString("urlgDesc"), json.getString("urlg"),
                            json.getString("urlhTitle"), json.getString("urlhDesc"), json.getString("urlh"),
                            json.getString("urliTitle"), json.getString("urliDesc"), json.getString("urli"),
                            json.getString("urljTitle"), json.getString("urljDesc"), json.getString("urlj"),
                            json.getString("urlkTitle"), json.getString("urlkDesc"), json.getString("urlk"),
                            json.getString("urllTitle"), json.getString("urllDesc"), json.getString("urll"),
                            json.getString("urlmTitle"), json.getString("urlmDesc"), json.getString("urlm"),
                            json.getString("urlnTitle"), json.getString("urlnDesc"), json.getString("urln"),
                            json.getString("urloTitle"), json.getString("urloDesc"), json.getString("urlo"),
                            json.getString("urlpTitle"), json.getString("urlpDesc"), json.getString("urlp"),
                            json.getString("urlqTitle"), json.getString("urlqDesc"), json.getString("urlq"),
                            json.getString("urlrTitle"), json.getString("urlrDesc"), json.getString("urlr"),
                            json.getString("urlsTitle"), json.getString("urlsDesc"), json.getString("urls"),
                            json.getString("urltTitle"), json.getString("urltDesc"), json.getString("urlt")
                    )
            );
        }

        @Override
        protected void onPostExecute(Void args) {
            endTime = System.currentTimeMillis();
            Log.d("Places ", "Task took: " + String.valueOf((endTime - startTime) / 1000) + " seconds");

            if (layout != null) setupLayout(false, Places.getPlacesList());
            if (wi != null) wi.checkPlacesListCreation(successful);
        }


    }


}
