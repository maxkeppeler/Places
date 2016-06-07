package com.earth.places.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.earth.places.utilities.Utils;

public class Disaster implements Parcelable {

    public static final Creator<Disaster> CREATOR = new Creator<Disaster>() {
        @Override
        public Disaster createFromParcel(Parcel in) {
            return new Disaster(in);
        }

        @Override
        public Disaster[] newArray(int size) {
            return new Disaster[size];
        }
    };

    private String
            title,
            images,
            url;


    protected Disaster(Parcel in) {

        title = in.readString();
        images = in.readString();
        url = in.readString();

    }


    public Disaster(String title, String images, String url) {
        this.title = Utils.cleanString(title);
        this.images = images;
        this.url = url;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(title);
        dest.writeString(images);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return Disasters.getDisastersList().size();
    }

    public String getTitle() {
        return title;
    }

    public String getImages() {
        return images;
    }

    public String getUrl() {
        return url;
    }
}