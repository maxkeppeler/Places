package com.mk.places.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.mk.places.R;
import com.mk.places.adapters.GalleryItemAdapter;

public class GalleryView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.gallery_view);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Intent intent = getIntent();
        final String[] imageLink = intent.getStringArrayExtra("imageLink");
        final String[] imageName = intent.getStringArrayExtra("imageName");
        final String[] imageDesc = intent.getStringArrayExtra("imageDesc");
        final int index = intent.getIntExtra("index", 0);

        ViewPager mPager = (ViewPager) findViewById(R.id.viewPagerImage);
        mPager.setAdapter(new GalleryItemAdapter(getApplicationContext(), imageLink, imageName, imageDesc, this.getWindow()));
        mPager.setCurrentItem(index);
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
