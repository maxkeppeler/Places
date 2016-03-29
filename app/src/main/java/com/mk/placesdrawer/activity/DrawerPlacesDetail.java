package com.mk.placesdrawer.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.Image;
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
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.transition.Slide;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.adapters.PlacesDetailAdapter;
import com.mk.placesdrawer.models.PlacesDetailItem;
import com.mk.placesdrawer.models.PlacesItem;
import com.mk.placesdrawer.utilities.PermissionUtil;
import com.mk.placesdrawer.utilities.Utils;
import com.mk.placesdrawer.widgets.SquareImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Arrays;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerPlacesDetail extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback {

    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0;

    private static String[] PERMISSION_WRITE_EXTERNAL_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};


    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.collapsingToolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.placeDescTitle) TextView placeDescTitle;
    @Bind(R.id.descDetailView) TextView placesDescText;
    @Bind(R.id.placeInfoTitle) TextView placeInfoTitle;

    Typeface typeface;
    private ViewGroup layout;
    private Activity context;
    private PlacesItem item;
    private static int generatedColor;

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

        context = this;
        initActivityTransitions();
        setContentView(R.layout.drawer_places_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        item = intent.getParcelableExtra("item");

        typeface = Typeface.createFromAsset(getAssets(), "fonts/BreeSerif-Regular.ttf");

        final String itemImage = item.getImgPlaceUrl();
        final String itemLocation = item.getLocation();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerViewInfoDetails);

        String position;
        String[] positionArray = {item.getCountry(), item.getState(), item.getCity()};
        position = Arrays.toString(positionArray).replace("[", "").replace("]", "").replace(",", "  ~ ");


        if (item.getSight().equals("City")) {
            PlacesDetailItem itemsData[] = {
                    new PlacesDetailItem("Location", position, R.drawable.ic_location),
                    new PlacesDetailItem("Religion", item.getReligion(), R.drawable.ic_religion),
            };
            finishRecycler(recyclerView, itemsData);
        }

        if (item.getSight().equals("National Park")) {
            PlacesDetailItem itemsData[] = {
                    new PlacesDetailItem("Location", position, R.drawable.ic_location),
            };
            finishRecycler(recyclerView, itemsData);
        }

        ViewCompat.setTransitionName(findViewById(R.id.appBarLayout), itemImage);
        supportPostponeEnterTransition();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        placeDescTitle.setTypeface(typeface);
        placeInfoTitle.setTypeface(typeface);
        placesDescText.setText(item.getDescription());
        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Floating Action Button", "onClick: works ");
            }
        });

        collapsingToolbarLayout.setTitle(itemLocation);
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);

        final SquareImageView image = (SquareImageView) findViewById(R.id.image);

        Glide.with(context)
                .load(itemImage)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
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

    private void finishRecycler(RecyclerView recyclerView, PlacesDetailItem itemsData[]) {
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
        getMenuInflater().inflate(R.menu.toolbar_drawer_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeViewer();
                break;
            case R.id.download:
                download();
                break;
            case R.id.share:
                share();
                break;
            case R.id.launch:
                launch();
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

    public void download() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
                || ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Log.i(TAG, "Storage permissions have NOT been granted. Requesting permissions.");
            requestContactsPermissions();

        } else {

            Log.i(TAG, "Storage permissions have already been granted. Displaying contact details.");


            Glide.with(context)
                    .load(item.getImgPlaceUrl())
                    .asBitmap()
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            if (resource != null) {
                                saveWallpaper(item.getLocation(), resource);
                                Log.d(TAG, "In der onResourceReady Methode, Bitmap wird geladen. ");
                            }
                        }
                    });
        }

    }

    public void saveWallpaper(final String location, final Bitmap bitmap) {

//        Text overlay over the orginal bitmap
        float textSize = 0.111979f * (bitmap.getWidth())/1.6f;
        Log.d(TAG, "Text size was adjusted to the image width: " + textSize);

        float marginLeft = 0.07421875f * bitmap.getWidth();
        Log.d(TAG, "Margin (left) size was adjusted to the image width: " + marginLeft);

        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.white));
        paint.setAlpha(255);
        paint.setTypeface(typeface);
        paint.setTextSize(textSize);
        paint.setShadowLayer(12, 0, 12, getResources().getColor(R.color.textShadow));
        paint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(location, marginLeft, bitmap.getHeight() - paint.getTextSize(), paint);

        OutputStream fOut = null;
        String imageName = location + " .jpg";
        File path = new File(Environment.getExternalStorageDirectory().toString());
        File myDir = new File(path, getResources().getString(R.string.app_name));

//        If no folder exists with the name "Places", it will be created
        if (!myDir.exists()) myDir.mkdir();

//        Create image with the given path and image name
        File file = new File(myDir, imageName);

        try {
                fOut = new FileOutputStream(file);
                Log.d(TAG, "Download: Image was saved.");

//            Compress image, when not succesfull, error.
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)) {
                    Log.d(TAG, "Download: Image was NOT saved: "+ path + imageName);
                }

            } catch (FileNotFoundException e) {
                Log.d(TAG, "Download: Image: Exception");
                e.printStackTrace();
            }
    }

    public void launch() {
        Utils.openLinkInChromeCustomTab(context, "http://www.google.com/search?q=" + item.getLocation(), generatedColor);
    }

    public void share() {
//        TODO Fix
        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("image/*");
        share.putExtra(Intent.EXTRA_SUBJECT, "Places - " + item.getLocation());
        share.putExtra(Intent.EXTRA_TEXT, item.getDescription());
        share.putExtra(Intent.EXTRA_STREAM, item.getImgPlaceUrl());
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

    private void requestContactsPermissions() {
        // BEGIN_INCLUDE(contacts_permission_request)
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)
                || ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_CONTACTS)) {

            // Provide an additional rationale to the user if the permission was not granted
            // and the user would benefit from additional context for the use of the permission.
            // For example, if the request has been denied previously.
            Log.i(TAG,
                    "Displaying contacts permission rationale to provide additional context.");

            // Display a SnackBar with an explanation and a button to trigger the request.
            Snackbar.make(layout, R.string.permission_contacts_rationale,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.ok, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ActivityCompat
                                    .requestPermissions(context, PERMISSION_WRITE_EXTERNAL_STORAGE,
                                            REQUEST_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {
            ActivityCompat.requestPermissions(this, PERMISSION_WRITE_EXTERNAL_STORAGE, REQUEST_WRITE_EXTERNAL_STORAGE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        if (requestCode == REQUEST_WRITE_EXTERNAL_STORAGE) {
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