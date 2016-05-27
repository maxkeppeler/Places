package com.mk.places.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mk.places.R;
import com.mk.places.models.InfosItem;
import com.mk.places.utilities.Utils;

public class InfosAdapter extends RecyclerView.Adapter<InfosAdapter.ViewHolder> {

    private InfosItem[] infosData;
    private Context context;

    public InfosAdapter(InfosItem[] infosData, Context context) {
        this.infosData = infosData;
        this.context = context;
    }

    @Override
    public InfosAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_details_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int index) {

        holder.detailsTitle.setText(infosData[index].getTitle());
        holder.detailsText.setText(infosData[index].getText());

        holder.detailsTitle.setTypeface(Utils.customTypeface(context, 2));
        holder.detailsText.setTypeface(Utils.customTypeface(context, 2));

    }

    @Override
    public int getItemCount() {
        return infosData.length;
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
