package com.mk.placesdrawer.fragment;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mk.placesdrawer.R;
import com.mk.placesdrawer.adapters.PlacesListViewAdapter;
import com.mk.placesdrawer.model.PlacesListItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Fragment_Home extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter mAdapter;
    private List<PlacesListItem> placesItemList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_1, container, false);

    }


    @Override
    public void onViewCreated(View view , Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        final String url ="https://drive.google.com/uc?id=0B6ky9fzTGl9XNFBhTTlxU2hhQmM";
        new AsyncHttpTask().execute(url);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mAdapter = new PlacesListViewAdapter(getActivity(), placesItemList);
        mRecyclerView.setAdapter(mAdapter);

        layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);

    }



    public class AsyncHttpTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            InputStream inputStream = null;
            Integer result = 0;
            HttpURLConnection urlConnection = null;

            try {
                /* forming the URL object */
                URL url = new URL(params[0]);

                urlConnection = (HttpURLConnection) url.openConnection();

                /* for Get request */
                urlConnection.setRequestMethod("GET");

                int statusCode = urlConnection.getResponseCode();

                /* 200 represents HTTP OK */
                if (statusCode ==  200) {

                    BufferedReader r = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder response = new StringBuilder();
                    String line;
                    while ((line = r.readLine()) != null) {
                        response.append(line);
                    }

                    parseResult(response.toString());
                    result = 1; // Successful
                }else{
                    result = 0; //"Failed to fetch data!";
                }

            } catch (Exception e) {
                Log.d(getTag(), e.getLocalizedMessage());
            }

            return result; //"Failed to fetch data!";
        }

        @Override
        protected void onPostExecute(Integer result) {
            if (result == 1) {
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(getActivity(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void parseResult(String result) {
        try {
            JSONObject response = new JSONObject(result);
            JSONArray places = response.optJSONArray("places");

            /*Initialize array if null*/
            if (null == placesItemList) {
                placesItemList = new ArrayList<>();
            }

            for (int i = 0; i < places.length(); i++) {
                JSONObject place = places.optJSONObject(i);

                PlacesListItem item = new PlacesListItem();
                item.setTitle(place.optString("location"));
                item.setDesc(place.optString("description"));
                item.setWhat(place.optString("sight"));
                item.setImage(place.optString("url"));

                placesItemList.add(item);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
