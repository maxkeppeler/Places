package com.mk.placesdrawer.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.activity.MainActivity;
import com.mk.placesdrawer.activity.PlaceDetailActivity;
import com.mk.placesdrawer.adapters.PlaceAdapter;
import com.mk.placesdrawer.models.Place;
import com.mk.placesdrawer.models.PlaceList;
import com.mk.placesdrawer.utilities.Dialogs;
import com.mk.placesdrawer.utilities.JSONParser;
import com.mk.placesdrawer.utilities.Utils;

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
    private String keyWord = "All";


    private static int columns = 1;

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_places, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
//        searchView.setOnQueryTextListener(context);

        int id = item.getItemId();

        switch (id) {
            case R.id.filter:
                filterDialog(context);
                break;
            case R.id.column:
                Dialogs.columnsDialog(context);
                break;
            case R.id.changelog:
                Dialogs.showChangelog(context);
                break;


        }
        return true;
    }

    public void filterDialog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.filterTitle)
                .items(R.array.filterContentArray)
                .negativeText("Reset Filter")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        setKeyWord("All");
                    }
                })
                .backgroundColor(context.getResources().getColor(R.color.dialogs))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {
                        if (i == 0) sightDialog(context);
                        if (i == 1) countryDialog(context);

                    }
                })
                .show();
    }

    public String sightDialog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.sightTitle)
                .items(R.array.sightContentArray)
                .backgroundColor(context.getResources().getColor(R.color.dialogs))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {

                        switch (i) {
                            case 0: setKeyWord(text.toString()); break;
                            case 1: setKeyWord(text.toString()); break;
                            case 2: setKeyWord(text.toString()); break;
                            case 3: setKeyWord(text.toString()); break;
                            case 4: setKeyWord(text.toString()); break;
                            case 5: setKeyWord(text.toString()); break;
                            case 6: setKeyWord(text.toString()); break;
                        }
                    }
                })
                .show();
        return "";
    }

    public String countryDialog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.filterTitle)
                .items(R.array.filterContentArray)
                .backgroundColor(context.getResources().getColor(R.color.dialogs))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {

//                        TODO on selection, filter the current placesList and return just the objects where the category (country) is correct.

                    }
                })
                .show();

        return "";
    }

    public void setKeyWord(String string) {
        this.keyWord = string;
        loadWallsList(context);
    }

    public void loadWallsList(Context context) {
        PlaceList.clearList();

        new DrawerPlaces.DownloadJSON(new MainActivity.PlacesListInterface() {
            @Override
            public void checkPlacesListCreation(boolean result) {
                if (DrawerPlaces.mAdapter != null) {
                    DrawerPlaces.mAdapter.notifyDataSetChanged();
                }
            }
        }, context, keyWord).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }

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
