package com.mk.places.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mk.places.R;
import com.mk.places.models.InfoItem;
import com.mk.places.utilities.Utils;

public class PlaceInfoAdapter extends RecyclerView.Adapter<PlaceInfoAdapter.ViewHolder> {

    private InfoItem[] itemsData;
    private Context context;

    public PlaceInfoAdapter(InfoItem[] itemsData, Context context) {
        this.itemsData = itemsData;
        this.context = context;
    }

    @Override
    public PlaceInfoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_places_detail_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder vH, int i) {

        vH.placeItemTitle.setTypeface(Utils.customTypeface(context, 2));
        vH.placeItemText.setTypeface(Utils.customTypeface(context, 2));

        vH.placeItemTitle.setText(itemsData[i].getTitle());
        vH.placeItemText.setText(itemsData[i].getText());

    }

    @Override
    public int getItemCount() {
        return itemsData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView placeItemTitle, placeItemText;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            placeItemTitle = (TextView) itemLayoutView.findViewById(R.id.placeItemTitle);
            placeItemText = (TextView) itemLayoutView.findViewById(R.id.placeItemText);
        }
    }

}
