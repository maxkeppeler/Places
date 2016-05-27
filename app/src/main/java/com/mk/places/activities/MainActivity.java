package com.mk.places.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.mk.places.fragment.DrawerPlacesTabs;
import com.mk.places.fragment.FragmentBookmarks;
import com.mk.places.fragment.FragmentPlaces;
import com.mk.places.models.Disasters;
import com.mk.places.models.GoodActs;
import com.mk.places.models.Places;
import com.mk.places.utilities.Constants;
import com.mk.places.utilities.Dialogs;
import com.mk.places.utilities.FilterLogic;
import com.mk.places.utilities.Preferences;

import java.util.Random;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon;

public class MainActivity extends AppCompatActivity {

    public static Drawer drawer = null;
    public static Drawer drawerFilter = null;
    public static TabLayout tabLayout;
    private static AppCompatActivity context;
    private static TabLayout.Tab tab1, tab2;
    private Toolbar toolbar;
    private AccountHeader drawerHeader;
    private String[] drawerHeaderURLS;
    public static int drawerIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        context = this;

        Preferences pref = new Preferences(context);

        FragmentPlaces.loadPlacesList(context);

//        TODO: If user has no WIFI, ask if we can use the mobile internet. Inform user that much data will be loaded. Afterwards load places list

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tab1 = tabLayout.newTab();
        tab2 = tabLayout.newTab();
        tabLayout.addTab(tab1);
        tabLayout.addTab(tab2);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setSelectedTabIndicatorColor(ContextCompat.getColor(context, R.color.white));

//        TODO: Check if app was updated and had then the first start

        if (pref.getFirstStart()) {
            Dialogs.showChangelog(context);
            pref.setFirstStart(false);
        }

        drawerHeaderURLS = getResources().getStringArray(R.array.headerUrl);

        drawerHeader = new AccountHeaderBuilder().withActivity(this)
                .withSelectionFirstLine("Beautiful Places").withSelectionSecondLine("on our Earth")
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
                        new PrimaryDrawerItem().withName(Constants.DRAWER_PLACES).withIcon(Icon.gmd_terrain).withIdentifier(Constants.ID_DRAWER_PLACES).withBadgeStyle(new BadgeStyle()),
                        new PrimaryDrawerItem().withName(Constants.DRAWER_NATURE).withIcon(Icon.gmd_public).withIdentifier(Constants.ID_DRAWER_NATURE),
                        new PrimaryDrawerItem().withName(Constants.DRAWER_HALL).withIcon(Icon.gmd_card_giftcard).withIdentifier(Constants.ID_DRAWER_HALL),
                        new SectionDrawerItem().withName("Various"),
                        new SecondaryDrawerItem().withName(Constants.DRAWER_ABOUT).withIcon(Icon.gmd_person).withIdentifier(Constants.ID_DRAWER_ABOUT),
                        new SecondaryDrawerItem().withName(Constants.DRAWER_SETTINGS).withIcon(Icon.gmd_settings).withIdentifier(Constants.ID_DRAWER_SETTINGS).withSelectable(false)
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
                                DrawerLayout drawerLayout = drawer.getDrawerLayout();

                                drawerIndex = (int) drawerItem.getIdentifier();

                                switch (drawerIndex) {

                                    case 0: fragment = new DrawerPlacesTabs();
                                        setTabTexts(Constants.TAB_DISASTERS, Constants.TAB_GOOD_ACTS);

                                        if (Places.getPlacesList() != null && FragmentBookmarks.getBookmarks() != null)
                                        updateTabTexts(0, Places.getPlacesList().size(), FragmentBookmarks.getBookmarks().size());

                                        break;

                                    case 1: fragment = new DrawerPlacesTabs();
                                        setTabTexts(Constants.TAB_DISASTERS, Constants.TAB_GOOD_ACTS);

                                        if (Disasters.getDisastersList() != null && GoodActs.getGoodActsList() != null)
                                            updateTabTexts(1, Disasters.getDisastersList().size(), GoodActs.getGoodActsList().size());

                                        break;

                                    case 2: fragment = new DrawerPlacesTabs();
                                        setTabTexts(Constants.TAB_PEOPLE, Constants.TAB_WEBSITES);
                                        break;

                                    case 3: fragment = new DrawerAbout();
                                        break;

                                    case 4: intent = new Intent(context, Settings.class);
                                        break;

                                    default: fragment = new FragmentPlaces();
                                }

