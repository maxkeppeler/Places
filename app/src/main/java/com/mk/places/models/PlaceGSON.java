package com.mk.places.models;

import com.google.gson.annotations.SerializedName;


// Place Object for parsing JSON with GSON
// TODO: Parsing JSON WITH GSON

public class PlaceGSON {

    @SerializedName("id")
    private String id;
    @SerializedName("location")
    private String location;
    @SerializedName("sight")
    private String sight;
    @SerializedName("continent")
    private String continent;
    @SerializedName("infoTitle")
    private String infoTitle;
    @SerializedName("info")
    private String info;
    @SerializedName("creditsTitle")
    private String creditsTitle;
    @SerializedName("creditsDesc")
    private String creditsDesc;
    @SerializedName("credits")
    private String credits;
    @SerializedName("description")
    private String description;
    @SerializedName("url")
    private String url;

    /**
     * @return The id
     */
    @SerializedName("id")
    public String getId() {
        return id;
    }

    /**
     * @param id The id
     */
    @SerializedName("id")
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return The location
     */
    @SerializedName("location")
    public String getLocation() {
        return location;
    }

    /**
     * @param location The location
     */
    @SerializedName("location")
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * @return The sight
     */
    @SerializedName("sight")
    public String getSight() {
        return sight;
    }

    /**
     * @param sight The sight
     */
    @SerializedName("sight")
    public void setSight(String sight) {
        this.sight = sight;
    }

    /**
     * @return The continent
     */
    @SerializedName("continent")
    public String getContinent() {
        return continent;
    }

    /**
     * @param continent The continent
     */
    @SerializedName("continent")
    public void setContinent(String continent) {
        this.continent = continent;
    }

    /**
     * @return The infoTitle
     */
    @SerializedName("infoTitle")
    public String getInfoTitle() {
        return infoTitle;
    }

    /**
     * @param infoTitle The infoTitle
     */
    @SerializedName("infoTitle")
    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    /**
     * @return The info
     */
    @SerializedName("info")
    public String getInfo() {
        return info;
    }

    /**
     * @param info The info
     */
    @SerializedName("info")
    public void setInfo(String info) {
        this.info = info;
    }

    /**
     * @return The creditsTitle
     */
    @SerializedName("creditsTitle")
    public String getCreditsTitle() {
        return creditsTitle;
    }

    /**
     * @param creditsTitle The creditsTitle
     */
    @SerializedName("creditsTitle")
    public void setCreditsTitle(String creditsTitle) {
        this.creditsTitle = creditsTitle;
    }

    /**
     * @return The creditsDesc
     */
    @SerializedName("creditsDesc")
    public String getCreditsDesc() {
        return creditsDesc;
    }

    /**
     * @param creditsDesc The creditsDesc
     */
    @SerializedName("creditsDesc")
    public void setCreditsDesc(String creditsDesc) {
        this.creditsDesc = creditsDesc;
    }

    /**
     * @return The credits
     */
    @SerializedName("credits")
    public String getCredits() {
        return credits;
    }

    /**
     * @param credits The credits
     */
    @SerializedName("credits")
    public void setCredits(String credits) {
        this.credits = credits;
    }

    /**
     * @return The description
     */
    @SerializedName("description")
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    @SerializedName("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The url
     */
    @SerializedName("url")
    public String getUrl() {
        return url;
    }

    /**
     * @param url The url
     */
    @SerializedName("url")
    public void setUrl(String url) {
        this.url = url;
    }

}