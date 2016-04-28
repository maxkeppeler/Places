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
import com.mk.places.widgets.TouchImageView;

import butterknife.Bind;

public class AboutMemberAdapter extends PagerAdapter {

    private String[] profileImage;
    private String[] profileName;
    private String[] profileDescription;
    private LayoutInflater inflater;
    private Context context;

    public AboutMemberAdapter(String[] profileImage, String[] profileName, String[] profileDescription, Context context) {
        this.profileImage = profileImage;
        this.profileName = profileName;
        this.profileDescription = profileDescription;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return profileImage.length;
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {

        View imageLayout = inflater.inflate(R.layout.drawer_about_member, view, false);

        final TouchImageView imageView = (TouchImageView) imageLayout.findViewById(R.id.imageView);
        final TextView pName = (TextView) imageLayout.findViewById(R.id.profileName);
        final TextView pDesc = (TextView) imageLayout.findViewById(R.id.profileDescription);

        pName.setText(profileName[position]);
        pDesc.setText(profileDescription[position]);

//        if (profileImage[position].length() > 3)
//        Glide.with(context)
//                .load(profileImage[position])
//                .asBitmap()
//                .override(2000, 2000)
////                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .sizeMultiplier(0.3f)
////                .skipMemoryCache(true)
////                .centerCrop()
//                .priority(Priority.HIGH)
//                .into(new BitmapImageViewTarget(imageView) {
//                    @Override
//                    protected void setResource(Bitmap resource) {
//                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(context.getResources(), resource)});
//                        assert imageView != null;
//                        imageView.setImageDrawable(td);
//                        td.startTransition(350);
//                    }
//                });

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
