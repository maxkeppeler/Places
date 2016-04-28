package com.mk.places.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.places.R;
import com.mk.places.models.Place;
import com.mk.places.widgets.TouchImageView;

import org.w3c.dom.Text;

public class GalleryViewAdapter extends PagerAdapter {


    private String[] gImage;
    private String[] gImageName;
    private String[] gImageDesc;
    private LayoutInflater inflater;
    private Context context;
    private Place item;


    public GalleryViewAdapter(Context context, String[] gImage, String[] gImageName, String[] gImageDesc) {
        this.context = context;
        this.gImage = gImage;
        inflater = LayoutInflater.from(context);
        this.gImageName = gImageName;
        this.gImageDesc = gImageDesc;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return gImage.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {

        View imageLayout = inflater.inflate(R.layout.image_swipe_fragment, view, false);

        final TouchImageView imageView = (TouchImageView) imageLayout.findViewById(R.id.imageView);
        final TextView viewDescription = (TextView) imageLayout.findViewById(R.id.imageDescription);
        final TextView viewName = (TextView) imageLayout.findViewById(R.id.imageName);

        viewName.setText(gImageName[position]);
        viewDescription.setText(gImageDesc[position]);

        if (gImage[position].length() > 3)
        Glide.with(context)
                .load(gImage[position])
                .asBitmap()
                .override(2000, 2000)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .sizeMultiplier(0.3f)
//                .skipMemoryCache(true)
                .priority(Priority.HIGH)
                .into(new BitmapImageViewTarget(imageView) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(context.getResources(), resource)});
                        assert imageView != null;
                        imageView.setImageDrawable(td);
                        td.startTransition(350);


                    }
                });


//        gImageDesc

        view.addView(imageLayout, 0);
        return imageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }




}
