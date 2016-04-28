package com.mk.places.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.places.R;
import com.mk.places.models.Place;
import com.mk.places.utilities.Utils;
import com.mk.places.widgets.TouchImageView;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Text;

public class GalleryViewAdapter extends PagerAdapter {


    private String[] gImage;
    private String[] gImageName;
    private String[] gImageDesc;
    private LayoutInflater inflater;
    private Context context;
    private Window window;
    private static int color = 0;
    LinearLayout viewPanel;
    SlidingUpPanelLayout viewPanel2;


    public GalleryViewAdapter(Context context, String[] gImage, String[] gImageName, String[] gImageDesc, Window window) {
        this.context = context;
        this.gImage = gImage;
        inflater = LayoutInflater.from(context);
        this.gImageName = gImageName;
        this.gImageDesc = gImageDesc;
        this.window = window;
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

        View mainView = inflater.inflate(R.layout.image_swipe_fragment, view, false);

        final TouchImageView imageView = (TouchImageView) mainView.findViewById(R.id.imageView);
        final TextView viewDescription = (TextView) mainView.findViewById(R.id.imageDescription);
        final TextView viewName = (TextView) mainView.findViewById(R.id.imageName);
        viewPanel = (LinearLayout) mainView.findViewById(R.id.linearLayoutPanel);




        viewName.setText(gImageName[position]);
        viewDescription.setText(gImageDesc[position]);


        Log.d("das", "instantiateItem: " + position);

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


//                        Palette palette = new Palette.Builder(resource).generate();
//                        color = Utils.colorFromPalette(context, palette);

                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(context.getResources(), resource)});
                        assert imageView != null;
                        imageView.setImageDrawable(td);
                        td.startTransition(350);

                        new Palette.Builder(resource).generate(paletteAsyncListener);
                    }
                });


        view.addView(mainView, 0);
        return mainView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }

    public Palette.PaletteAsyncListener paletteAsyncListener = new Palette.PaletteAsyncListener() {
        @Override
        public void onGenerated(Palette palette) {
            if (palette == null) return;
            color = Utils.colorFromPalette(context, palette);
            if(0==0) {

            Log.d("Oben", "onGenerated: " + color);
            viewPanel.setBackgroundColor(color);

            Log.d("Unten", "onGenerated: " + color);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                window.setNavigationBarColor(color);

            };
        }
    };


}
