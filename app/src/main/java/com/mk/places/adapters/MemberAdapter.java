package com.mk.places.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.places.R;
import com.mk.places.models.MemberItem;
import com.mk.places.utilities.Utils;
import com.mk.places.views.ButtonLayout;
import com.mk.places.views.SquareImageView;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> implements View.OnClickListener {

    private MemberItem[] itemsData;
    private Context context;
    private int color = 0;

    public MemberAdapter(MemberItem[] itemsData, Context context) {
        this.context = context;
        this.itemsData = itemsData;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_about_member, parent, false);
        return  new ViewHolder(itemLayoutView);
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.mName.setText(itemsData[position].getmName());
        viewHolder.mName.setTypeface(Utils.customTypeface(context, 3));
        viewHolder.mTitle.setText(itemsData[position].getmTitle());
        viewHolder.mTitle.setTypeface(Utils.customTypeface(context, 1));
        viewHolder.mDesc.setText(itemsData[position].getmDesc());
        viewHolder.mDesc.setTypeface(Utils.customTypeface(context, 2));

        viewHolder.ButtonLayout.setbAmount(itemsData[position].getmButtomNames().length);

        Glide.with(context)
                .load(itemsData[position].getmImage())
                .asBitmap()
                .override(500, 500)
                .centerCrop()
                .skipMemoryCache(true)
                .into(new BitmapImageViewTarget(viewHolder.mImage) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);

                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);

                        viewHolder.mImage.setImageDrawable(circularBitmapDrawable);

                        Palette palette = new Palette.Builder(resource).generate();

                        color = Utils.colorFromPalette(context, palette);

                        viewHolder.cardView.setCardBackgroundColor(color);
                        viewHolder.coloredLinearLaoyut.setBackgroundColor(Utils.colorVariant(color, 0.9f));
                        viewHolder.buttons.setBackgroundColor(Utils.colorVariant(color, 0.9f));



                    }
                });

        for (int j = 0; j < itemsData[position].getmButtomNames().length; j++)
            viewHolder.ButtonLayout.addButton(itemsData[position].getmButtomNames()[j], itemsData[position].getmButtomLinks()[j], true);

        for (int i = 0; i < viewHolder.ButtonLayout.getChildCount(); i++)
            viewHolder.ButtonLayout.getChildAt(i).setOnClickListener(MemberAdapter.this);

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

        private TextView mName, mTitle, mDesc;
        private ImageView mImage;
        private ButtonLayout ButtonLayout;
        private CardView cardView;
        private LinearLayout coloredLinearLaoyut, buttons;

        public ViewHolder(View v) {
            super(v);

            ButtonLayout = (ButtonLayout) v.findViewById(R.id.buttonLayout);

            mName = (TextView) v.findViewById(R.id.profileName);
            mTitle = (TextView) v.findViewById(R.id.profileTitle);
            mDesc = (TextView) v.findViewById(R.id.profileDesc);
            mImage = (ImageView) v.findViewById(R.id.profileImage);
            cardView = (CardView) v.findViewById(R.id.cardViewMember);
            coloredLinearLaoyut = (LinearLayout) v.findViewById(R.id.coloredLinearLaoyut);
            buttons = (LinearLayout) v.findViewById(R.id.buttons);
        }
    }


}
