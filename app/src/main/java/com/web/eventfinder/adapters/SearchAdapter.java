package com.web.eventfinder.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.web.eventfinder.R;
import com.web.eventfinder.SearchItem;
import com.web.eventfinder.SearchViewHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchViewHolder>{

    Context context;
    List<SearchItem> searchItems;

    public SearchAdapter(Context context, List<SearchItem> searchItems) {
        this.context = context;
        this.searchItems = searchItems;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SearchViewHolder(LayoutInflater.from(context).inflate(R.layout.search_item_layout, parent, false), context);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder searchHolder, int position) {

        Picasso.get().load(searchItems.get(position).getSearch_image()).fit().into(searchHolder.search_image);
        SearchItem item = searchItems.get(position);

        //Get shared preferences here
        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
        String fav_items = sharedPreferences.getString("jsondata", "[]");
        JSONArray jsonArray;
        boolean eventInFavorites = false;
        try {
            jsonArray = new JSONArray(fav_items);
            for(int i = 0; i< jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                if(jsonObject.has("eventID") && jsonObject.get("eventID").equals(item.getSearch_id())){
                    searchHolder.favorite_icon.setImageResource(R.drawable.fav_filled);
                    eventInFavorites = true;
                    break;
                }
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        searchHolder.search_name.setText(searchItems.get(position).getSearch_name());
        searchHolder.search_name.setSelected(true);
        searchHolder.search_venue.setText(searchItems.get(position).getSearch_venue());
        searchHolder.search_category.setText(searchItems.get(position).getSearch_category());
        searchHolder.search_date.setText(searchItems.get(position).getSearch_date());
        searchHolder.search_time.setText(searchItems.get(position).getSearch_time());

        //Set tag to entire item
        searchHolder.itemView.setTag(item.getSearch_id());
        //Set tag to favorite icon
        searchHolder.favorite_icon.setTag(item.getSearch_id());
        if(eventInFavorites){
            searchHolder.favorite_icon.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN);
        }
        else{
            searchHolder.favorite_icon.setColorFilter(ContextCompat.getColor(context, R.color.text_color), PorterDuff.Mode.SRC_IN);
        }
        //Set tag to image
        searchHolder.search_image.setTag((item.getSearch_image()));


        new CountDownTimer(500, 500) {
            public void onTick(long millisUntilFinished) {
                // Do nothing while waiting for the timer to finish
            }
            public void onFinish() {
                if (searchHolder.search_name.isFocusable()) {
                    searchHolder.search_name.setFocusable(true);
                    searchHolder.search_name.setHorizontallyScrolling(true);
                    searchHolder.search_name.setSelected(true);
                }
            }
        }.start();

    }

    @Override
    public int getItemCount() {
        return searchItems.size();
    }
}
