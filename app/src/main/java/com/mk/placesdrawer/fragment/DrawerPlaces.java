package com.mk.placesdrawer.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mk.placesdrawer.R;
import com.mk.placesdrawer.activity.PlacesViewerActivity;
import com.mk.placesdrawer.utilities.JSONParser;
import com.mk.placesdrawer.utilities.Utils;
import com.mk.placesdrawer.activity.MainActivity;
import com.mk.placesdrawer.adapters.PlacesAdapter;
import com.mk.placesdrawer.model.PlacesItem;
import com.mk.placesdrawer.model.PlacesList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DrawerPlaces extends Fragment {

    private static RecyclerView mRecyclerView;
    private static Activity context;
    public static PlacesAdapter mAdapter;
    private static ViewGroup layout;
    private static boolean worked;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {

        context = getActivity();

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }
        try {
            layout = (ViewGroup) inflater.inflate(R.layout.drawer_places, container, false);
        } catch (InflateException e) {

        }

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        setupRecyclerView(false, 0);

        mRecyclerView.setVisibility(View.GONE);

        setupLayout(false);

        return layout;
    }


    private static void setupLayout(final boolean fromTask) {

        if (PlacesList.getPlacesList() != null && PlacesList.getPlacesList().size() > 0) {
            context.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter = new PlacesAdapter(context,
                            new PlacesAdapter.ClickListener() {
                                @Override
                                public void onClick(PlacesAdapter.PlacesViewHolder view,
                                                    int position, boolean longClick) {

                                        final Intent intent = new Intent(context, PlacesViewerActivity.class);

                                        intent.putExtra("item", PlacesList.getPlacesList().get(position));
                                        intent.putExtra("transitionName", ViewCompat.getTransitionName(view.image));

                                        Bitmap bitmap;

                                        if (view.image.getDrawable() != null) {
                                            bitmap = Utils.drawableToBitmap(view.image.getDrawable());
                                            try {
                                                String filename = "temp.png";
                                                FileOutputStream stream = context.openFileOutput(filename, Context.MODE_PRIVATE);
                                                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                                                stream.close();
                                                intent.putExtra("image", filename);
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }

                                            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(context, view.image, ViewCompat.getTransitionName(view.image));
                                            context.startActivity(intent, options.toBundle());


                                    }
                                }
                            });

                    mAdapter.setData(PlacesList.getPlacesList());
                    mRecyclerView.setAdapter(mAdapter);
                    mRecyclerView.setVisibility(View.VISIBLE);

                }
            });
        } else {
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



    // DownloadJSON AsyncTask
    public static class DownloadJSON extends AsyncTask<Void, Void, Void> {

        final MainActivity.PlacesListInterface wi;
        private final static ArrayList<PlacesItem> places = new ArrayList<>();

        private Context taskContext;

        private WeakReference<Activity> wrActivity;

        static long startTime, endTime;

        public DownloadJSON(MainActivity.PlacesListInterface wi, AppCompatActivity activity) {
            this.wi = wi;
            this.wrActivity = new WeakReference<Activity>(activity);
        }

        public DownloadJSON(MainActivity.PlacesListInterface wi, Context context) {
            this.wi = wi;
            this.taskContext = context;
        }

        @Override
        protected void onPreExecute() {
            startTime = System.currentTimeMillis();

            if (wrActivity != null) {
                final Activity a = wrActivity.get();
                if (a != null) {
                    this.taskContext = a.getApplicationContext();
                }
            }
        }

        @Override
        protected Void doInBackground(Void... params) {

            JSONObject json = JSONParser.getJSONFromURL(
                    Utils.getStringFromResources(taskContext,
                            R.string.json_file_url));

            if (json != null) {
                try {
                    // Locate the array name in JSON
                    JSONArray jsonarray = json.getJSONArray("places");

                    for (int i = 0; i < jsonarray.length(); i++) {
                        json = jsonarray.getJSONObject(i);
                        // Retrieve JSON Objects

                        places.add(new PlacesItem(
                                json.getString("location"),
                                json.getString("sight"),
                                json.getString("description"),
                                json.getString("imgPlacesUrl")));

                    }

                    PlacesList.createPlacesList(places);

                    worked = true;
                } catch (JSONException e) {
                    worked = false;
                }
            } else {
                worked = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void args) {

            endTime = System.currentTimeMillis();
            Utils.showLog("Places Task completed in: " +
                    String.valueOf((endTime - startTime) / 1000) + " secs.");

            if (layout != null) {
                setupLayout(true);
            }

                setupLayout(true);


            if (wi != null)
                wi.checkPlacesListCreation(worked);
        }
    }

    private static void setupRecyclerView(boolean updating, int newColumns) {

        mRecyclerView.setHasFixedSize(true);

        if (mRecyclerView.getVisibility() != View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }

    }

}
