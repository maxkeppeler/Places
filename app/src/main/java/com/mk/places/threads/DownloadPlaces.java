package com.mk.places.threads;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.mk.places.R;
import com.mk.places.fragment.FragmentBookmarks;
import com.mk.places.fragment.FragmentPlaces;
import com.mk.places.models.Place;
import com.mk.places.models.Places;
import com.mk.places.utilities.JSONParser;
import com.mk.places.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DownloadPlaces extends AsyncTask<Void, Void, Void> {

    private final static ArrayList<Place> places = new ArrayList<>();
    private long startTime;
    private Activity context;
    private static final String TAG = "DownloadPlaces";

    public DownloadPlaces(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Places.clearList();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected Void doInBackground(Void... params) {

        JSONObject json = JSONParser.getJSONFromURL(context.getResources().getString(R.string.json_places));

        if (json != null)
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
                                    json.getString("creditsTitle"),
                                    json.getString("creditsDesc"),
                                    json.getString("credits"),
                                    json.getString("description"),
                                    json.getString("url")
                    ));
                }

                Places.createPlaceList(places);

            } catch (JSONException ignored) {

            }

        return null;
    }

    @Override
    protected void onPostExecute(Void args) {
        Log.d(TAG, "Task took " + String.valueOf((System.currentTimeMillis() - startTime) / 1000) + " seconds");
        FragmentPlaces.updateLayout(false, null);

        if (FragmentPlaces.mRefreshLayout != null)
        FragmentPlaces.mRefreshLayout.setRefreshing(false);

        FragmentPlaces.mRecyclerView.setVisibility(View.VISIBLE);

        FragmentBookmarks.loadBookmarks(context);
    }



}


