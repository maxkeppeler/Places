package com.mk.places.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mk.places.R;
import com.mk.places.models.GoodAct;
import com.mk.places.utilities.Utils;

import java.util.ArrayList;

public class GoodActsAdapter extends RecyclerView.Adapter<GoodActsAdapter.SinsViewHolder> {


    private ClickListener clickListener;
    private ArrayList<GoodAct> sinsList;
    private Activity context;

    public GoodActsAdapter(Activity context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public SinsViewHolder onCreateViewHolder(ViewGroup parent, int index) {
        return new SinsViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_nature_item, parent, false));
    }

    public void setData(ArrayList<GoodAct> sinsList) {

        this.sinsList = sinsList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final SinsViewHolder holder, int index) {

        final GoodAct sin = sinsList.get(index);
        final String[] url = sin.getImages().replace(" ", "").split("\\|");

        holder.title.setText(sin.getTitle());

        Glide.with(context)
                .load(url[0] != null ? url[0] :  sin.getImages())
                .asBitmap()
                .override(1000, 700)
                .priority(Priority.IMMEDIATE)
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {

                        new Palette.Builder(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                                if (palette == null) return;
                                int color = Utils.colorFromPalette(context, palette);
                                holder.coloredBackground.setBackgroundColor(color);
                            }
                        });

                        return false;
                    }
                })
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return sinsList.size();
    }

    public interface ClickListener {
        void onClick(SinsViewHolder v, int index);
    }

    public class SinsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final ImageView image;
        private final TextView title;
        private final LinearLayout coloredBackground;
        private final MaterialRippleLayout ripple;

        SinsViewHolder(View v) {
            super(v);

            coloredBackground = (LinearLayout) v.findViewById(R.id.coloredBackground);
            ripple = (MaterialRippleLayout) v.findViewById(R.id.thumbRipple);
            image = (ImageView) v.findViewById(R.id.sinsImage);
            title = (TextView) v.findViewById(R.id.thumbSin);
            ripple.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            if (clickListener != null)
                clickListener.onClick(this, index);
        }

    }


}