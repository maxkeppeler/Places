package com.mk.places.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.places.R;
import com.mk.places.models.Place;
import com.mk.places.utilities.AnimUtils;
import com.mk.places.utilities.Utils;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlacesViewHolder> {


    private ClickListener callback;
    private ArrayList<Place> placesList;
    private Activity context;
    private AppCompatDelegate mDelegate;

    public PlaceAdapter(Activity context, ClickListener callBack) {
        this.context = context;
        this.callback = callBack;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int index) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return new PlacesViewHolder(inflater.inflate(R.layout.drawer_places_item, parent, false));
    }

    public void setData(ArrayList<Place> placesList) {

        this.placesList = placesList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final PlacesViewHolder holder, int index) {

        Place place = placesList.get(index);
        final String imgPlaceUrl = place.getUrl();

        Glide.with(context)
                .load(imgPlaceUrl)
                .asBitmap()
                .override(1022, 784)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(new BitmapImageViewTarget(holder.image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(context.getResources(), resource)});
                        holder.image.setImageDrawable(td);
                        td.startTransition(350);
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);
                        holder.viewShadow.setVisibility(View.VISIBLE);
                    }
                });

        if (context.getResources().getBoolean(R.bool.placesZoomItems)) {
            AnimUtils.zoomInAndOut(context, holder.image);
        }

        holder.location.setText(place.getLocation());
        holder.sight.setText(place.getSight());
        holder.continent.setText(place.getContinent());

        if (holder.sight.getText().equals("City"))
            holder.sightDrawable.setBackground(context.getResources().getDrawable(R.drawable.sight_city));

        else if (holder.sight.getText().equals("Country"))
            holder.sightDrawable.setBackground(context.getResources().getDrawable(R.drawable.sight_country));

        else if (holder.sight.getText().equals("National Park"))
            holder.sightDrawable.setBackground(context.getResources().getDrawable(R.drawable.sight_national_park));

        else if (holder.sight.getText().equals("Park"))
            holder.sightDrawable.setBackground(context.getResources().getDrawable(R.drawable.sight_park));

    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public interface ClickListener {
        void onClick(PlacesViewHolder view, int index, boolean longClick);
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public final View view;
        public final ImageView image;
        public final TextView location, sight, continent;
        public final ImageView sightDrawable;
        private MaterialRippleLayout ripple;
        private View viewShadow;

        PlacesViewHolder(View v) {
            super(v);
            view = v;

            ripple = (MaterialRippleLayout) view.findViewById(R.id.rippleOverlay);
            image = (ImageView) view.findViewById(R.id.placeImage);
            viewShadow = (View) view.findViewById(R.id.schadowBelow);
            location = (TextView) view.findViewById(R.id.locationText);

            location.setTypeface(Utils.customTypeface(context, 1));
            sight = (TextView) view.findViewById(R.id.sightText);

            sight.setTypeface(Utils.customTypeface(context, 2));
            sightDrawable = (ImageView) view.findViewById(R.id.sightImage);

            continent = (TextView) view.findViewById(R.id.continentText);
            continent.setTypeface(Utils.customTypeface(context, 2));

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