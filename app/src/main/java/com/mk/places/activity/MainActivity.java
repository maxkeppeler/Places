package com.mk.places.activity;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mk.places.R;
import com.mk.places.fragment.DrawerAbout;
import com.mk.places.fragment.DrawerPlaces;
import com.mk.places.fragment.DrawerSettings;
import com.mk.places.fragment.DrawerSupport;
import com.mk.places.utilities.Animation;

import java.util.Random;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon;

public class MainActivity extends AppCompatActivity {

    private static AppCompatActivity context;
    private Toolbar toolbar;
    private Drawer materialDrawer = null;
    private Drawer materialDrawerAppended = null;

    private AccountHeader header;
    private String[] imageArray;

    private int current = 0;
    private DrawerPlaces places;

    private static String

            drawerPlaces,
            drawerFavorite,
            drawerAbout,
            drawerSupport,
            drawerSettings,
            drawerWrong;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setNavigationBarColor(getResources().getColor(R.color.navigationBar));
        }

        context = this;
        places = new DrawerPlaces();
        places.loadPlacesList(context);

        imageArray = getResources().getStringArray(R.array.headerUrl);

        drawerPlaces = getResources().getString(R.string.app_places);
        drawerFavorite = getResources().getString(R.string.app_favorite);
        drawerAbout = getResources().getString(R.string.app_about);
        drawerSupport = getResources().getString(R.string.app_support);
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        header = new AccountHeaderBuilder().withActivity(this).withSelectionFirstLine("Places").withSelectionSecondLine("by Maximilian Keppeler").withHeightDp(300).build();
        headerImage();

        materialDrawer = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(header)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(drawerPlaces).withIcon(Icon.gmd_terrain).withIdentifier(0),
                        new PrimaryDrawerItem().withName(drawerFavorite).withIcon(Icon.gmd_favorite).withIdentifier(1),
                        new SectionDrawerItem().withName("Various"),
                        new SecondaryDrawerItem().withName(drawerAbout).withIcon(Icon.gmd_person).withIdentifier(2),
                        new SecondaryDrawerItem().withName(drawerSupport).withIcon(Icon.gmd_chat).withIdentifier(3),
                        new SecondaryDrawerItem().withName(drawerSettings).withIcon(Icon.gmd_settings).withIdentifier(4)
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

                                DrawerLayout drawerLayout = materialDrawer.getDrawerLayout();

                                switch ((int) drawerItem.getIdentifier()) {


                                    case 0:
                                        fragment = new DrawerPlaces();
                                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNDEFINED, Gravity.RIGHT);
                                        break;

                                    case 1:
                                        fragment = new DrawerPlaces();
                                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);

