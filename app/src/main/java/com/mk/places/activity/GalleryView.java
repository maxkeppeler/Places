package com.mk.places.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.mk.places.R;
import com.mk.places.adapters.GalleryItemAdapter;

public class GalleryView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery_view);

//      TODO: Add translucent toolbar at the top of the nav bar with a apply and download option

        Intent intent = getIntent();

        ViewPager pager = (ViewPager) findViewById(R.id.galleryViewPager);
        pager.setAdapter(new GalleryItemAdapter(getApplicationContext(), intent.getStringArrayExtra("imageLink")));
        pager.setOffscreenPageLimit(1);
        pager.setCurrentItem(intent.getIntExtra("index", 0));

    }
}
