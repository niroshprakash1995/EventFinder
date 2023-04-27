package com.web.eventfinder;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.web.eventfinder.adapters.FavoritesAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class SearchResultsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_results, container, false);

        String json = getArguments().getString("searchItemsJson");
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<SearchItem>>() {}.getType();
        ArrayList<SearchItem> searchItems = gson.fromJson(json, type);

        RecyclerView recyclerView = rootView.findViewById(R.id.search_result_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        TextView noSearchResults = rootView.findViewById(R.id.no_search_results);
        if (searchItems.size() != 0) {
            noSearchResults.setVisibility(View.GONE);
            recyclerView.setAdapter(new SearchAdapter(getActivity().getApplicationContext(), searchItems));
            recyclerView.setVisibility(View.VISIBLE);

        } else {
            noSearchResults.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

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
                            Snackbar snackbar = Snackbar.make(rootView, " added to favorites", Snackbar.LENGTH_SHORT);
                            View snackBarView= snackbar.getView();
                            snackBarView.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(getContext(), R.color.snackBar_grey)));
                            TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
                            textView.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                            snackbar.show();
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
}