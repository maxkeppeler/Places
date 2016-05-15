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

        final View mView = inflater.inflate(R.layout.place_item_gallery_view_item, view, false);
        final ImageView vImage = (ImageView) mView.findViewById(R.id.vImage);

        Glide.with(context)
                .load(imageLink[i])
                .crossFade()
                .override(context.getWindowManager().getDefaultDisplay().getWidth(), context.getWindowManager().getDefaultDisplay().getHeight())
                .into(vImage);

        view.addView(mView, 0);
        return mView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}
