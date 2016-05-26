package com.mk.places.threads;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.mk.places.R;
import com.mk.places.fragment.FragmentDisasters;
import com.mk.places.models.Disaster;
import com.mk.places.models.Disasters;
import com.mk.places.utilities.JSONParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SinsJSON extends AsyncTask<Void, Void, Void> {

    private final static ArrayList<Disaster> DISASTERs = new ArrayList<>();
    private static final String TAG = "SinsJSON";
    private long startTime;
    private Activity context;

    public SinsJSON(Activity context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        Disasters.clearList();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected Void doInBackground(Void... params) {

        JSONObject json = JSONParser.getJSONFromURL(context.getResources().getString(R.string.Sins_JSON));

        if (json != null)
            try {
                JSONArray jsonarray = json.getJSONArray("disasters");

                for (int i = 0; i < jsonarray.length(); i++) {

                    json = jsonarray.getJSONObject(i);

                    DISASTERs.add(new Disaster(
                            json.getString("title"),
                            json.getString("images"),
                            json.getString("url")
                    ));
                }

                Disasters.createDisastersList(DISASTERs);

            } catch (JSONException e) {
                Log.e(TAG, " Problem with the JSON API", e);
            }

        return null;
    }

    @Override
    protected void onPostExecute(Void args) {
        Log.d(TAG, "Task took " + String.valueOf((System.currentTimeMillis() - startTime) / 1000) + " seconds. Loaded Disasters: " + Disasters.getDisastersList().size());

        FragmentDisasters.updateLayout();

        if (FragmentDisasters.refreshLayout != null)
            FragmentDisasters.refreshLayout.setRefreshing(false);

        FragmentDisasters.recyclerView.setVisibility(View.VISIBLE);

    }


}


