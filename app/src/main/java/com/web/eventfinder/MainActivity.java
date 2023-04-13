package com.web.eventfinder;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.web.eventfinder.adapters.FavoritesAdapter;
import com.web.eventfinder.adapters.SectionsPagerAdapter;
import com.web.eventfinder.databinding.ActivityMainBinding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();
        View view1 = findViewById(R.id.fragment_search_results);
        View view2 = findViewById(R.id.fragment_favorites);

        if(view1 != null) {

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

            RecyclerView searchRecyclerView = view1.findViewById(R.id.search_result_recyclerview);
            for (int i = 0; i < searchRecyclerView.getChildCount(); i++) {
                View rv = searchRecyclerView.getChildAt(i);
                ImageView favoriteIcon = rv.findViewById(R.id.favorite_icon);
                String eventId = (String) favoriteIcon.getTag();
                if(searchIds.contains(eventId)){
                    favoriteIcon.setImageResource(R.drawable.fav_filled);
                }
                else{
                    favoriteIcon.setImageResource(R.drawable.fav);
                }
            }

            RecyclerView favoritesRecyclerView = view2.findViewById(R.id.favorites_recyclerview);
            for (int i = 0; i < favoritesRecyclerView.getChildCount(); i++) {
                View rv = favoritesRecyclerView.getChildAt(i);
                ImageView favoriteIcon = rv.findViewById(R.id.favorite_icon1);
                String eventId = (String) favoriteIcon.getTag();
                if(searchIds.contains(eventId)){
                   favoriteIcon.setImageResource(R.drawable.fav_filled);
                }
                else{
                    FavoritesAdapter adapter = (FavoritesAdapter) ((RecyclerView) rv.getParent()).getAdapter();
                    adapter.removeItem(i);
                    if(favoritesRecyclerView.getChildCount() == 1){
                        TextView noFav = view2.findViewById(R.id.no_favorites_available);
                        noFav.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Remove these
        SharedPreferences.Editor editor = getSharedPreferences("sharedpref", Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();

        SharedPreferences.Editor editor1 = getSharedPreferences("formsharedpref", Context.MODE_PRIVATE).edit();
        editor1.clear();
        editor1.commit();

        //Request location permission
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                PackageManager.PERMISSION_GRANTED);

        com.web.eventfinder.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = binding.viewPager;
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = binding.tabs;
        tabs.setupWithViewPager(viewPager);
    }
}