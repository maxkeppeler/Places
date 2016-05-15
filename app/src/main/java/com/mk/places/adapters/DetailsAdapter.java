package com.mk.places.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mk.places.R;
import com.mk.places.models.DetailsItem;
import com.mk.places.utilities.Utils;

public class DetailsAdapter extends RecyclerView.Adapter<DetailsAdapter.ViewHolder> {

    private DetailsItem[] itemsData;
    private Context context;

    public DetailsAdapter(DetailsItem[] itemsData, Context context) {
        this.itemsData = itemsData;
        this.context = context;
    }

    @Override
    public DetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_details_item, parent, false);
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
