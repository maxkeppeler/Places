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

    private CreditsItem[] credtisData;
    private Context context;

    public CreditsAdapter(CreditsItem[] credtisData, Context context) {
        this.credtisData = credtisData;
        this.context = context;
    }

    @Override
    public CreditsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.place_item_credits_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int index) {

        holder.creditsTitle.setText(credtisData[index].getTitle());
        holder.creditsDesc.setText(credtisData[index].getDesc());

        holder.creditsTitle.setTypeface(Utils.customTypeface(context, 2));
        holder.creditsDesc.setTypeface(Utils.customTypeface(context, 2));

        holder.creditsLayout.setTag(credtisData[index].getTag());
        holder.creditsLayout.setOnClickListener(this);

    }

    @Override
    public int getItemCount() {
        return credtisData.length;
    }

    @Override
    public void onClick(View view) {
        Utils.openChromeTab(context, (String) view.getTag(), 0);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView creditsTitle, creditsDesc;
        private LinearLayout creditsLayout;

        public ViewHolder(View view) {
            super(view);

            creditsLayout = (LinearLayout) view.findViewById(R.id.creditsLayout);
            creditsTitle = (TextView) view.findViewById(R.id.creditsTitle);
            creditsDesc = (TextView) view.findViewById(R.id.creditsDesc);

        }
    }

}
