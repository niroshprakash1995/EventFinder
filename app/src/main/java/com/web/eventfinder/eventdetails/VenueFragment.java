package com.web.eventfinder.eventdetails;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.web.eventfinder.R;

import org.json.JSONObject;

public class VenueFragment extends Fragment {

    private VenueItem venueItem;
    private double lat;
    private double lon;

    public VenueFragment(VenueItem venueItem) {
        this.venueItem = venueItem;
        lat = venueItem.getLat();
        lon = venueItem.getLon();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_venue, container, false);
        populateVenueDetails(view);

        TextView openHoursValue = view.findViewById(R.id.openHoursValue);
        openHoursValue.setOnClickListener(new View.OnClickListener() {
            boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    openHoursValue.setMaxLines(3);
                    isExpanded = false;
                } else {
                    openHoursValue.setMaxLines(Integer.MAX_VALUE);
                    isExpanded = true;
                }
            }
        });
        TextView generalRulesValue = view.findViewById(R.id.generalRulesValue);
        generalRulesValue.setOnClickListener(new View.OnClickListener() {
            boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    generalRulesValue.setMaxLines(3);
                    isExpanded = false;
                } else {
                    generalRulesValue.setMaxLines(Integer.MAX_VALUE);
                    isExpanded = true;
                }
            }
        });
        TextView childRulesValue = view.findViewById(R.id.childRulesValue);
        childRulesValue.setOnClickListener(new View.OnClickListener() {
            boolean isExpanded = false;

            @Override
            public void onClick(View v) {
                if (isExpanded) {
                    childRulesValue.setMaxLines(3);
                    isExpanded = false;
                } else {
                    childRulesValue.setMaxLines(Integer.MAX_VALUE);
                    isExpanded = true;
                }
            }
        });

        return view;
    }

    private OnMapReadyCallback mapCallback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng location = new LatLng(lat, lon);
            googleMap.addMarker(new MarkerOptions().position(location));
            CameraPosition cameraPosition = new CameraPosition.Builder().target(location).zoom(15.0f).build();
            CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
            googleMap.moveCamera(cameraUpdate);
        }
    };

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(mapCallback);
        }
    }

    public void populateVenueDetails(View view){
        //Get all the fields
        TextView venueName = view.findViewById(R.id.venueNameValue);
        venueName.setText(venueItem.getName());
        venueName.setSelected(true);

        TextView address = view.findViewById(R.id.venueAddressValue);
        address.setText(venueItem.getAddress());
        address.setSelected(true);

        TextView cityState = view.findViewById(R.id.venueCityStateValue);
        cityState.setText(venueItem.getCityState());
        cityState.setSelected(true);

        TextView contactInfo = view.findViewById(R.id.venueContactInfoValue);
        contactInfo.setText(venueItem.getContactInfo());
        contactInfo.setSelected(true);

        TextView openHours = view.findViewById(R.id.openHoursValue);
        openHours.setText(venueItem.getOpenHoursDetail());

        TextView generalRules = view.findViewById(R.id.generalRulesValue);
        generalRules.setText(venueItem.getGeneralRule());

        TextView childRules = view.findViewById(R.id.childRulesValue);
        childRules.setText(venueItem.getChildRule());
    }
}