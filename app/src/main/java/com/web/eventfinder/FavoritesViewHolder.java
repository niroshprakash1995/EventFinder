package com.web.eventfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.web.eventfinder.adapters.FavoritesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FavoritesViewHolder extends RecyclerView.ViewHolder{

    public ImageView search_image1, favorite_icon1;
    public TextView search_name1, search_venue1, search_category1, search_date1, search_time1, no_favorites_available;
    public String eventId;
    public String imageLink;

    public FavoritesViewHolder(View itemView, Context context) {
        super(itemView);

        search_image1 = itemView.findViewById(R.id.search_image1);
        favorite_icon1 = itemView.findViewById(R.id.favorite_icon1);
        search_name1 = itemView.findViewById(R.id.search_name1);
        search_venue1 = itemView.findViewById(R.id.search_venue1);
        search_category1 = itemView.findViewById(R.id.search_category1);
        search_date1 = itemView.findViewById(R.id.search_date1);
        search_time1 = itemView.findViewById(R.id.search_time1);
        no_favorites_available = itemView.findViewById(R.id.no_favorites_available);


        favorite_icon1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventId = (String) favorite_icon1.getTag();
                imageLink = (String) search_image1.getTag();

                //Get position of the clicked item
                int removedItemPosition = getAdapterPosition();

                FavoritesAdapter adapter = (FavoritesAdapter) ((RecyclerView) itemView.getParent()).getAdapter();
                RecyclerView recyclerView = (RecyclerView) itemView.getParent();

                Snackbar snackbar = Snackbar.make(v,search_name1.getText() + " removed from favorites", Snackbar.LENGTH_SHORT);
                View snackBarView= snackbar.getView();
                snackBarView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.snackBar_grey)));
                TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(ContextCompat.getColor(context, R.color.black));
                snackbar.show();

                // Remove the current item from the adapter
                adapter.removeItem(removedItemPosition);
                // Update the RecyclerView
                recyclerView.setAdapter(adapter);

                //Get shared preferences here
                SharedPreferences sharedPreferences = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
                String fav_items = sharedPreferences.getString("jsondata", "[]");
                boolean noMoreFavorites = false;
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(fav_items);
                    for(int i = 0; i< jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.has("eventID") && jsonObject.get("eventID").equals(eventId)){
                            jsonArray.remove(i);
                            if(jsonArray.length() == 0){
                                noMoreFavorites = true;
                            }
                            sharedPreferences = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("jsondata", jsonArray.toString());
                            myEdit.commit();
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if(noMoreFavorites){
                    LinearLayout linearLayout = (LinearLayout) recyclerView.getParent();
                    TextView noEvents = linearLayout.findViewById(R.id.no_favorites_available);
                    noEvents.setVisibility(View.VISIBLE);
                }
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventId = (String) v.getTag();
                imageLink = (String) search_image1.getTag();
                String venueName = search_venue1.getText().toString();
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("search_image", imageLink);
                    jsonObject.put("eventID", eventId);
                    jsonObject.put("search_name", search_name1.getText().toString());
                    jsonObject.put("search_venue", search_venue1.getText().toString());
                    jsonObject.put("search_category", search_category1.getText().toString());
                    jsonObject.put("search_date", search_date1.getText().toString());
                    jsonObject.put("search_time", search_time1.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //Create json object here
                Intent intent = new Intent(itemView.getContext(), EventDetailsActivity.class);
                intent.putExtra("eventId", eventId);
                intent.putExtra("venueName", venueName);
                intent.putExtra("jsonObjVal", jsonObject.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itemView.getContext().startActivity(intent);
            }
        });
    }
}
