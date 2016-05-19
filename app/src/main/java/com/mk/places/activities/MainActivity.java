package com.mk.places.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import com.mk.places.fragment.DrawerAbout;
import com.mk.places.fragment.DrawerEmpty;
import com.mk.places.fragment.DrawerPlacesTabs;
import com.mk.places.fragment.DrawerSubmit;
import com.mk.places.fragment.FragmentPlaces;
import com.mk.places.utilities.Dialogs;
import com.mk.places.utilities.FilterLogic;
import com.mk.places.utilities.Preferences;
import com.mk.places.utilities.Utils;

import java.util.Random;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon;

public class MainActivity extends AppCompatActivity {

    public static final String
            SIGHT = "Sight",
            SIGHT_CITY = "City",
            SIGHT_COUNTRY = "Country",
            SIGHT_NATIONAL_PARK = "National Park",
            SIGHT_PARK = "Park",
            SIGHT_BEACH = "Beach",
            SIGHT_LAKE = "Lake",
            SIGHT_DESERT = "Desert",
            SIGHT_GEYSER = "Geyser",
            SIGHT_LANDFORM = "Landform";

    private final String
            CONTINENT = "Continent",
            CONTINENT_AFRICA = "Africa",
            CONTINENT_ANTARCTICA = "Antarctica",
            CONTINENT_ASIA = "Asia",
            CONTINENT_AUSTRALIA = "Australia",
            CONTINENT_EUROPE = "Europe",
            CONTINENT_NORTH_AMERICA = "North America",
            CONTINENT_SOUTH_AMERICA = "South America";

    public static Drawer drawer = null;
    public static Drawer drawerFilter = null;
    public static TabLayout tabLayout;
    private Activity context;
    private static String drawerPlaces, drawerNature, drawerHall, drawerUpload, drawerAbout, drawerSettings, drawerWrong;
    private static TabLayout.Tab
            discover,
            bookmarks;
    private Toolbar toolbar;
    private AccountHeader drawerHeader;
    private String[] drawerHeaderURLS;
    private int drawerIndex;

    public static void updateTabTexts(int valueDiscover, int valueBookmarks) {
        discover.setText("Discover" + " (" + valueDiscover + ")");
        bookmarks.setText("Bookmarks" + " (" + valueBookmarks + ")");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = this;

        FragmentPlaces.loadPlacesList(context);

        Preferences pref = new Preferences(context);

//        TODO: If user has no WIFI, ask if we can use the mobile internet. Inform user that much data will be loaded. Afterwards load places list

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);

        discover = tabLayout.newTab().setText("Discover");
        bookmarks = tabLayout.newTab().setText("Bookmarks");

        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context, R.color.white));

