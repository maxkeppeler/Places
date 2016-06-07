package com.earth.places.threads;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.earth.places.R;
import com.earth.places.fragment.FragmentBookmarks;
import com.earth.places.fragment.FragmentPlaces;
import com.earth.places.models.Place;
import com.earth.places.models.Places;
import com.earth.places.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PlacesJSON extends AsyncTask<Void, Void, Void> {

    private final static ArrayList<Place> places = new ArrayList<>();
    private static final String TAG = "PlacesJSON";
    private long startTime;
    private Activity context;

    public PlacesJSON(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Places.clearList();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected Void doInBackground(Void... params) {

        JSONObject json = JSONParser.getJSONFromURL(context.getResources().getString(R.string.Places_JSON));

        if (json != null)
            try {
                JSONArray jsonarray = json.getJSONArray("places");

                for (int i = 0; i < jsonarray.length(); i++) {

                    json = jsonarray.getJSONObject(i);

                    places.add(new Place(
                            json.getString("id"),
                            json.getString("place"),
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

            } catch (JSONException e) {
                Log.e(TAG, " Problem with the JSON API", e);
            }

        return null;
    }

    @Override
    protected void onPostExecute(Void args) {
        Log.d(TAG, "Task took " + String.valueOf((System.currentTimeMillis() - startTime) / 1000) + " seconds. Loaded Places: " + Places.getPlacesList().size());

        FragmentPlaces.updateLayout(false, null);

        if (FragmentPlaces.refreshLayout != null)
            FragmentPlaces.refreshLayout.setRefreshing(false);

        FragmentPlaces.recyclerView.setVisibility(View.VISIBLE);

        FragmentBookmarks.loadBookmarks(context);
    }


}


