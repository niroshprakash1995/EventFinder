package com.web.eventfinder.eventdetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.squareup.picasso.Picasso;
import com.web.eventfinder.EventDetailsActivity;
import com.web.eventfinder.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class DetailsFragment extends Fragment {

    private DetailsItem detailsItem;

    public DetailsFragment(DetailsItem detailsItem) {
        this.detailsItem = detailsItem;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        populateDetails(view);
        return view;
    }

    public void populateDetails(View view){
        //Get all the fields
        TextView artistTeamsValue = view.findViewById(R.id.artistTeamsValue);
        artistTeamsValue.setText(detailsItem.getArtistNames());
        artistTeamsValue.setSelected(true);

        TextView venueValue = view.findViewById(R.id.venueValue);
        venueValue.setText(detailsItem.getVenueName());
        venueValue.setSelected(true);

        TextView dateValue = view.findViewById(R.id.dateValue);
        dateValue.setText(detailsItem.getDate());

        TextView timeValue = view.findViewById(R.id.timeValue);
        timeValue.setText(detailsItem.getTime());

        TextView genresValue = view.findViewById(R.id.genresValue);
        genresValue.setText(detailsItem.getGenres());
        genresValue.setSelected(true);

        TextView priceRangeValue = view.findViewById(R.id.priceRangeValue);
        priceRangeValue.setText(detailsItem.getPriceRange());

        TextView ticketStatusValue = view.findViewById(R.id.ticketStatusValue);
        ticketStatusValue.setText(detailsItem.getTicketStatus());

        TextView buyTicketsAtValue = view.findViewById(R.id.buyTicketsAtValue);
        buyTicketsAtValue.setText(detailsItem.getBuyTicketLink());
        buyTicketsAtValue.setSelected(true);

        ImageView search_image = view.findViewById(R.id.search_image);
        Picasso.get().load(detailsItem.getSeatMapLink()).fit().into(search_image);

        ((EventDetailsActivity) getActivity()).hideProgressBar();
    }
}