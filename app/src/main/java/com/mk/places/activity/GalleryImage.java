package com.mk.places.activity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.mk.places.R;
import com.mk.places.adapters.GalleryViewAdapter;
import com.mk.places.models.Place;

public class GalleryImage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_page);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        final String[] URLs = intent.getStringArrayExtra("URLs");
        final String[] URLsName = intent.getStringArrayExtra("URLsName");
        final String[] URLsDesc = intent.getStringArrayExtra("URLsDesc");
        final int index = intent.getIntExtra("index", 0);

        ViewPager mPager = (ViewPager) findViewById(R.id.imageViewPager);
        mPager.setAdapter(new GalleryViewAdapter(getApplicationContext(), URLs, URLsName, URLsDesc, this.getWindow()));
//        mPager.setCurrentItem(index);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
