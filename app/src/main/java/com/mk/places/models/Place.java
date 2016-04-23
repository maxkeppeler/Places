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
            location,
            sight,
            continent,
            religion,
            description,

            url,
            url_a,
            url_b,
            url_c,
            url_d,
            url_e,
            url_f,
            url_g,
            url_h,
            url_i,
            url_j,
            url_k,
            url_l,
            url_m,
            url_n,
            url_o,
            url_p,
            url_q,
            url_r,
            url_s,
            url_t;

    private int favorite;

    public Place(
                 String location,
                 String sight,
                 String continent,
                 String religion,
                 String description,
                 String url,
                 String url_a,
                 String url_b,
                 String url_c,
                 String url_d,
                 String url_e,
                 String url_f,
                 String url_g,
                 String url_h,
                 String url_i,
                 String url_j,
                 String url_k,
                 String url_l,
                 String url_m,
                 String url_n,
                 String url_o,
                 String url_p,
                 String url_q,
                 String url_r,
                 String url_s,
                 String url_t,
                 int favorite

    ) {

        this.favorite = favorite;
        this.location = location;
        this.sight = sight;
        this.continent = continent;
        this.religion = religion;
        this.description = description;
        this.url = url;
        this.url_a = url_a;
        this.url_b = url_b;
        this.url_c = url_c;
        this.url_d = url_d;
        this.url_e = url_e;
        this.url_f = url_f;
        this.url_g = url_g;
        this.url_h = url_h;
        this.url_i = url_i;
        this.url_j = url_j;
        this.url_k = url_k;
        this.url_l = url_l;
        this.url_m = url_m;
        this.url_n = url_n;
        this.url_o = url_o;
        this.url_p = url_p;
        this.url_q = url_q;
        this.url_r = url_r;
        this.url_s = url_s;
        this.url_t = url_t;

    }

    @Override
    public int describeContents() {
        return Places.getPlacesList().size();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(location);
        dest.writeString(sight);
        dest.writeString(continent);
        dest.writeString(religion);
        dest.writeString(description);
        dest.writeInt(favorite);

        dest.writeString(url);
        dest.writeString(url_a);
        dest.writeString(url_b);
        dest.writeString(url_c);
        dest.writeString(url_d);
        dest.writeString(url_e);
        dest.writeString(url_f);
        dest.writeString(url_g);
        dest.writeString(url_h);
        dest.writeString(url_i);
        dest.writeString(url_j);
        dest.writeString(url_k);
        dest.writeString(url_l);
        dest.writeString(url_m);
        dest.writeString(url_n);
        dest.writeString(url_o);
        dest.writeString(url_p);
        dest.writeString(url_q);
        dest.writeString(url_r);
        dest.writeString(url_s);
        dest.writeString(url_t);
    }

    protected Place(Parcel in) {

        location = in.readString();
        sight = in.readString();
        continent = in.readString();
        religion = in.readString();
        description = in.readString();
        favorite = in.readInt();

        url = in.readString();
        url_a = in.readString();
        url_b = in.readString();
        url_c = in.readString();
        url_d = in.readString();
        url_e = in.readString();
        url_f = in.readString();
        url_g = in.readString();
        url_h = in.readString();
        url_i = in.readString();
        url_j = in.readString();
        url_k = in.readString();
        url_l = in.readString();
        url_m = in.readString();
        url_n = in.readString();
        url_o = in.readString();
        url_p = in.readString();
        url_q = in.readString();
        url_r = in.readString();
        url_s = in.readString();
        url_t = in.readString();

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

    public String getUrl_f() {
        return url_f;
    }

    public String getUrl_g() {
        return url_g;
    }

    public String getUrl_h() {
        return url_h;
    }

    public String getUrl_i() {
        return url_i;
    }

    public String getUrl_j() {
        return url_j;
    }

    public String getUrl_k() {
        return url_k;
    }

    public String getUrl_l() {
        return url_l;
    }

    public String getUrl_m() {
        return url_m;
    }

    public String getUrl_n() {
        return url_n;
    }

    public String getUrl_o() {
        return url_o;
    }

    public String getUrl_p() {
        return url_p;
    }

    public String getUrl_q() {
        return url_q;
    }

    public String getUrl_r() {
        return url_r;
    }

    public String getUrl_s() {
        return url_s;
    }

    public String getUrl_t() {
        return url_t;
    }

    public int getFavorite() {
        return this.favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getContinent() {
        return continent;
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

    public String getUrl() {
        return url;
    }

}