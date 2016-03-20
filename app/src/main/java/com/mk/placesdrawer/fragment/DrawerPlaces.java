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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.activity.PlacesViewerActivity;
import com.mk.placesdrawer.utilities.JSONParser;
import com.mk.placesdrawer.utilities.Utils;
import com.mk.placesdrawer.activity.MainActivity;
import com.mk.placesdrawer.adapters.PlacesAdapter;
import com.mk.placesdrawer.models.PlacesItem;
import com.mk.placesdrawer.models.PlacesList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class DrawerPlaces extends Fragment {

    // TODO - content only loads when there's a mobile internet connection. Not with wifi. Whyever.

    //Declare Layout, Adapter, RecyclerView in order to
    private static ViewGroup layout;
    private static RecyclerView mRecyclerView;
    public static PlacesAdapter mAdapter;

    //Is a layout manager necessary?
    //private static RecyclerView.LayoutManager layoutManager;
    //private static LinearLayoutManager layoutManager;

    private static Activity context;

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
            // Do nothing
        }

        mRecyclerView = (RecyclerView) layout.findViewById(R.id.placecRecyclerView);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));

        mRecyclerView.setHasFixedSize(true);

        if (mRecyclerView.getVisibility() != View.VISIBLE) {
            mRecyclerView.setVisibility(View.VISIBLE);
        }

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

                                    if (longClick) {

                                        PlacesItem wallItem = PlacesList.getPlacesList().get(position);

                                        Glide.with(context)
                                                .load(wallItem.getImgPlaceUrl())
                                                .asBitmap()
                                                .into(new SimpleTarget<Bitmap>() {
                                                    @Override
                                                    public void onResourceReady(final Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                                        if (resource != null) {
                                                        }
                                                    }
                                                });
                                    } else {

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
                                }
                            });

                    mAdapter.setData(PlacesList.getPlacesList());
                    mRecyclerView.setAdapter(mAdapter);

                    if (Utils.hasNetwork(context)) {
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }

                }
            });
        } else {
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


    public static void reloadPlaces(Activity context) {
        mRecyclerView.setVisibility(View.GONE);
        if (Utils.hasNetwork(context)) {
            Utils.showSimpleSnackbar(context, layout,
                    context.getResources().getString(R.string.reload_places));
        } else {
            Utils.showSimpleSnackbar(context, layout,
                    context.getResources().getString(R.string.no_internet));
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

        // I understood the principle of this method
        @Override
        protected Void doInBackground(Void... params) {

            // Create new JSONObject (json) by getting the current context and the json raw file link
            JSONObject json = JSONParser.getJSONFromURL(Utils.getStringFromResources(taskContext, R.string.json_file_url));

            // When json is null, find the array....
            if (json != null) {
                try {

                    // ...with the name "places"
                    JSONArray jsonarray = json.getJSONArray("places");

                    // loop, grabbing all json objects out of the "places"
                    for (int i = 0; i < jsonarray.length(); i++) {

                        // Retrieve JSON Objects
                        json = jsonarray.getJSONObject(i);

                        // ArrayList receives per object/ per loop the following strings/ values from the json object
                        places.add(new PlacesItem(
                                json.getString("location"),
                              //  json.getString("description"),
                                json.getString("sight"),
                                json.getString("url")
                                                 )
                        );

                    }

                    // Created new ArrayList out for all the Array Lists for all strings / values of each object
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
            Utils.showLog("Walls Task completed in: " +
                    String.valueOf((endTime - startTime) / 1000) + " secs.");

            if (layout != null) {
                setupLayout(true);
            }

            if (wi != null)
                wi.checkPlacesListCreation(worked);
        }
    }

}
