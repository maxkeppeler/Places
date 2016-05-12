package com.mk.places.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.mk.places.R;
import com.mk.places.adapters.GalleryItemAdapter;

public class GalleryView extends AppCompatActivity {

    private static Toolbar toolbar;
    private Activity context = this;
    private String[] imageLinks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery_view);
        Intent intent = getIntent();

        int index = intent.getIntExtra("index", 0);
        int userPosition = index + 1;
        imageLinks = intent.getStringArrayExtra("imageLink");

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
        getMenuInflater().inflate(R.menu.toolbar_place, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                close();
                break;
        }
        return true;
    }

    private void close() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            supportFinishAfterTransition();
        else finish();
    }


}
