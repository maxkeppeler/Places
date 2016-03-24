package com.mk.placesdrawer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.models.PlacesItem;
import com.mk.placesdrawer.utilities.Animations;

public class CollapsingToolbar extends AppCompatActivity {

    public static Toolbar toolbar;
    private PlacesItem item;
    private Activity context;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = this.getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setNavigationBarColor(getResources().getColor(R.color.navigationBar));
            window.setStatusBarColor(getResources().getColor(R.color.colorStatusBarOverlay));
        }

        context = this;

        initActivityTransitions();
        setContentView(R.layout.collapsing_toolbar_layout);

        Intent intent = getIntent();

        item = intent.getParcelableExtra("item");

        final String itemImage = item.getImgPlaceUrl();
        String itemTitle = item.getLocation();

        ViewCompat.setTransitionName(findViewById(R.id.app_bar_layout), itemImage);
        supportPostponeEnterTransition();

        toolbar = (Toolbar) findViewById(R.id.toolbarTest);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(itemTitle);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimary));
        collapsingToolbarLayout.setContentScrimColor(getResources().getColor(R.color.colorPrimary));

        final ImageView image = (ImageView) findViewById(R.id.image);

        Glide.with(context)
                .load(itemImage)
//                .override(3000, 2000)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(new BitmapImageViewTarget(image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(getResources(), resource)});
                        image.setImageDrawable(td);
                        td.startTransition(750);
                        new Palette.Builder(resource).generate(paletteAsyncListener);
                    }
                });

        if (getResources().getBoolean(R.bool.zoomCollapsingToolbarImage)) {
            Animations.zoomInAndOut(context, image);
        }


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
        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeViewer();
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

    public Palette.PaletteAsyncListener paletteAsyncListener =
            new Palette.PaletteAsyncListener() {

                @Override
                public void onGenerated(Palette palette) {
                    if (palette == null) return;

                        int defaultRippleColor = ContextCompat.getColor(context, R.color.rippleColorView);
                        int defaultFabColor = ContextCompat.getColor(context, R.color.rippleColorView);

                        int defaultNavigationBar = ContextCompat.getColor(context, R.color.colorPrimary);
                        int defaultToolbar = ContextCompat.getColor(context, R.color.colorPrimary);
                        int defaultStatusBar = ContextCompat.getColor(context, R.color.colorStatusBarOverlay);

                        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                        fab.setRippleColor(palette.getLightVibrantColor(defaultRippleColor));
                        fab.setBackgroundTintList(ColorStateList.valueOf(palette.getVibrantColor(defaultFabColor)));

                        collapsingToolbarLayout.setContentScrimColor(palette.getVibrantColor(defaultToolbar));
                        collapsingToolbarLayout.setStatusBarScrimColor(palette.getVibrantColor(defaultStatusBar));

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Window window = getWindow();
                            window.setNavigationBarColor(palette.getVibrantColor(defaultNavigationBar));
                        } else {
                            Log.d("Palette is null", "CollapsingToolbar.java");
                        }
                }
            };

}