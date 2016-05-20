package com.mk.places.adapters;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mk.places.R;

public class GalleryItemAdapter extends PagerAdapter {

    private Activity context;
    private String[] url;

    public GalleryItemAdapter(Activity context, String[] url) {
        this.context = context;
        this.url = url;
    }

    @Override
    public void destroyItem(ViewGroup container, int index, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return url.length;
    }

    @Override
    public Object instantiateItem(ViewGroup viewGroup, final int i) {

        final View view = LayoutInflater.from(context).inflate(R.layout.place_item_gallery_view_item, viewGroup, false);
        final ImageView vImage = (ImageView) view.findViewById(R.id.vImage);

        Glide.with(context)
                .load(url[i])
                .crossFade()
                .override(context.getWindowManager().getDefaultDisplay().getWidth(), context.getWindowManager().getDefaultDisplay().getHeight())
                .into(vImage);

        viewGroup.addView(view, 0);
        return view;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}
