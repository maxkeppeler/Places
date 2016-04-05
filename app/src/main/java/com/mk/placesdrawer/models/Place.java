package com.mk.placesdrawer.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by maximiliankeppeler1 on 06.01.16.
 */
public class Place implements Parcelable {

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    private String position, religion,
            location, sight, description,
            imgPlaceUrl;

    public Place(String location,
                 String sight,
                 String description,
                 String position,
                 String religion,
                 String imgPlaceUrl) {

        this.location = location;
        this.sight = sight;
        this.description = description;
        this.religion = religion;
        this.imgPlaceUrl = imgPlaceUrl;

    }

    @Override
    public int describeContents() {
        return PlaceList.getPlacesList().size();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(location);
        dest.writeString(sight);
        dest.writeString(description);
        dest.writeString(position);
        dest.writeString(religion);
        dest.writeString(imgPlaceUrl);
    }

    protected Place(Parcel in) {
        location = in.readString();
        sight = in.readString();
        description = in.readString();
        position = in.readString();
        religion = in.readString();
        imgPlaceUrl = in.readString();
    }

    public String getPosition() {
        return position;
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
}