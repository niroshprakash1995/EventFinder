package com.web.eventfinder;

public class FavoritesItem {

    String search_image;
    String search_name;
    String search_venue;
    String search_category;
    String search_date;
    String search_time;
    String search_id;

    public FavoritesItem(String search_image, String search_name, String search_venue, String search_category, String search_date, String search_time, String search_id) {
        this.search_image = search_image;
        this.search_name = search_name;
        this.search_venue = search_venue;
        this.search_category = search_category;
        this.search_date = search_date;
        this.search_time = search_time;
        this.search_id = search_id;
    }

    public String getSearch_image() {
        return search_image;
    }

    public void setSearch_image(String search_image) {
        this.search_image = search_image;
    }

    public String getSearch_name() {
        return search_name;
    }

    public void setSearch_name(String search_name) {
        this.search_name = search_name;
    }

    public String getSearch_venue() {
        return search_venue;
    }

    public void setSearch_venue(String search_venue) {
        this.search_venue = search_venue;
    }

    public String getSearch_category() {
        return search_category;
    }

    public void setSearch_category(String search_category) {
        this.search_category = search_category;
    }

    public String getSearch_date() {
        return search_date;
    }

    public void setSearch_date(String search_date) {
        this.search_date = search_date;
    }

    public String getSearch_time() {
        return search_time;
    }

    public void setSearch_time(String search_time) {
        this.search_time = search_time;
    }

    public String getSearch_id() {
        return search_id;
    }

    public void setSearch_id(String search_id) {
        this.search_id = search_id;
    }
}
