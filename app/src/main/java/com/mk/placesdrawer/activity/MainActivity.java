package com.mk.placesdrawer.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.fragment.DrawerHome;
import com.mk.placesdrawer.fragment.DrawerPlaces;
import com.mk.placesdrawer.utilities.Animations;

import java.util.Random;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon;

public class MainActivity extends AppCompatActivity {

    public static Drawer result;
    private static AppCompatActivity context;
    private static String drawerPlaces, drawerSubmit, drawerFavorite;
    private static String drawerAbout, drawerFeedback, drawerSettings;
    private static String drawerWrong;
    private int currentDrawerItem;
    private String[] urlHeaderArray;

    // TODO Differnt Toolbar and Status Bar color depending on the current fragment.
    // Places - Dark Grey Toolbar and Status Bar
    // Submit - Red
    // Settings - Blue Grey
    // Just an example, and just an idea... I will think about it

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = this;

        urlHeaderArray = getResources().getStringArray(R.array.headerUrl);

        drawerPlaces = getResources().getString(R.string.app_places);
        drawerFavorite = getResources().getString(R.string.app_favorite);
        drawerSubmit = getResources().getString(R.string.app_submit);
        drawerAbout = getResources().getString(R.string.app_about);
        drawerFeedback = getResources().getString(R.string.app_Feedback);
        drawerSettings = getResources().getString(R.string.app_settings);
        drawerWrong = getResources().getString(R.string.app_wrong);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setNavigationBarColor(getResources().getColor(R.color.navigationBar));
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DrawerBuilder().withActivity(this).build();

        // TODO find better, better matching, icons for the categories

        final PrimaryDrawerItem itemPlaces = new PrimaryDrawerItem()
                .withName(drawerPlaces).withIcon(Icon.gmd_home).withIdentifier(1);

        final PrimaryDrawerItem itemFavorite = new PrimaryDrawerItem()
                .withName(drawerFavorite).withIcon(Icon.gmd_favorite).withIdentifier(2);

        final PrimaryDrawerItem itemSubmit = new PrimaryDrawerItem()
                .withName(drawerSubmit).withIcon(Icon.gmd_local_post_office).withIdentifier(3);

        final PrimaryDrawerItem itemAbout = new PrimaryDrawerItem()
                .withName(drawerAbout).withIcon(Icon.gmd_account).withIdentifier(4);

        final PrimaryDrawerItem itemFeedback = new PrimaryDrawerItem()
                .withName(drawerFeedback).withIcon(Icon.gmd_chart_donut).withIdentifier(5);

        final PrimaryDrawerItem itemSettings = new PrimaryDrawerItem()
                .withName(drawerSettings).withIcon(Icon.gmd_settings).withIdentifier(6);

        final AccountHeader header;

        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionSecondLine("by Maximilian Keppeler")
                .build();

        changeHeader(header);

        // TODO (Maybe) add MultiDrawer and replace it with the current one

        result = new DrawerBuilder()
                .withAccountHeader(header)
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(1)  // Default selected item
                .addDrawerItems(
                        itemPlaces
                                .withBadgeStyle(
                                        new BadgeStyle()        // TODO, only Cyan, when current item is 1, otherwise no background color
                                                .withTextColor(Color.WHITE)
                                                .withColorRes(R.color.colorAccent)
                                                .withCornersDp(100000).withPadding(20)),
                        itemFavorite.
                                withBadgeStyle(
                                        new BadgeStyle()        // TODO, only Cyan, when current item is 2, otherwise no background color
                                                .withTextColor(Color.WHITE)
                                                .withColorRes(R.color.colorAccent)
                                                .withCornersDp(100000).withPadding(20)),
                        itemSubmit,
                        new DividerDrawerItem(),
                        itemAbout,
                        itemFeedback,
                        itemSettings
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Intent intent = null;

                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            Fragment fragment = null;

                            switch ((int) drawerItem.getIdentifier()) {

                                case 1:
                                    fragment = new DrawerPlaces();
                                    break;

                                case 2:
                                    fragment = new DrawerHome();
                                    break;

                                case 3:
                                    fragment = new DrawerHome();
                                    break;

                                case 4:
                                    fragment = new DrawerHome();
                                    break;

                                case 5:
                                    fragment = new DrawerHome();
                                    break;

                                case 6:
                                    fragment = new DrawerHome();
                                    break;

                                default:
                                    fragment = new DrawerPlaces();
                            }

                            transaction.replace(R.id.container, fragment);
                            transaction.commit();

                            currentDrawerItem = (int) drawerItem.getIdentifier();
                            toolbar.setTitle(toolbarText(currentDrawerItem));

                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }
                        return false;
                    }
                })
                .build();

        if (result != null) {
            result.setSelection(1);
        }

        result.updateBadge(1, new StringHolder("    " + "212" + "    "));
        result.updateBadge(2, new StringHolder("    " + "35" + "    "));

        // TODO HELP NEEDED get actual correct PlacesList SIZE goes wrong. It keeps being 0 or the amount doubles itself after every reload. I tried many methods and ideas, but nothing worked
        // result.updateBadge(1, new StringHolder("    " + PlacesList.getPlacesList().size() + "    "));

    }

    public String toolbarText(int fragmentPosition) {
        switch (fragmentPosition) {
            case 1:
                return drawerPlaces;
            case 2:
                return drawerFavorite;
            case 3:
                return drawerSubmit;
            case 4:
                return drawerAbout;
            case 5:
                return drawerFeedback;
            case 6:
                return drawerSettings;
            default:
                return drawerWrong;
        }
    }

    @Override
    public void onBackPressed() {
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        } else if (result != null && result.getCurrentSelection() != 1) {
            result.setSelection(1);
        } else if (result != null) {
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();

        switch (id) {

            case R.id.filter:
                break;

            case R.id.sort:
                break;

            case R.id.reload:
                DrawerPlaces.reloadPlaces(context);
                loadPlacesList();
                break;

            case R.id.changelog:
                break;
        }
        return true;
    }

    private void loadPlacesList() {
        new DrawerPlaces.DownloadJSON(new PlacesListInterface() {
            @Override
            public void checkPlacesListCreation(boolean result) {
                if (DrawerPlaces.mAdapter != null) {
                    DrawerPlaces.mAdapter.notifyDataSetChanged();
                }
            }
        }, context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public AccountHeader changeHeader(AccountHeader headerResult) {

        final ImageView cover = headerResult.getHeaderBackgroundView();

        Random r = new Random();
        String rnb = urlHeaderArray[r.nextInt(urlHeaderArray.length)];

        Glide.with(context)
                .load(rnb)
                .asBitmap()
                .override(912, 688)
                .centerCrop()
                .into(new BitmapImageViewTarget(cover) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(getResources(), resource)});
                        cover.setImageDrawable(td);
                        td.startTransition(50);
                    }

                });

        if (this.getResources().getBoolean(R.bool.zoomHeader)) {
            Animations.zoomInAndOut(context, cover);
        }

        return headerResult;
    }

    public interface PlacesListInterface {
        void checkPlacesListCreation(boolean result);
    }

}
