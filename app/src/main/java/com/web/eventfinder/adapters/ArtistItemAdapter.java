package com.web.eventfinder.adapters;
//Used ChatGPT
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.web.eventfinder.R;
import com.web.eventfinder.eventdetails.ArtistItem;
import com.web.eventfinder.ArtistViewHolder;

import java.util.ArrayList;

public class ArtistItemAdapter extends RecyclerView.Adapter<ArtistViewHolder> {

    private Activity activityContext;
    private ArrayList<ArtistItem> artistItemList;

    public ArtistItemAdapter(Activity activity, ArrayList<ArtistItem> list){
        this.activityContext = activity;
        this.artistItemList = list;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(activityContext).inflate(R.layout.artist_item_layout, parent, false);
        return new ArtistViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        ArtistItem currentItem = artistItemList.get(position);

        String artistImage = currentItem.getImageLink();
        String artistName = currentItem.getName();
        String artistFollowers = currentItem.getFollowers();
        String artistSpotifyLinkVal = currentItem.getSpotifyLink();
        int artistPopularityNumber = currentItem.getPopularity();
        String artistAlbum1Img = currentItem.getAlbum1img();
        String artistAlbum2Img = currentItem.getAlbum2img();
        String artistAlbum3Img = currentItem.getAlbum3img();

        Picasso.get().load(artistImage).fit().into(holder.artistImage);
        holder.artistName.setText(artistName);
        holder.artistFollowers.setText(artistFollowers + " Followers");
        holder.artistSpotifyLink.setClickable(true);
        holder.artistSpotifyLink.setMovementMethod(LinkMovementMethod.getInstance());
        String text = "<a href='" + artistSpotifyLinkVal + "'> Check out on Spotify </a>";
        holder.artistSpotifyLink.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
        holder.artistSpotifyLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(artistSpotifyLinkVal));
                browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                activityContext.startActivity(browserIntent);
            }
        });
        holder.artistPopularityProgressBar.setProgress(artistPopularityNumber);
        holder.artistPopularityNumber.setText(String.valueOf(artistPopularityNumber));
        Picasso.get().load(artistAlbum1Img).fit().into(holder.artistAlbum1Img);
        Picasso.get().load(artistAlbum2Img).fit().into(holder.artistAlbum2Img);
        Picasso.get().load(artistAlbum3Img).fit().into(holder.artistAlbum3Img);

        holder.artistName.setSelected(true);
        holder.artistFollowers.setSelected(true);
    }

    @Override
    public int getItemCount() {
        return artistItemList.size();
    }
}
