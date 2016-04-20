package com.mk.places.activity;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.places.R;
import com.mk.places.adapters.PlaceDetailAdapter;
import com.mk.places.models.Place;
import com.mk.places.models.PlaceDetail;
import com.mk.places.threads.ImageFromLayout;
import com.mk.places.threads.ImageFromURL;
import com.mk.places.utilities.Utils;
import com.mk.places.widgets.SquareImageView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailView extends AppCompatActivity {


    private static int color;
    String location, sight, desc, imageUrl, position, religion;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.collapsingToolbar) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.placeDescTitle) TextView placeDescTitle;
    @Bind(R.id.descDetailView) TextView placesDescText;
    @Bind(R.id.placeInfoTitle) TextView placeInfoTitle;
    @Bind(R.id.recyclerViewInfoDetails) RecyclerView recyclerView;
    Window window;
    private ViewGroup layout;
    private Activity context;

    //    REQUEST PERMISSIONS
    private static final int PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
//            window.setStatusBarColor(getResources().getColor(R.color.colorStatusBarOverlay));
        }

        if (layout != null) {
            ViewGroup parent = (ViewGroup) layout.getParent();
            if (parent != null) {
                parent.removeView(layout);
            }
        }

        context = this;

        setContentView(R.layout.drawer_places_detail);

        ButterKnife.bind(this);

        Typeface typeface = Utils.customTypeface(context, 1);

        Intent intent = getIntent();
        final Place item = intent.getParcelableExtra("item");

        imageUrl = item.getImgPlaceUrl();
        location = item.getLocation();
        sight = item.getSight();
        desc = item.getDescription();
        position = item.getPosition();
        religion = item.getReligion();

        if (location.equals("")) location = "Unknown";

        sightDependingLayouts();

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

                if (item.getFavorite() == 0) item.setFavorite(1);
                else item.setFavorite(0);

                Utils.simpleSnackBar(context, color, R.id.coordinatorLayout, R.string.snackbarFavoredText, Snackbar.LENGTH_SHORT);
            }
        });

        collapsingToolbarLayout.setTitle(location);
        collapsingToolbarLayout.setCollapsedTitleTypeface(typeface);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeface);
        collapsingToolbarLayout.setSelected(true);
        toolbar.setSelected(true);

        final ImageView image = (ImageView) findViewById(R.id.image);

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


    public Palette.PaletteAsyncListener paletteAsyncListener = new Palette.PaletteAsyncListener() {
        @Override
        public void onGenerated(Palette palette) {
            if (palette == null) return;

            color = Utils.colorFromPalette(context, palette);

            fab.setRippleColor(color);
            fab.setBackgroundTintList(ColorStateList.valueOf(color));
            fab.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_up);
            fab.startAnimation(animation);

            collapsingToolbarLayout.setContentScrimColor(color);
            collapsingToolbarLayout.setStatusBarScrimColor((Utils.colorVariant(color, 0.92f)));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                window.setNavigationBarColor(color);
        }
    };


//  TODO - add more sight depending layouts for variety and more info
    private void sightDependingLayouts() {

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

        
    }

    private void finishRecycler(RecyclerView recyclerView, PlaceDetail itemsData[]) {
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
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

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    getStoragePermission();
                } else downloadDialog();

                break;

            case R.id.launch:
                Utils.customChromeTab(context, "http://www.google.com/search?q=" + location, color);
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

    public void downloadDialog() {

        new MaterialDialog.Builder(context)
                .title(R.string.saveImageTitle)
                .items(R.array.saveImageContentArray)
                .backgroundColor(context.getResources().getColor(R.color.dialogs))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {

                        if (i == 0) {

                            final ImageFromURL downloadTask = new ImageFromURL(context, location);
                            downloadTask.execute(imageUrl);

//                            Snackbar
                            View layout = findViewById(R.id.coordinatorLayout);
                            Snackbar snackbar = Snackbar.make(layout, R.string.snackbarDownloadImageText, Snackbar.LENGTH_LONG)
                                    .setActionTextColor(getResources().getColor(R.color.white))
                                    .setAction(R.string.snackbarDownloadImageAction, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Utils.intentOpen(Uri.fromFile(downloadTask.getOpenPath().getAbsoluteFile()), context, getResources().getString(R.string.snackbarDownloadImageIntent));
                                        }
                                    });

                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(color);
                            snackbar.show();
                        }

                        if (i == 1) {

//                          TODO -   Layout is too large to fit into a software layer or drawing cache 0,8gb ram wird benötigt, 0,14 nur vorhanden

                            CoordinatorLayout coView = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
                            coView.setDrawingCacheEnabled(true);
                            coView.setDrawingCacheQuality(CoordinatorLayout.DRAWING_CACHE_QUALITY_LOW);
                            coView.setLayerType(WebView.LAYER_TYPE_NONE, null);
                            coView.measure(CoordinatorLayout.MeasureSpec.makeMeasureSpec(0, CoordinatorLayout.MeasureSpec.UNSPECIFIED),
                                    CoordinatorLayout.MeasureSpec.makeMeasureSpec(0, CoordinatorLayout.MeasureSpec.UNSPECIFIED));
                            coView.layout(0, 0, coView.getMeasuredWidth(), coView.getMeasuredHeight());
                            coView.buildDrawingCache(true);

                            Bitmap createdBitmap = Bitmap.createBitmap(coView.getDrawingCache());
                            coView.setDrawingCacheEnabled(false);

                            final ImageFromLayout imageFromLayout = new ImageFromLayout(context, location, createdBitmap);
                            imageFromLayout.execute();


//                            Snack Bar
                            View layout = findViewById(R.id.coordinatorLayout);
                            Snackbar snackbar = Snackbar.make(layout, R.string.snackbarDownloadLayoutText, Snackbar.LENGTH_LONG)
                                    .setActionTextColor(getResources().getColor(R.color.white))
                                    .setAction(R.string.snackbarDownloadLayoutAction, new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            Utils.intentOpen(Uri.fromFile(imageFromLayout.getOpenPath().getAbsoluteFile()), context, getResources().getString(R.string.snackbarDownloadLayoutIntent));
                                        }
                                    });

                            View snackBarView = snackbar.getView();
                            snackBarView.setBackgroundColor(Utils.colorVariant(color, 0.92f));
                            snackbar.show();
                        }
                    }
                })
                .show();
    }



//    PERMISSION METHODS

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {

            case PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    downloadDialog();
                    Log.d("requestPermission", "Write External Storage: Permission granted.");

                } else {

                    //Show snack bar if check never ask again
                    Log.d("requestPermission", "Write External Storage: Permission NOT granted.");

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                        Snackbar.make(layout, "sdad",
                                Snackbar.LENGTH_LONG)
                                .setAction("dasdas", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivityForResult(intent, PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE);
                                    }
                                })
                                .show();
                    }
                    Log.d("requestPermission", "Write External Storage: Permission denied.");
                }
            }
        }
    }

    private void getStoragePermission() {
        //Explain the first time for what we need this permission and also if check never ask again
        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new MaterialDialog.Builder(this)
                    .content(R.string.storageContent)
                    .positiveText(R.string.storagePositive).positiveColor(color)
                    .negativeText(R.string.storageNegative).negativeColor(color)
                    .onAny(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            switch (which) {
                                case POSITIVE:
                                    ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                            PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE);
                                    break;
                                case NEGATIVE:
                                    break;
                            }
                        }
                    })
                    .cancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            downloadDialog();
                        }
                    })
                    .show();
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE);
        }
    }


}
