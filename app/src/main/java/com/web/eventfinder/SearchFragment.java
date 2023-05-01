package com.web.eventfinder;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;


import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import ch.hsr.geohash.GeoHash;
import io.github.muddz.styleabletoast.StyleableToast;

public class SearchFragment extends Fragment {
    public static String geohash;
    private Double latitude;
    private Double longitude;

    private static final String API_URL_KEYWORD_SEARCH = "https://web-csci571-np1995.wl.r.appspot.com/keywordsearch";
    private static final String API_URL_GET_DATA = "https://web-csci571-np1995.wl.r.appspot.com/getData";
    private AutoCompleteTextView autoCompleteTextView_keyword;
    private ArrayAdapter<String> adapter;

    private ArrayList<SearchItem> searchItems;
    private Spinner categoriesSpinner;
    private Switch locationSwitch;
    private EditText locationText;
    private EditText distanceEditText;
    private Button clearButton;
    private Button searchButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getViews(view);

        adapter = new ArrayAdapter < String > (getContext(), R.layout.dropdown_layout, new ArrayList < String > ());
        autoCompleteTextView_keyword.setAdapter(adapter);
        final ProgressBar autoCompleteProgressBar = view.findViewById(R.id.progressLoading);

        autoCompleteTextView_keyword.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView < ? > parent, View view, int position, long id) {
                // Handle item selection
                autoCompleteTextView_keyword.clearFocus();
            }
        });
        autoCompleteTextView_keyword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                autoCompleteProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                autoCompleteProgressBar.setVisibility(View.VISIBLE);
                if(autoCompleteTextView_keyword.getText().toString().length() == 0){
                    autoCompleteProgressBar.setVisibility(View.GONE);
                }
                getKeywordSuggestions(autoCompleteTextView_keyword.getText().toString(), autoCompleteProgressBar);
                autoCompleteTextView_keyword.setAdapter(adapter);
            }
        });

        ArrayAdapter < CharSequence > spinnerAdapter = ArrayAdapter.createFromResource(getContext(), R.array.categories, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_layout);
        categoriesSpinner.setAdapter(spinnerAdapter);
        categoriesSpinner.setSelection(0);

        categoriesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView < ? > adapterView, View view, int pos, long l) {
                TextView selectedTextView = view.findViewById(android.R.id.text1);
                selectedTextView.setTextColor(getResources().getColor(R.color.white));
                selectedTextView.setText(adapterView.getItemAtPosition(pos).toString());
            }

            @Override
            public void onNothingSelected(AdapterView < ? > adapterView) {

            }
        });

        //Auto-detect switch
        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    getLatAndLon();
                    locationSwitch.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.green)));
                    locationSwitch.setTrackTintList(ColorStateList.valueOf(getResources().getColor((R.color.green))));
                    locationText.setVisibility(View.GONE);
                    locationText.setText("");
                } else {
                    locationSwitch.setThumbTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
                    locationSwitch.setTrackTintList(ColorStateList.valueOf(getResources().getColor((R.color.track_tint))));
                    locationText.setVisibility(View.VISIBLE);
                }
            }
        });

        //Clear button
        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                autoCompleteTextView_keyword.setText("");
                distanceEditText.setText("10");
                categoriesSpinner.setSelection(0);
                locationSwitch.setChecked(false);
                locationText.setText("");
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            String old_keyword = bundle.getString("old_keyword");
            autoCompleteTextView_keyword.setText(old_keyword);
            autoCompleteProgressBar.setVisibility(View.GONE);
            String old_distance = bundle.getString("old_distance", "");
            distanceEditText.setText(old_distance);
            int old_categoryIndex = bundle.getInt("old_categoryIndex", 0);
            categoriesSpinner.setSelection(old_categoryIndex);
            boolean old_autodetect = bundle.getBoolean("old_autodetect", false);
            locationSwitch.setChecked(old_autodetect);
            String old_location = bundle.getString("old_location", "");
            locationText.setText(old_location);
        }

        //Search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Make API call here
                String keyword = autoCompleteTextView_keyword.getText().toString();
                String distance = distanceEditText.getText().toString();
                String category = categoriesSpinner.getSelectedItem().toString();
                String location = "";

                if (!locationSwitch.isChecked()) {
                    location = locationText.getText().toString();
                }

                String errorMsg = "Please fill all fields";
                if (keyword.length() == 0){
                    StyleableToast.makeText(getContext(), errorMsg, R.style.customToast).show();
                }
                else if (distance.length() == 0){
                    StyleableToast.makeText(getContext(), errorMsg, R.style.customToast).show();
                }
                else if (!locationSwitch.isChecked() && location.length() == 0){
                    StyleableToast.makeText(getContext(), errorMsg, R.style.customToast).show();
                }
                else{
                    getKeywordData(location, keyword, distance, category, view);
                }
            }
        });

    }

    public void getKeywordData(String location, String keyword, String distance, String category, View view) {
        getGeohash(location, new GeohashCallback() {
            @Override
            public void onSuccess(String geohash) {
                String categoryVal = category;
                if(category.equals("All")){
                    categoryVal = "Default";
                }
                String get_Data_url = API_URL_GET_DATA + "?keyword=" + keyword + "&distance=" + distance +
                        "&category=" + categoryVal + "&geohash=" + geohash;

                //Put the data Shared Preferences
                SharedPreferences sharedPref = getContext().getSharedPreferences("formsharedpref",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("keyword", keyword);
                editor.putString("distance", distance);
                String selectedCategory = categoriesSpinner.getSelectedItem().toString();
                int categoryIndex = Arrays.asList(getResources().getStringArray(R.array.categories)).indexOf(selectedCategory);
                editor.putInt("categoryIndex", categoryIndex);
                editor.putBoolean("autodetect", locationSwitch.isChecked());
                editor.putString("location", location);
                editor.commit();

//                getSearchResults(get_Data_url, view);
                // create a bundle and put the URL string into it
                Bundle bundle = new Bundle();
                bundle.putString("get_Data_url", get_Data_url);
                Navigation.findNavController(view).navigate(R.id.action_first_fragment_to_second_fragment, bundle);
            }

            @Override
            public void onFailure(Exception e) {
            }
        });
    }

    public interface GeohashCallback {
        void onSuccess(String geohash);
        void onFailure(Exception e);
    }

    public void getViews(View view) {
        //For keyword autocomplete field
        autoCompleteTextView_keyword = view.findViewById(R.id.autoCompleteTextView_keyword);
        //For the categories dropdown
        categoriesSpinner = view.findViewById(R.id.spinner_category);
        //For location field
        locationText = view.findViewById(R.id.editText_location);
        //For location switch
        locationSwitch = view.findViewById(R.id.location_switch);
        //For distance field
        distanceEditText = view.findViewById(R.id.editText_distance);
        //For clear button
        clearButton = view.findViewById(R.id.clear_button);
        //For search button
        searchButton = view.findViewById(R.id.search_button);
    }

    public void getKeywordSuggestions(CharSequence s, ProgressBar autoCompleteProgressBar) {
        String keyword = s.toString();
        if (keyword.length() > 0) {
            String url = API_URL_KEYWORD_SEARCH + "?keyword=" + keyword;
            RequestQueue queue = Volley.newRequestQueue(getContext());
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener < String > () {
                        @Override
                        public void onResponse(String response) {
                            Log.d("SearchFragment", "onResponse: " + response);
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                JSONArray keywordArray = jsonResponse.getJSONArray("keywords");
                                List < String > suggestions = new ArrayList < > ();
                                for (int i = 0; i < keywordArray.length(); i++) {
                                    String suggestion = keywordArray.getString(i);
                                    suggestions.add(suggestion);
                                }
                                adapter = new ArrayAdapter < String > (getContext(), R.layout.dropdown_layout, suggestions);
                                autoCompleteProgressBar.setVisibility(View.GONE);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("SearchFragment", "onErrorResponse: " + error.getMessage());
                    error.printStackTrace();
                }
            });
            queue.add(stringRequest);
        }
    }

    private void getLatAndLon() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "https://ipinfo.io/?token=b3a26d7c4db9dc";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject> () {
                    @Override
                    public void onResponse(JSONObject response) {
                            String[] loc;
                            try {
                                loc = response.getString("loc").split(",");
                                Log.d("latitude", loc[0].toString());
                                Log.d("longitude", loc[1].toString());
                                latitude = Double.parseDouble(loc[0]);
                                longitude = Double.parseDouble(loc[1]);

                            } catch (JSONException e) {
                                Log.d("IP LOG", "Got error");
                                e.printStackTrace();
                            }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("getLatAndLon - ERROR OCCURRED --->", error.toString());
            }
        });
        queue.add(jsonObjectRequest);
    }

    public void getGeohash(String location, GeohashCallback callback) {
        if (locationSwitch.isChecked()) {
            GeoHash geoHash = GeoHash.withCharacterPrecision(latitude, longitude, 7);
            geohash = geoHash.toBase32();
            Log.d("Geohash ---> ", geohash);
            callback.onSuccess(geohash);

        } else {
            String address = location;
            String key = "AIzaSyCXalOHLiAuigAYK-BHrWvKTknN1-LzdJI";
            String geoHashURL = "https://maps.googleapis.com/maps/api/geocode/json?address=" + address + "&key=" + key;
            try {
                computeGeohash(geoHashURL, callback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void computeGeohash(String geoHashURL, GeohashCallback callback) {
        // Request a string response from the provided URL.
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, geoHashURL,
                new Response.Listener < String > () {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parse the JSON response
                            JSONObject jsonResponse = new JSONObject(response);
                            String status = jsonResponse.getString("status");

                            if (status.equals("OK")) {
                                // Extract the latitude and longitude from the JSON data
                                JSONObject locationObj = jsonResponse.getJSONArray("results")
                                        .getJSONObject(0)
                                        .getJSONObject("geometry")
                                        .getJSONObject("location");
                                double latitude = locationObj.getDouble("lat");
                                double longitude = locationObj.getDouble("lng");

                                GeoHash geoHash = GeoHash.withCharacterPrecision(latitude, longitude, 7);
                                geohash = geoHash.toBase32();
                                callback.onSuccess(geohash);
                                Log.d("GEOHASH --->", geohash);
                            } else {
                                Log.d("GEOHASH - STATUS ERROR --->", status);
                            }
                        } catch (JSONException e) {
                            Log.d("GEOHASH - JSON ERROR --->", e.toString());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("GEOHASH - ERROR OCCURRED --->", error.toString());
            }
        });
        queue.add(stringRequest);
    }

}


