package com.mk.placesdrawer.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.fragment.DrawerHome;
import com.mk.placesdrawer.fragment.DrawerPlaces;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.*;

public class MainActivity extends AppCompatActivity {

    private static AppCompatActivity context;
    private static String drawer1, drawer2, drawer3, drawer4;
    private static String drawerWrong;

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

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .build();

        new DrawerBuilder().withActivity(this).build();

        // TODO find better, better matching, icons for the categories

        final PrimaryDrawerItem item1 = new PrimaryDrawerItem().
                withName(drawer1).
                withIcon(Icon.gmd_home).
                withIdentifier(1);

        final PrimaryDrawerItem item2 = new PrimaryDrawerItem().
                withName(drawer2).
                withIcon(Icon.gmd_local_post_office).
                withIdentifier(2);

        final PrimaryDrawerItem item3 = new PrimaryDrawerItem().
                withName(drawer3).
                withIcon(Icon.gmd_account).
                withIdentifier(3);

        final PrimaryDrawerItem item4 = new PrimaryDrawerItem().
                withName(drawer4).
                withIcon(Icon.gmd_settings).
                withIdentifier(4);

        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        item2,
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

                            toolbar.setTitle(toolbarText((int) drawerItem.getIdentifier()));

                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .build();

        // Default selection at the start of the app
        if (result != null) {
            result.setSelection(1);
        }
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
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.reload) {
            DrawerPlaces.reloadPlaces(context);
            loadPlacesList();
            return true;
        }



        return super.onOptionsItemSelected(item);
    }

    //TODO - method for a later refresh option button in the toolbar
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


}
