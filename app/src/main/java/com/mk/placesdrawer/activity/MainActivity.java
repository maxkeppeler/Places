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
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.fragment.DrawerAbout;
import com.mk.placesdrawer.fragment.DrawerPlaces;
import com.mk.placesdrawer.models.PlaceList;
import com.mk.placesdrawer.utilities.Animation;
import com.mk.placesdrawer.utilities.Dialogs;

import java.util.Random;

import static com.mikepenz.google_material_typeface_library.GoogleMaterial.Icon;

public class MainActivity extends AppCompatActivity {

    private static Drawer result;
    private static AppCompatActivity context;
    private static String drawerPlaces, drawerSubmit, drawerFavorite;
    private static String drawerAbout, drawerFeedback, drawerSettings;
    private static String drawerWrong;
    private int currentDrawerItem;
    private String[] urlHeaderArray;
    private Window window;
    private Toolbar toolbar;
    private AccountHeader header;
    private String keyWord = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        context = this;

        loadWallsList(context);

        urlHeaderArray = getResources().getStringArray(R.array.headerUrl);

        drawerPlaces = getResources().getString(R.string.app_places);
        drawerFavorite = getResources().getString(R.string.app_favorite);
        drawerAbout = getResources().getString(R.string.app_about);
        drawerFeedback = getResources().getString(R.string.app_Feedback);
        drawerSettings = getResources().getString(R.string.app_settings);
        drawerWrong = getResources().getString(R.string.app_wrong);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = this.getWindow();
            window.setNavigationBarColor(getResources().getColor(R.color.navigationBar));
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        new DrawerBuilder().withActivity(this).build();

        final PrimaryDrawerItem itemPlaces = new PrimaryDrawerItem()
                .withName(drawerPlaces).withIcon(Icon.gmd_place).withIdentifier(1);

        final PrimaryDrawerItem itemFavorite = new PrimaryDrawerItem()
                .withName(drawerFavorite).withIcon(Icon.gmd_favorite).withIdentifier(2);

        final PrimaryDrawerItem itemAbout = new PrimaryDrawerItem()
                .withName(drawerAbout).withIcon(Icon.gmd_person).withIdentifier(3);

        final PrimaryDrawerItem itemFeedback = new PrimaryDrawerItem()
                .withName(drawerFeedback).withIcon(Icon.gmd_feedback).withIdentifier(4);

        final PrimaryDrawerItem itemSettings = new PrimaryDrawerItem()
                .withName(drawerSettings).withIcon(Icon.gmd_settings).withIdentifier(5);


        header = new AccountHeaderBuilder().withActivity(this).withSelectionFirstLine("Places").withSelectionSecondLine("by Maximilian Keppeler")
                .withHeightDp(300)
                .build();

        changeHeader();

        result = new DrawerBuilder().withAccountHeader(header).withActivity(this).withToolbar(toolbar).withSelectedItem(1)
                .addDrawerItems(
                        itemPlaces
                                .withBadgeStyle(
                                        new BadgeStyle()        // TODO, only Cyan, when current item is 1, otherwise no background color
                                                .withTextColor(Color.WHITE)
                                                .withColorRes(R.color.colorAccent)
                                                .withCornersDp(100000).withPadding(20)),
                        itemFavorite.
                                withBadgeStyle(
                                        new BadgeStyle()        // TODO, only Cyan, when current item is 2, otherwise no background color
                                                .withTextColor(Color.WHITE)
                                                .withColorRes(R.color.colorAccent)
                                                .withCornersDp(100000).withPadding(20)),
                        new DividerDrawerItem(),
                        itemAbout,
                        itemFeedback,
                        itemSettings
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        if (drawerItem != null) {
                            Intent intent = null;

                            FragmentManager manager = getSupportFragmentManager();
                            FragmentTransaction transaction = manager.beginTransaction();
                            transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out);

                            Fragment fragment = null;

                            switch ((int) drawerItem.getIdentifier()) {

                                case 1:
                                    fragment = new DrawerPlaces();
                                    break;

                                case 2:
//                                    TODO - Favorite Fragment, filter out the json objects where int favorite is 1 (for favored)
                                    fragment = new DrawerAbout();
//                                    setKeyWord("favorite");
                                    break;

                                case 3:
                                    fragment = new DrawerAbout();
                                    break;

                                case 4:
//                                    fragment = new DrawerTabs();

                                    ViewPager pager= (ViewPager) findViewById(R.id.viewPager);
                                    TabLayout tabLayout= (TabLayout) findViewById(R.id.tab_layout);

//                                TODO

                                    break;

                                case 5:
                                    fragment = new DrawerAbout();
                                    break;

                                case 6:
                                    fragment = new DrawerAbout();
                                    break;

                                default:
                                    fragment = new DrawerPlaces();
                            }



                            if (fragment != null) {
                            transaction.replace(R.id.container, fragment);
                            transaction.commit();

                            currentDrawerItem = (int) drawerItem.getIdentifier();
                            toolbar.setTitle(toolbarText(currentDrawerItem));
                            }

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


        Intent in = new Intent();
        int dburl = in.getIntExtra("size", -1);
////
//////      TODO intent restarts MainActivity, UI restarts
////
//
//        Intent one = new Intent(context, MainActivity.class);
//        one.putExtra("size", PlaceList.getPlacesList().size());
//        context.startActivity(one);
//
//        Log.d("SIZE MAIN", "onCreate: " + DrawerPlaces.getJsonArraySize());
//        result.updateBadge(1, new StringHolder("    " + String.valueOf(DrawerPlaces.getJsonArraySize()) + "    "));
//            result.updateBadge(1, new StringHolder("    " + size + "    "));
//        result.updateBadge(2, new StringHolder("    " + "35" + "    "));
    }

