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
            infoTitle,
            info,
            description,
            url;


    protected Place(Parcel in) {

        id = in.readString();
        location = in.readString();
        sight = in.readString();
        continent = in.readString();
        infoTitle = in.readString();
        info = in.readString();
        description = in.readString();
        url = in.readString();

    }


    public Place(String id, String location, String sight, String continent, String infoTitle, String info, String description, String url) {
        this.id = id;
        this.location = location;
        this.sight = sight;
        this.continent = continent;
        this.infoTitle = infoTitle;
        this.info = info;
        this.description = description;
        this.url = url;
    }

    @Override
    public int describeContents() {
        return Places.getPlacesList().size();
    }

    public String getUrl() {
        return url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getInfoTitle() {
        return infoTitle;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(location);
        dest.writeString(sight);
        dest.writeString(continent);
        dest.writeString(infoTitle);
        dest.writeString(info);
        dest.writeString(description);
        dest.writeString(url);
    }


}