package com.mk.places.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.mk.places.R;
import com.mk.places.models.MemberItem;
import com.mk.places.views.ButtonLayout;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> implements View.OnClickListener  {

    private MemberItem[] itemsData;
    private Context context;

    public MemberAdapter(MemberItem[] itemsData, Context context) {
        this.context = context;
        this.itemsData = itemsData;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_about_member, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int position) {

        viewHolder.mName.setText(itemsData[position].getmName());
        viewHolder.mTitle.setText(itemsData[position].getmTitle());
        viewHolder.mDesc.setText(itemsData[position].getmDesc());

        viewHolder.buttonLayout.setbAmount(itemsData[position].getmButtomNames().length);

        Glide.with(context)
                .load(itemsData[position].getmImage())
                .asBitmap()
                .priority(Priority.IMMEDIATE)
                .skipMemoryCache(true)
                .into(viewHolder.mImage);


        for (int j = 0; j < itemsData[position].getmButtomNames().length; j++)
            viewHolder.buttonLayout.addButton(itemsData[position].getmButtomNames()[j], itemsData[position].getmButtomLinks()[j]);

        for (int i = 0; i < viewHolder.buttonLayout.getChildCount(); i++)
            viewHolder.buttonLayout.getChildAt(i).setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return itemsData.length;
    }

    @Override
    public void onClick(View view) {
        if (view.getTag() instanceof String) {
            try {
                context.startActivity(new Intent(Intent.ACTION_VIEW)
                        .setData(Uri.parse((String) view.getTag())));
            } catch (Exception e) {
                Toast.makeText(context, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mName, mTitle, mDesc;
        private ImageView mImage;
        private ButtonLayout buttonLayout;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            buttonLayout = (ButtonLayout) itemLayoutView.findViewById(R.id.buttonLayout);

            mName = (TextView) itemLayoutView.findViewById(R.id.profileName);
            mTitle = (TextView) itemLayoutView.findViewById(R.id.profileTitle);
            mDesc = (TextView) itemLayoutView.findViewById(R.id.profileDesc);
            mImage = (ImageView) itemLayoutView.findViewById(R.id.profileImage);
        }
    }


}
