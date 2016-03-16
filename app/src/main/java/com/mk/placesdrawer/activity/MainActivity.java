package com.mk.placesdrawer.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.fragment.DrawerHome;
import com.mk.placesdrawer.fragment.DrawerPlaces;
import com.mk.placesdrawer.model.PlacesList;

public class MainActivity extends AppCompatActivity {

    private static AppCompatActivity context;

    FragmentManager manager = getSupportFragmentManager();
    FragmentTransaction transaction = manager.beginTransaction();
    Fragment fragment;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        context = this;

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .build();

        new DrawerBuilder().withActivity(this).build();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().
                withName(R.string.drawer_item_home).
                withIcon(GoogleMaterial.Icon.gmd_home).
                withIdentifier(1);

        PrimaryDrawerItem item2 = new PrimaryDrawerItem().
                withName(R.string.drawer_item_submit).
                withIcon(GoogleMaterial.Icon.gmd_local_post_office).
                withIdentifier(2);

        PrimaryDrawerItem item3 = new PrimaryDrawerItem().
                withName(R.string.drawer_item_about).
                withIcon(GoogleMaterial.Icon.gmd_account).
                withIdentifier(3);

        PrimaryDrawerItem item4 = new PrimaryDrawerItem().
                withName(R.string.drawer_item_settings).
                withIcon(GoogleMaterial.Icon.gmd_settings).
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

                            if (drawerItem.getIdentifier() == 1) {
                                fragment = new DrawerPlaces();
                            }

                            if (drawerItem.getIdentifier() == 2) {
                                fragment = new DrawerPlaces();
                            }

                            if (drawerItem.getIdentifier() == 3) {
                                fragment = new DrawerPlaces();
                            }

                            if (drawerItem.getIdentifier() == 4) {
                                fragment = new DrawerPlaces();
                            }

                            else {
                                fragment = new DrawerPlaces();
                            }

                            transaction.replace(R.id.container, fragment);
                            transaction.commit();

                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }

                        return false;
                    }
                })
                .build();

        //Default selection at the start of the app
        result.setSelection(1);
    }

    public interface PlacesListInterface {

        void checkPlacesListCreation(boolean result);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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




}
