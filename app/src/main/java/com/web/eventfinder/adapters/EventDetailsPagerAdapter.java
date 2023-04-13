package com.web.eventfinder.adapters;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.web.eventfinder.eventdetails.ArtistItem;
import com.web.eventfinder.eventdetails.ArtistsFragment;
import com.web.eventfinder.eventdetails.DetailsFragment;
import com.web.eventfinder.eventdetails.DetailsItem;
import com.web.eventfinder.eventdetails.VenueFragment;
import com.web.eventfinder.eventdetails.VenueItem;

import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;


public class EventDetailsPagerAdapter extends FragmentPagerAdapter {

    private JsonObject jsonResponse;
    private DetailsItem detailsItem;
    private ArrayList<ArtistItem> artistsDataList;
    private VenueItem venueItem;
    private double lat;
    private double lon;
    private Context adapterContext;

    public EventDetailsPagerAdapter(Context context, FragmentManager fm, JsonObject jsonObject) {
        super(fm);
        adapterContext = context;
        jsonResponse = jsonObject;
        parseDetailsData();
        parseArtistsData();
        parseVenueData();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        Log.d("Adapter", "Creating fragment at position: " + position);
        switch(position){
            case 0:
                DetailsFragment detailsFragment = new DetailsFragment(detailsItem);
                return detailsFragment;
            case 1:
                ArtistsFragment artistsFragment = new ArtistsFragment(artistsDataList);
                return artistsFragment;
            default:
                VenueFragment venueFragment = new VenueFragment(venueItem);
                return venueFragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    public void parseDetailsData(){
        //Get the details tab data
        String artistNames = "";
        String venueName = "";
        String date = "";
        String time = "";
        String genres = "";
        String priceRange = "";
        String ticketStatus = "";
        String buyTicketLink = "";
        String seatMapLink = "";

        if(jsonResponse != null){
            if(jsonResponse.has("eventData")){
                JsonObject eventData = jsonResponse.getAsJsonObject("eventData");
                if(eventData.has("_embedded")){
                    //Get artist names
                    JsonArray artists = eventData.getAsJsonObject("_embedded").getAsJsonArray("attractions");
                    if(!artists.isJsonNull() && artists.size() > 0){
                        int size = artists.size();
                        for(int i = 0; i<size; i++){
                            String name = artists.get(i).getAsJsonObject().get("name").getAsString();
                            if(i == 0){
                                artistNames = name;
                            }
                            else{
                                artistNames = artistNames + " | " + name;
                            }
                        }
                    }
                    //Get the venue name
                    JsonArray venues = eventData.getAsJsonObject("_embedded").getAsJsonArray("venues");
                    if(!venues.isJsonNull() && venues.size() > 0 && venues.get(0).getAsJsonObject().has("name")){
                        venueName = venues.get(0).getAsJsonObject().get("name").getAsString();
                    }
                }

                //Get date, time and ticket status
                if(eventData.has("dates")){
                    JsonObject dates = eventData.getAsJsonObject("dates").get("start").getAsJsonObject();
                    if(!dates.isJsonNull()) {
                        if (dates.has("localDate")) {
                            date = dates.get("localDate").getAsString();
                            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
                            SimpleDateFormat outputFormat = new SimpleDateFormat("MMMM d, yyyy");

                            try {
                                Date d = inputFormat.parse(date);
                                date = outputFormat.format(d);
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                        if (dates.has("localTime")) {
                            time = dates.get("localTime").getAsString();
                            LocalTime t = LocalTime.parse(time);
                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("h:mm a");
                            time = t.format(formatter);
                        }
                    }
                }

                //Get status
                if(eventData.has("dates")){
                    JsonObject datesObj = eventData.get("dates").getAsJsonObject();
                    JsonObject statusObj = datesObj.get("status").getAsJsonObject();
                    if(!statusObj.isJsonNull()){
                        if(statusObj.has("code")){
                            ticketStatus = statusObj.get("code").getAsString();
                        }
                    }
                }

                //Get classifications
                if(eventData.has("classifications")){
                    JsonArray classifications = eventData.get("classifications").getAsJsonArray();
                    if(!classifications.isJsonNull() && classifications.size() > 0){
                        JsonObject classification = classifications.get(0).getAsJsonObject();
                        if(classification.has("segment") && classification.get("segment").getAsJsonObject().get("name").toString() != ""){
                            genres = classification.get("segment").getAsJsonObject().get("name").toString();
                        }
                        if(classification.has("genre") && classification.get("genre").getAsJsonObject().get("name").toString() != ""){
                            if(genres != ""){
                                genres = genres + " | " + classification.get("genre").getAsJsonObject().get("name").toString();
                            }
                            else{
                                genres = classification.get("genre").getAsJsonObject().get("name").toString();
                            }
                        }

                        if(classification.has("subGenre") && classification.get("subGenre").getAsJsonObject().get("name").toString() != ""){
                            if(genres != ""){
                                genres = genres + " | " + classification.get("subGenre").getAsJsonObject().get("name").toString();
                            }
                            else{
                                genres = classification.get("subGenre").getAsJsonObject().get("name").toString();
                            }
                        }

                        if(classification.has("type") && classification.get("type").getAsJsonObject().get("name").toString() != ""){
                            if(genres != ""){
                                genres = genres + " | " + classification.get("type").getAsJsonObject().get("name").toString();
                            }
                            else{
                                genres = classification.get("type").getAsJsonObject().get("name").toString();
                            }
                        }

                        if(classification.has("subType") && classification.get("subType").getAsJsonObject().get("name").toString() != ""){
                            if(genres != ""){
                                genres = genres + " | " + classification.get("subType").getAsJsonObject().get("name").toString();
                            }
                            else{
                                genres = classification.get("subType").getAsJsonObject().get("name").toString();
                            }
                        }
                    }
                }

                //Get price ranges
                if(eventData.has("priceRanges")){
                    JsonArray pRanges = eventData.get("priceRanges").getAsJsonArray();
                    if(!pRanges.isJsonNull() && pRanges.size() > 0){
                        if(pRanges.get(0).getAsJsonObject().get("min").getAsString() != ""){
                            priceRange = pRanges.get(0).getAsJsonObject().get("min").getAsString();
                        }
                        if(pRanges.get(0).getAsJsonObject().get("min").getAsString() != ""){
                            if(priceRange != ""){
                                priceRange = priceRange + " - " + pRanges.get(0).getAsJsonObject().get("min").getAsString();
                            }
                            else {
                                priceRange = pRanges.get(0).getAsJsonObject().get("min").getAsString();
                            }
                        }
                        priceRange = priceRange + " USD";
                    }
                }

                //Get buy ticket url
                if(eventData.has("url")){
                    if(eventData.get("url").getAsString() != ""){
                        buyTicketLink = eventData.get("url").getAsString();
                    }
                }

                if(eventData.has("seatmap")){
                    JsonObject seatMapObj = eventData.get("seatmap").getAsJsonObject();
                    if(seatMapObj.has("staticUrl") && seatMapObj.get("staticUrl").getAsString() != ""){
                        seatMapLink = seatMapObj.get("staticUrl").getAsString();
                    }
                }
            }
            detailsItem = new DetailsItem(artistNames, venueName, date, time, genres, priceRange, ticketStatus, buyTicketLink, seatMapLink);
        }
    }

    public void parseArtistsData(){
        artistsDataList = new ArrayList<>();

        if(jsonResponse != null) {
            if (jsonResponse.has("spotifyData")) {
                String jsonString = jsonResponse.get("spotifyData").getAsString();
                JsonArray spotifyData = JsonParser.parseString(jsonString).getAsJsonArray();

                int len = spotifyData.size();
                if(len > 0){
                    for(int i = 0; i<len; i++){
                        JsonObject obj = spotifyData.get(i).getAsJsonObject();
                        if(!obj.isJsonNull()){
                            String imageLink = obj.get("image").getAsString();
                            String name = obj.get("name").getAsString();
                            String followers = formatNumber(obj.get("followers").getAsInt());
                            String spotifyLink = obj.get("spotifyLink").getAsString();
                            int popularity = obj.get("popularity").getAsInt();
                            String album1img = obj.get("album1img").getAsString();
                            String album2img = obj.get("album2img").getAsString();
                            String album3img = obj.get("album3img").getAsString();

                            ArtistItem artistItem = new ArtistItem(imageLink, name, followers, spotifyLink, popularity, album1img, album2img, album3img);
                            artistsDataList.add(artistItem);
                        }
                    }
                }
            }
            System.out.println("Reached here");
        }
    }

    public static String formatNumber(int number) {
        String suffix = "";
        double value = number;
        if (number >= 1000) {
            suffix = "K";
            value = number / 1000.0;
        }
        if (number >= 1000000) {
            suffix = "M";
            value = number / 1000000.0;
        }
        return String.format("%.1f%s", value, suffix);
    }

    public void parseVenueData(){
        if(jsonResponse != null) {
            if (jsonResponse.has("venueData")) {
                JsonObject venueData = jsonResponse.get("venueData").getAsJsonObject();
                if(!venueData.isJsonNull() && venueData.has("_embedded")){
                    JsonObject embeddedObj = venueData.get("_embedded").getAsJsonObject();
                    if(!embeddedObj.isJsonNull() && embeddedObj.has("venues")){
                        JsonArray venuesObj = embeddedObj.get("venues").getAsJsonArray();
                        if(venuesObj.size() >= 1){
                            JsonObject venueObj = venuesObj.get(0).getAsJsonObject();

                            String name = venueObj.get("name").getAsString();
                            String address = "";
                            String cityState = "";
                            String contactInfo = "";
                            String openHoursDetail = "";
                            String generalRule = "";
                            String childRule = "";

                            venueItem = new VenueItem();

                            //For address
                            if(venueObj.has("address")){
                                address = venueObj.get("address").getAsJsonObject().get("line1").getAsString();
                            }
                            venueItem.setAddress(address);

                            //For city/state
                            String city = "";
                            if(venueObj.has("city")){
                                city = venueObj.get("city").getAsJsonObject().get("name").getAsString();
                            }
                            String state = "";
                            if(venueObj.has("state")){
                                state = venueObj.get("state").getAsJsonObject().get("name").getAsString();
                            }
                            if(city != ""){
                                cityState = city;
                            }
                            if(state != ""){
                                if(cityState != ""){
                                    cityState = cityState + ", ";
                                }
                                cityState = cityState + state;
                            }
                            venueItem.setCityState(cityState);


                            if(venueObj.has("boxOfficeInfo")){
                                JsonObject boxOfficeInfoObj = venueObj.get("boxOfficeInfo").getAsJsonObject();
                                //For contact info
                                if(boxOfficeInfoObj.has("phoneNumberDetail")) {
                                    contactInfo = boxOfficeInfoObj.get("phoneNumberDetail").getAsString();
                                }

                                //For open hours details
                                if(boxOfficeInfoObj.has("openHoursDetail")) {
                                    openHoursDetail = boxOfficeInfoObj.get("openHoursDetail").getAsString();
                                }
                            }
                            venueItem.setContactInfo(contactInfo);
                            venueItem.setOpenHoursDetail(openHoursDetail);

                            if(venueObj.has("generalInfo")){
                                JsonObject generalInfoObj = venueObj.get("generalInfo").getAsJsonObject();

                                //For open hours details
                                if(generalInfoObj.has("generalRule")) {
                                    generalRule = generalInfoObj.get("generalRule").getAsString();
                                }

                                //For child rule
                                if(generalInfoObj.has("childRule")) {
                                    childRule = generalInfoObj.get("childRule").getAsString();
                                }
                            }
                            venueItem.setGeneralRule(generalRule);
                            venueItem.setChildRule(childRule);

                            //Map
                            String mapAddress = name + " " + address + " " + cityState;
                            getLatLong(mapAddress);
                        }
                    }
                }
            }
        }
    }

    public void getLatLong(String mapAddress){
        RequestQueue queue = Volley.newRequestQueue(adapterContext);
        String key = "AIzaSyCXalOHLiAuigAYK-BHrWvKTknN1-LzdJI";
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                mapAddress +
                "&key=" +
                key;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener <JSONObject> () {
                    @Override
                    public void onResponse(JSONObject response) {

                        Gson gson = new Gson();
                        JsonObject jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();

                        if(jsonObject.has("results")){
                            JsonArray resultsObjects = jsonObject.get("results").getAsJsonArray();
                            if(!resultsObjects.isJsonNull() && resultsObjects.size()>=1){
                                JsonObject resultsObj = resultsObjects.get(0).getAsJsonObject();
                                if(resultsObj.has("geometry")){
                                    JsonObject geometryObj = resultsObj.get("geometry").getAsJsonObject();
                                    if(geometryObj.has("location")){
                                        JsonObject locationObj = geometryObj.get("location").getAsJsonObject();
                                        if(locationObj.has("lat")){
                                            lat = locationObj.get("lat").getAsDouble();
                                        }
                                        if(locationObj.has("lng")){
                                            lon = locationObj.get("lng").getAsDouble();
                                        }
                                        venueItem.setLat(lat);
                                        venueItem.setLon(lon);
                                    }
                                }
                            }
                        }

                    }}, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR --- getLatLong() --->", error.toString());
                error.printStackTrace();
            }
        });
        queue.add(request);
    }

}
