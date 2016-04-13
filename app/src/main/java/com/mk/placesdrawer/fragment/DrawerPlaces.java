package com.mk.placesdrawer.fragment;

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
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mk.placesdrawer.R;
import com.mk.placesdrawer.activity.MainActivity;
import com.mk.placesdrawer.activity.PlaceDetailActivity;
import com.mk.placesdrawer.adapters.PlaceAdapter;
import com.mk.placesdrawer.models.Place;
import com.mk.placesdrawer.models.PlaceList;
import com.mk.placesdrawer.utilities.JSONParser;
import com.mk.placesdrawer.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DrawerPlaces extends Fragment {

    public static PlaceAdapter mAdapter;
    private static ViewGroup layout;
    private static RecyclerView mRecycler;
    private static Activity context;
    private static boolean successful;

    private static int columns = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

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

        if (PlaceList.getPlacesList() != null && PlaceList.getPlacesList().size() > 0) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    mAdapter = new PlaceAdapter(context,
                            new PlaceAdapter.ClickListener() {
                                @Override
                                public void onClick(PlaceAdapter.PlacesViewHolder view,
                                                    int position, boolean longClick) {


                                    if (longClick) {
                                        Toast.makeText(context, "Long Click", Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(context, "Short Click", Toast.LENGTH_SHORT).show();
                                        final Intent intent = new Intent(context, PlaceDetailActivity.class);
                                        intent.putExtra("item", PlaceList.getPlacesList().get(position));
                                        context.startActivity(intent);
                                    }
                                }
                            });

                    mAdapter.setData(PlaceList.getPlacesList());
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
                    if (mAdapter != null) {
                    }
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
        final MainActivity.PlacesListInterface wi;
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
                                        || json.getString("position").equals(keyWord)
                                        || json.getString("religion").equals(keyWord)
                        )) {
                            addJsonObject(jsonarray, i);
                        }
                        else if (keyWord.equals("All")) addJsonObject(jsonarray, i);
                    }

                    PlaceList.createPlaceList(places);
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
                            json.getString("location"), json.getString("sight"), json.getString("description"),
                            json.getString("position"), json.getString("religion"),
                            json.getString("url"),
                            favorite
                    )
            );
        }

        //    Methods for filtering out the favored items/ json objects

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
