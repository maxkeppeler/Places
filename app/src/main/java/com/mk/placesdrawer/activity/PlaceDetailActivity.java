package com.mk.placesdrawer.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.transition.Explode;
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
import com.mk.placesdrawer.adapters.PlaceDetailAdapter;
import com.mk.placesdrawer.models.Place;
import com.mk.placesdrawer.models.PlaceDetail;
import com.mk.placesdrawer.threads.DownloadImage;
import com.mk.placesdrawer.threads.SharePlace;
import com.mk.placesdrawer.utilities.Utils;
import com.mk.placesdrawer.widgets.SquareImageView;


import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaceDetailActivity extends AppCompatActivity {

    String location;
    String sight;
    String desc;
    String imageUrl;
    String position;
    String religion;

    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.collapsingToolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.placeDescTitle) TextView placeDescTitle;
    @Bind(R.id.descDetailView) TextView placesDescText;
    @Bind(R.id.placeInfoTitle) TextView placeInfoTitle;
    @Bind(R.id.recyclerViewInfoDetails) RecyclerView recyclerView;

    private ViewGroup layout;
    private Activity context;
    private static int color;

    Window window;


    public Palette.PaletteAsyncListener paletteAsyncListener =
            new Palette.PaletteAsyncListener() {
                @Override
                public void onGenerated(Palette palette) {
                    if (palette == null) return;
                    color = getAColor(palette);

                    fab.setRippleColor(color);
                    fab.setBackgroundTintList(ColorStateList.valueOf(color));
                    fab.setVisibility(View.VISIBLE);
                    Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                    fab.startAnimation(animation);

                    collapsingToolbarLayout.setContentScrimColor(color);

                    if (color != R.color.colorPrimary)
                        collapsingToolbarLayout.setStatusBarScrimColor(color);
                    else
                        collapsingToolbarLayout.setStatusBarScrimColor(getResources().getColor(R.color.colorPrimaryDark));


                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        window = getWindow();
                        window.setNavigationBarColor(color);
                    }
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = this.getWindow();
//            window.setNavigationBarColor(getResources().getColor(R.color.navigationBar));
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
        final Place item = intent.getParcelableExtra("item");


        imageUrl = item.getImgPlaceUrl();



        location = item.getLocation();
        sight = item.getSight();
        desc = item.getDescription();
        position =item.getPosition();
        religion = item.getReligion();

        Typeface typeface = Utils.getTypeface(context, 1);

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
        placesDescText.setText(Html.fromHtml(desc).toString().replace("â€™", "'"));
        fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (item.getFavorite() == 0) {
                    item.setFavorite(1);

                } else item.setFavorite(0);

                Log.d("fab", "onClick: " + item.getFavorite());

            }


        });

        collapsingToolbarLayout.setTitle(location + " " + item.getFavorite());
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
        collapsingToolbarLayout.setSelected(true);
        toolbar.setSelected(true);

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
                    }
                });
    }

    private void finishRecycler(RecyclerView recyclerView, PlaceDetail itemsData[]) {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        PlaceDetailAdapter mAdapter = new PlaceDetailAdapter(itemsData);
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

                downloadDialog();
                break;

            case R.id.share:
                share();
                break;

            case R.id.launch:
                Utils.openLinkInChromeCustomTab(context, "http://www.google.com/search?q=" + location, color);
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

        ProgressDialog mProgressDialog;
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setMessage("A message");
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setCancelable(true);

        final SharePlace sharePlace = new SharePlace(context, location, Html.fromHtml(desc).toString().replace("â€™", ""));
        sharePlace.execute(imageUrl);

        mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                sharePlace.cancel(true);
            }
        });

    }

    private int getAColor(Palette palette) {
        final int defaultColor = getResources().getColor(R.color.colorPrimary);
        int mutedLight = palette.getLightMutedColor(defaultColor);
        int vibrantLight = palette.getLightVibrantColor(mutedLight);
        int mutedDark = palette.getDarkMutedColor(vibrantLight);
        int vibrantDark = palette.getDarkVibrantColor(mutedDark);
        int muted = palette.getMutedColor(vibrantDark);
        return palette.getVibrantColor(muted);
    }

    public void downloadDialog() {

        new MaterialDialog.Builder(context)
                .title(R.string.saveImageTitle)
                .items(R.array.saveImageContentArray)
                .backgroundColor(context.getResources().getColor(R.color.dialogs))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {

                        if (i == 0) {

                            ProgressDialog mProgressDialog;

                            mProgressDialog = new ProgressDialog(context);
                            mProgressDialog.setMessage("A message");
                            mProgressDialog.setIndeterminate(true);
                            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                            mProgressDialog.setCancelable(true);

                            // execute this when the downloader must be fired
                            final DownloadImage downloadTask = new DownloadImage(context, location);
                            downloadTask.execute(imageUrl);

                            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    downloadTask.cancel(true);
                                }
                            });

                        }

                        if (i == 1) {


                        }


                    }
                })
                .show();
    }
}
