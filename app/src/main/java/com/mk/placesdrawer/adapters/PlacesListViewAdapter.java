package com.mk.placesdrawer.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mk.placesdrawer.R;
import com.mk.placesdrawer.model.PlacesListItem;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by florentchampigny on 24/04/15.
 */
public class PlacesListViewAdapter extends RecyclerView.Adapter<PlacesListRowHolder> {


    private List<PlacesListItem> placeItemList;
    private Context mContext;


    public PlacesListViewAdapter(Context context, List<PlacesListItem> placeItemList) {
        this.placeItemList = placeItemList;
        this.mContext = context;
    }

    @Override
    public PlacesListRowHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item_fragment_home, null);
        return new PlacesListRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final PlacesListRowHolder placesListRowHolder, int i) {
        PlacesListItem placeItem = placeItemList.get(i);

        placesListRowHolder.location.setText(Html.fromHtml(placeItem.getTitle()));
        placesListRowHolder.sight.setText(Html.fromHtml(placeItem.getWhat()));


        Picasso.with(mContext)
                .load(placeItem.getImage())

                .resize(1400, 1094)                 //TODO adjust the width resize value automatically to the users screen width
                .onlyScaleDown()
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(placesListRowHolder.image);

    }



    @Override
    public int getItemCount() {
        return placeItemList.size();
    }

}