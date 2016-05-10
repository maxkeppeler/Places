package com.mk.places.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.media.Image;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.mk.places.R;
import com.mk.places.activities.GalleryView;
import com.mk.places.activities.PlaceView;
import com.mk.places.utilities.Utils;
import com.mk.places.views.TouchImageView;

public class GalleryItemAdapter extends PagerAdapter {

    private Activity context;
    private String[] imageLink;
    private LayoutInflater inflater;

    public GalleryItemAdapter(Activity context, String[] imageLink) {
        this.context = context;
        this.imageLink = imageLink;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int index, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imageLink.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int i) {

        final View mView = inflater.inflate(R.layout.gallery_view_item, view, false);
        final ImageView vImage = (ImageView) mView.findViewById(R.id.vImage);

        new Thread(new Runnable() {
            @Override
            public void run() {

                Glide.with(context)
                        .load(imageLink[i])
                        .crossFade()
                        .override(context.getWindowManager().getDefaultDisplay().getWidth(), context.getWindowManager().getDefaultDisplay().getHeight())
                        .into(vImage);

//                Call this if image download failed  and select current page again
//                GalleryView.pager.setAdapter(GalleryView.adapter);
            }
        }).run();

        view.addView(mView, 0);
        return mView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}
