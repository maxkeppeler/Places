package com.mk.places.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.mk.places.R;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.ViewHolder> {

    private final ClickListener clickListener;
    private String[] url;
    private Context context;

    public GalleryAdapter(Context context, String[] url, ClickListener clickListener) {
        this.context = context;
        this.url = url;
        this.clickListener = clickListener;
    }

    @Override
    public GalleryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_gallery_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int index) {

        if (!url[index].isEmpty() || url.length > 3)
        Glide.with(context)
                .load(url[index])
                .override(800, 800)
                .centerCrop()
                .into(holder.viewImage);
    }

    @Override
    public int getItemCount() {
        return url.length;
    }

    public interface ClickListener {
        void onClick(ViewHolder view, int index, boolean longOnClick);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private final ImageView viewImage;

        public ViewHolder(View v) {
            super(v);

            viewImage = (ImageView) v.findViewById(R.id.viewImage);

            viewImage.setOnClickListener(this);
            viewImage.setOnLongClickListener(this);
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
