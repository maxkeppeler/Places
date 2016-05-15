package com.mk.places.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mk.places.R;
import com.mk.places.adapters.GalleryItemAdapter;
import com.mk.places.threads.DownloadImage;
import com.mk.places.utilities.Utils;

public class GalleryView extends AppCompatActivity {

    private static Toolbar toolbar;
    private Activity context = this;
    private String[] imageLinks;
    private int truePosition;
    private String location;
    private CoordinatorLayout layout;

    private static final int PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.place_item_gallery_view);
        Intent intent = getIntent();

        int index = intent.getIntExtra("index", 0);
        int userPosition = index + 1;
        imageLinks = intent.getStringArrayExtra("imageLink");
        location = intent.getStringExtra("location");
        layout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);

//        TODO: Snackbar is behind the Navigation Bar

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setTitle("Image " + userPosition + "/" + imageLinks.length);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ViewPager pager = (ViewPager) findViewById(R.id.galleryViewPager);
        pager.setAdapter(new GalleryItemAdapter(context, imageLinks));
        pager.setCurrentItem(index);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                truePosition = i;
                int userPosition = i + 1;
                toolbar.setTitle("Image " + userPosition + "/" + imageLinks.length);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.action_place_image, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                close();
                break;

            case R.id.download:

                if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED) {
                    getStoragePermission();
                } else downloadImage(location + String.valueOf(truePosition), truePosition);

                break;
        }
        return true;
    }

    private void close() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            supportFinishAfterTransition();
        else finish();
    }

    public void downloadImage(String location, int index) {

        final DownloadImage downloadTask = new DownloadImage(context, location, Utils.getColor(context, R.color.transparentBit));
        downloadTask.execute(imageLinks[index]);

    }

//    PERMISSION METHODS

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {

            case PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    downloadImage(location, 0);
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
                                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
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
                    .canceledOnTouchOutside(false)
                    .contentColor(Utils.getColor(context, R.color.primaryText))
                    .backgroundColor(Utils.getColor(context, R.color.cardBackground))
//                    .positiveText(R.string.storagePositive).positiveColor(color)
//                    .negativeText(R.string.storageNegative).negativeColor(color)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE);
        }
    }



}