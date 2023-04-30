package com.web.eventfinder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SearchViewHolder extends RecyclerView.ViewHolder {
    public ImageView search_image, favorite_icon;
    public TextView search_name, search_venue, search_category, search_date, search_time;

    public String eventId;
    public String imageLink;

    public SearchViewHolder( View itemView, Context context) {
        super(itemView);

        search_image = itemView.findViewById(R.id.search_image);
        favorite_icon = itemView.findViewById(R.id.favorite_icon);
        search_name = itemView.findViewById(R.id.search_name);
        search_venue = itemView.findViewById(R.id.search_venue);
        search_category = itemView.findViewById(R.id.search_category);
        search_date = itemView.findViewById(R.id.search_date);
        search_time = itemView.findViewById(R.id.search_time);




        favorite_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean eventInFavorites = false;

               eventId = (String) favorite_icon.getTag(); // retrieve the tag associated with the parent view
               imageLink = (String) search_image.getTag();

                //Get shared preferences here
                SharedPreferences sharedPreferences = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
                String fav_items = sharedPreferences.getString("jsondata", "[]");
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(fav_items);
                    for(int i = 0; i< jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.has("eventID") && jsonObject.get("eventID").equals(eventId)){
                            jsonArray.remove(i);
                            eventInFavorites = true;
                            break;
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if(eventInFavorites){
                    //Change icon to empty heart
                    favorite_icon.setImageResource(R.drawable.fav);
                    favorite_icon.setColorFilter(ContextCompat.getColor(context, R.color.text_color), PorterDuff.Mode.SRC_IN);
                    Snackbar snackbar = Snackbar.make(v, search_name.getText() + " removed from favorites", Snackbar.LENGTH_SHORT);
                    View snackBarView= snackbar.getView();
                    snackBarView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.snackBar_grey)));
                    TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(context, R.color.black));
                    snackbar.show();
                }
                else{
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("eventID", eventId);
                        jsonObject.put("search_image", imageLink);
                        jsonObject.put("search_name", search_name.getText().toString());
                        jsonObject.put("search_venue", search_venue.getText().toString());
                        jsonObject.put("search_category", search_category.getText().toString());
                        jsonObject.put("search_date", search_date.getText().toString());
                        jsonObject.put("search_time", search_time.getText().toString());
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                    jsonArray.put(jsonObject);
                    //Change icon to full heart
                    favorite_icon.setImageResource(R.drawable.fav_filled);
                    favorite_icon.setColorFilter(ContextCompat.getColor(context, R.color.white), PorterDuff.Mode.SRC_IN);
                    Snackbar snackbar = Snackbar.make(v, search_name.getText() + " added to favorites", Snackbar.LENGTH_SHORT);
                    View snackBarView= snackbar.getView();
                    snackBarView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.snackBar_grey)));
                    TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(context, R.color.black));
                    snackbar.show();
                }
                sharedPreferences = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("jsondata", jsonArray.toString());
                myEdit.commit();
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventId = (String) v.getTag();
                String venueName = search_venue.getText().toString();
                imageLink = (String) search_image.getTag();

                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("search_image", imageLink);
                    jsonObject.put("eventID", eventId);
                    jsonObject.put("search_name", search_name.getText().toString());
                    jsonObject.put("search_venue", search_venue.getText().toString());
                    jsonObject.put("search_category", search_category.getText().toString());
                    jsonObject.put("search_date", search_date.getText().toString());
                    jsonObject.put("search_time", search_time.getText().toString());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                Intent intent = new Intent(itemView.getContext(), EventDetailsActivity.class);
                intent.putExtra("eventId", eventId);
                intent.putExtra("venueName", venueName);
                intent.putExtra("jsonObjVal", jsonObject.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                itemView.getContext().startActivity(intent);
            }
        });

    }

//    public void checkIfFavorite(Context context) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
//        String fav_items = sharedPreferences.getString("jsondata", "[]");
//        JSONArray jsonArray;
//        try {
//            jsonArray = new JSONArray(fav_items);
//            for(int i = 0; i< jsonArray.length(); i++){
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
//                if(jsonObject.has("eventID") && jsonObject.get("eventID").equals(eventId)){
//                    favorite_icon.setImageResource(R.drawable.fav_filled);
//                    break;
//                }
//            }
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//    }
}
