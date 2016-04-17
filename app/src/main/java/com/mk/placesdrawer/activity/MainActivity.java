package com.mk.placesdrawer.activity;

import android.content.Context;
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
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
import com.mk.placesdrawer.fragment.DrawerAbout;
import com.mk.placesdrawer.fragment.DrawerPlaces;
import com.mk.placesdrawer.models.PlaceList;
import com.mk.placesdrawer.utilities.Animation;
import com.mk.placesdrawer.utilities.Dialogs;
import com.mk.placesdrawer.utilities.JSONParser;
import com.mk.placesdrawer.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Random;
import java.util.concurrent.ExecutionException;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon;

public class MainActivity extends AppCompatActivity {

    private static Drawer result;
    private static AppCompatActivity context;
    private static String drawerPlaces, drawerFavorite, drawerAbout, drawerFeedback, drawerLiveChat, drawerSettings, drawerWrong;
    private String[] urlHeaderArray;
    private Toolbar toolbar;
    private AccountHeader header;
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final DrawerPlaces drawer= new DrawerPlaces();

        context = this;
        drawer.loadWallsList(context);

        urlHeaderArray = getResources().getStringArray(R.array.headerUrl);

        drawerPlaces = getResources().getString(R.string.app_places);
        drawerFavorite = getResources().getString(R.string.app_favorite);
        drawerAbout = getResources().getString(R.string.app_about);
        drawerFeedback = getResources().getString(R.string.app_feedback);
        drawerLiveChat = getResources().getString(R.string.app_liveChat);
        drawerSettings = getResources().getString(R.string.app_settings);
        drawerWrong = getResources().getString(R.string.app_wrong);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setNavigationBarColor(getResources().getColor(R.color.navigationBar));
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DrawerBuilder().withActivity(this).build();
        final PrimaryDrawerItem itemPlaces = new PrimaryDrawerItem().withName(drawerPlaces).withIcon(Icon.gmd_place).withIdentifier(0);
        final PrimaryDrawerItem itemFavorite = new PrimaryDrawerItem().withName(drawerFavorite).withIcon(Icon.gmd_favorite).withIdentifier(1);
        final PrimaryDrawerItem itemAbout = new PrimaryDrawerItem().withName(drawerAbout).withIcon(Icon.gmd_person).withIdentifier(2);
        final PrimaryDrawerItem itemFeedback = new PrimaryDrawerItem().withName(drawerFeedback).withIcon(Icon.gmd_feedback).withIdentifier(3);
        final PrimaryDrawerItem itemLiveChat = new PrimaryDrawerItem().withName(drawerLiveChat).withIcon(Icon.gmd_chat).withIdentifier(4);
        final PrimaryDrawerItem itemSettings = new PrimaryDrawerItem().withName(drawerSettings).withIcon(Icon.gmd_settings).withIdentifier(5);

        header = new AccountHeaderBuilder().withActivity(this).withSelectionFirstLine("Places").withSelectionSecondLine("by Maximilian Keppeler").withHeightDp(300).build();

        grabHeaderImage();

        result = new DrawerBuilder().withAccountHeader(header).withActivity(this).withToolbar(toolbar).withSelectedItem(0)
                .addDrawerItems(
                        itemPlaces
                                .withBadgeStyle(
                                        new BadgeStyle()        // TODO, only Cyan, when current item is 1, otherwise no background color
                                                .withTextColor(Color.WHITE).withPadding(20)),
                        itemFavorite,
                        new DividerDrawerItem(),
                        itemAbout, itemFeedback, itemLiveChat, itemSettings
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {

                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                            Fragment fragment = null;

                            switch ((int) drawerItem.getIdentifier()) {


                                case 0: fragment = new DrawerPlaces();
                                    break;

                                case 1:  fragment = new DrawerAbout();
//                                    TODO - Favorite Fragment, filter out the json objects where int favorite is 1 (for favored)
                                    break;

                                case 2: fragment = new DrawerAbout();
                                    break;

                                case 3: fragment = new DrawerAbout();
                                    break;

                                case 4: fragment = new DrawerAbout();
                                    break;

                                case 5: fragment = new DrawerAbout();
                                    break;

                                default: fragment = new DrawerAbout();
                            }

                            fragment.setRetainInstance(true);
                            transaction.replace(R.id.container, fragment);
                            transaction.commit();

                            Log.d("DrawerItem", "Identifier: " + drawerItem.getIdentifier());
                            toolbar.setTitle(toolbarTitle((int) drawerItem.getIdentifier()));
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        if (result != null) {
            result.setSelection(0);
        }

    }

    public String toolbarTitle(int position) {
        switch (position) {
            case 0: return drawerPlaces;
            case 1: return drawerFavorite;
            case 2: return drawerAbout;
            case 3: return drawerFeedback;
            case 4: return drawerLiveChat;
            case 5: return drawerSettings;
            default: return drawerWrong;
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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int id = item.getItemId();
        return true;
    }

    public AccountHeader grabHeaderImage() {

        final ImageView cover = header.getHeaderBackgroundView();
        Random random = new Random();
        String randomURL = urlHeaderArray[random.nextInt(urlHeaderArray.length)];

        Glide.with(context)
                .load(randomURL)
                .asBitmap()
                .override(1512, 1288)
                .centerCrop()
                .into(new BitmapImageViewTarget(cover) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(getResources(), resource)});
                        cover.setImageDrawable(td);
                        td.startTransition(400);
                    }
                });

        if (this.getResources().getBoolean(R.bool.zoomDrawerHeader)) Animation.zoomInAndOut(context, cover);

        return header;
    }

    public interface PlacesListInterface {
        void checkPlacesListCreation(boolean result);
    }

}
