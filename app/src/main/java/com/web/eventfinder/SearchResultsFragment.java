package com.web.eventfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.web.eventfinder.adapters.SearchAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import java.util.Collections;
import java.util.Comparator;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class SearchResultsFragment extends Fragment {

    private ArrayList<SearchItem> searchItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);
        String url = getArguments().getString("get_Data_url");
        TextView noSearchResults = rootView.findViewById(R.id.no_search_results);
        noSearchResults.setVisibility(View.GONE);

        RecyclerView recyclerView = rootView.findViewById(R.id.search_result_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        getSearchResults(url, rootView, recyclerView);



        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        // Add tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
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

                    for(int i = 0; i < recyclerView.getChildCount(); i++){
                        View itemView = recyclerView.getChildAt(i);
                        String id = (String) itemView.getTag();
                        ImageView imageView = itemView.findViewById(R.id.favorite_icon);
                        if(searchIds.contains(id)){
                            imageView.setImageResource(R.drawable.fav_filled);
                        }
                        else{
                            imageView.setImageResource(R.drawable.fav);
                        }
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        LinearLayout backButton = rootView.findViewById(R.id.search_results_back);

        backButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                SharedPreferences sharedPreferences = getContext().getSharedPreferences("formsharedpref", Context.MODE_PRIVATE);
                String old_keyword = sharedPreferences.getString("keyword", "");
                bundle.putString("old_keyword", old_keyword);
                String old_distance = sharedPreferences.getString("distance", "");
                bundle.putString("old_distance", old_distance);
                int categoryIndex = sharedPreferences.getInt("categoryIndex", 0);
                bundle.putInt("old_categoryIndex", categoryIndex);
                Boolean old_autodetect = sharedPreferences.getBoolean("autodetect", false);
                bundle.putBoolean("old_autodetect", old_autodetect);
                String old_location = sharedPreferences.getString("location", "");
                bundle.putString("old_location", old_location);

                Navigation.findNavController(rootView).navigate(R.id.action_second_fragment_to_first_fragment, bundle);

            }
        });
        return rootView;
    }


    private void getSearchResults(String url, View view, RecyclerView recyclerView) {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        ArrayList<SearchItem> searchItems = new ArrayList<>();
        StringRequest request = new StringRequest(Request.Method.GET, url,
                new Response.Listener <String> () {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        JsonObject jsonObject = gson.fromJson(response, JsonObject.class);

                        // Access embedded events array
                        JsonObject embedded = jsonObject.getAsJsonObject("_embedded");
                        JsonArray events = embedded != null ? embedded.getAsJsonArray("events") : new JsonArray();

                        for (int i = 0; i < events.size(); i++) {
                            JsonObject event = events.get(i).getAsJsonObject();

                            // Access event properties with null checks
                            String name = event.has("name") ? event.get("name").getAsString() : "";
                            String id = event.has("id") ? event.get("id").getAsString() : "";
                            String date = event.has("dates") && event.getAsJsonObject("dates").has("start") ?
                                    event.getAsJsonObject("dates").getAsJsonObject("start").has("localDate") ?
                                            event.getAsJsonObject("dates").getAsJsonObject("start").get("localDate").getAsString() :
                                            "" :
                                    "";
                            String outputDate = "";
                            if (date != "") {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat outputFormat = new SimpleDateFormat("MM/dd/yyyy");
                                try {
                                    Date dateInFormat = inputFormat.parse(date);
                                    outputDate = outputFormat.format(dateInFormat);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                            String time = event.has("dates") && event.getAsJsonObject("dates").has("start") ?
                                    event.getAsJsonObject("dates").getAsJsonObject("start").has("localTime") ?
                                            event.getAsJsonObject("dates").getAsJsonObject("start").get("localTime").getAsString() :
                                            "" :
                                    "";
                            String outputTime = "";
                            if (!time.isEmpty()) {
                                SimpleDateFormat inputFormat = new SimpleDateFormat("HH:mm:ss");
                                SimpleDateFormat outputFormat = new SimpleDateFormat("h:mma");
                                try {
                                    Date dateInp = inputFormat.parse(time);
                                    String output = outputFormat.format(dateInp);
                                    outputTime = output;
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }

                            String image_url = event.has("images") && event.getAsJsonArray("images").size() > 0 ?
                                    event.getAsJsonArray("images").get(0).getAsJsonObject().has("url") ?
                                            event.getAsJsonArray("images").get(0).getAsJsonObject().get("url").getAsString() :
                                            "" :
                                    "";
                            String category = event.has("classifications") && event.getAsJsonArray("classifications").size() > 0 ?
                                    event.getAsJsonArray("classifications").get(0).getAsJsonObject().has("segment") ?
                                            event.getAsJsonArray("classifications").get(0).getAsJsonObject().getAsJsonObject("segment").has("name") ?
                                                    event.getAsJsonArray("classifications").get(0).getAsJsonObject().getAsJsonObject("segment").get("name").getAsString() :
                                                    "" :
                                            "" :
                                    "";
                            String venue = event.has("_embedded") && event.getAsJsonObject("_embedded").has("venues") &&
                                    event.getAsJsonObject("_embedded").getAsJsonArray("venues").size() > 0 ?
                                    event.getAsJsonObject("_embedded").getAsJsonArray("venues").get(0).getAsJsonObject().has("name") ?
                                            event.getAsJsonObject("_embedded").getAsJsonArray("venues").get(0).getAsJsonObject().get("name").getAsString() :
                                            "" :
                                    "";
                            searchItems.add(new SearchItem(id, image_url, name, venue, category, outputDate, outputTime));
                        }
                        // serialize the ArrayList to a JSON string
                        Gson gson2 = new Gson();
                        String json = gson2.toJson(searchItems);

                        Type type = new TypeToken<ArrayList<SearchItem>>() {}.getType();
                        ArrayList<SearchItem> searchItems = gson2.fromJson(json, type);

                        ProgressBar progressBar = view.findViewById(R.id.searchResultsProgressBar);
                        TextView noSearchResults = view.findViewById(R.id.no_search_results);
                        if (searchItems.size() != 0) {
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
                            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("h:mma");
                            Collections.sort(searchItems, Comparator.comparing((SearchItem s) -> LocalDate.parse(s.getSearch_date(), dateFormatter))
                                    .thenComparing((SearchItem s) -> LocalTime.parse(s.search_time.toUpperCase(), timeFormatter)));

                            noSearchResults.setVisibility(View.GONE);
                            recyclerView.setAdapter(new SearchAdapter(getActivity().getApplicationContext(), searchItems));
                            recyclerView.setVisibility(View.VISIBLE);
                            progressBar.setVisibility(View.GONE);
                        } else {
                            noSearchResults.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}