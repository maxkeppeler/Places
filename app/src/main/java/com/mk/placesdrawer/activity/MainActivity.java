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
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
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
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.adapters.PlacesAdapter;
import com.mk.placesdrawer.fragment.DrawerHome;
import com.mk.placesdrawer.fragment.DrawerPlaces;
import com.mk.placesdrawer.models.PlacesItem;
import com.mk.placesdrawer.models.PlacesList;
import com.mk.placesdrawer.utilities.ApplicationBase;
import com.mk.placesdrawer.utilities.Preferences;

import java.util.Random;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.*;

public class MainActivity extends AppCompatActivity {

    private static AppCompatActivity context;
    private static String drawer1, drawer2, drawer3, drawer4;
    private static String drawerWrong;
    private int currentDrawerItem;
    private String[] urlHeaderArray;
    private static  Drawer result;
    private static Preferences mPrefs;

    public boolean placesPicker = true;

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

        urlHeaderArray = getResources().getStringArray(R.array.headerUrlWiFi);

        drawer1 = getResources().getString(R.string.app_places);
        drawer2 = getResources().getString(R.string.app_submit);
        drawer3 = getResources().getString(R.string.app_about);
        drawer4 = getResources().getString(R.string.app_settings);
        drawerWrong = getResources().getString(R.string.app_wrong);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = this.getWindow();
            window.setNavigationBarColor(getResources().getColor(R.color.navigationBar));
        }

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DrawerBuilder().withActivity(this).build();

        // TODO find better, better matching, icons for the categories

        final PrimaryDrawerItem item1 = new PrimaryDrawerItem()
                .withName(drawer1)
                .withIcon(Icon.gmd_home)
                .withIdentifier(1);

        final PrimaryDrawerItem item2 = new PrimaryDrawerItem()
                .withName(drawer2)
                .withIcon(Icon.gmd_local_post_office)
                .withIdentifier(2);

        final PrimaryDrawerItem item3 = new PrimaryDrawerItem()
                .withName(drawer3)
                .withIcon(Icon.gmd_account)
                .withIdentifier(3);

        final PrimaryDrawerItem item4 = new PrimaryDrawerItem()
                .withName(drawer4)
                .withIcon(Icon.gmd_settings)
                .withIdentifier(4);

        final AccountHeader header;

        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withSelectionSecondLine("by Maximilian Keppeler")
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {

                        return false;
                    }
                })
                .build();

        changeHeader(header);

        result = new DrawerBuilder()
                .withAccountHeader(header)
                .withActivity(this)
                .withToolbar(toolbar)
                .withSelectedItem(1)  // Default selected item
                .addDrawerItems(
                        item1.withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_cyan_500).withCornersDp(100000).withPadding(20)),
                        item2.withBadgeStyle(new BadgeStyle().withTextColor(Color.WHITE).withColorRes(R.color.md_cyan_500).withCornersDp(100000).withPadding(20)),
                        //item2,
                        new DividerDrawerItem(),
                        item3,
                        item4
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Intent intent = null;

                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            Fragment fragment;

                            switch ((int) drawerItem.getIdentifier()) {

                                case 1: fragment = new DrawerPlaces();
                                    break;

                                case 2: fragment = new DrawerHome();
                                    break;

                                case 3: fragment = new DrawerHome();
                                    break;

                                case 4:
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

        // TODO get actual correct PlacesList SIZE goes wrong. It keeps beeing 0 or the amount doubles itself after every reload
        // TODO need help, tried many methods and ideas, but nothing worked
        // result.updateBadge(1, new StringHolder("    " + PlacesList.getPlacesList().size() + "    "));
    }

    public String toolbarText(int fragmentPosition) {
        switch (fragmentPosition) {
            case 1:  return drawer1;
            case 2:  return drawer2;
            case 3:  return drawer3;
            case 4:  return drawer4;
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

    public interface PlacesListInterface {
        void checkPlacesListCreation(boolean result);
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
                    .centerCrop() //.error(R.drawable.header).placeholder(R.drawable.header)
                    .into(new BitmapImageViewTarget(cover) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(getResources(), resource)});
                            cover.setImageDrawable(td);
                            td.startTransition(1050);
                        }

                    });

        return headerResult;
    }

}
