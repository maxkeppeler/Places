package com.mk.places.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import com.mk.places.R;
import com.mk.places.adapters.GalleryItemAdapter;

public class GalleryView extends AppCompatActivity {

    private Activity context = this;
    public static ViewPager pager;
    public static GalleryItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery_view);

//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);

//      TODO: Add translucent toolbar at the top of the nav bar with a apply and download option

        Intent intent = getIntent();

        pager = (ViewPager) findViewById(R.id.galleryViewPager);
        adapter = new GalleryItemAdapter(context, intent.getStringArrayExtra("imageLink"));
        pager.setOffscreenPageLimit(0);

        pager.setAdapter(adapter);
        pager.setCurrentItem(intent.getIntExtra("index", 0));

    }
}
