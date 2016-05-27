package com.mk.places.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.mk.places.utilities.Utils;

public class GoodAct implements Parcelable {

    public static final Creator<GoodAct> CREATOR = new Creator<GoodAct>() {
        @Override
        public GoodAct createFromParcel(Parcel in) {
            return new GoodAct(in);
        }

        @Override
        public GoodAct[] newArray(int size) {
            return new GoodAct[size];
        }
    };

    private String
            title,
            images,
            url;


    protected GoodAct(Parcel in) {

        title = in.readString();
        images = in.readString();
        url = in.readString();

    }


    public GoodAct(String title, String images, String url) {
        this.title = Utils.convertEntitiesCharsHTML(title);
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
        return GoodActs.getGoodActsList().size();
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