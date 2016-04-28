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
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
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
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.places.R;
import com.mk.places.adapters.GalleryAdapter;
import com.mk.places.adapters.PlaceDetailAdapter;
import com.mk.places.models.Place;
import com.mk.places.models.PlaceInfo;
import com.mk.places.models.PlaceInfoGallery;
import com.mk.places.models.Places;
import com.mk.places.threads.DownloadImage;
import com.mk.places.utilities.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DetailView extends AppCompatActivity {


    //    REQUEST PERMISSIONS
    private static final int PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE = 42;
    private static int color;
    String location, sight, desc, url, continent, religion;
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
    @Bind(R.id.recyclerViewInfoDetails)
    RecyclerView recyclerViewDetail;
    @Bind(R.id.recyclerViewGallery)
    RecyclerView recyclerViewGallery;
    @Bind(R.id.scroll)
    NestedScrollView scroll;
    Window window;
    boolean active;
    private ViewGroup layout;
    private Activity context;
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
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
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
        pos = intent.getIntExtra("pos", -1);

        url = item.getUrl();
        location = item.getLocation();
        sight = item.getSight();
        continent = item.getContinent();
        religion = item.getReligion();
        desc = item.getDescription();

        if (continent.isEmpty() || continent.length() < 4) continent = "Unknown";

        sightDependingLayouts();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        placeDescTitle.setTypeface(typeface);
        placeInfoTitle.setTypeface(typeface);
        placesDescText.setText(Html.fromHtml(desc).toString().replace("â€“", "–").replace("â€™", "\"").replace("â€™", "\"").replace("â€˜", "\"").replace("\\n", "\n").replace("\\", ""));
        fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//              TODO: DATA BASE / permanent Boolean Array

                if (Places.getPlacesList().get(pos).getFavorite() == 0) {
                    Places.getPlacesList().get(pos).setFavorite(1);
                    Log.d("1", "FAB: " + pos + " bool: " + Places.getPlacesList().get(pos).getFavorite());
                    active = true;
                } else if (Places.getPlacesList().get(pos).getFavorite() == 1) {
                    Places.getPlacesList().get(pos).setFavorite(0);
                    Log.d("1", "FAB: " + pos + " bool: " + Places.getPlacesList().get(pos).getFavorite());
                    active = false;
                }

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
                .load(url)
                .asBitmap()
                .priority(Priority.IMMEDIATE)
                .skipMemoryCache(true)
                .centerCrop()
                .into(new BitmapImageViewTarget(image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(getResources(), resource)});
                        assert image != null;
                        image.setImageDrawable(td);
                        td.startTransition(150);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        new Palette.Builder(resource).generate(paletteAsyncListener);
                    }
                });

        recyclerViewGallery.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false));

        int arraySize = 20;

        final String
                galleryURL[] = new String[arraySize],
                galleryUrlName[] = new String[arraySize],
                galleryUrlDesc[] = new String[arraySize];

        galleryURL[0] = item.getUrl_a();
        galleryURL[1] = item.getUrl_b();
        galleryURL[2] = item.getUrl_c();
        galleryURL[3] = item.getUrl_d();
        galleryURL[4] = item.getUrl_e();
        galleryURL[5] = item.getUrl_f();
        galleryURL[6] = item.getUrl_g();
        galleryURL[7] = item.getUrl_h();
        galleryURL[8] = item.getUrl_i();
        galleryURL[9] = item.getUrl_j();
        galleryURL[10] = item.getUrl_k();
        galleryURL[11] = item.getUrl_l();
        galleryURL[12] = item.getUrl_m();
        galleryURL[13] = item.getUrl_n();
        galleryURL[14] = item.getUrl_o();
        galleryURL[15] = item.getUrl_p();
        galleryURL[16] = item.getUrl_q();
        galleryURL[17] = item.getUrl_r();
        galleryURL[18] = item.getUrl_s();
        galleryURL[19] = item.getUrl_t();

        galleryUrlName[0] = item.getUrl_a_title();
        galleryUrlName[1] = item.getUrl_b_title();
        galleryUrlName[2] = item.getUrl_c_title();
        galleryUrlName[3] = item.getUrl_d_title();
        galleryUrlName[4] = item.getUrl_e_title();
        galleryUrlName[5] = item.getUrl_f_title();
        galleryUrlName[6] = item.getUrl_g_title();
        galleryUrlName[7] = item.getUrl_h_title();
        galleryUrlName[8] = item.getUrl_i_title();
        galleryUrlName[9] = item.getUrl_j_title();
        galleryUrlName[10] = item.getUrl_k_title();
        galleryUrlName[11] = item.getUrl_l_title();
        galleryUrlName[12] = item.getUrl_m_title();
        galleryUrlName[13] = item.getUrl_n_title();
        galleryUrlName[14] = item.getUrl_o_title();
        galleryUrlName[15] = item.getUrl_p_title();
        galleryUrlName[16] = item.getUrl_q_title();
        galleryUrlName[17] = item.getUrl_r_title();
        galleryUrlName[18] = item.getUrl_s_title();
        galleryUrlName[19] = item.getUrl_t_title();

        galleryUrlDesc[0] = item.getUrl_a_desc();
        galleryUrlDesc[1] = item.getUrl_b_desc();
        galleryUrlDesc[2] = item.getUrl_c_desc();
        galleryUrlDesc[3] = item.getUrl_d_desc();
        galleryUrlDesc[4] = item.getUrl_e_desc();
        galleryUrlDesc[5] = item.getUrl_f_desc();
        galleryUrlDesc[6] = item.getUrl_g_desc();
        galleryUrlDesc[7] = item.getUrl_h_desc();
        galleryUrlDesc[8] = item.getUrl_i_desc();
        galleryUrlDesc[9] = item.getUrl_j_desc();
        galleryUrlDesc[10] = item.getUrl_k_desc();
        galleryUrlDesc[11] = item.getUrl_l_desc();
        galleryUrlDesc[12] = item.getUrl_m_desc();
        galleryUrlDesc[13] = item.getUrl_n_desc();
        galleryUrlDesc[14] = item.getUrl_o_desc();
        galleryUrlDesc[15] = item.getUrl_p_desc();
        galleryUrlDesc[16] = item.getUrl_q_desc();
        galleryUrlDesc[17] = item.getUrl_r_desc();
        galleryUrlDesc[18] = item.getUrl_s_desc();
        galleryUrlDesc[19] = item.getUrl_t_desc();

        int gallerySize = -1;

        for (int j = 0; j < arraySize; j++) {
//            Log.d("j", "onCreate: " + j);
            if (galleryURL[j].length() > 5) {
                gallerySize++;
                Log.d("Schleife", "onCreate: " + gallerySize);
            }
        }

        PlaceInfoGallery placeDetailGalleries[] = new PlaceInfoGallery[gallerySize];

        for (int i = 0; i < gallerySize; i++) {
            placeDetailGalleries[i] = new PlaceInfoGallery(galleryURL[i], galleryUrlName[i], galleryUrlDesc[i]);
        }

        final String
                galleryURLNew[] = new String[gallerySize],
                galleryUrlNameNEW[] = new String[gallerySize],
                galleryUrlDescNEw[] = new String[gallerySize];


        for (int i = 0; i < gallerySize; i++) {
                    galleryURLNew[i] = galleryURL[i];
                    galleryUrlNameNEW[i] = galleryUrlName[i];
                    galleryUrlDescNEw[i] = galleryUrlDesc[i];
        }

        final GalleryAdapter galleryAdapter = new GalleryAdapter(context, placeDetailGalleries, new GalleryAdapter.ClickListener() {

            @Override
            public void onClick(GalleryAdapter.ViewHolder view, int index, boolean longClick) {

                if (longClick) {

                    Log.d("Lang", "onClick: with ID " + index);

                } else {

                    Intent intent = new Intent(context, GalleryImage.class);
                    intent.putExtra("URLs", galleryURLNew);
                    intent.putExtra("URLsName", galleryUrlNameNEW);
                    intent.putExtra("URLsDesc", galleryUrlDescNEw);
                    intent.putExtra("index", index);
                    context.startActivity(intent);

                    Log.d("Kurz", "onClick: with ID " + index);
                }
            }
        });

        recyclerViewGallery.setNestedScrollingEnabled(true);
        recyclerViewGallery.setClipToPadding(false);
        recyclerViewGallery.setAdapter(galleryAdapter);
        recyclerViewGallery.setHasFixedSize(true);
    }

    //  TODO - add more sight depending layouts for variety and more info
    private void sightDependingLayouts() {

        if (sight.equals("City")) {
            PlaceInfo itemsData[] = {
                    new PlaceInfo("Location", continent, R.drawable.ic_location),
                    new PlaceInfo("Location", continent, R.drawable.ic_location),
                    new PlaceInfo("Location", continent, R.drawable.ic_location),
                    new PlaceInfo("Religion", religion, R.drawable.ic_religion),
            };
            finishRecycler(recyclerViewDetail, itemsData);
        }

        if (sight.equals("National Park")) {
            PlaceInfo itemsData[] = {
                    new PlaceInfo("Location", continent, R.drawable.ic_location),
            };
            finishRecycler(recyclerViewDetail, itemsData);
        }


    }

    private void finishRecycler(RecyclerView recyclerView, PlaceInfo itemsData[]) {
        recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
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

    private void closeViewer() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            supportFinishAfterTransition();
        } else {
            finish();
        }
    }

    public void downloadImage() {

        final DownloadImage downloadTask = new DownloadImage(context, location);
        downloadTask.execute(url);

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
