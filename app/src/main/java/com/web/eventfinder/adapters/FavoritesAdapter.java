package com.web.eventfinder.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.web.eventfinder.FavoritesItem;
import com.web.eventfinder.FavoritesViewHolder;
import com.web.eventfinder.R;
import com.web.eventfinder.SearchItem;

import java.util.List;

public class FavoritesAdapter extends RecyclerView.Adapter<FavoritesViewHolder>{

    Context context;
    List<SearchItem> favItems;

    public FavoritesAdapter(Context context, List<SearchItem> favItems) {
        this.context = context;
        this.favItems = favItems;
    }

    @NonNull
    @Override
    public FavoritesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoritesViewHolder(LayoutInflater.from(context).inflate(R.layout.favorite_item_layout, parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesViewHolder favHolder, int position) {
        SearchItem item = favItems.get(position);
        Picasso.get().load(favItems.get(position).getSearch_image()).fit().into(favHolder.search_image1);
        favHolder.search_name1.setText(favItems.get(position).getSearch_name());
        favHolder.search_venue1.setText(favItems.get(position).getSearch_venue());
        favHolder.search_category1.setText(favItems.get(position).getSearch_category());
        favHolder.search_date1.setText(favItems.get(position).getSearch_date());
        favHolder.search_time1.setText(favItems.get(position).getSearch_time());

        //Set tag to entire item
        favHolder.itemView.setTag(item.getSearch_id());
        //Set tag to favorite icon
        favHolder.favorite_icon1.setTag(item.getSearch_id());
        //Set tag to image
        favHolder.search_image1.setTag((item.getSearch_image()));
    }

    @Override
    public int getItemCount() {
        return favItems.size();
    }

    public void removeItem(int position) {
        favItems.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
    }
}
