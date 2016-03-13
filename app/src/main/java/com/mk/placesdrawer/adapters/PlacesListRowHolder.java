package com.mk.placesdrawer.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mk.placesdrawer.R;

/**
 * Created by florentchampigny on 06.01.16.
 */
public class PlacesListRowHolder extends RecyclerView.ViewHolder {

        public ImageView image;

        public TextView location;
        public TextView sight;
        public TextView desc;


        public PlacesListRowHolder(View view) {

                super(view);
                this.image = (ImageView) view.findViewById(R.id.imageView);
                this.location = (TextView) view.findViewById(R.id.textViewLocation);
                this.sight = (TextView) view.findViewById(R.id.textViewSight);
                this.desc = (TextView) view.findViewById(R.id.textViewDescription);

        }




}

