package com.web.eventfinder;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ArtistViewHolder extends RecyclerView.ViewHolder{

    public ImageView artistImage, artistAlbum1Img, artistAlbum2Img, artistAlbum3Img;
    public TextView artistName, artistFollowers, artistSpotifyLink, artistPopularityNumber;
    public ProgressBar artistPopularityProgressBar;

    public ArtistViewHolder(View view){
        super(view);
        artistImage = view.findViewById(R.id.artistImage);
        artistAlbum1Img = view.findViewById(R.id.artistAlbum1Img);
        artistAlbum2Img = view.findViewById(R.id.artistAlbum2Img);
        artistAlbum3Img = view.findViewById(R.id.artistAlbum3Img);

        //To give corner radius to images
        artistImage.setClipToOutline(true);
        artistAlbum1Img.setClipToOutline(true);
        artistAlbum2Img.setClipToOutline(true);
        artistAlbum3Img.setClipToOutline(true);

        artistName = view.findViewById(R.id.artistName);
        artistFollowers = view.findViewById(R.id.artistFollowers);
        artistSpotifyLink = view.findViewById(R.id.artistSpotifyLink);
        artistPopularityNumber = view.findViewById(R.id.artistPopularityNumber);

        artistPopularityProgressBar = view.findViewById(R.id.artistPopularityProgressBar);
    }

}
