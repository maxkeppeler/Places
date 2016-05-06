package com.mk.places.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.places.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private String[] imageLink;
    private Context context;
    private final ClickListener clickListener;

    public GalleryAdapter(Context context, String[] imageLink, ClickListener callBack) {
        this.context = context;
        this.imageLink = imageLink;
        this.clickListener = callBack;
    }


    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_places_item_gallery, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder v, int position) {

        if (imageLink[position] == null) return;
        Glide.with(context)
                .load(imageLink[position])
                .override(800, 800)
                .centerCrop()
                .priority(Priority.IMMEDIATE)
                .into(v.imageView);


    }

    @Override
    public int getItemCount() {
        return imageLink.length;
    }

    public interface ClickListener {
        void onClick(ViewHolder view, int index, boolean longClick);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final ImageView imageView;
        private final View mView;

        public ViewHolder(View v) {
            super(v);
            mView = v;

            imageView = (ImageView) mView.findViewById(R.id.thumbImage);
            imageView.setOnClickListener(this);
            imageView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            if (clickListener != null)
                clickListener.onClick(this, index, false);
        }

        @Override
        public boolean onLongClick(View v) {
            int index = getLayoutPosition();
            if (clickListener != null)
                clickListener.onClick(this, index, true);
            return true;
        }
    }




}
