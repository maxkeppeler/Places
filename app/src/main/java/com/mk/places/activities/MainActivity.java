package com.mk.places.activities;

import android.content.Intent;
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
                        new PrimaryDrawerItem().withName(Constants.DRAWER_PLACES).withIcon(Icon.gmd_terrain).withIdentifier(0).withBadgeStyle(new BadgeStyle()),
                        new PrimaryDrawerItem().withName(Constants.DRAWER_NATURE).withIcon(Icon.gmd_public).withIdentifier(1),
                        new PrimaryDrawerItem().withName(Constants.DRAWER_HALL).withIcon(Icon.gmd_card_giftcard).withIdentifier(2),
                        new SectionDrawerItem().withName("Various"),
                        new SecondaryDrawerItem().withName(Constants.DRAWER_ABOUT).withIcon(Icon.gmd_person).withIdentifier(3),
                        new SecondaryDrawerItem().withName(Constants.DRAWER_SETTINGS).withIcon(Icon.gmd_settings).withIdentifier(4).withSelectable(false)
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

                                switch ((int) drawerItem.getIdentifier()) {

                                    case 0: fragment = new DrawerPlacesTabs();
                                        if (Places.getPlacesList() != null && FragmentBookmarks.bookmarks != null)
                                        MainActivity.updateTabTexts(Places.getPlacesList().size(), FragmentBookmarks.bookmarks.size());
                                        break;

                                    case 1: fragment = new DrawerPlacesTabs();
                                        MainActivity.setTabTexts(Constants.TAB_SINS, Constants.TAB_GOOD_ACTS);
                                        break;

                                    case 2: fragment = new DrawerPlacesTabs();
                                        MainActivity.setTabTexts(Constants.TAB_PEOPLE, Constants.TAB_WEBSITES);
                                        break;

                                    case 3: fragment = new DrawerAbout();
                                        break;

                                    case 4: intent = new Intent(context, Settings.class);
                                        break;

                                    default: fragment = new FragmentPlaces();
                                }

                                if (fragment != null) {
//                                    fragment.setRetainInstance(true); TODO: WHAT IS THIS DOING?
                                    transaction.replace(R.id.container, fragment);
                                    transaction.commit();
                                    toolbar.setTitle(toolbarTitle(drawerIndex));

                                } else if (intent != null) {
                                    startActivity(intent);
                                    drawer.setSelection(0);
                                }

                                if (drawerFilter != null)
                                    drawerFilter.setSelection(0);

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

        if (drawer != null) drawer.setSelection(0);

        drawerFilter = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(

                        new SectionDrawerItem().withName(Constants.SIGHT).withDivider(false),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_CITY).withLevel(2).withIcon(Icon.gmd_location_city).withIdentifier(101),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_COUNTRY).withLevel(2).withIcon(Icon.gmd_terrain).withIdentifier(102),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_NATIONAL_PARK).withLevel(2).withIcon(Icon.gmd_nature).withIdentifier(103),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_PARK).withLevel(2).withIcon(Icon.gmd_nature_people).withIdentifier(104),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_BEACH).withLevel(2).withIcon(Icon.gmd_beach_access).withIdentifier(105),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_LAKE).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(106),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_GEYSER).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(107),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_LANDFORM).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(108),
                        new SecondaryDrawerItem().withName(Constants.SIGHT_DESERT).withLevel(2).withIcon(Icon.gmd_more).withIdentifier(109),

                        new SectionDrawerItem().withName(Constants.CONTINENT).withDivider(false),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_AFRICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(201),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_ANTARCTICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(202),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_ASIA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(203),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_AUSTRALIA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(204),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_EUROPE).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(205),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_NORTH_AMERICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(206),
                        new SecondaryDrawerItem().withName(Constants.CONTINENT_SOUTH_AMERICA).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(207)

                ).addStickyDrawerItems(
                        new PrimaryDrawerItem().withName("Reset Filter").withSelectable(false).withIcon(Icon.gmd_clear_all).withIdentifier(999)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {

                            switch ((int) drawerItem.getIdentifier()) {

                                case 101: FilterLogic.filterList(Constants.SIGHT_CITY);
                                    break;
                                case 102: FilterLogic.filterList(Constants.SIGHT_COUNTRY);
                                    break;
                                case 103: FilterLogic.filterList(Constants.SIGHT_NATIONAL_PARK);
                                    break;
                                case 104: FilterLogic.filterList(Constants.SIGHT_PARK);
                                    break;
                                case 105: FilterLogic.filterList(Constants.SIGHT_BEACH);
                                    break;
                                case 106: FilterLogic.filterList(Constants.SIGHT_LAKE);
                                    break;
                                case 107: FilterLogic.filterList(Constants.SIGHT_GEYSER);
                                    break;
                                case 108: FilterLogic.filterList(Constants.SIGHT_LANDFORM);
                                    break;
                                case 109: FilterLogic.filterList(Constants.SIGHT_DESERT);
                                    break;
                                case 201: FilterLogic.filterList(Constants.CONTINENT_AFRICA);
                                    break;
                                case 202: FilterLogic.filterList(Constants.CONTINENT_ANTARCTICA);
                                    break;
                                case 203: FilterLogic.filterList(Constants.CONTINENT_ASIA);
                                    break;
                                case 204: FilterLogic.filterList(Constants.CONTINENT_AUSTRALIA);
                                    break;
                                case 205: FilterLogic.filterList(Constants.CONTINENT_EUROPE);
                                    break;
                                case 206: FilterLogic.filterList(Constants.CONTINENT_NORTH_AMERICA);
                                    break;
                                case 207: FilterLogic.filterList(Constants.CONTINENT_SOUTH_AMERICA);
                                    break;
                                case 999:
                                    FilterLogic.filterList(Constants.NO_FILTER);
                                    drawerFilter.setSelection(0);
                                    break;
                                default:
                                    FilterLogic.filterList(Constants.NO_FILTER);
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

            case 0: return Constants.DRAWER_PLACES;
            case 1: return Constants.DRAWER_NATURE;
            case 2: return Constants.DRAWER_HALL;
            case 3: return Constants.DRAWER_ABOUT;
            case 4: return Constants.DRAWER_SETTINGS;

            default: return Constants.DRAWER_WRONG;
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

    public static void updateTabTexts(int discover, int bookmarks) {
        tab1.setText(Constants.TAB_PLACES + "   ⋗ " + discover + " ⋖");
        tab2.setText(Constants.TAB_BOOKMARKS + "   ⋗ " + bookmarks + " ⋖");
    }

    public static void setTabTexts(String nameTab1, String nameTab2) {
        tab1.setText(nameTab1);
        tab2.setText(nameTab2);
    }

    public AccountHeader drawerHeader() {

        final ImageView cover = drawerHeader.getHeaderBackgroundView();
        Random random = new Random();
        String randomURL = drawerHeaderURLS[random.nextInt(drawerHeaderURLS.length)];

        Glide.with(context)
                .load(randomURL)
                .override(1012, 788)
                .centerCrop()
                .into(cover);

        return drawerHeader;
    }

}
