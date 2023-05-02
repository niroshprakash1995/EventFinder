package com.web.eventfinder.eventdetails;

public class DetailsItem {
    String artistNames;
    String venueName;
    String date;
    String time;
    String genres;
    String priceRange;
    String ticketStatus;
    String buyTicketLink;
    String seatMapLink;
    String ticketStatusColor;

    public DetailsItem(){
    super();
    }

    public DetailsItem(String artistNames, String venueName, String date, String time, String genres, String priceRange, String ticketStatus, String ticketStatusColor, String buyTicketLink, String seatMapLink) {
        this.artistNames = artistNames;
        this.venueName = venueName;
        this.date = date;
        this.time = time;
        this.genres = genres;
        this.priceRange = priceRange;
        this.ticketStatus = ticketStatus;
        this.ticketStatusColor = ticketStatusColor;
        this.buyTicketLink = buyTicketLink;
        this.seatMapLink = seatMapLink;
    }

    public String getArtistNames() {
        return artistNames;
    }

    public void setArtistNames(String artistNames) {
        this.artistNames = artistNames;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getPriceRange() {
        return priceRange;
    }

    public void setPriceRange(String priceRange) {
        this.priceRange = priceRange;
    }

    public String getTicketStatus() {
        return ticketStatus;
    }

    public void setTicketStatus(String ticketStatus) {
        this.ticketStatus = ticketStatus;
    }

    public String getTicketStatusColor() {
        return ticketStatusColor;
    }

    public void setTicketStatusColor(String ticketStatusColor) {
        this.ticketStatusColor = ticketStatusColor;
    }

    public String getBuyTicketLink() {
        return buyTicketLink;
    }

    public void setBuyTicketLink(String buyTicketLink) {
        this.buyTicketLink = buyTicketLink;
    }

    public String getSeatMapLink() {
        return seatMapLink;
    }

    public void setSeatMapLink(String seatMapLink) {
        this.seatMapLink = seatMapLink;
    }
}
