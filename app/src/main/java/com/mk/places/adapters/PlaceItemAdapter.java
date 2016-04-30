package com.mk.places.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mk.places.R;
import com.mk.places.models.GalleryItem;
import com.mk.places.utilities.Utils;

public class PlaceItemAdapter extends RecyclerView.Adapter<PlaceItemAdapter.ViewHolder> {

    private GalleryItem[] itemsData;
    private Context context;

    public PlaceItemAdapter(GalleryItem[] itemsData, Context context) {
        this.itemsData = itemsData;
        this.context = context;
    }

    @Override
    public PlaceItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_places_detail_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        viewHolder.placeItemTitle.setTypeface(Utils.customTypeface(context, 3));
        viewHolder.placeItemText.setTypeface(Utils.customTypeface(context, 2));

        viewHolder.placeItemTitle.setText(itemsData[position].getTitle());
        viewHolder.placeItemText.setText(itemsData[position].getText());
        viewHolder.placeItemImage.setImageResource(itemsData[position].getDrawable());
    }

    @Override
    public int getItemCount() {
        return itemsData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView placeItemTitle, placeItemText;
        private ImageView placeItemImage;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            placeItemTitle = (TextView) itemLayoutView.findViewById(R.id.placeItemTitle);
            placeItemText = (TextView) itemLayoutView.findViewById(R.id.placeItemText);
            placeItemImage = (ImageView) itemLayoutView.findViewById(R.id.placeItemImage);
        }
    }

}
