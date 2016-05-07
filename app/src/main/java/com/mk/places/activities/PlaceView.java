package com.mk.places.activities;

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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.inquiry.Inquiry;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.places.R;
import com.mk.places.adapters.GalleryAdapter;
import com.mk.places.adapters.PlaceItemAdapter;
import com.mk.places.models.GalleryItem;
import com.mk.places.models.Place;
import com.mk.places.threads.DownloadImage;
import com.mk.places.utilities.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaceView extends AppCompatActivity {


    @Bind(R.id.placeItemFAB)
    FloatingActionButton fab;

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout toolbarLayout;

    @Bind(R.id.pDescTitle)
    TextView pDescTitle;

    @Bind(R.id.pDescText)
    TextView pDescText;

    @Bind(R.id.pInfoTitle)
    TextView pInfoTitle;

    @Bind(R.id.pInfoRecycler)
    RecyclerView pInfoRecycler;

    @Bind(R.id.pGalleryRecyler)
    RecyclerView pGalleryRecycler;

    @Bind(R.id.shadowOverlay)
    LinearLayout shadowOverlay;

    @Bind(R.id.pMainImage)
    ImageView pMainImage;

    private static final int PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE = 42;
    private int color;
    private Window window;
    private String location, sight, desc, url, continent, religion;
    private ViewGroup layout;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
        }

        context = this;
        setContentView(R.layout.drawer_places_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_close);

        final Place item = getIntent().getParcelableExtra("item");
        url = item.getUrl();
        location = item.getLocation();
        sight = item.getSight();
        continent = item.getContinent();
        religion = item.getReligion();
        desc = item.getDescription();

        Glide.with(context)
                .load(url)
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .priority(Priority.IMMEDIATE)
                .centerCrop()
                .into(new BitmapImageViewTarget(pMainImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(getResources(), resource)});

                        if (pMainImage != null) {
                            pMainImage.setImageDrawable(td);
                            td.startTransition(50);
                            shadowOverlay.setVisibility(View.VISIBLE);
                        }

                        new Palette.Builder(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                                if (palette == null) return;

                                color = Utils.colorFromPalette(context, palette);
                                fab.setBackgroundTintList(ColorStateList.valueOf(color));
                                fab.setVisibility(View.VISIBLE);
                                Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                                fab.startAnimation(animation);
                                toolbarLayout.setContentScrimColor(color);
                                toolbarLayout.setStatusBarScrimColor((Utils.colorVariant(color, 0.92f)));

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                                    window.setNavigationBarColor(color);


                            }
                        });
                    }

                });

