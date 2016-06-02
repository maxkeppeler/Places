package com.mk.places.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mk.places.utilities.Utils;

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

    private int color;

    private String
            id,
            place,
            sight,
            continent,
            infoTitle,
            info,
            creditsTitle,
            creditsDesc,
            credits,
            description,
            url;


    protected Place(Parcel in) {

        id = in.readString();
        place = in.readString();
        sight = in.readString();
        continent = in.readString();
        infoTitle = in.readString();
        info = in.readString();
        creditsTitle = in.readString();
        creditsDesc = in.readString();
        credits = in.readString();
        description = in.readString();
        url = in.readString();


    }

    public Place(String id, String place, String sight, String continent, String infoTitle, String info, String creditsTitle, String creditsDesc, String credits, String description, String url) {

        this.id = Utils.cleanString(id);
        this.place = Utils.cleanString(place);
        this.sight = Utils.cleanString(sight);
        this.continent = Utils.cleanString(continent);
        this.infoTitle = Utils.cleanString(infoTitle);
        this.info = Utils.cleanString(info);
        this.creditsTitle = Utils.cleanString(creditsTitle);
        this.creditsDesc = Utils.cleanString(creditsDesc);
        this.credits = credits.replace(" ", "");
        this.description = Utils.cleanString(description);
        this.url = url.replace(" ", "");
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(place);
        dest.writeString(sight);
        dest.writeString(continent);
        dest.writeString(infoTitle);
        dest.writeString(info);
        dest.writeString(creditsTitle);
        dest.writeString(creditsDesc);
        dest.writeString(credits);
        dest.writeString(description);
        dest.writeString(url);
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

    public String getPlace() {
        return place;
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

    public String getCreditsTitle() {
        return creditsTitle;
    }

    public String getCredits() {
        return credits;
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

    public String getCreditsDesc() {
        return creditsDesc;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}