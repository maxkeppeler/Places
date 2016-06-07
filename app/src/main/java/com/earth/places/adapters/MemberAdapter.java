package com.earth.places.adapters;

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
import com.earth.places.R;
import com.earth.places.models.MemberItem;
import com.earth.places.utilities.Utils;
import com.earth.places.views.ButtonLayout;

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

        holder.name.setText(memberData[index].getmName());
        holder.title.setText(memberData[index].getmTitle());
        holder.desc.setText(memberData[index].getmDesc());

        holder.name.setTypeface(Utils.customTypeface(context, 3));
        holder.title.setTypeface(Utils.customTypeface(context, 1));
        holder.desc.setTypeface(Utils.customTypeface(context, 2));

        holder.buttonLayout.setAmount(memberData[index].getmButtomNames().length);

        Glide.with(context)
                .load(memberData[index].getmImage())
                .asBitmap()
                .override(500, 500)
                .centerCrop()
                .skipMemoryCache(true)
                .into(new BitmapImageViewTarget(holder.image) {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        super.onResourceReady(resource, glideAnimation);

                        RoundedBitmapDrawable circularBitmapDrawable = RoundedBitmapDrawableFactory.create(context.getResources(), resource);
                        circularBitmapDrawable.setCircular(true);

                        holder.image.setImageDrawable(circularBitmapDrawable);

                        Palette palette = new Palette.Builder(resource).generate();

                        color = Utils.colorFromPalette(context, palette);

                        holder.cardView.setCardBackgroundColor(color);
                        holder.linearLayout.setBackgroundColor(Utils.colorVariant(color, 0.9f));
                        holder.buttonLayout.setBackgroundColor(Utils.colorVariant(color, 0.9f));


                    }
                });

        for (int j = 0; j < memberData[index].getmButtomNames().length; j++)
            holder.buttonLayout.addButton(memberData[index].getmButtomNames()[j], memberData[index].getmButtomLinks()[j], true);

        for (int i = 0; i < holder.buttonLayout.getChildCount(); i++)
            holder.buttonLayout.getChildAt(i).setOnClickListener(MemberAdapter.this);

    }

    @Override
    public int getItemCount() {
        return memberData.length;
    }

    @Override
    public void onClick(View v) {

        // Chrome tab handles links. Either it opens a custom chrome tab if the user does not the respective app installed, or the respective app will be opened.
        Utils.openChromeTab(context, (String) v.getTag(), 0);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name, title, desc;
        private ImageView image;
        private ButtonLayout buttonLayout;
        private CardView cardView;
        private LinearLayout linearLayout;

        public ViewHolder(View v) {
            super(v);

            name = (TextView) v.findViewById(R.id.mName);
            title = (TextView) v.findViewById(R.id.mTitle);
            desc = (TextView) v.findViewById(R.id.mDesc);
            image = (ImageView) v.findViewById(R.id.mImage);
            cardView = (CardView) v.findViewById(R.id.cardView);
            buttonLayout = (ButtonLayout) v.findViewById(R.id.buttonLayout);
            linearLayout = (LinearLayout) v.findViewById(R.id.linearLayout);
        }
    }


}