//        TODO: Check if other strings are empty (empty = unknown)

        if (continent.length() < 4) continent = "Unknown";
        if (religion.length() < 4) religion = "Unknown";

        sightDependingLayouts();

        final Typeface typeTitles = Utils.customTypeface(context, 1);
        final Typeface typeTexts = Utils.customTypeface(context, 2);

        pDescTitle.setTypeface(typeTitles);
        pInfoTitle.setTypeface(typeTitles);
        pDescText.setTypeface(typeTexts);

        pDescText.setText(Html.fromHtml(desc).toString().replace("â€“", "–").replace("â€™", "\"").replace("â€™", "\"").replace("â€˜", "\"").replace("\\n", "\n").replace("\\", ""));

        fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FavoriteUtil.init(context);
                FavoriteUtil.favoriteItem(item.getId());
                Log.d("FAB", " with ID: " + item.getId() + " Selected: " + FavoriteUtil.isFavorited(item.getId()));
                Inquiry.deinit();

                Utils.simpleSnackBar(context, color, R.id.coordinatorLayout, R.string.snackbarFavoredText, Snackbar.LENGTH_SHORT);
            }
        });

        toolbarLayout.setTitle(location);
        toolbarLayout.setCollapsedTitleTypeface(typeTitles);
        toolbarLayout.setExpandedTitleTypeface(typeTitles);
        toolbarLayout.setSelected(true);
        toolbar.setSelected(true);

        createGallery(item);
    }


    private void createGallery(Place item) {

        pGalleryRecycler.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false));

        int arraySize = 20;
        int gallerySize = -1;
        final String imageLink[] = new String[arraySize];

        imageLink[0] = item.getUrl_0();
        imageLink[1] = item.getUrl_1();
        imageLink[2] = item.getUrl_2();
        imageLink[3] = item.getUrl_3();
        imageLink[4] = item.getUrl_4();
        imageLink[5] = item.getUrl_5();
        imageLink[6] = item.getUrl_6();
        imageLink[7] = item.getUrl_7();
        imageLink[8] = item.getUrl_8();
        imageLink[9] = item.getUrl_9();
        imageLink[10] = item.getUrl_10();
        imageLink[11] = item.getUrl_11();
        imageLink[12] = item.getUrl_12();
        imageLink[13] = item.getUrl_13();
        imageLink[14] = item.getUrl_14();
        imageLink[15] = item.getUrl_15();
        imageLink[16] = item.getUrl_16();
        imageLink[17] = item.getUrl_17();
        imageLink[18] = item.getUrl_18();
        imageLink[19] = item.getUrl_19();

        for (int j = 0; j < arraySize; j++) {
            if (imageLink[j].length() > 5)
                gallerySize++;
        }

        final String nImageLink[] = new String[gallerySize];

        for (int j = 0; j < gallerySize; j++) {

            if (imageLink[j].length() > 5)
                nImageLink[j] = imageLink[j];

            else nImageLink[j] = imageLink[j + 1];
        }

        final GalleryAdapter galleryAdapter = new GalleryAdapter(context, nImageLink, new GalleryAdapter.ClickListener() {

            @Override
            public void onClick(GalleryAdapter.ViewHolder view, int index, boolean longClick) {

                if (longClick) {
                    Log.d("Lang", "onClick: with ID " + index);

                } else {

                    Intent intent = new Intent(context, GalleryView.class);
                    intent.putExtra("imageLink", nImageLink);
                    intent.putExtra("index", index);
                    context.startActivity(intent);

                    Log.d("Kurz", "onClick: with ID " + index);
                }
            }
        });

        pGalleryRecycler.setNestedScrollingEnabled(true);
        pGalleryRecycler.setClipToPadding(false);
        pGalleryRecycler.setAdapter(galleryAdapter);
        pGalleryRecycler.setHasFixedSize(true);

    }

    //  TODO - add more sight depending layouts (more sights?)

    private void sightDependingLayouts() {

        if (sight.equals("City")) {
            GalleryItem itemsData[] = {
                    new GalleryItem("Location", continent, R.drawable.ic_location),
                    new GalleryItem("Location", continent, R.drawable.ic_location),
                    new GalleryItem("Location", continent, R.drawable.ic_location),
                    new GalleryItem("Religion", religion, R.drawable.ic_religion),
            };
            completeRecycler(pInfoRecycler, itemsData);
        }

        if (sight.equals("National Park")) {
            GalleryItem itemsData[] = {
                    new GalleryItem("Location", continent, R.drawable.ic_location),
            };
            completeRecycler(pInfoRecycler, itemsData);
        }
    }



    private void completeRecycler(RecyclerView recyclerView, GalleryItem itemsData[]) {

        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
        PlaceItemAdapter mAdapter = new PlaceItemAdapter(itemsData, context);
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);

    }


    @Override
    public void onBackPressed() {
        close();
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
                close();
                break;

            case R.id.download:

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    getStoragePermission();
                } else downloadImage();

                break;

            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_EMAIL, "chvent94@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Places - Support Mail");
                intent.putExtra(Intent.EXTRA_TEXT, desc);
                startActivity(Intent.createChooser(intent, "Send Email with"));
                break;

            case R.id.launch:
                Utils.customChromeTab(context, "http://www.google.com/search?q=" + location, color);
                break;
        }
        return true;
    }

    private void close() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            supportFinishAfterTransition();
        else finish();
    }

    public void downloadImage() {

//        TODO: FIX: Image will not be downloaded

        final DownloadImage downloadTask = new DownloadImage(context, location);
        downloadTask.execute(url);

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


//    PERMISSION METHODS

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {

            case PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    downloadImage();
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
                            downloadImage();
                        }
                    })
                    .show();
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE);
        }
    }


}
