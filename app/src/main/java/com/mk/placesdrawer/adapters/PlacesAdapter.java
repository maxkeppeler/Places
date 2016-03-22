package com.mk.placesdrawer.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.activity.MainActivity;
import com.mk.placesdrawer.models.PlacesItem;

import java.util.ArrayList;
import java.util.NavigableMap;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.PlacesViewHolder> {

    private static Activity context;

    public interface ClickListener {

        void onClick(PlacesViewHolder view, int index, boolean longClick);
    }

    private ArrayList<PlacesItem> placesList;
    private Activity mContext;

    private final ClickListener mCallback;

    public PlacesAdapter(Activity context, ClickListener callBack) {
        this.mContext = context;
        this.mCallback = callBack;
    }

    @Override
    public PlacesViewHolder onCreateViewHolder(ViewGroup parent, int index) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        return new PlacesViewHolder(inflater.inflate(R.layout.drawer_places_list_item, parent, false));
    }

    public void setData(ArrayList<PlacesItem> placesList) {
        this.placesList = placesList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(final PlacesViewHolder holder, int index) {

        PlacesItem placeItem = placesList.get(index);
        holder.location.setText(placeItem.getLocation());
        holder.sight.setText(placeItem.getSight());
        //holder.desc.setText(placeItem.getDescription());

        final String imgPlaceUrl = placeItem.getImgPlaceUrl();

/*      Old
        Glide.with(mContext)
                .load(imgPlaceUrl)
                .override(1400, 1094)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder)
                .into(holder.image);
*/

        Glide.with(mContext)
                .load(imgPlaceUrl)
                .asBitmap()
                .override(2022, 1784)
                .into(new BitmapImageViewTarget(holder.image) {
                    @Override
                    protected void setResource(Bitmap resource) {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(mContext.getResources(), resource)});
                        holder.image.setImageDrawable(td);
                        td.startTransition(250);
                    }
                });

    }


    @Override
    public int getItemCount() {
        return placesList == null ? 0 : placesList.size();
    }


    public class PlacesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        private MaterialRippleLayout mRipple;
        public final View view;
        public final ImageView image;
        public final TextView location, sight; //, desc;

        PlacesViewHolder(View v) {
            super(v);
            view = v;

            mRipple = (MaterialRippleLayout) view.findViewById(R.id.ripple);
            image = (ImageView) view.findViewById(R.id.imageView);
            location = (TextView) view.findViewById(R.id.textViewLocation);
            sight = (TextView) view.findViewById(R.id.textViewSight);
            //desc = (TextView) view.findViewById(R.id.textViewDescription);

            mRipple.setOnClickListener(this);
            mRipple.setOnLongClickListener(this);
        }


        @Override
        public void onClick(View v) {
            int index = getLayoutPosition();
            if (mCallback != null)
                mCallback.onClick(this, index, false);
        }

        @Override
        public boolean onLongClick(View v) {
            int index = getLayoutPosition();
            if (mCallback != null)
                mCallback.onClick(this, index, true);
            return true;
        }
    }
}