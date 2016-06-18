package com.earth.places.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.inquiry.Inquiry;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.earth.places.R;
import com.earth.places.activities.MainActivity;
import com.earth.places.fragment.FragmentBookmarks;
import com.earth.places.models.Place;
import com.earth.places.models.Places;
import com.earth.places.utilities.Bookmarks;
import com.earth.places.utilities.Constants;
import com.earth.places.utilities.Utils;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;

import java.util.ArrayList;

public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.PlacesViewHolder> {

    private ClickListener clickListener;
    private ArrayList<Place> placesList;
    private Activity context;
    private IconicsDrawable iconBookmark;
    private IconicsDrawable iconBookmarkBorder;

    private IconicsDrawable city, country, nationalPark, map, misc;

    public PlaceAdapter(Activity context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;

        // Create Drawables just one time when the Adapter is initialized, not for each PlacesViewHolder
        iconBookmark = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_bookmark).color(Color.WHITE).sizeDp(24);
        iconBookmarkBorder = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_bookmark_border).color(Color.WHITE).sizeDp(24);

        final int size = 18;
        final int color = ContextCompat.getColor(context, R.color.white);
        city = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_location_city).color(color).sizeDp(size);
        country = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_terrain).color(color).sizeDp(size);
        nationalPark = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_nature).color(color).sizeDp(size);
        map = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_map).color(color).sizeDp(size);
        misc = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_more).color(color).sizeDp(size);

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
    public void onBindViewHolder(final PlacesViewHolder holder, final int index) {

        final Place place = placesList.get(index);
        final String sight = place.getSight();
        final String[] url = place.getUrl().replace(" ", "").split("\\|");

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                Glide.with(context.getApplicationContext())
                        .load(url[0])
                        .asBitmap()
                        .override(1200, 900)
                        .centerCrop()
                        .priority(Priority.IMMEDIATE)
                        .listener(new RequestListener<String, Bitmap>() {
                            @Override
                            public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                                Log.d("PlaceView", "Toolbar Image onException: ");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                holder.layout.setVisibility(View.VISIBLE);

                                new Palette.Builder(resource).generate(new Palette.PaletteAsyncListener() {
                                    @Override
                                    public void onGenerated(Palette palette) {

                                        if (palette == null) return;
                                        int color = Utils.colorFromPalette(context, palette);
                                        Places.getPlacesList().get(index).setColor(color);
                                    }
                                });

                                return false;
                            }
                        })
                        .into(holder.image);
            }
        });

        thread.setPriority(Thread.MAX_PRIORITY);
        thread.run();



        if (MainActivity.tabLayout.getTabAt(0).isSelected()) {
            Bookmarks.init(context);
            holder.stateReaction(Bookmarks.isBookmarked(Places.getPlacesList().get(index).getId()), false);
            Inquiry.deinit();
        }

        if (MainActivity.tabLayout.getTabAt(1).isSelected())
            holder.drawableBookmark.setImageDrawable(iconBookmark);

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



            drawableBookmark.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (MainActivity.tabLayout.getTabAt(0).isSelected())
                        stateReaction(Bookmarks.bookmark(Places.getPlacesList().get(getLayoutPosition()).getId()), true);

                    if (MainActivity.tabLayout.getTabAt(1).isSelected())
                        stateReaction(Bookmarks.bookmark(FragmentBookmarks.getBookmarks().get(getLayoutPosition()).getId()), true);

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            FragmentBookmarks.loadBookmarks(context);
                        }
                    }).run();

                }
            });

        }

        private void stateReaction(boolean state, boolean showSnackBar) {

            if(state) {

                if (showSnackBar)
                Utils.showSnackBar(context, ContextCompat.getColor(context, R.color.cardBackground), R.id.coordinatorLayout, R.string.bookmarkedPlace, Snackbar.LENGTH_LONG);

                drawableBookmark.setImageDrawable(iconBookmark);
            }

            else {
                if (showSnackBar)
                Utils.showSnackBar(context, ContextCompat.getColor(context, R.color.cardBackground), R.id.coordinatorLayout, R.string.removedPlace, Snackbar.LENGTH_LONG);

                drawableBookmark.setImageDrawable(iconBookmarkBorder);
            }

        }

        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            if (clickListener != null)
                clickListener.onClick(this, index);
        }

    }


}