                                if (fragment != null) {
                                    fragment.setRetainInstance(true);
                                    transaction.replace(R.id.container, fragment);
                                    transaction.commit();
                                    toolbar.setTitle(toolbarTitle(drawerIndex));

                                } else if (intent != null) {
                                    startActivity(intent);
                                    drawer.setSelection(Constants.ID_DRAWER_PLACES);
                                }

                                if (drawerFilter != null)
                                    drawerFilter.setSelection(Constants.NO_SELECTION);

                                if (drawerIndex > 2)
                                    tabLayout.setVisibility(View.GONE);
                                else tabLayout.setVisibility(View.VISIBLE);

                                if (drawerIndex > 0)
                                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

                                else
                                    drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED, Gravity.RIGHT);

                            }
                        }

                        return false;
                    }
                })
                .build();

        if (drawer != null) drawer.setSelection(Constants.ID_DRAWER_PLACES);

        drawerFilter = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(

                        new SectionDrawerItem().withName(Constants.SIGHT).withDivider(false),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_CITY).withLevel(2).withIcon(Icon.gmd_location_city).withIdentifier(Constants.ID_CITY),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_COUNTRY).withLevel(2).withIcon(Icon.gmd_terrain).withIdentifier(Constants.ID_COUNTRY),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_ISLAND).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(Constants.ID_ISLAND),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_NATIONAL_PARK).withLevel(2).withIcon(Icon.gmd_nature).withIdentifier(Constants.ID_NATIONALR_PARK),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_GARDEN).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(Constants.ID_GARDEN),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_CAVE).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(Constants.ID_CAVE),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_BEACH).withLevel(2).withIcon(Icon.gmd_beach_access).withIdentifier(Constants.ID_BEACH),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_LAKE).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(Constants.ID_LAKE),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_RIVER).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(Constants.ID_RIVER),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_GEYSER).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(Constants.ID_GEYSER),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_LANDFORM).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(Constants.ID_LANDFORM),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_DESERT).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(Constants.ID_DESERT),

                        new SectionDrawerItem().withName(Constants.CONTINENT).withDivider(false),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_AFRICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(Constants.ID_AFRICA),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_ANTARCTICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(Constants.ID_ANTARCTICA),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_ASIA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(Constants.ID_ASIA),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_AUSTRALIA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(Constants.ID_AUSTRALIA),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_EUROPE).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(Constants.ID_EUROPE),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_NORTH_AMERICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(Constants.ID_NORTH_AMERICA),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_SOUTH_AMERICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(Constants.ID_SOUTH_AMERICA)

                ).addStickyDrawerItems(
                        new PrimaryDrawerItem().withName("Reset Filter").withSelectable(false).withIcon(Icon.gmd_clear_all).withIdentifier(Constants.ID_ALL)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {

                            long pos = drawerItem.getIdentifier();

                            if (pos == Constants.ID_CITY) FilterLogic.filterList(Constants.SIGHT_CITY);
                            if (pos == Constants.ID_COUNTRY) FilterLogic.filterList(Constants.SIGHT_COUNTRY);
                            if (pos == Constants.ID_NATIONALR_PARK) FilterLogic.filterList(Constants.SIGHT_NATIONAL_PARK);
                            if (pos == Constants.ID_GARDEN) FilterLogic.filterList(Constants.SIGHT_GARDEN);
                            if (pos == Constants.ID_CAVE) FilterLogic.filterList(Constants.SIGHT_CAVE);
                            if (pos == Constants.ID_BEACH) FilterLogic.filterList(Constants.SIGHT_BEACH);
                            if (pos == Constants.ID_LAKE) FilterLogic.filterList(Constants.SIGHT_LAKE);
                            if (pos == Constants.ID_RIVER) FilterLogic.filterList(Constants.SIGHT_RIVER);
                            if (pos == Constants.ID_DESERT) FilterLogic.filterList(Constants.SIGHT_DESERT);
                            if (pos == Constants.ID_GEYSER) FilterLogic.filterList(Constants.SIGHT_GEYSER);
                            if (pos == Constants.ID_LANDFORM) FilterLogic.filterList(Constants.SIGHT_LANDFORM);
                            if (pos == Constants.ID_ISLAND) FilterLogic.filterList(Constants.SIGHT_ISLAND);

                            if (pos == Constants.ID_AFRICA) FilterLogic.filterList(Constants.CONTINENT_AFRICA);
                            if (pos == Constants.ID_ANTARCTICA) FilterLogic.filterList(Constants.CONTINENT_ANTARCTICA);
                            if (pos == Constants.ID_ASIA) FilterLogic.filterList(Constants.CONTINENT_ASIA);
                            if (pos == Constants.ID_AUSTRALIA) FilterLogic.filterList(Constants.CONTINENT_AUSTRALIA);
                            if (pos == Constants.ID_EUROPE) FilterLogic.filterList(Constants.CONTINENT_EUROPE);
                            if (pos == Constants.ID_NORTH_AMERICA) FilterLogic.filterList(Constants.CONTINENT_NORTH_AMERICA);
                            if (pos == Constants.ID_SOUTH_AMERICA) FilterLogic.filterList(Constants.CONTINENT_SOUTH_AMERICA);

                            if (pos == Constants.ID_ALL) {
                                FilterLogic.filterList(Constants.NO_FILTER);
                                drawerFilter.setSelection(Constants.NO_SELECTION);
                            }

                        }
                        return false;
                    }
                })
                .withDrawerGravity(Gravity.END)
                .append(drawer);

        if (drawerFilter != null) drawerFilter.setSelection(Constants.NO_SELECTION);

    }


    /**
     * Set the correct title, depending on the position in the drawer.
     * @param index
     * @return
     */
    public String toolbarTitle(int index) {

        switch (index) {

            case Constants.ID_DRAWER_PLACES: return Constants.DRAWER_PLACES;
            case Constants.ID_DRAWER_NATURE: return Constants.DRAWER_NATURE;
            case Constants.ID_DRAWER_HALL: return Constants.DRAWER_HALL;
            case Constants.ID_DRAWER_ABOUT: return Constants.DRAWER_ABOUT;
            case Constants.ID_DRAWER_SETTINGS: return Constants.DRAWER_SETTINGS;

            default: return Constants.DRAWER_WRONG;
        }
    }

    /**
     * Depending on index (position of drawer), texts for tabs are set. In addition the sizes of the arrays are set.
     * @param index
     * @param value1
     * @param value2
     */
    public static void updateTabTexts(int index, int value1, int value2) {

        if (index == 0) {
            tab1.setText(Constants.TAB_PLACES + " (" + value1 + ")");
            tab2.setText(Constants.TAB_BOOKMARKS + " (" + value2 + ")");
        }

        if (index == 1) {
            if (value1 > 0)
                tab1.setText(Constants.TAB_DISASTERS + " (" + value1 + ")");
            if (value2 > 0)
                tab2.setText(Constants.TAB_GOOD_ACTS + " (" + value2 + ")");
        }

    }

    @Override
    public void onBackPressed() {

        if (drawer != null && drawer.isDrawerOpen())
            drawer.closeDrawer();

        else if (drawer != null && drawer.getCurrentSelection() != Constants.ID_DRAWER_PLACES)
            drawer.setSelection(Constants.ID_DRAWER_PLACES);

        else if (drawer != null)
            super.onBackPressed();

        else super.onBackPressed();

    }

    /**
     * Set texts for current tabs
     * @param nameTab1
     * @param nameTab2
     */
    public static void setTabTexts(String nameTab1, String nameTab2) {
        tab1.setText(nameTab1);
        tab2.setText(nameTab2);
    }

    /**
     * Pick random another image for the drawer header
     * @return drawerHeader
     */
    public AccountHeader drawerHeader() {

        final ImageView cover = drawerHeader.getHeaderBackgroundView();
        Random random = new Random();
        String randomURL = drawerHeaderURLS[random.nextInt(drawerHeaderURLS.length)];

        Glide.with(context)
                .load(randomURL)
                .crossFade()
                .override(1012, 788)
                .centerCrop()
                .into(cover);

        return drawerHeader;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // When the device is in landscape mode, always set the navigation bar color to black.
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.md_black_1000));
        }

        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(context, R.color.backgroundColor));
        }

    }

}
