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
import android.support.v7.widget.LinearLayoutManager;
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
import com.mk.places.adapters.PlaceInfoAdapter;
import com.mk.places.models.InfoItem;
import com.mk.places.models.Place;
import com.mk.places.threads.DownloadImage;
import com.mk.places.utilities.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaceView extends AppCompatActivity {


    private static final int PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE = 42;
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
    private int color;
    private Window window;
    private String location, desc;
    private String[] infoTitle, info;
    private ViewGroup layout;
    private Activity context;
    private String[] images;

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
        location = item.getLocation();
        desc = item.getDescription();
        images = item.getUrl().replace(" ", "").split("\\|");
        info = item.getInfo().split("\\|");
        infoTitle = item.getInfoTitle().split("\\|");

        Glide.with(context)
                .load(images[0])
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

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    window.setNavigationBarColor(color);
                                }


                            }
                        });
                    }

                });


        InfoItem[] infoItems = new InfoItem[infoTitle.length];

        for (int i = 0; i < infoTitle.length; i++)
            infoItems[i] = new InfoItem(infoTitle[i], info[i]);

        pInfoRecycler.setLayoutManager(new LinearLayoutManager(context));
        PlaceInfoAdapter mAdapter = new PlaceInfoAdapter(infoItems, context);
        pInfoRecycler.setAdapter(mAdapter);
        pInfoRecycler.setHasFixedSize(true);


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

                Bookmarks.init(context);
                Bookmarks.favoriteItem(item.getId());
                Log.d("FAB", " with ID: " + item.getId() + " Selected: " + Bookmarks.isFavorited(item.getId()));
                Inquiry.deinit();

                Utils.simpleSnackBar(context, color, R.id.coordinatorLayout, R.string.snackbarFavoredText, Snackbar.LENGTH_SHORT);
            }
        });

        toolbarLayout.setTitle(location);
        toolbarLayout.setCollapsedTitleTypeface(typeTitles);
        toolbarLayout.setExpandedTitleTypeface(typeTitles);
        createGallery();
    }


    private void createGallery() {


        final GalleryAdapter galleryAdapter = new GalleryAdapter(context, images, new GalleryAdapter.ClickListener() {

            @Override
            public void onClick(GalleryAdapter.ViewHolder view, int index) {

                Intent intent = new Intent(context, GalleryView.class);
                intent.putExtra("imageLink", images);
                intent.putExtra("index", index);
                context.startActivity(intent);
            }
        });

        pGalleryRecycler.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false));
        pGalleryRecycler.setAdapter(galleryAdapter);
        pGalleryRecycler.setHasFixedSize(true);

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
        downloadTask.execute(images[0]);

        View layout = findViewById(R.id.coordinatorLayout);
        Snackbar snackbar = Snackbar.make(layout, R.string.snackbarDownloadImageText, Snackbar.LENGTH_LONG)
                .setActionTextColor(Utils.getColor(context, R.color.white))
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
