package com.mk.placesdrawer.model;

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

    private String location, sight, description, imgPlaceUrl;

    public PlacesItem(String location, String sight, String description, String imgPlaceUrl) {
        this.location = location;
        this.sight = sight;
        this.description = description;
        this.imgPlaceUrl = imgPlaceUrl;

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
        return 0;
    }


    protected PlacesItem(Parcel in) {
        location = in.readString();
        sight = in.readString();
        description = in.readString();
        imgPlaceUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeString(sight);
        dest.writeString(description);
        dest.writeString(imgPlaceUrl);
    }







}