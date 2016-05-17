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
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Interpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mk.places.R;
import com.mk.places.activities.MainActivity;
import com.mk.places.models.Place;
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
        final String[] imgPlaceUrl = place.getUrl().replace(" ", "").split("\\|");

                Glide.with(context)
                        .load(imgPlaceUrl[0])
                        .crossFade()
                        .override(1000, 800)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .into(holder.image);

        holder.viewShadow.setVisibility(View.VISIBLE);

        holder.location.setText(place.getLocation());
        holder.sight.setText(place.getSight());
        holder.continent.setText(place.getContinent());


        final int size = 18;
        final int color = context.getResources().getColor(R.color.white);

        IconicsDrawable city = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_location_city).color(color).sizeDp(size);
        IconicsDrawable country = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_terrain).color(color).sizeDp(size);
        IconicsDrawable nationalPark = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_nature).color(color).sizeDp(size);
        IconicsDrawable park = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_nature_people).color(color).sizeDp(size);
        IconicsDrawable more = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_more).color(color).sizeDp(size);


        if (holder.sight.getText().equals(MainActivity.SIGHT_CITY))
            holder.sightDrawable.setBackground(city);

        if (holder.sight.getText().equals(MainActivity.SIGHT_COUNTRY))
            holder.sightDrawable.setBackground(country);

        if (holder.sight.getText().equals(MainActivity.SIGHT_NATIONAL_PARK))
            holder.sightDrawable.setBackground(nationalPark);

        if (holder.sight.getText().equals(MainActivity.SIGHT_PARK))
            holder.sightDrawable.setBackground(park);

        if (holder.sight.getText().equals(MainActivity.SIGHT_BEACH))
            holder.sightDrawable.setBackground(more);

        if (holder.sight.getText().equals(MainActivity.SIGHT_LAKE))
            holder.sightDrawable.setBackground(more);

        if (holder.sight.getText().equals(MainActivity.SIGHT_DESERT))
            holder.sightDrawable.setBackground(more);

        if (holder.sight.getText().equals(MainActivity.SIGHT_GEYSER))
            holder.sightDrawable.setBackground(more);

        if (holder.sight.getText().equals(MainActivity.SIGHT_LANDFORM))
            holder.sightDrawable.setBackground(more);

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
        private LinearLayout viewShadow;

        PlacesViewHolder(View v) {
            super(v);
            view = v;

            ripple = (MaterialRippleLayout) view.findViewById(R.id.rippleOverlay);
            image = (ImageView) view.findViewById(R.id.placeImage);
            viewShadow = (LinearLayout) view.findViewById(R.id.schadowBelow);
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