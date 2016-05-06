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
            url_0,
            url_1,
            url_2,
            url_3,
            url_4,
            url_5,
            url_6,
            url_7,
            url_8,
            url_9,
            url_10,
            url_11,
            url_12,
            url_13,
            url_14,
            url_15,
            url_16,
            url_17,
            url_18,
            url_19;


    protected Place(Parcel in) {

        id = in.readString();
        location = in.readString();
        sight = in.readString();
        continent = in.readString();
        religion = in.readString();
        description = in.readString();

        url = in.readString();
        url_0 = in.readString();
        url_1 = in.readString();
        url_2 = in.readString();
        url_3 = in.readString();
        url_4 = in.readString();
        url_5 = in.readString();
        url_6 = in.readString();
        url_7 = in.readString();
        url_8 = in.readString();
        url_9 = in.readString();
        url_10 = in.readString();
        url_11 = in.readString();
        url_12 = in.readString();
        url_13 = in.readString();
        url_14 = in.readString();
        url_15 = in.readString();
        url_16 = in.readString();
        url_17 = in.readString();
        url_18 = in.readString();
        url_19 = in.readString();
    }

    public Place(String id, String location, String sight, String continent, String religion, String description, String url, String url_0, String url_1, String url_2, String url_3, String url_4, String url_5, String url_6, String url_7, String url_8, String url_9, String url_10, String url_11, String url_12, String url_13, String url_14, String url_15, String url_16, String url_17, String url_18, String url_19) {
        this.id = id;
        this.location = location;
        this.sight = sight;
        this.continent = continent;
        this.religion = religion;
        this.description = description;
        this.url = url;
        this.url_0 = url_0;
        this.url_1 = url_1;
        this.url_2 = url_2;
        this.url_3 = url_3;
        this.url_4 = url_4;
        this.url_5 = url_5;
        this.url_6 = url_6;
        this.url_7 = url_7;
        this.url_8 = url_8;
        this.url_9 = url_9;
        this.url_10 = url_10;
        this.url_11 = url_11;
        this.url_12 = url_12;
        this.url_13 = url_13;
        this.url_14 = url_14;
        this.url_15 = url_15;
        this.url_16 = url_16;
        this.url_17 = url_17;
        this.url_18 = url_18;
        this.url_19 = url_19;
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
        dest.writeString(url_0);
        dest.writeString(url_1);
        dest.writeString(url_2);
        dest.writeString(url_3);
        dest.writeString(url_4);
        dest.writeString(url_5);
        dest.writeString(url_6);
        dest.writeString(url_7);
        dest.writeString(url_8);
        dest.writeString(url_9);
        dest.writeString(url_10);
        dest.writeString(url_11);
        dest.writeString(url_12);
        dest.writeString(url_13);
        dest.writeString(url_13);
        dest.writeString(url_15);
        dest.writeString(url_16);
        dest.writeString(url_17);
        dest.writeString(url_18);
        dest.writeString(url_19);

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

    public String getUrl_0() {
        return url_0;
    }

    public String getUrl_1() {
        return url_1;
    }

    public String getUrl_2() {
        return url_2;
    }

    public String getUrl_3() {
        return url_3;
    }

    public String getUrl_4() {
        return url_4;
    }

    public String getUrl_5() {
        return url_5;
    }

    public String getUrl_6() {
        return url_6;
    }

    public String getUrl_7() {
        return url_7;
    }

    public String getUrl_8() {
        return url_8;
    }

    public String getUrl_9() {
        return url_9;
    }

    public String getUrl_10() {
        return url_10;
    }

    public String getUrl_11() {
        return url_11;
    }

    public String getUrl_12() {
        return url_12;
    }

    public String getUrl_13() {
        return url_13;
    }

    public String getUrl_14() {
        return url_14;
    }

    public String getUrl_15() {
        return url_15;
    }

    public String getUrl_16() {
        return url_16;
    }

    public String getUrl_17() {
        return url_17;
    }

    public String getUrl_18() {
        return url_18;
    }

    public String getUrl_19() {
        return url_19;
    }
}