//                                    TODO - Favorite Fragment, filter out the json objects where int favorite is 1 (for favored)
                                        break;

                                    case 2:
                                        fragment = new DrawerAbout();
                                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                                        break;

                                    case 3:
                                        fragment = new DrawerSupport();
                                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                                        break;

                                    case 4:
                                        fragment = new DrawerSettings();
                                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                                        break;


                                    default:
                                        fragment = new DrawerAbout();
                                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, Gravity.RIGHT);
                                }

                                    if (fragment != null) {
                                    fragment.setRetainInstance(true);
                                    transaction.replace(R.id.container, fragment);
                                    transaction.commit();

                                    current = (int) drawerItem.getIdentifier();
                                    toolbar.setTitle(toolbarTitle((int) drawerItem.getIdentifier()));
                                }

                            }
                        }

                        return false;
                    }
                })
                .withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof SecondaryDrawerItem) {
                            Toast.makeText(context, ((SecondaryDrawerItem) drawerItem).getName().getText(context), Toast.LENGTH_SHORT).show();
                        }
                        return false;
                    }
                })
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {

                        if (drawerView == materialDrawer.getSlider()) {
                            Log.e("sample", "left opened");
                        } else if (drawerView == materialDrawerAppended.getSlider()) {
                            Log.e("sample", "right opened");
                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        if (drawerView == materialDrawer.getSlider()) {
                            Log.e("sample", "left closed");
                        } else if (drawerView == materialDrawerAppended.getSlider()) {
                            Log.e("sample", "right closed");
                        }
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }

                })
                .build();

        if (materialDrawer != null) {
            materialDrawer.setSelection(0);
        }


            materialDrawerAppended = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withDisplayBelowStatusBar(true)
                .withSavedInstance(savedInstanceState)
                .addDrawerItems(

                        new SectionDrawerItem().withName("Filter Places by").withDivider(false),
                        new DividerDrawerItem(),

                        new ExpandableDrawerItem().withName(sight).withIcon(Icon.gmd_style).withIdentifier(100).withSelectable(false).withIsExpanded(true).withSubItems(
                                new SecondaryDrawerItem().withName(sightCity).withLevel(2).withIcon(Icon.gmd_location_city).withIdentifier(101),
                                new SecondaryDrawerItem().withName(sightCountry).withLevel(2).withIcon(Icon.gmd_terrain).withIdentifier(102),
                                new SecondaryDrawerItem().withName(sightNationalPark).withLevel(2).withIcon(Icon.gmd_nature).withIdentifier(103),
                                new SecondaryDrawerItem().withName(sightPark).withLevel(2).withIcon(Icon.gmd_nature_people).withIdentifier(104)
                        ),

                        new ExpandableDrawerItem().withName(continent).withIcon(Icon.gmd_style).withIdentifier(200).withSelectable(false).withIsExpanded(false).withSubItems(

                                new SecondaryDrawerItem().withName(continentAfrica).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(201),
                                new SecondaryDrawerItem().withName(continentAntarctica).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(202),
                                new SecondaryDrawerItem().withName(continentAsia).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(203),
                                new SecondaryDrawerItem().withName(continentAustralia).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(204),
                                new SecondaryDrawerItem().withName(continentEurope).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(205),
                                new SecondaryDrawerItem().withName(continentNorthAmerica).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(206),
                                new SecondaryDrawerItem().withName(continentSouthAmerica).withLevel(2).withIcon(Icon.gmd_map).withIdentifier(207)
                        )

                ).addStickyDrawerItems(
                            new PrimaryDrawerItem().withName("Reset Filter").withSelectable(false).withIcon(Icon.gmd_clear_all).withIdentifier(999)
                    )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof Nameable) {
                            Toast.makeText(context, ((Nameable) drawerItem).getName().getText(context), Toast.LENGTH_SHORT).show();

                            switch ((int) drawerItem.getIdentifier()) {

//                                Identifiers 100+
                                case 101: places.setFilterKey(sightCity); break;
                                case 102: places.setFilterKey(sightCountry); break;
                                case 103: places.setFilterKey(sightNationalPark); break;
                                case 104: places.setFilterKey(sightPark); break;

//                                Identifiers 200+
                                case 301: places.setFilterKey(continentAfrica); break;
                                case 302: places.setFilterKey(continentAntarctica); break;
                                case 303: places.setFilterKey(continentAsia); break;
                                case 304: places.setFilterKey(continentAustralia); break;
                                case 305: places.setFilterKey(continentEurope); break;
                                case 306: places.setFilterKey(continentNorthAmerica); break;
                                case 307: places.setFilterKey(continentSouthAmerica); break;

//                                No change for these identifiers
                                case 100:  break;
                                case 200:  break;
                                case 300:  break;

                                case 999: places.setFilterKey("All");
                                    materialDrawerAppended.setSelection(0);
                                    break;
                                default: places.setFilterKey("All");
                                    materialDrawerAppended.setSelection(0);
                                    break;
                            }
                        }
                        return false;
                    }
                })
                .withDrawerGravity(Gravity.END)
                .append(materialDrawer);


        if (materialDrawerAppended != null) {
            materialDrawerAppended.setSelection(0);
        }
    }

    public String toolbarTitle(int position) {
        switch (position) {
            case 0: return drawerPlaces;
            case 1: return drawerFavorite;
            case 2: return drawerAbout;
            case 3: return drawerSupport;
            case 4: return drawerSettings;
            default: return drawerWrong;
        }
    }

    @Override
    public void onBackPressed() {
        if (materialDrawer != null && materialDrawer.isDrawerOpen()) {
            materialDrawer.closeDrawer();
        } else if (materialDrawer != null && materialDrawer.getCurrentSelection() != 1) {
            materialDrawer.setSelection(1);
        } else if (materialDrawer != null) {
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

    public AccountHeader headerImage() {

        final ImageView cover = header.getHeaderBackgroundView();
        Random random = new Random();
        String randomURL = imageArray[random.nextInt(imageArray.length)];

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