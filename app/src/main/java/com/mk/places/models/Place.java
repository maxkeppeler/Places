package com.mk.places.models;

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

    private String
            position, religion, location, sight, description, imgPlaceUrl,
            url_a,
            url_b,
            url_c,
            url_d,
            url_e
            ;
    private int favorite;

    public Place(
                 String location,
                 String sight,
                 String description,
                 String position,
                 String religion,
                 String imgPlaceUrl,
                 String url_a,
                 String url_b,
                 String url_c,
                 String url_d,
                 String url_e,
                 int favorite

    ) {

        this.favorite = favorite;
        this.location = location;
        this.sight = sight;
        this.description = description;
        this.religion = religion;
        this.imgPlaceUrl = imgPlaceUrl;
        this.imgPlaceUrl = imgPlaceUrl;
        this.url_a = url_a;
        this.url_b = url_b;
        this.url_c = url_c;
        this.url_d = url_d;
        this.url_e = url_e;

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
        dest.writeString(url_a);
        dest.writeString(url_b);
        dest.writeString(url_c);
        dest.writeString(url_d);
        dest.writeString(url_e);
        dest.writeInt(favorite);
    }

    protected Place(Parcel in) {
        location = in.readString();
        sight = in.readString();
        description = in.readString();
        position = in.readString();
        religion = in.readString();
        imgPlaceUrl = in.readString();
        url_a = in.readString();
        url_b = in.readString();
        url_c = in.readString();
        url_d = in.readString();
        url_e = in.readString();
        favorite = in.readInt();

    }


    public String getUrl_a() {
        return url_a;
    }

    public String getUrl_b() {
        return url_b;
    }

    public String getUrl_c() {
        return url_c;
    }

    public String getUrl_d() {
        return url_d;
    }

    public String getUrl_e() {
        return url_e;
    }

    public int getFavorite() {
        return this.favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
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