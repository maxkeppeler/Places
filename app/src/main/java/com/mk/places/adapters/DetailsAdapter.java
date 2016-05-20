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

    private DetailsItem[] detailsData;
    private Context context;

    public DetailsAdapter(DetailsItem[] detailsData, Context context) {
        this.detailsData = detailsData;
        this.context = context;
    }

    @Override
    public DetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder vH, int index) {

        vH.detailsTitle.setText(detailsData[index].getTitle());
        vH.detailsText.setText(detailsData[index].getText());

        vH.detailsTitle.setTypeface(Utils.customTypeface(context, 2));
        vH.detailsText.setTypeface(Utils.customTypeface(context, 2));

    }

    @Override
    public int getItemCount() {
        return detailsData.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView detailsTitle, detailsText;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            detailsTitle = (TextView) itemLayoutView.findViewById(R.id.detailsTitle);
            detailsText = (TextView) itemLayoutView.findViewById(R.id.detailsText);
        }
    }

}
