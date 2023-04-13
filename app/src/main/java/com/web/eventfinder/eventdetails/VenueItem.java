package com.web.eventfinder.eventdetails;

public class VenueItem {

    private String name;
    private String address;
    private String cityState;
    private String contactInfo;
    private String openHoursDetail;
    private String generalRule;
    private String childRule;
    private Double lat;
    private Double lon;

    public VenueItem(){
        super();
    }

    public VenueItem(String name, String address, String cityState, String contactInfo, String openHoursDetail, String generalRule, String childRule, Double lat, Double lon) {
        this.name = name;
        this.address = address;
        this.cityState = cityState;
        this.contactInfo = contactInfo;
        this.openHoursDetail = openHoursDetail;
        this.generalRule = generalRule;
        this.childRule = childRule;
        this.lat = lat;
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCityState() {
        return cityState;
    }

    public void setCityState(String cityState) {
        this.cityState = cityState;
    }

    public String getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(String contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getOpenHoursDetail() {
        return openHoursDetail;
    }

    public void setOpenHoursDetail(String openHoursDetail) {
        this.openHoursDetail = openHoursDetail;
    }

    public String getGeneralRule() {
        return generalRule;
    }

    public void setGeneralRule(String generalRule) {
        this.generalRule = generalRule;
    }

    public String getChildRule() {
        return childRule;
    }

    public void setChildRule(String childRule) {
        this.childRule = childRule;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLon() {
        return lon;
    }

    public void setLon(Double lon) {
        this.lon = lon;
    }
}
