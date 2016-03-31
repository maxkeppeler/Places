package com.mk.placesdrawer.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.adapters.PlacesDetailAdapter;
import com.mk.placesdrawer.models.Place;
import com.mk.placesdrawer.models.PlaceDetail;
import com.mk.placesdrawer.utilities.Dialogs;
import com.mk.placesdrawer.utilities.PermissionUtil;
import com.mk.placesdrawer.utilities.Utils;
import com.mk.placesdrawer.widgets.SquareImageView;

import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerPlacesDetail extends AppCompatActivity {

    private static final int REQUEST_STORAGE = 0;

    private static String[] PERMISSION_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    String location, sight, desc, imageUrl,
            position, country, state, city, religion;


    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.placeDescTitle)
    TextView placeDescTitle;
    @Bind(R.id.descDetailView)
    TextView placesDescText;
    @Bind(R.id.placeInfoTitle)
    TextView placeInfoTitle;

    private Typeface typeface;
    private ViewGroup layout;
    private Activity context;
    private Place item;
    private static int generatedColor;

    private Bitmap dBitmap = null;

    public static final String TAG = "DrawerPlacesDetail";

    public Palette.PaletteAsyncListener paletteAsyncListener =
            new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    if (palette == null) return;
                    generatedColor = getAColor(palette);

                    fab.setRippleColor(generatedColor);
                    fab.setBackgroundTintList(ColorStateList.valueOf(generatedColor));
                    fab.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                    fab.startAnimation(animation);

                    collapsingToolbarLayout.setContentScrimColor(generatedColor);

                    if (generatedColor != R.color.colorPrimary) {
                        collapsingToolbarLayout.setStatusBarScrimColor(generatedColor);
                    } else {
                        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Window window = getWindow();
                        window.setNavigationBarColor(generatedColor);
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
            window.setNavigationBarColor(getResources().getColor(R.color.navigationBar));
            window.setStatusBarColor(getResources().getColor(R.color.colorStatusBarOverlay));
        }

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }

        context = this;

        initActivityTransitions();
        setContentView(R.layout.drawer_places_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        item = intent.getParcelableExtra("item");

        location = item.getLocation();
        sight = item.getSight();
        desc = item.getDescription();
        imageUrl = item.getImgPlaceUrl();
        country = item.getCountry();
        state = item.getState();
        city = item.getCity();
        religion = item.getReligion();

        typeface = Utils.getTypeface(context, 1);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewInfoDetails);

        String[] positionArray = {country, state, city};
        position = Arrays.toString(positionArray).replace("[", "").replace("]", "").replace(",", "  ~ ");

        if (sight.equals("City")) {
            PlaceDetail itemsData[] = {
                    new PlaceDetail("Location", position, R.drawable.ic_location),
                    new PlaceDetail("Religion", religion, R.drawable.ic_religion),
            };
            finishRecycler(recyclerView, itemsData);
        }

        if (sight.equals("National Park")) {
            PlaceDetail itemsData[] = {
                    new PlaceDetail("Location", position, R.drawable.ic_location),
            };
            finishRecycler(recyclerView, itemsData);
        }

        ViewCompat.setTransitionName(findViewById(R.id.appBarLayout), imageUrl);
        supportPostponeEnterTransition();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        placeDescTitle.setTypeface(typeface);
        placeInfoTitle.setTypeface(typeface);
        placesDescText.setText(desc);
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Floating Action Button", "onClick: works ");
            }
        });

        collapsingToolbarLayout.setTitle(location);
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);

        final SquareImageView image = (SquareImageView) findViewById(R.id.image);

        Glide.with(context)
                .load(imageUrl)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .skipMemoryCache(true)
                .centerCrop()
                .into(new BitmapImageViewTarget(image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(getResources(), resource)});
                        assert image != null;
                        image.setImageDrawable(td);
                        td.startTransition(450);
                        new Palette.Builder(resource).generate(paletteAsyncListener);
                        dBitmap = resource;
                    }
                });
    }

    private void finishRecycler(RecyclerView recyclerView, PlaceDetail itemsData[]) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        PlacesDetailAdapter mAdapter = new PlacesDetailAdapter(itemsData);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        try {
            return super.dispatchTouchEvent(motionEvent);
        } catch (NullPointerException e) {
            return false;
        }
    }

    private void initActivityTransitions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Slide transition = new Slide();
            transition.excludeTarget(android.R.id.statusBarBackground, true);
            getWindow().setEnterTransition(transition);
            getWindow().setReturnTransition(transition);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        closeViewer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.toolbar_places_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeViewer();
                break;
            case R.id.download:

                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "Storage permissions have NOT been granted. Requesting permissions.");
                    requestStoragePermissions();

                } else {
                    Dialogs.saveImageDialog(context, dBitmap, location);
                }

                break;
            case R.id.share:
                share();
                break;
            case R.id.launch:
                Utils.openLinkInChromeCustomTab(context, "http://www.google.com/search?q=" + location, generatedColor);
                break;
        }
        return true;
    }

    private void closeViewer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportFinishAfterTransition();
        } else {
            finish();
        }
    }



    public void share() {
//        TODO Fix
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_SUBJECT, "Places - " + location);
        share.putExtra(Intent.EXTRA_TEXT, desc);
        share.putExtra(Intent.EXTRA_STREAM, imageUrl);
        startActivity(Intent.createChooser(share, "Share Place"));
    }

    private int getAColor(Palette palette) {

        final int defaultColor = getResources().getColor(R.color.colorPrimary);
        int mutedDark = palette.getDarkMutedColor(defaultColor);
        int vibrantDark = palette.getDarkVibrantColor(mutedDark);
        int mutedLight = palette.getLightMutedColor(vibrantDark);
        int muted = palette.getMutedColor(mutedLight);
        int vibrantLight = palette.getLightVibrantColor(muted);
        return palette.getVibrantColor(vibrantLight);
    }

    //    TODO permissions don't work, you can press ok or no, and app will crash whatever you will do
    private void requestStoragePermissions() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            Snackbar.make(layout, R.string.permission_contacts_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat
                                    .requestPermissions(context, PERMISSION_STORAGE, REQUEST_STORAGE);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSION_STORAGE, REQUEST_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_STORAGE) {
            Log.i(TAG, "Received response for contact permissions request.");
            if (PermissionUtil.verifyPermissions(grantResults)) {
                Snackbar.make(layout, R.string.permision_available_contacts,
                        Snackbar.LENGTH_SHORT)
                        .show();
            } else {
                Log.i(TAG, "Contacts permissions were NOT granted.");
                Snackbar.make(layout, R.string.permissions_not_granted,
                        Snackbar.LENGTH_SHORT)
                        .show();
            }

        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }


}