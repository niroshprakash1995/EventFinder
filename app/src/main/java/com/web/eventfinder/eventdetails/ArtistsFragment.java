package com.web.eventfinder.eventdetails;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.web.eventfinder.R;
import com.web.eventfinder.adapters.ArtistItemAdapter;

import java.util.ArrayList;

public class ArtistsFragment extends Fragment {

    private String mParam1;
    private String mParam2;
    private ArrayList<ArtistItem> artistsDataList;
    private ArtistItemAdapter artistItemAdapter;
    private RecyclerView recyclerView;

    public ArtistsFragment(ArrayList<ArtistItem> artistsDataList) {
        this.artistsDataList = artistsDataList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_artists, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        if(artistsDataList.size() == 0){
            getView().findViewById(R.id.artist_music_data_unavailable).setVisibility(View.VISIBLE);
        }
        else{
            getView().findViewById(R.id.artist_music_data_unavailable).setVisibility(View.GONE);
            recyclerView = getView().findViewById(R.id.artists_recyclerview);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(new ArtistItemAdapter((Activity) getActivity(), artistsDataList));
        }

    }
}