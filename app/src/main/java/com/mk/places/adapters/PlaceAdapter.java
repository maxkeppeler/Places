package com.mk.places.adapters;

import android.app.Activity;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.inquiry.Inquiry;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mk.places.R;
import com.mk.places.utilities.Bookmarks;
import com.mk.places.activities.MainActivity;
import com.mk.places.fragment.FragmentBookmarks;
import com.mk.places.models.Place;
import com.mk.places.models.Places;
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

        final IconicsDrawable city = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_location_city).color(color).sizeDp(size);
        final IconicsDrawable country = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_terrain).color(color).sizeDp(size);
        final IconicsDrawable nationalPark = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_nature).color(color).sizeDp(size);
        final IconicsDrawable map = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_map).color(color).sizeDp(size);

        final IconicsDrawable misc = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_more).color(color).sizeDp(size);

        holder.sight.setText(sight);
        holder.place.setText(place.getPlace());
        holder.continent.setText(place.getContinent());

        holder.place.setTypeface(Utils.customTypeface(context, 1));
        holder.sight.setTypeface(Utils.customTypeface(context, 2));
        holder.continent.setTypeface(Utils.customTypeface(context, 2));

        if (Utils.equalStrings(sight, Constants.SIGHT_CITY))
            holder.drawableSight.setBackground(city);

        else if (Utils.equalStrings(sight, Constants.SIGHT_COUNTRY))
            holder.drawableSight.setBackground(country);

        else if (Utils.equalStrings(sight, Constants.SIGHT_NATIONAL_PARK))
            holder.drawableSight.setBackground(nationalPark);

        else holder.drawableSight.setBackground(misc);

        holder.drawableContinent.setBackground(map);

        Glide.with(context.getApplicationContext())
                .load(url[0] != null ? url[0] :  place.getUrl())
                .override(1000, 700)
                .fitCenter()
                .priority(Priority.IMMEDIATE)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        holder.layout.setVisibility(View.VISIBLE);

                        return false;
                    }
                })
                .into(holder.image);



    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public interface ClickListener {
        void onClick(PlacesViewHolder v, int index);
    }

    public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final FrameLayout layout;
        private final ImageView image;
        private final TextView place, sight, continent;
        private final ImageView drawableSight, drawableContinent;
        private final ImageView drawableBookmark;

        PlacesViewHolder(View v) {
            super(v);

            layout = (FrameLayout) v.findViewById(R.id.layout);

            image = (ImageView) v.findViewById(R.id.thumbImage);

            image.setOnClickListener(this);

            place = (TextView) v.findViewById(R.id.thumbPlace);
            continent = (TextView) v.findViewById(R.id.thumbContinent);
            sight = (TextView) v.findViewById(R.id.thumbSight);

            drawableSight = (ImageView) v.findViewById(R.id.thumbSightDrawable);
            drawableContinent = (ImageView) v.findViewById(R.id.thumbContinentDrawable);

            drawableBookmark = (ImageView) v.findViewById(R.id.bookmarkIcon);
            drawableBookmark.setBackground(new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_bookmark).color(Color.WHITE).sizeDp(24));

            drawableBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (MainActivity.tabLayout.getTabAt(0).isSelected()) {

                        if (Bookmarks.bookmarkItem(Places.getPlacesList().get(getLayoutPosition()).getId())) {
                            Utils.showSnackBar(context, ContextCompat.getColor(context, R.color.cardBackground), R.id.coordinatorLayout, R.string.bookmarkedPlace, Snackbar.LENGTH_LONG);
                        } else
                            Utils.showSnackBar(context, ContextCompat.getColor(context, R.color.cardBackground), R.id.coordinatorLayout, R.string.removedPlace, Snackbar.LENGTH_LONG);
                    }

                    if (MainActivity.tabLayout.getTabAt(1).isSelected()) {

                        if (Bookmarks.bookmarkItem(FragmentBookmarks.bookmarks.get(getLayoutPosition()).getId())) {
                            Utils.showSnackBar(context, ContextCompat.getColor(context, R.color.cardBackground), R.id.coordinatorLayout, R.string.bookmarkedPlace, Snackbar.LENGTH_LONG);
                        } else
                            Utils.showSnackBar(context, ContextCompat.getColor(context, R.color.cardBackground), R.id.coordinatorLayout, R.string.removedPlace, Snackbar.LENGTH_LONG);
                    }


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FragmentBookmarks.loadBookmarks(context);
                            Inquiry.deinit();
                        }
                    }).run();

                }
            });

        }

        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            if (clickListener != null)
                clickListener.onClick(this, index);
        }

    }


}