//        TODO: Check if app was updated and had then the first start

        if (pref.getFirstStart()) {
            Dialogs.showChangelog(context);
            pref.setFirstStart(false);
        }

        drawerHeaderURLS = getResources().getStringArray(R.array.headerUrl);

        drawerPlaces = getResources().getString(R.string.app_places);
        drawerNature = getResources().getString(R.string.app_nature);
        drawerHall = getResources().getString(R.string.app_hall);
        drawerUpload = getResources().getString(R.string.app_uploads);
        drawerAbout = getResources().getString(R.string.app_about);
        drawerSettings = getResources().getString(R.string.app_settings);
        drawerWrong = getResources().getString(R.string.app_wrong);

        drawerHeader = new AccountHeaderBuilder().withActivity(this)
                .withSelectionFirstLine("Beautiful Places").withSelectionSecondLine("on our Earth")
                .withTypeface(Utils.customTypeface(context, 1))
                .withHeightDp(330)
                .build();
        drawerHeader();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(drawerHeader)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(drawerPlaces).withIcon(Icon.gmd_terrain).withIdentifier(0).withBadgeStyle(new BadgeStyle()),
                        new PrimaryDrawerItem().withName(drawerNature).withIcon(Icon.gmd_public).withIdentifier(1),
                        new PrimaryDrawerItem().withName(drawerHall).withIcon(Icon.gmd_card_giftcard).withIdentifier(2),
                        new PrimaryDrawerItem().withName(drawerUpload).withIcon(Icon.gmd_cloud_upload).withIdentifier(3),
                        new SectionDrawerItem().withName("Various"),
                        new SecondaryDrawerItem().withName(drawerAbout).withIcon(Icon.gmd_person).withIdentifier(4),
                        new SecondaryDrawerItem().withName(drawerSettings).withIcon(Icon.gmd_settings).withIdentifier(5).withSelectable(false)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable) {

                                FragmentManager manager = getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                Fragment fragment = null;
                                Intent intent = null;
                                drawerIndex = (int) drawerItem.getIdentifier();

                                switch ((int) drawerItem.getIdentifier()) {

                                    case 0: fragment = new DrawerPlacesTabs();
                                        break;

                                    case 1: fragment = new DrawerEmpty();
                                        break;

                                    case 2: fragment = new DrawerEmpty();
                                        break;

                                    case 3: fragment = new DrawerSubmit();
                                        break;

                                    case 4: fragment = new DrawerAbout();
                                        break;

                                    case 5: intent = new Intent(context, Settings.class);
                                        break;

                                    default: fragment = new FragmentPlaces();
                                }

                                if (fragment != null) {
                                    fragment.setRetainInstance(true);
                                    transaction.replace(R.id.container, fragment);
                                    transaction.commit();
                                    toolbar.setTitle(toolbarTitle(drawerIndex));

                                } else if (intent != null && fragment == null) {
                                    startActivity(intent);
                                    drawer.setSelection(0);
                                }

                                DrawerLayout drawerLayout = drawer.getDrawerLayout();

                                if (drawerFilter != null)
                                    drawerFilter.setSelection(0);

                                if (drawerIndex == 0 || drawerIndex == 1) {
                                    tabLayout.setVisibility(View.VISIBLE);

//                                    if (drawerIndex == 0) {
//
//                                        tabLayout.removeAllTabs();
//
//                                        tabLayout.addTab(discover);
//                                        tabLayout.addTab(bookmarks);
//                                    }
//                                    if (drawerIndex == 1) {
//
//                                        tabLayout.removeAllTabs();
//
//                                        tabLayout.addTab(people);
//                                        tabLayout.addTab(websites);
//                                    }

                                }
                                else tabLayout.setVisibility(View.GONE);

                                if (drawerIndex == 0)
                                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED, Gravity.RIGHT);
                                else
                                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);


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

                        new SectionDrawerItem().withName(SIGHT).withDivider(false),
                        new SecondaryDrawerItem().withName(SIGHT_CITY).withLevel(2).withIcon(Icon.gmd_location_city).withIdentifier(101),
                        new SecondaryDrawerItem().withName(SIGHT_COUNTRY).withLevel(2).withIcon(Icon.gmd_terrain).withIdentifier(102),
                        new SecondaryDrawerItem().withName(SIGHT_NATIONAL_PARK).withLevel(2).withIcon(Icon.gmd_nature).withIdentifier(103),
                        new SecondaryDrawerItem().withName(SIGHT_PARK).withLevel(2).withIcon(Icon.gmd_nature_people).withIdentifier(104),
                        new SecondaryDrawerItem().withName(SIGHT_BEACH).withLevel(2).withIcon(Icon.gmd_beach_access).withIdentifier(105),
                        new SecondaryDrawerItem().withName(SIGHT_LAKE).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(106),
                        new SecondaryDrawerItem().withName(SIGHT_GEYSER).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(107),
                        new SecondaryDrawerItem().withName(SIGHT_LANDFORM).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(108),
                        new SecondaryDrawerItem().withName(SIGHT_DESERT).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(109),

                        new SectionDrawerItem().withName(CONTINENT).withDivider(false),
                        new SecondaryDrawerItem().withName(CONTINENT_AFRICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(201),
                        new SecondaryDrawerItem().withName(CONTINENT_ANTARCTICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(202),
                        new SecondaryDrawerItem().withName(CONTINENT_ASIA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(203),
                        new SecondaryDrawerItem().withName(CONTINENT_AUSTRALIA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(204),
                        new SecondaryDrawerItem().withName(CONTINENT_EUROPE).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(205),
                        new SecondaryDrawerItem().withName(CONTINENT_NORTH_AMERICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(206),
                        new SecondaryDrawerItem().withName(CONTINENT_SOUTH_AMERICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(207)

                ).addStickyDrawerItems(
                        new PrimaryDrawerItem().withName("Reset Filter").withSelectable(false).withIcon(Icon.gmd_clear_all).withIdentifier(999)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {

                            switch ((int) drawerItem.getIdentifier()) {

                                case 101: FilterLogic.filterList(SIGHT_CITY);
                                    break;
                                case 102: FilterLogic.filterList(SIGHT_COUNTRY);
                                    break;
                                case 103: FilterLogic.filterList(SIGHT_NATIONAL_PARK);
                                    break;
                                case 104: FilterLogic.filterList(SIGHT_PARK);
                                    break;
                                case 105: FilterLogic.filterList(SIGHT_BEACH);
                                    break;
                                case 106: FilterLogic.filterList(SIGHT_LAKE);
                                    break;
                                case 107: FilterLogic.filterList(SIGHT_GEYSER);
                                    break;
                                case 108: FilterLogic.filterList(SIGHT_LANDFORM);
                                    break;
                                case 109: FilterLogic.filterList(SIGHT_DESERT);
                                    break;
                                case 201: FilterLogic.filterList(CONTINENT_AFRICA);
                                    break;
                                case 202: FilterLogic.filterList(CONTINENT_ANTARCTICA);
                                    break;
                                case 203: FilterLogic.filterList(CONTINENT_ASIA);
                                    break;
                                case 204: FilterLogic.filterList(CONTINENT_AUSTRALIA);
                                    break;
                                case 205: FilterLogic.filterList(CONTINENT_EUROPE);
                                    break;
                                case 206: FilterLogic.filterList(CONTINENT_NORTH_AMERICA);
                                    break;
                                case 207: FilterLogic.filterList(CONTINENT_SOUTH_AMERICA);
                                    break;
                                case 999:
                                    FilterLogic.filterList("All");
                                    drawerFilter.setSelection(0);
                                    break;
                                default:
                                    FilterLogic.filterList("All");
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
                return drawerNature;
            case 2:
                return drawerHall;
            case 3:
                return drawerUpload;
            case 4:
                return drawerAbout;
            case 5:
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

        return drawerHeader;
    }

}
