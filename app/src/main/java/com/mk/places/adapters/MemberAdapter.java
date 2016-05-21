package com.mk.places.adapters;

import android.content.Context;
import android.graphics.Bitmap;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.places.R;
import com.mk.places.models.MemberItem;
import com.mk.places.utilities.Utils;
import com.mk.places.views.ButtonLayout;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> implements View.OnClickListener {

    private MemberItem[] memberData;
    private Context context;
    private int color = 0;

    public MemberAdapter(MemberItem[] memberData, Context context) {
        this.context = context;
        this.memberData = memberData;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_about_member, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int index) {

        holder.memberName.setText(memberData[index].getmName());
        holder.memberTitle.setText(memberData[index].getmTitle());
        holder.memberDesc.setText(memberData[index].getmDesc());

        holder.memberName.setTypeface(Utils.customTypeface(context, 3));
        holder.memberTitle.setTypeface(Utils.customTypeface(context, 1));
        holder.memberDesc.setTypeface(Utils.customTypeface(context, 2));

        holder.memberButtonLayout.setAmount(memberData[index].getmButtomNames().length);

        Glide.with(context)
                .load(memberData[index].getmImage())
                .asBitmap()
                .override(500, 500)
                .centerCrop()
                .skipMemoryCache(true)
                .into(new BitmapImageViewTarget(holder.memberImage) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);

                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);

                        holder.memberImage.setImageDrawable(circularBitmapDrawable);

                        Palette palette = new Palette.Builder(resource).generate();

                        color = Utils.colorFromPalette(context, palette);

                        holder.memberCard.setCardBackgroundColor(color);
                        holder.memberLinearLayout.setBackgroundColor(Utils.colorVariant(color, 0.9f));
                        holder.memberButtonLayout.setBackgroundColor(Utils.colorVariant(color, 0.9f));


                    }
                });

        for (int j = 0; j < memberData[index].getmButtomNames().length; j++)
            holder.memberButtonLayout.addButton(memberData[index].getmButtomNames()[j], memberData[index].getmButtomLinks()[j], true);

        for (int i = 0; i < holder.memberButtonLayout.getChildCount(); i++)
            holder.memberButtonLayout.getChildAt(i).setOnClickListener(MemberAdapter.this);

    }

    @Override
    public int getItemCount() {
        return memberData.length;
    }

    @Override
    public void onClick(View v) {

        Utils.customChromeTab(context, (String) v.getTag(), 0);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView memberName, memberTitle, memberDesc;
        private ImageView memberImage;
        private ButtonLayout memberButtonLayout;
        private CardView memberCard;
        private LinearLayout memberLinearLayout;

        public ViewHolder(View v) {
            super(v);

            memberName = (TextView) v.findViewById(R.id.memberName);
            memberTitle = (TextView) v.findViewById(R.id.memberTitle);
            memberDesc = (TextView) v.findViewById(R.id.memberDesc);
            memberImage = (ImageView) v.findViewById(R.id.memberImage);
            memberCard = (CardView) v.findViewById(R.id.memberCard);
            memberButtonLayout = (ButtonLayout) v.findViewById(R.id.memberButtonLayout);
            memberLinearLayout = (LinearLayout) v.findViewById(R.id.memberLinearLayout);
        }
    }


}
