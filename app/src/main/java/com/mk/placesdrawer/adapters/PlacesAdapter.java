package com.mk.placesdrawer.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.model.PlacesItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    public interface ClickListener {

        void onClick(PlacesViewHolder view, int index, boolean longClick);
    }
    private List<PlacesItem> placesList;
    private Activity mContext;

    private final ClickListener mCallback;

    public PlacesAdapter(Activity context, ClickListener callBack) {
        this.mContext = context;
        this.mCallback = callBack;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int index) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new PlacesViewHolder(inflater.inflate(R.layout.drawer_places_list_item, parent, false));
    }

    public void setData(ArrayList<PlacesItem> wallsList) {
        this.placesList = wallsList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final PlacesViewHolder placesViewHolder, int index) {
        PlacesItem placeItem = placesList.get(index);


        placesViewHolder.location.setText(placeItem.getLocation());
        placesViewHolder.sight.setText(placeItem.getSight());
        placesViewHolder.desc.setText(placeItem.getDescription());

        final String imgPlaceUrl = placeItem.getImgPlaceUrl();

        //TODO - issue when the app starts for the first time. The cards are on the left and are then build after the images loads. Looks weird.
/*
        Glide.with(mContext)
                .load(imgPlaceUrl)
                .override(1400, 1094)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(placesViewHolder.image);

*/

        Glide.with(mContext)
                .load(imgPlaceUrl)
                .asBitmap()
                .into(placesViewHolder.image);

    }


    @Override
    public int getItemCount() {
        return placesList.size();
    }


    public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        public final View view;

        public final ImageView image;

        public final TextView location;
        public final TextView sight;
        public final TextView desc;

        PlacesViewHolder(View v) {
            super(v);
            view = v;

            image = (ImageView) view.findViewById(R.id.imageView);
            location = (TextView) view.findViewById(R.id.textViewLocation);
            sight = (TextView) view.findViewById(R.id.textViewSight);
            desc = (TextView) view.findViewById(R.id.textViewDescription);
            view.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            if (mCallback != null)
                mCallback.onClick(this, index, false);
        }

        @Override
        public boolean onLongClick(View v) {
            int index = getLayoutPosition();
            if (mCallback != null)
                mCallback.onClick(this, index, true);
            return false;
        }
    }
}