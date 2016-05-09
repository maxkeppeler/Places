package com.mk.places.models;

import android.os.Parcel;
import android.os.Parcelable;

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

    private String
            id,
            location,
            sight,
            continent,
            religion,
            description,
            url,
            mUrl;


    protected Place(Parcel in) {

        id = in.readString();
        location = in.readString();
        sight = in.readString();
        continent = in.readString();
        religion = in.readString();
        description = in.readString();

        url = in.readString();
        mUrl = in.readString();

    }

    public Place(String id, String location, String sight, String continent, String religion, String description, String url, String mUrl) {
        this.id = id;
        this.location = location;
        this.sight = sight;
        this.continent = continent;
        this.religion = religion;
        this.description = description;
        this.url = url;
        this.mUrl = mUrl;
    }

    @Override
    public int describeContents() {
        return Places.getPlacesList().size();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(location);
        dest.writeString(sight);
        dest.writeString(continent);
        dest.writeString(religion);
        dest.writeString(description);
        dest.writeString(url);
        dest.writeString(mUrl);
    }

    public String getId() {
        return id;
    }

    public String getLocation() {
        return location;
    }

    public String getSight() {
        return sight;
    }

    public String getContinent() {
        return continent;
    }

    public String getReligion() {
        return religion;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getmUrl() {
        return mUrl;
    }
}