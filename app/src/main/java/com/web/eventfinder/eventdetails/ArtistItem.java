package com.web.eventfinder.eventdetails;

public class ArtistItem {

    private String imageLink;
    private String name;
    private String followers;
    private String spotifyLink;
    private int popularity;
    private String album1img;
    private String album2img;
    private String album3img;

    public ArtistItem(String imageLink, String name, String followers, String spotifyLink, int popularity, String album1img, String album2img, String album3img) {
        this.imageLink = imageLink;
        this.name = name;
        this.followers = followers;
        this.spotifyLink = spotifyLink;
        this.popularity = popularity;
        this.album1img = album1img;
        this.album2img = album2img;
        this.album3img = album3img;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFollowers() {
        return followers;
    }

    public void setFollowers(String followers) {
        this.followers = followers;
    }

    public String getSpotifyLink() {
        return spotifyLink;
    }

    public void setSpotifyLink(String spotifyLink) {
        this.spotifyLink = spotifyLink;
    }

    public int getPopularity() {
        return popularity;
    }

    public void setPopularity(int popularity) {
        this.popularity = popularity;
    }

    public String getAlbum1img() {
        return album1img;
    }

    public void setAlbum1img(String album1img) {
        this.album1img = album1img;
    }

    public String getAlbum2img() {
        return album2img;
    }

    public void setAlbum2img(String album2img) {
        this.album2img = album2img;
    }

    public String getAlbum3img() {
        return album3img;
    }

    public void setAlbum3img(String album3img) {
        this.album3img = album3img;
    }
}
