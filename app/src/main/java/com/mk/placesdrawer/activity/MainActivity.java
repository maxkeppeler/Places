package com.mk.placesdrawer.activity;

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
import android.view.MenuItem;
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
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.fragment.DrawerAbout;
import com.mk.placesdrawer.fragment.DrawerPlaces;
import com.mk.placesdrawer.utilities.Animation;

import java.util.Random;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon;

public class MainActivity extends AppCompatActivity {

    private Drawer result = null;
    private Drawer resultAppended = null;
    private static AppCompatActivity context;
    private static String drawerPlaces, drawerFavorite, drawerAbout, drawerFeedback, drawerLiveChat, drawerSettings, drawerWrong;
    private String[] urlHeaderArray;
    private Toolbar toolbar;
    private AccountHeader header;
    private int current = 0;
    private View drawerViewO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        final DrawerPlaces drawer = new DrawerPlaces();

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
        //set the back arrow in the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().withActionBarDrawerToggle(false);

        header = new AccountHeaderBuilder().withActivity(this).withSelectionFirstLine("Places").withSelectionSecondLine("by Maximilian Keppeler").withHeightDp(300).build();
        grabHeaderImage();



        result = new DrawerBuilder()
                .withActivity(this)
                .withAccountHeader(header)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0)
                .withToolbar(toolbar)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(drawerPlaces).withIcon(Icon.gmd_place).withIdentifier(0),
                        new PrimaryDrawerItem().withName(drawerFavorite).withIcon(Icon.gmd_place).withIdentifier(1),
                        new PrimaryDrawerItem().withName(drawerAbout).withIcon(Icon.gmd_person).withIdentifier(2),
                        new SectionDrawerItem().withName("Various"),
                        new SecondaryDrawerItem().withName(drawerFeedback).withIcon(Icon.gmd_feedback).withIdentifier(3),
                        new SecondaryDrawerItem().withName(drawerLiveChat).withIcon(Icon.gmd_chat).withIdentifier(4),
                        new SecondaryDrawerItem().withName(drawerSettings).withIcon(Icon.gmd_settings).withIdentifier(5)
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        Toast.makeText(context, "onDrawerOpened", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        Toast.makeText(context, "onDrawerClosed", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {

                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {


                        if (drawerItem != null) {
                            if (drawerItem instanceof Nameable) {

                                FragmentManager manager = getSupportFragmentManager();
                                FragmentTransaction transaction = manager.beginTransaction();
                                transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                                Fragment fragment = null;

                                switch ((int) drawerItem.getIdentifier()) {


                                    case 0:
                                        fragment = new DrawerPlaces();
                                        current = 0;
                                        break;

                                    case 1:
                                        current = 1;
                                        fragment = new DrawerAbout();

//                                    TODO - Favorite Fragment, filter out the json objects where int favorite is 1 (for favored)
                                        break;

                                    case 2:
                                        fragment = new DrawerAbout();
                                        current = 2;

                                        break;

                                    default:
                                        fragment = new DrawerAbout();
                                        current = 32;

                                }
                                        fragment.setRetainInstance(true);
                                        transaction.replace(R.id.container, fragment);
                                        transaction.commit();

//                                        current = (int) drawerItem.getIdentifier();
                                        Log.d("DrawerItem", "Identifier: " + drawerItem.getIdentifier());
                                        toolbar.setTitle(toolbarTitle((int) drawerItem.getIdentifier()));
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

                        drawerViewO = drawerView;
                        if (drawerView == result.getSlider()) {
                            Log.e("sample", "left opened");
                        } else if (drawerView == resultAppended.getSlider()) {
                            Log.e("sample", "right opened");
                        }
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {
                        if (drawerView == result.getSlider()) {
                            Log.e("sample", "left closed");
                        } else if (drawerView == resultAppended.getSlider()) {
                            Log.e("sample", "right closed");
                        }
                    }

                    @Override
                    public void onDrawerSlide(View drawerView, float slideOffset) {
                    }

                })
                .build();

        if (result != null) {
            result.setSelection(0);
        }

            resultAppended = new DrawerBuilder()
                    .withActivity(this)
                    .withDisplayBelowStatusBar(true)
                    .withSavedInstance(savedInstanceState)
                    .addDrawerItems(
                            new PrimaryDrawerItem().withName(drawerPlaces).withIcon(Icon.gmd_place).withIdentifier(0),
                            new PrimaryDrawerItem().withName(drawerFavorite).withIcon(Icon.gmd_place).withIdentifier(1),
                            new PrimaryDrawerItem().withName(drawerAbout).withIcon(Icon.gmd_person).withIdentifier(2),
                            new SectionDrawerItem().withName("Various"),
                            new SecondaryDrawerItem().withName(drawerFeedback).withIcon(Icon.gmd_feedback).withIdentifier(3),
                            new SecondaryDrawerItem().withName(drawerLiveChat).withIcon(Icon.gmd_chat).withIdentifier(4),
                            new SecondaryDrawerItem().withName(drawerSettings).withIcon(Icon.gmd_settings).withIdentifier(5)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            if (drawerItem instanceof Nameable) {
                                Toast.makeText(context, ((Nameable) drawerItem).getName().getText(context), Toast.LENGTH_SHORT).show();
                            }
                            return false;
                        }
                    })
                    .withDrawerGravity(Gravity.END)
                    .append(result);


        if (resultAppended != null) {
            resultAppended.setSelection(0);
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
