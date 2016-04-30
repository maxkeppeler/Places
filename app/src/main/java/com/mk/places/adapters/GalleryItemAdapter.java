package com.mk.places.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.places.R;
import com.mk.places.utilities.Utils;
import com.mk.places.views.TouchImageView;

public class GalleryItemAdapter extends PagerAdapter {

    private Context context;
    private String[] imageLink;
    private String[] imageName;
    private String[] imageDesc;
    private LayoutInflater inflater;
    private Window window;

    public GalleryItemAdapter(Context context, String[] imageLink, String[] imageName, String[] imageDesc, Window window) {
        this.context = context;
        this.imageLink = imageLink;
        this.inflater = LayoutInflater.from(context);
        this.imageName = imageName;
        this.imageDesc = imageDesc;
        this.window = window;
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
    public Object instantiateItem(ViewGroup view, final int index) {

        final View mView = inflater.inflate(R.layout.gallery_view_item, view, false);
        final TouchImageView vImage = (TouchImageView) mView.findViewById(R.id.vImage);
        final TextView vName = (TextView) mView.findViewById(R.id.vName);
        final TextView vDesc = (TextView) mView.findViewById(R.id.vDesc);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.setNavigationBarColor(context.getResources().getColor(R.color.appBackgroundColor));

        vName.setText(Html.fromHtml(imageName[index]).toString().replace("â€“", "–").replace("â€™", "\"").replace("â€™", "\"").replace("â€˜", "\"").replace("\\n", "\n").replace("\\", ""));
        vDesc.setText(Html.fromHtml(imageDesc[index]).toString().replace("â€“", "–").replace("â€™", "\"").replace("â€™", "\"").replace("â€˜", "\"").replace("\\n", "\n").replace("\\", ""));

        vName.setTypeface(Utils.customTypeface(context, 1));
        vDesc.setTypeface(Utils.customTypeface(context, 2));

        Glide.with(context)
                .load(imageLink[index])
                .asBitmap()
                .override(2000, 2000)
                .sizeMultiplier(0.4f)
                .skipMemoryCache(true)
                .priority(Priority.HIGH)
                .into(new BitmapImageViewTarget(vImage) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(context.getResources(), resource)});
                        assert vImage != null;
                        vImage.setImageDrawable(td);
                        td.startTransition(450);
                    }
                });

        view.addView(mView, 0);
        return mView;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }


}
