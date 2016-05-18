package com.mk.places.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mk.places.R;
import com.mk.places.models.CreditsItem;
import com.mk.places.utilities.Utils;

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.ViewHolder> implements View.OnClickListener{

    private CreditsItem[] itemsData;
    private Context context;

    public CreditsAdapter(CreditsItem[] itemsData, Context context) {
        this.itemsData = itemsData;
        this.context = context;
    }

    @Override
    public CreditsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_credits_item, parent, false);
        return new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(ViewHolder vH, int i) {

        vH.placeCreditsTitle.setTypeface(Utils.customTypeface(context, 3));
        vH.placeCreditsDesc.setTypeface(Utils.customTypeface(context, 2));

        vH.placeCreditsTitle.setText(itemsData[i].getTitle());
        vH.placeCreditsDesc.setText(itemsData[i].getDesc());

        vH.creditsLayout.setTag(itemsData[i].getTag());
        vH.creditsLayout.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return itemsData.length;
    }

    @Override
    public void onClick(View view) {
        Utils.customChromeTab(context, (String) view.getTag(), 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView placeCreditsTitle, placeCreditsDesc;
        private LinearLayout creditsLayout;

        public ViewHolder(View view) {
            super(view);

            creditsLayout = (LinearLayout) view.findViewById(R.id.CreditsLayout);
            placeCreditsTitle = (TextView) view.findViewById(R.id.placeCreditsTitle);
            placeCreditsDesc = (TextView) view.findViewById(R.id.placeCreditsDesc);
        }
    }

}
