package com.mk.places.adapters;

import android.app.Activity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mk.places.R;
import com.mk.places.models.Place;
import com.mk.places.utilities.Constants;
import com.mk.places.utilities.Utils;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlacesViewHolder> {


    private ClickListener clickListener;
    private ArrayList<Place> placesList;
    private Activity context;

    public PlaceAdapter(Activity context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int index) {
        return new PlacesViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_places_item, parent, false));
    }

    public void setData(ArrayList<Place> placesList) {

        this.placesList = placesList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final PlacesViewHolder holder, int index) {

        final Place place = placesList.get(index);
        final String sight = place.getSight();

        final String[] url = place.getUrl().replace(" ", "").split("\\|");

        final int size = 18;
        final int color = ContextCompat.getColor(context, R.color.white);
        final IconicsDrawable
                city = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_location_city).color(color).sizeDp(size),
                country = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_terrain).color(color).sizeDp(size),
                nationalPark = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_nature).color(color).sizeDp(size),
                park = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_nature_people).color(color).sizeDp(size),
                misc = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_more).color(color).sizeDp(size);


        Glide.with(context)
                .load(url[0])
                .override(1000, 800)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        holder.shadow.setVisibility(View.VISIBLE);
                        return false;
                    }
                })
                .into(holder.image);

        holder.sight.setText(sight);
        holder.place.setText(place.getPlace());
        holder.continent.setText(place.getContinent());

        if (Utils.compareStrings(sight, Constants.SIGHT_CITY))
            holder.drawable.setBackground(city);

        if (Utils.compareStrings(sight, Constants.SIGHT_COUNTRY))
            holder.drawable.setBackground(country);

        if (Utils.compareStrings(sight, Constants.SIGHT_NATIONAL_PARK))
            holder.drawable.setBackground(nationalPark);

        if (Utils.compareStrings(sight, Constants.SIGHT_PARK))
            holder.drawable.setBackground(park);

        if (Utils.compareStrings(sight, Constants.SIGHT_BEACH))
            holder.drawable.setBackground(misc);

        if (Utils.compareStrings(sight, Constants.SIGHT_LAKE))
            holder.drawable.setBackground(misc);

        if (Utils.compareStrings(sight, Constants.SIGHT_DESERT))
            holder.drawable.setBackground(misc);

        if (Utils.compareStrings(sight, Constants.SIGHT_GEYSER))
            holder.drawable.setBackground(misc);

        if (Utils.compareStrings(sight, Constants.SIGHT_LANDFORM))
            holder.drawable.setBackground(misc);
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public interface ClickListener {
        void onClick(PlacesViewHolder v, int index);
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView image;
        private final TextView place, sight, continent;
        private final ImageView drawable;
        private final MaterialRippleLayout ripple;
        private final LinearLayout shadow;

        PlacesViewHolder(View v) {
            super(v);

            ripple = (MaterialRippleLayout) v.findViewById(R.id.thumbRipple);
            image = (ImageView) v.findViewById(R.id.thumbImage);
            shadow = (LinearLayout) v.findViewById(R.id.thumbShadow);
            place = (TextView) v.findViewById(R.id.thumbPlace);
            continent = (TextView) v.findViewById(R.id.thumbContinent);
            sight = (TextView) v.findViewById(R.id.thumbSight);
            drawable = (ImageView) v.findViewById(R.id.thumbSightDrawable);

            place.setTypeface(Utils.customTypeface(context, 1));
            sight.setTypeface(Utils.customTypeface(context, 2));
            continent.setTypeface(Utils.customTypeface(context, 2));

            ripple.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            if (clickListener != null)
                clickListener.onClick(this, index);
        }

    }


}