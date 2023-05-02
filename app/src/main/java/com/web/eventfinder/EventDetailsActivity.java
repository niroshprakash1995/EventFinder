package com.web.eventfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.web.eventfinder.adapters.EventDetailsPagerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventDetailsActivity extends AppCompatActivity {

    private String eventId;
    private String venueName;
    private JsonObject jsonObject;
    private JSONObject jsonObjVal;
    private boolean isDataReceived = false;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.drawable.details_tab_icon,
            R.drawable.artists_tab_icon,
            R.drawable.venue_tab_icon
    };
    private ViewPager viewPager;
    private String facebookLink;
    private String twitterLink;
    private TextView toolbarTitle;
    ProgressBar detailsProgressBar;

    private static final String API_URL_SEARCH_EVENT = "https://web-csci571-np1995.wl.r.appspot.com/getCardDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        eventId = getIntent().getStringExtra("eventId");
        venueName = getIntent().getStringExtra("venueName");
        String stringJson = getIntent().getStringExtra("jsonObjVal");
        try {
            jsonObjVal = new JSONObject(stringJson);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }

        detailsProgressBar = findViewById(R.id.detailsProgressBar);

        setContentView(R.layout.activity_event_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbarTitle = findViewById(R.id.toolbar_title);
        toolbarTitle.setSelected(true);

        // Add back button to the toolbar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Set click listener for the back button
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        ImageView faceBookIcon = findViewById(R.id.facebookIcon);
        faceBookIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookUrl = facebookLink;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(facebookUrl));
                startActivity(intent);
            }
        });

        ImageView twitterIcon = findViewById(R.id.twitterIcon);
        twitterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String twitterUrl = twitterLink;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(twitterUrl));
                startActivity(intent);
            }
        });

        ImageView heartIcon = findViewById(R.id.heartIcon);

        //To set the heart icon by default on landing in the details page
        SharedPreferences sharedPreferences = getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
        String fav_items = sharedPreferences.getString("jsondata", "[]");
        JSONArray jsonArray;
        ArrayList<String> searchIds = new ArrayList<>();
        try {
            jsonArray = new JSONArray(fav_items);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                String search_id = jsonObj.get("eventID").toString();
                searchIds.add(search_id);
            }
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        if(searchIds.contains(eventId)){
            heartIcon.setImageResource(R.drawable.fav_filled);
        }
        else{
            heartIcon.setImageResource(R.drawable.fav);
        }

        heartIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean eventWasInFavorites = false;
                String eventIdForFav = "";
                try {
                    eventIdForFav = jsonObjVal.getString("eventID");
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
                //Get shared preferences here
                SharedPreferences sharedPreferences = getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
                String fav_items = sharedPreferences.getString("jsondata", "[]");
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(fav_items);
                    for(int i = 0; i< jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        if(jsonObject.has("eventID") && jsonObject.get("eventID").equals(eventIdForFav)){
                            jsonArray.remove(i);
                            eventWasInFavorites = true;
                            break;
                        }
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }

                if(eventWasInFavorites){
                    //Change icon to empty heart
                    heartIcon.setImageResource(R.drawable.fav);
                    Snackbar snackbar = Snackbar.make(v, toolbarTitle.getText() + " removed from favorites", Snackbar.LENGTH_SHORT);
                    View snackBarView= snackbar.getView();
                    snackBarView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.snackBar_grey)));
                    TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
                    snackbar.show();

                }
                else{
                    jsonArray.put(jsonObjVal);
                    heartIcon.setImageResource(R.drawable.fav_filled);
                    Snackbar snackbar = Snackbar.make(v, toolbarTitle.getText() + " added to favorites", Snackbar.LENGTH_SHORT);
                    View snackBarView= snackbar.getView();
                    snackBarView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getBaseContext(), R.color.snackBar_grey)));
                    TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
                    textView.setTextColor(ContextCompat.getColor(getBaseContext(), R.color.black));
                    snackbar.show();

                }
                sharedPreferences = getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sharedPreferences.edit();
                myEdit.putString("jsondata", jsonArray.toString());
                myEdit.commit();
            }
        });



        tabLayout = findViewById(R.id.event_details_tabs);
        viewPager = findViewById(R.id.view_pager2);

        getEventDetails(eventId, venueName, new EventDetailsCallback() {
            @Override
            public void onDataReceived(JsonObject jsonObject) {
                EventDetailsPagerAdapter eventDetailsPagerAdapter = new EventDetailsPagerAdapter(EventDetailsActivity.this, getSupportFragmentManager(), jsonObject);
                viewPager.setAdapter(eventDetailsPagerAdapter);
                // Set up the tabs
                for (int i = 0; i < eventDetailsPagerAdapter.getCount(); i++) {
                    TabLayout.Tab tab = tabLayout.newTab();
                    switch (i) {
                        case 0:
                            tab.setText("DETAILS");
                            break;
                        case 1:
                            tab.setText("ARTIST(S)");
                            break;
                        case 2:
                            tab.setText("VENUE");
                            break;
                    }
                    tabLayout.addTab(tab);
                }

                // Set up the tab icons
                setupTabIcons();

                // Add tab selection listener
                tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        viewPager.setCurrentItem(tab.getPosition());
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {}

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {}
                });
            }
        });
    }

    public void getEventDetails(String eventId, String venueName, final EventDetailsCallback callback){
        String url = API_URL_SEARCH_EVENT + "?id=" + eventId + "&venue=" + venueName;
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener <JSONObject> () {
                    @Override
                    public void onResponse(JSONObject response) {
                        Gson gson = new Gson();
                        jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                        isDataReceived = true;
                        callback.onDataReceived(jsonObject);

                        JsonObject eventDataObj = jsonObject.get("eventData").getAsJsonObject();
                        String name = eventDataObj.get("name").getAsString();
                        String url = eventDataObj.get("url").getAsString();


                        //Set facebook link
                        facebookLink = "https://facebook.com/sharer/sharer.php?u=" + url;
                        //Set twitter link
                        twitterLink = "https://twitter.com/share?url=" + url + "&text=Check " + name + "on Ticketmaster.%0A";
                        toolbarTitle.setText(name);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

    public interface EventDetailsCallback {
        void onDataReceived(JsonObject jsonObject);
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
    }

    public void hideProgressBar() {
        detailsProgressBar = findViewById(R.id.detailsProgressBar);
        detailsProgressBar.setVisibility(View.GONE);
    }
}