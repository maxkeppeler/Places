package com.mk.places.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
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
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        if (imageLink[position] == null) return;
        Glide.with(context)
                .load(imageLink[position])
                .asBitmap()
                .override(450, 450)
                .centerCrop()
                .into(new BitmapImageViewTarget(viewHolder.image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(context.getResources(), resource)});
                        assert viewHolder.image != null;
                        viewHolder.image.setImageDrawable(td);
                        td.startTransition(150);
                    }
                });
    }

    @Override
    public int getItemCount() {
        return imageLink.length;
    }

    public interface ClickListener {
        void onClick(ViewHolder view, int index, boolean longClick);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public final ImageView image;
        public final View view;

        public ViewHolder(View v) {
            super(v);
            view = v;

            image = (ImageView) view.findViewById(R.id.thumbImage);
            image.setOnClickListener(this);
            image.setOnLongClickListener(this);
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
