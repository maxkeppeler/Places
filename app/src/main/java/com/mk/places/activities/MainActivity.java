package com.mk.places.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.BadgeStyle;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mk.places.R;
import com.mk.places.adapters.PagerAdapter;
import com.mk.places.fragment.DrawerAbout;
import com.mk.places.fragment.DrawerEmpty;
import com.mk.places.fragment.DrawerUpload;
import com.mk.places.fragment.TabFragment;
import com.mk.places.utilities.Anim;
import com.mk.places.utilities.Dialogs;
import com.mk.places.utilities.FilterLogic;
import com.mk.places.utilities.Preferences;
import com.mk.places.utilities.Utils;

import java.util.Random;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon;

public class MainActivity extends AppCompatActivity {

    public static Drawer drawer = null;
    public static Drawer drawerFilter = null;
    private static AppCompatActivity context;
    private static String drawerPlaces, drawerUpload, drawerAbout, drawerSettings, drawerWrong;
    private Toolbar toolbar;
    private AccountHeader drawerHeader;
    private String[] drawerHeaderURLS;
    private int drawerIndex;
    private TabLayout placesTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = this;

//        TabPlaces.loadPlacesList(context);

//        placesTabLayout = (TabLayout) findViewById(R.id.tabLayout);
//        viewPager = (ViewPager) findViewById(R.id.pager);


//        placesTabLayout.addTab(placesTabLayout.newTab(), 0);
//        placesTabLayout.addTab(placesTabLayout.newTab(), 1);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        TODO: Check if app was updated and had then the first start

        Preferences pref = new Preferences(context);

        if (pref.getFirstStart()) {
            Dialogs.showChangelog(context);
            pref.setFirstStart(false);
        }

        drawerHeaderURLS = getResources().getStringArray(R.array.headerUrl);

        drawerPlaces = getResources().getString(R.string.app_places);
        drawerUpload = getResources().getString(R.string.app_uploads);
        drawerAbout = getResources().getString(R.string.app_about);
        drawerSettings = getResources().getString(R.string.app_settings);
        drawerWrong = getResources().getString(R.string.app_wrong);

        final String

                sight = getResources().getString(R.string.sight),
                sightCity = getResources().getString(R.string.sight_city),
                sightCountry = getResources().getString(R.string.sight_country),
                sightNationalPark = getResources().getString(R.string.sight_national_park),
                sightPark = getResources().getString(R.string.sight_park),

                continent = getResources().getString(R.string.continent),
                continentAfrica = getResources().getString(R.string.continentAfrica),
                continentAntarctica = getResources().getString(R.string.continentAntarctica),
                continentAsia = getResources().getString(R.string.continentAsia),
                continentAustralia = getResources().getString(R.string.continentAustralia),
                continentEurope = getResources().getString(R.string.continentEurope),
                continentNorthAmerica = getResources().getString(R.string.continentNorthAmerica),
                continentSouthAmerica = getResources().getString(R.string.continentSouthAmerica);

