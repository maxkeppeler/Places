package com.mk.placesdrawer.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.models.PlacesItem;
import com.mk.placesdrawer.utilities.Animations;

import java.util.ArrayList;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    private final ClickListener callback;
    private ArrayList<PlacesItem> placesList;
    private Activity context;

    public PlacesAdapter(Activity context, ClickListener callBack) {
        this.context = context;
        this.callback = callBack;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int index) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new PlacesViewHolder(inflater.inflate(R.layout.drawer_places_item, parent, false));
    }

    public void setData(ArrayList<PlacesItem> placesList) {
        this.placesList = placesList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final PlacesViewHolder holder, int index) {

        PlacesItem placeItem = placesList.get(index);
        final String imgPlaceUrl = placeItem.getImgPlaceUrl();

        Glide.with(context)
                .load(imgPlaceUrl)
                .asBitmap()
//                .override(1022, 784)
//                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                .skipMemoryCache(true)
                .into(new BitmapImageViewTarget(holder.image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(context.getResources(), resource)});
                        holder.image.setImageDrawable(td);
                        td.startTransition(550);
                    }
                });

        if (context.getResources().getBoolean(R.bool.placesZoomItems)) {
            Animations.zoomInAndOut(context, holder.image);
        }

        holder.location.setText(placeItem.getLocation());
        holder.sight.setText(placeItem.getSight());
    }

    @Override
    public int getItemCount() {
        return placesList == null ? 0 : placesList.size();
    }


    public interface ClickListener {

        void onClick(PlacesViewHolder view, int index, boolean longClick);
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/BreeSerif-Regular.ttf");


        public final View view;
        public final ImageView image;
        public final TextView location, sight, desc;
        public final TextView country, state, city, religion;
        private MaterialRippleLayout ripple;

        PlacesViewHolder(View v) {
            super(v);
            view = v;


            ripple = (MaterialRippleLayout) view.findViewById(R.id.rippleThumb);
            image = (ImageView) view.findViewById(R.id.imageThumb);
            location = (TextView) view.findViewById(R.id.locationThumb);
                location.setTypeface(typeface);
            sight = (TextView) view.findViewById(R.id.sightThumb);

            desc = null;
            country = null;
            state = null;
            city = null;
            religion = null;

            ripple.setOnClickListener(this);
            ripple.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            if (callback != null)
                callback.onClick(this, index, false);
        }

        @Override
        public boolean onLongClick(View v) {
            int index = getLayoutPosition();
            if (callback != null)
                callback.onClick(this, index, true);
            return true;
        }
    }
}