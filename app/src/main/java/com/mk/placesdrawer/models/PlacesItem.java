package com.mk.placesdrawer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maximiliankeppeler1 on 06.01.16.
 */
public class PlacesItem implements Parcelable {

    public static final Creator<PlacesItem> CREATOR = new Creator<PlacesItem>() {
        @Override
        public PlacesItem createFromParcel(Parcel in) {
            return new PlacesItem(in);
        }

        @Override
        public PlacesItem[] newArray(int size) {
            return new PlacesItem[size];
        }
    };

    private String  country, state, city, religion,
                    location, sight, description,
                    imgPlaceUrl;

    public PlacesItem(String location,
                      String sight,
                      String description,
                      String country,
                      String state,
                      String city,
                      String religion,
                      String imgPlaceUrl) {

        this.location = location;
        this.sight = sight;
        this.description = description;
        this.country = country;
        this.state = state;
        this.city = city;
        this.religion = religion;
        this.imgPlaceUrl = imgPlaceUrl;

    }

    protected PlacesItem(Parcel in) {
        location = in.readString();
        sight = in.readString();
        description = in.readString();
        country = in.readString();
        state = in.readString();
        city = in.readString();
        religion = in.readString();
        imgPlaceUrl = in.readString();
    }

    public String getCountry() {
        return country;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getReligion() {
        return religion;
    }

    public String getLocation() {
        return location;
    }

    public String getSight() {
        return sight;
    }

    public String getDescription() {
        return description;
    }

    public String getImgPlaceUrl() {
        return imgPlaceUrl;
    }

    @Override
    public int describeContents() {
        return PlacesList.getPlacesList().size();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeString(sight);
        dest.writeString(description);
        dest.writeString(country);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(religion);
        dest.writeString(imgPlaceUrl);
    }

}