    public String toolbarText(int fragmentPosition) {
        switch (fragmentPosition) {
            case 1:
                return drawerPlaces;
            case 2:
                return drawerFavorite;
            case 3:
                return drawerAbout;
            case 4:
                return drawerFeedback;
            case 5:
                return drawerSettings;
            default:
                return drawerWrong;
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
        getMenuInflater().inflate(R.menu.toolbar_places, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id) {
            case R.id.filter:
                filterDialog(context);
                break;
            case R.id.column:
                Dialogs.columnsDialog(context);
                break;
            case R.id.changelog:
                Dialogs.showChangelog(context);
                break;
        }
        return true;
    }

    public AccountHeader changeHeader() {

        final ImageView cover = header.getHeaderBackgroundView();

        Random r = new Random();
        String rnb = urlHeaderArray[r.nextInt(urlHeaderArray.length)];

        Glide.with(context)
                .load(rnb)
                .asBitmap()
                .override(2012, 1788)
                .centerCrop()
                .into(new BitmapImageViewTarget(cover) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(getResources(), resource)});
                        cover.setImageDrawable(td);
                        td.startTransition(400);
                    }

                });

        if (this.getResources().getBoolean(R.bool.zoomDrawerHeader)) {
            Animation.zoomInAndOut(context, cover);
        }

        return header;
    }

    public void filterDialog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.filterTitle)
                .items(R.array.filterContentArray)
                .negativeText("Reset Filter")
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        setKeyWord("All");
                    }
                })
                .backgroundColor(context.getResources().getColor(R.color.dialogs))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {
                        if (i == 0) sightDialog(context);
                        if (i == 1) countryDialog(context);

                    }
                })
                .show();
    }

    public String sightDialog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.sightTitle)
                .items(R.array.sightContentArray)
                .backgroundColor(context.getResources().getColor(R.color.dialogs))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {

                        switch (i) {
                            case 0: setKeyWord(text.toString()); break;
                            case 1: setKeyWord(text.toString()); break;
                            case 2: setKeyWord(text.toString()); break;
                            case 3: setKeyWord(text.toString()); break;
                            case 4: setKeyWord(text.toString()); break;
                            case 5: setKeyWord(text.toString()); break;
                            case 6: setKeyWord(text.toString()); break;
                        }
                    }
                })
                .show();
        return "";
    }

    public String countryDialog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.filterTitle)
                .items(R.array.filterContentArray)
                .backgroundColor(context.getResources().getColor(R.color.dialogs))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {

//                        TODO on selection, filter the current placesList and return just the objects where the category (country) is correct.

                    }
                })
                .show();

        return "";
    }

    public void setKeyWord(String string) {
        this.keyWord = string;
        loadWallsList(context);
    }

    private void loadWallsList(Context context) {
        PlaceList.clearList();

        new DrawerPlaces.DownloadJSON(new PlacesListInterface() {
            @Override
            public void checkPlacesListCreation(boolean result) {
                if (DrawerPlaces.mAdapter != null) {
                    DrawerPlaces.mAdapter.notifyDataSetChanged();
                }
            }
        }, context, keyWord).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }
    public interface PlacesListInterface {
        void checkPlacesListCreation(boolean result);
    }

}
