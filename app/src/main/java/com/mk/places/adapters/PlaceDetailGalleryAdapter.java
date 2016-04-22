package com.mk.places.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mk.places.R;
import com.mk.places.models.PlaceDetailGallery;
import com.mk.places.widgets.SquareImageView;

public class PlaceDetailGalleryAdapter extends RecyclerView.Adapter<PlaceDetailGalleryAdapter.ViewHolder> {
    private PlaceDetailGallery[] URL;
    private Context context;
    private final ClickListener clickListener;

    public PlaceDetailGalleryAdapter(Context context, PlaceDetailGallery[] URL, ClickListener callBack) {
        this.context = context;
        this.URL = URL;
        this.clickListener = callBack;
    }


    @Override
    public PlaceDetailGalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_places_detail_gallery_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        if (URL[position] != null) {
        Glide.with(context)
                .load(URL[position].getImageURL())
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(viewHolder.image);
        }
    }

    @Override
    public int getItemCount() {
        return URL.length;
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

            image = (ImageView) view.findViewById(R.id.imageURL);
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
