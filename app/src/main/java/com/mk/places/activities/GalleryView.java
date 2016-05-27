package com.mk.places.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import com.mk.places.utilities.Constants;

public class GalleryView extends AppCompatActivity {

    private Toolbar toolbar;
    private Activity context;
    private String[] urls;
    private int position;
    private String location;
    private ViewPager layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.place_item_gallery_view);
//        TODO: Translucent Navigation Bar while Snackbar will still be displayed over it.

        context = this;

        Intent intent = getIntent();
        int index = intent.getIntExtra("index", 0);
        int userPosition = index + 1;
        urls = intent.getStringArrayExtra("urls");
        location = intent.getStringExtra("place");
        layout = (ViewPager) findViewById(R.id.viewPager);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setTitle("Image " + userPosition + "/" + urls.length);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        ViewPager pager = (ViewPager) findViewById(R.id.galleryViewPager);
        pager.setAdapter(new GalleryItemAdapter(context, urls));
        pager.setCurrentItem(index);
        pager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {

            }

            @Override
            public void onPageSelected(int i) {
                position = i;
                int userPosition = i + 1;
                toolbar.setTitle("Image " + userPosition + "/" + urls.length);
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear(); // clear previous menu from the last activity
        getMenuInflater().inflate(R.menu.action_place_image, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                close();
                break;

            case R.id.save:

                if (ContextCompat.checkSelfPermission(context, Constants.PERMISSION_WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    getStoragePermission();
                } else downloadImage(location + String.valueOf(position), position);

                break;
        }
        return true;
    }

    /**
     * Close current activity correctly
     */
    private void close() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            supportFinishAfterTransition();
        else finish();
    }


    /**
     * Download image at specific index in a new async task. Image will be named after the location.
     * @param location
     * @param index
     */
    public void downloadImage(String location, int index) {

        new DownloadImage(context, location, ContextCompat.getColor(context, R.color.transparent1)).execute(urls[index]);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {

            case Constants.PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE: {
                if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    downloadImage(location, 0);
                    Log.d("requestPermission", "Write External Storage: Permission granted.");

                } else {

                    //Show snack bar if check never ask again
                    Log.d("requestPermission", "Write External Storage: Permission NOT granted.");

                    if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Constants.PERMISSION_WRITE_EXTERNAL_STORAGE)) {
//                        TODO
                        Snackbar.make(layout, "sdad",
                                Snackbar.LENGTH_LONG)
                                .setAction("dasdas", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + getPackageName()));
                                        intent.addCategory(Intent.CATEGORY_DEFAULT);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivityForResult(intent, Constants.PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE);
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
                Constants.PERMISSION_WRITE_EXTERNAL_STORAGE)) {

            new MaterialDialog.Builder(this)
                    .content(R.string.storageContent)
                    .canceledOnTouchOutside(false)
                    .contentColor(ContextCompat.getColor(context, R.color.textLevel1))
                    .backgroundColor(ContextCompat.getColor(context, R.color.cardBackground))
                    .positiveText(R.string.storagePositive)
                    .negativeText(R.string.storageNegative)
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                            ActivityCompat.requestPermissions(context, new String[]{Constants.PERMISSION_WRITE_EXTERNAL_STORAGE},
                                    Constants.PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE);
                        }
                    })
                    .show();
        } else {

            ActivityCompat.requestPermissions(this, new String[]{Constants.PERMISSION_WRITE_EXTERNAL_STORAGE},
                    Constants.PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE);
        }
    }



}
