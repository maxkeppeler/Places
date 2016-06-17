package com.earth.places.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.earth.places.R;
import com.earth.places.models.Disaster;
import com.earth.places.utilities.Utils;

import java.util.ArrayList;

public class DisastersAdapter extends RecyclerView.Adapter<DisastersAdapter.SinsViewHolder> {


    private ClickListener clickListener;
    private ArrayList<Disaster> disasters;
    private Activity context;

    public DisastersAdapter(Activity context, ClickListener clickListener) {
        this.context = context;
        this.clickListener = clickListener;
    }

    @Override
    public SinsViewHolder onCreateViewHolder(ViewGroup parent, int index) {
        return new SinsViewHolder(LayoutInflater.from(context).inflate(R.layout.fragment_nature_item, parent, false));
    }

    public void setData(ArrayList<Disaster> disasters) {

        this.disasters = disasters;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final SinsViewHolder holder, int index) {

        final Disaster disaster = disasters.get(index);
        final String[] url = disaster.getImages().replace(" ", "").split("\\|");

        holder.title.setText(disaster.getTitle());
        holder.title.setTypeface(Utils.customTypeface(context, 2));

        Glide.with(context)
                .load(url[0] != null ? url[0] : disaster.getImages())
                .asBitmap()
                .override(1000, 1000)
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

                                holder.layout.setVisibility(View.VISIBLE);
                            }
                        });

                        return false;
                    }
                })
                .into(holder.image);

    }

    @Override
    public int getItemCount() {
        return disasters.size();
    }

    public interface ClickListener {
        void onClick(SinsViewHolder v, int index);
    }

    public class SinsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final FrameLayout layout;
        private final ImageView image;
        private final TextView title;
        private final LinearLayout coloredBackground;

        SinsViewHolder(View v) {
            super(v);


            coloredBackground = (LinearLayout) v.findViewById(R.id.coloredBackground);
            image = (ImageView) v.findViewById(R.id.image);
            title = (TextView) v.findViewById(R.id.title);

            layout = (FrameLayout) v.findViewById(R.id.layout);
            layout.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            if (clickListener != null)
                clickListener.onClick(this, index);
        }

    }


}