package com.earth.places.threads;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.earth.places.R;
import com.earth.places.activities.MainActivity;
import com.earth.places.fragment.FragmentGoodActs;
import com.earth.places.models.GoodAct;
import com.earth.places.models.GoodActs;
import com.earth.places.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GoodActsJSON extends AsyncTask<Void, Void, Void> {

    private final static ArrayList<GoodAct> goodActs = new ArrayList<>();
    private static final String TAG = "DisastersJSON";
    private long startTime;
    private Activity context;

    public GoodActsJSON(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        GoodActs.clearList();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected Void doInBackground(Void... params) {

        JSONObject json = JSONParser.getJSONFromURL(context.getResources().getString(R.string.Sins_JSON));

        if (json != null)
            try {
                JSONArray jsonarray = json.getJSONArray("good acts");

                for (int i = 0; i < jsonarray.length(); i++) {

                    json = jsonarray.getJSONObject(i);

                    goodActs.add(new GoodAct(
                            json.getString("title"),
                            json.getString("images"),
                            json.getString("url")
                    ));
                }

                GoodActs.createGoodActsList(goodActs);

            } catch (JSONException e) {
                Log.e(TAG, " Problem with the JSON API", e);
            }

        return null;
    }

    @Override
    protected void onPostExecute(Void args) {
        Log.d(TAG, "Task took " + String.valueOf((System.currentTimeMillis() - startTime) / 1000) + " seconds. Loaded Good Acts: " + GoodActs.getGoodActsList().size());

        FragmentGoodActs.updateLayout();

        if (FragmentGoodActs.refreshLayout != null)
            FragmentGoodActs.refreshLayout.setRefreshing(false);

        FragmentGoodActs.recyclerView.setVisibility(View.VISIBLE);

        MainActivity.updateTabTexts(1, 0, GoodActs.getGoodActsList().size());
    }


}