        drawerHeader = new AccountHeaderBuilder().withActivity(this)
                .withSelectionFirstLine("Places").withSelectionSecondLine("on our Earth")
                .withTypeface(Utils.customTypeface(context, 2)).withHeightDp(380).build();
        drawerHeader();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(drawerHeader)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(drawerPlaces).withIcon(Icon.gmd_terrain).withIdentifier(0).withBadgeStyle(new BadgeStyle()),
                        new PrimaryDrawerItem().withName(drawerUpload).withIcon(Icon.gmd_cloud_upload).withIdentifier(1),
                        new SectionDrawerItem().withName("Various"),
                        new SecondaryDrawerItem().withName(drawerAbout).withIcon(Icon.gmd_person).withIdentifier(2),
                        new SecondaryDrawerItem().withName(drawerSettings).withIcon(Icon.gmd_settings).withIdentifier(3).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable) {

                                FragmentManager manager = getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                                Fragment fragment = null;
                                DrawerLayout drawerLayout = drawer.getDrawerLayout();

                                Intent intent = null;

                                switch ((int) drawerItem.getIdentifier()) {

                                    case 0:
                                        fragment = new TabFragment();
//
//                                        final android.support.v4.view.PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), 2);
//                                        viewPager.setAdapter(adapter);
//
//                                        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(placesTabLayout));
//                                        placesTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//                                            @Override
//                                            public void onTabSelected(TabLayout.Tab tab) {
//                                                viewPager.setCurrentItem(tab.getPosition());
//                                                Log.d("MainActivity", "ViewPager - position: " + tab.getPosition() + " TabLayout - position: " + placesTabLayout.getSelectedTabPosition());
//                                            }
//
//                                            @Override
//                                            public void onTabUnselected(TabLayout.Tab tab) {
//                                            }
//
//                                            @Override
//                                            public void onTabReselected(TabLayout.Tab tab) {
//                                            }
//                                        });

                                        if (drawerFilter != null)
                                            drawerFilter.setSelection(0);

//                                        placesTabLayout.setVisibility(View.VISIBLE);
//                                        viewPager.setVisibility(View.VISIBLE);

                                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED, Gravity.RIGHT);
                                        break;

                                    case 1:
                                        fragment = new DrawerUpload();
                                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                                        break;

                                    case 2:
                                        fragment = new DrawerAbout();
                                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                                        break;

                                    case 3:
                                        fragment = null;
                                        intent = new Intent(context, Settings.class);
                                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED, Gravity.RIGHT);
                                        break;


                                    default:
                                        fragment = new DrawerEmpty();
                                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED, Gravity.RIGHT);
                                }

                                if (fragment != null) {
                                    fragment.setRetainInstance(true);
                                    transaction.replace(R.id.container, fragment).commit();
                                    drawerIndex = (int) drawerItem.getIdentifier();
                                    toolbar.setTitle(toolbarTitle((int) drawerItem.getIdentifier()));
                                }

                                else if (fragment == null && intent != null) {
                                    startActivity(intent);
                                }


                            }
                        }

                        return false;
                    }
                })
                .build();

        if (drawer != null) drawer.setSelection(0);

        drawerFilter = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(

                        new SectionDrawerItem().withName(sight).withDivider(false),
                        new SecondaryDrawerItem().withName(sightCity).withLevel(2).withIcon(Icon.gmd_location_city).withIdentifier(101),
                        new SecondaryDrawerItem().withName(sightCountry).withLevel(2).withIcon(Icon.gmd_terrain).withIdentifier(102),
                        new SecondaryDrawerItem().withName(sightNationalPark).withLevel(2).withIcon(Icon.gmd_nature).withIdentifier(103),
                        new SecondaryDrawerItem().withName(sightPark).withLevel(2).withIcon(Icon.gmd_nature_people).withIdentifier(104),

                        new SectionDrawerItem().withName(continent).withDivider(false),
                        new SecondaryDrawerItem().withName(continentAfrica).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(201),
                        new SecondaryDrawerItem().withName(continentAntarctica).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(202),
                        new SecondaryDrawerItem().withName(continentAsia).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(203),
                        new SecondaryDrawerItem().withName(continentAustralia).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(204),
                        new SecondaryDrawerItem().withName(continentEurope).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(205),
                        new SecondaryDrawerItem().withName(continentNorthAmerica).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(206),
                        new SecondaryDrawerItem().withName(continentSouthAmerica).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(207)

                ).addStickyDrawerItems(
                        new PrimaryDrawerItem().withName("Reset Filter").withSelectable(false).withIcon(Icon.gmd_clear_all).withIdentifier(999)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {

                            switch ((int) drawerItem.getIdentifier()) {

                                case 101:
                                    FilterLogic.filterList(drawerIndex, sightCity);
                                    break;
                                case 102:
                                    FilterLogic.filterList(drawerIndex, sightCountry);
                                    break;
                                case 103:
                                    FilterLogic.filterList(drawerIndex, sightNationalPark);
                                    break;
                                case 104:
                                    FilterLogic.filterList(drawerIndex, sightPark);
                                    break;

                                case 201:
                                    FilterLogic.filterList(drawerIndex, continentAfrica);
                                    break;
                                case 202:
                                    FilterLogic.filterList(drawerIndex, continentAntarctica);
                                    break;
                                case 203:
                                    FilterLogic.filterList(drawerIndex, continentAsia);
                                    break;
                                case 204:
                                    FilterLogic.filterList(drawerIndex, continentAustralia);
                                    break;
                                case 205:
                                    FilterLogic.filterList(drawerIndex, continentEurope);
                                    break;
                                case 206:
                                    FilterLogic.filterList(drawerIndex, continentNorthAmerica);
                                    break;
                                case 207:
                                    FilterLogic.filterList(drawerIndex, continentSouthAmerica);
                                    break;

                                case 999:
                                    FilterLogic.filterList(drawerIndex, "All");
                                    drawerFilter.setSelection(0);
                                    break;

                                default:
                                    FilterLogic.filterList(drawerIndex, "All");
                                    drawerFilter.setSelection(0);
                                    break;
                            }
                        }
                        return false;
                    }
                })
                .withDrawerGravity(Gravity.END)
                .append(drawer);

        if (drawerFilter != null) drawerFilter.setSelection(0);

    }

    public String toolbarTitle(int index) {

        switch (index) {
            case 0:
                return drawerPlaces;
            case 1:
                return drawerUpload;
            case 2:
                return drawerAbout;
            case 3:
                return drawerSettings;
            default:
                return drawerWrong;
        }

    }

    @Override
    public void onBackPressed() {

        if (drawer != null && drawer.isDrawerOpen())
            drawer.closeDrawer();

        else if (drawer != null && drawer.getCurrentSelection() != 0)
            drawer.setSelection(0);

        else if (drawer != null)
            super.onBackPressed();

        else super.onBackPressed();

    }

    public AccountHeader drawerHeader() {

        final ImageView cover = drawerHeader.getHeaderBackgroundView();
        Random random = new Random();
        String randomURL = drawerHeaderURLS[random.nextInt(drawerHeaderURLS.length)];

        Glide.with(context)
                .load(randomURL)
                .override(1512, 1288)
                .centerCrop()
                .into(cover);

        if (this.getResources().getBoolean(R.bool.zoomDrawerHeader))
            Anim.zoomInAndOut(context, cover);

        return drawerHeader;
    }

    public interface PlacesListInterface {
        void checkPlacesListCreation(boolean result);
    }


}
