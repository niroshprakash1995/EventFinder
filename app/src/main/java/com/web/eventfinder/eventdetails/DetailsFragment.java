package com.web.eventfinder.eventdetails;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
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
        if(detailsItem.getPriceRange().isEmpty()){
            TextView priceRangeTitle = view.findViewById(R.id.priceRangeTitle);
            priceRangeTitle.setVisibility(View.GONE);
            priceRangeValue.setVisibility(View.GONE);
        }
        else{
            priceRangeValue.setText(detailsItem.getPriceRange());
        }

        TextView ticketStatusValue = view.findViewById(R.id.ticketStatusValue);
        ticketStatusValue.setText(detailsItem.getTicketStatus());

        TextView buyTicketsAtValue = view.findViewById(R.id.buyTicketsAtValue);
        buyTicketsAtValue.setText(detailsItem.getBuyTicketLink());
        buyTicketsAtValue.setClickable(true);
        buyTicketsAtValue.setSelected(true);
        buyTicketsAtValue.setPaintFlags(buyTicketsAtValue.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        buyTicketsAtValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(detailsItem.getBuyTicketLink()));
                startActivity(browserIntent);
            }
        });

        ImageView search_image = view.findViewById(R.id.search_image);
        if(!detailsItem.getSeatMapLink().isEmpty()){
            Picasso.get().load(detailsItem.getSeatMapLink()).fit().into(search_image);
        }
        else{
            search_image.setVisibility(View.GONE);
        }
        ((EventDetailsActivity) getActivity()).hideProgressBar();
    }
}