package com.web.eventfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.web.eventfinder.adapters.FavoritesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoritesFragment extends Fragment {

    public FavoritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_favorites, container, false);

        TabLayout tabLayout = getActivity().findViewById(R.id.tabs);
        // Add tab selection listener
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 1){

                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("sharedpref", Context.MODE_PRIVATE);
                    String fav_items = sharedPreferences.getString("jsondata", "[]");
                    JSONArray jsonArray;
                    ArrayList<SearchItem> searchItems = new ArrayList<>();
                    boolean noFavorites = false;
                    try {
                        jsonArray = new JSONArray(fav_items);
                        if (jsonArray.length() == 0) {
                            noFavorites = true;
                        } else {

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObj = (JSONObject) jsonArray.get(i);
                                String search_image = jsonObj.get("search_image").toString();
                                String search_name = jsonObj.get("search_name").toString();
                                String search_venue = jsonObj.get("search_venue").toString();
                                String search_category = jsonObj.get("search_category").toString();
                                String search_date = jsonObj.get("search_date").toString();
                                String search_time = jsonObj.get("search_time").toString();
                                String search_id = jsonObj.get("eventID").toString();
                                SearchItem searchItem = new SearchItem(search_id, search_image, search_name, search_venue, search_category, search_date, search_time);
                                searchItems.add(searchItem);
                            }
                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                    if (!noFavorites) {
                        TextView noEvents = getView().findViewById(R.id.no_favorites_available);
                        noEvents.setVisibility(View.GONE);
                        RecyclerView recyclerView = rootView.findViewById(R.id.favorites_recyclerview);
                        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                        recyclerView.setAdapter(new FavoritesAdapter(getActivity().getApplicationContext(), searchItems));
                        recyclerView.setVisibility(View.VISIBLE);

                    } else {
                        RecyclerView recyclerView = rootView.findViewById(R.id.favorites_recyclerview);
                        recyclerView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        return rootView;
    }
}

