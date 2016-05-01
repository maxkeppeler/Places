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
            url_a_title, url_a_desc, url_a,
            url_b_title, url_b_desc, url_b,
            url_c_title, url_c_desc, url_c,
            url_d_title, url_d_desc, url_d,
            url_e_title, url_e_desc, url_e,
            url_f_title, url_f_desc, url_f,
            url_g_title, url_g_desc, url_g,
            url_h_title, url_h_desc, url_h,
            url_i_title, url_i_desc, url_i,
            url_j_title, url_j_desc, url_j,
            url_k_title, url_k_desc, url_k,
            url_l_title, url_l_desc, url_l,
            url_m_title, url_m_desc, url_m,
            url_n_title, url_n_desc, url_n,
            url_o_title, url_o_desc, url_o,
            url_p_title, url_p_desc, url_p,
            url_q_title, url_q_desc, url_q,
            url_r_title, url_r_desc, url_r,
            url_s_title, url_s_desc, url_s,
            url_t_title, url_t_desc, url_t;

    private int favorite;

    public Place(String id, String location, String sight, String continent, String religion, String description, String url,
                 String url_a_title, String url_a_desc, String url_a,
                 String url_b_title, String url_b_desc, String url_b,
                 String url_c_title, String url_c_desc, String url_c,
                 String url_d_title, String url_d_desc, String url_d,
                 String url_e_title, String url_e_desc, String url_e,
                 String url_f_title, String url_f_desc, String url_f,
                 String url_g_title, String url_g_desc, String url_g,
                 String url_h_title, String url_h_desc, String url_h,
                 String url_i_title, String url_i_desc, String url_i,
                 String url_j_title, String url_j_desc, String url_j,
                 String url_k_title, String url_k_desc, String url_k,
                 String url_l_title, String url_l_desc, String url_l,
                 String url_m_title, String url_m_desc, String url_m,
                 String url_n_title, String url_n_desc, String url_n,
                 String url_o_title, String url_o_desc, String url_o,
                 String url_p_title, String url_p_desc, String url_p,
                 String url_q_title, String url_q_desc, String url_q,
                 String url_r_title, String url_r_desc, String url_r,
                 String url_s_title, String url_s_desc, String url_s,
                 String url_t_title, String url_t_desc, String url_t,

                 int favorite) {

        this.id = id;
        this.location = location;
        this.sight = sight;
        this.continent = continent;
        this.religion = religion;
        this.description = description;
        this.url = url;
        this.url_a_title = url_a_title;
        this.url_a_desc = url_a_desc;
        this.url_a = url_a;
        this.url_b_title = url_b_title;
        this.url_b_desc = url_b_desc;
        this.url_b = url_b;
        this.url_c_title = url_c_title;
        this.url_c_desc = url_c_desc;
        this.url_c = url_c;
        this.url_d_title = url_d_title;
        this.url_d_desc = url_d_desc;
        this.url_d = url_d;
        this.url_e_title = url_e_title;
        this.url_e_desc = url_e_desc;
        this.url_e = url_e;
        this.url_f_title = url_f_title;
        this.url_f_desc = url_f_desc;
        this.url_f = url_f;
        this.url_g_title = url_g_title;
        this.url_g_desc = url_g_desc;
        this.url_g = url_g;
        this.url_h_title = url_h_title;
        this.url_h_desc = url_h_desc;
        this.url_h = url_h;
        this.url_i_title = url_i_title;
        this.url_i_desc = url_i_desc;
        this.url_i = url_i;
        this.url_j_title = url_j_title;
        this.url_j_desc = url_j_desc;
        this.url_j = url_j;
        this.url_k_title = url_k_title;
        this.url_k_desc = url_k_desc;
        this.url_k = url_k;
        this.url_l_title = url_l_title;
        this.url_l_desc = url_l_desc;
        this.url_l = url_l;
        this.url_m_title = url_m_title;
        this.url_m_desc = url_m_desc;
        this.url_m = url_m;
        this.url_n_title = url_n_title;
        this.url_n_desc = url_n_desc;
        this.url_n = url_n;
        this.url_o_title = url_o_title;
        this.url_o_desc = url_o_desc;
        this.url_o = url_o;
        this.url_p_title = url_p_title;
        this.url_p_desc = url_p_desc;
        this.url_p = url_p;
        this.url_q_title = url_q_title;
        this.url_q_desc = url_q_desc;
        this.url_q = url_q;
        this.url_r_title = url_r_title;
        this.url_r_desc = url_r_desc;
        this.url_r = url_r;
        this.url_s_title = url_s_title;
        this.url_s_desc = url_s_desc;
        this.url_s = url_s;
        this.url_t_title = url_t_title;
        this.url_t_desc = url_t_desc;
        this.url_t = url_t;
        this.favorite = favorite;
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
        dest.writeInt(favorite);
        dest.writeString(url);
        dest.writeString(url_a);
        dest.writeString(url_a_title);
        dest.writeString(url_a_desc);
        dest.writeString(url_b);
        dest.writeString(url_b_title);
        dest.writeString(url_b_desc);
        dest.writeString(url_c);
        dest.writeString(url_c_title);
        dest.writeString(url_c_desc);
        dest.writeString(url_d);
        dest.writeString(url_d_title);
        dest.writeString(url_d_desc);
        dest.writeString(url_e);
        dest.writeString(url_e_title);
        dest.writeString(url_e_desc);
        dest.writeString(url_f);
        dest.writeString(url_f_title);
        dest.writeString(url_f_desc);
        dest.writeString(url_g);
        dest.writeString(url_g_title);
        dest.writeString(url_g_desc);
        dest.writeString(url_h);
        dest.writeString(url_h_title);
        dest.writeString(url_h_desc);
        dest.writeString(url_i);
        dest.writeString(url_i_title);
        dest.writeString(url_i_desc);
        dest.writeString(url_j);
        dest.writeString(url_j_title);
        dest.writeString(url_j_desc);
        dest.writeString(url_k);
        dest.writeString(url_k_title);
        dest.writeString(url_k_desc);
        dest.writeString(url_l);
        dest.writeString(url_l_title);
        dest.writeString(url_l_desc);
        dest.writeString(url_m);
        dest.writeString(url_m_title);
        dest.writeString(url_m_desc);
        dest.writeString(url_n);
        dest.writeString(url_n_title);
        dest.writeString(url_n_desc);
        dest.writeString(url_o);
        dest.writeString(url_o_title);
        dest.writeString(url_o_desc);
        dest.writeString(url_p);
        dest.writeString(url_p_title);
        dest.writeString(url_p_desc);
        dest.writeString(url_q);
        dest.writeString(url_q_title);
        dest.writeString(url_q_desc);
        dest.writeString(url_r);
        dest.writeString(url_r_title);
        dest.writeString(url_r_desc);
        dest.writeString(url_s);
        dest.writeString(url_s_title);
        dest.writeString(url_s_desc);
        dest.writeString(url_t);
        dest.writeString(url_t_title);
        dest.writeString(url_t_desc);

    }

    protected Place(Parcel in) {

        id = in.readString();
        location = in.readString();
        sight = in.readString();
        continent = in.readString();
        religion = in.readString();
        description = in.readString();
        favorite = in.readInt();

        url = in.readString();
        url_a = in.readString();
        url_a_title = in.readString();
        url_a_desc = in.readString();
        url_b = in.readString();
        url_b_title = in.readString();
        url_b_desc = in.readString();
        url_c = in.readString();
        url_c_title = in.readString();
        url_c_desc = in.readString();
        url_d = in.readString();
        url_d_title = in.readString();
        url_d_desc = in.readString();
        url_e = in.readString();
        url_e_title = in.readString();
        url_e_desc = in.readString();
        url_f = in.readString();
        url_f_title = in.readString();
        url_f_desc = in.readString();
        url_g = in.readString();
        url_g_title = in.readString();
        url_g_desc = in.readString();
        url_h = in.readString();
        url_h_title = in.readString();
        url_h_desc = in.readString();
        url_i = in.readString();
        url_i_title = in.readString();
        url_i_desc = in.readString();
        url_j = in.readString();
        url_j_title = in.readString();
        url_j_desc = in.readString();
        url_k = in.readString();
        url_k_title = in.readString();
        url_k_desc = in.readString();
        url_l = in.readString();
        url_l_title = in.readString();
        url_l_desc = in.readString();
        url_m = in.readString();
        url_m_title = in.readString();
        url_m_desc = in.readString();
        url_n = in.readString();
        url_n_title = in.readString();
        url_n_desc = in.readString();
        url_o = in.readString();
        url_o_title = in.readString();
        url_o_desc = in.readString();
        url_p = in.readString();
        url_p_title = in.readString();
        url_p_desc = in.readString();
        url_q = in.readString();
        url_q_title = in.readString();
        url_q_desc = in.readString();
        url_r = in.readString();
        url_r_title = in.readString();
        url_r_desc = in.readString();
        url_s = in.readString();
        url_s_title = in.readString();
        url_s_desc = in.readString();
        url_t = in.readString();
        url_t_title = in.readString();
        url_t_desc = in.readString();
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

    public String getUrl_a_title() {
        return url_a_title;
    }

    public String getUrl_a_desc() {
        return url_a_desc;
    }

    public String getUrl_a() {
        return url_a;
    }

    public String getUrl_b_title() {
        return url_b_title;
    }

    public String getUrl_b_desc() {
        return url_b_desc;
    }

    public String getUrl_b() {
        return url_b;
    }

    public String getUrl_c_title() {
        return url_c_title;
    }

    public String getUrl_c_desc() {
        return url_c_desc;
    }

    public String getUrl_c() {
        return url_c;
    }

    public String getUrl_d_title() {
        return url_d_title;
    }

    public String getUrl_d_desc() {
        return url_d_desc;
    }

    public String getUrl_d() {
        return url_d;
    }

    public String getUrl_e_title() {
        return url_e_title;
    }

    public String getUrl_e_desc() {
        return url_e_desc;
    }

    public String getUrl_e() {
        return url_e;
    }

    public String getUrl_f_title() {
        return url_f_title;
    }

    public String getUrl_f_desc() {
        return url_f_desc;
    }

    public String getUrl_f() {
        return url_f;
    }

    public String getUrl_g_title() {
        return url_g_title;
    }

    public String getUrl_g_desc() {
        return url_g_desc;
    }

    public String getUrl_g() {
        return url_g;
    }

    public String getUrl_h_title() {
        return url_h_title;
    }

    public String getUrl_h_desc() {
        return url_h_desc;
    }

    public String getUrl_h() {
        return url_h;
    }

    public String getUrl_i_title() {
        return url_i_title;
    }

    public String getUrl_i_desc() {
        return url_i_desc;
    }

    public String getUrl_i() {
        return url_i;
    }

    public String getUrl_j_title() {
        return url_j_title;
    }

    public String getUrl_j_desc() {
        return url_j_desc;
    }

    public String getUrl_j() {
        return url_j;
    }

    public String getUrl_k_title() {
        return url_k_title;
    }

    public String getUrl_k_desc() {
        return url_k_desc;
    }

    public String getUrl_k() {
        return url_k;
    }

    public String getUrl_l_title() {
        return url_l_title;
    }

    public String getUrl_l_desc() {
        return url_l_desc;
    }

    public String getUrl_l() {
        return url_l;
    }

    public String getUrl_m_title() {
        return url_m_title;
    }

    public String getUrl_m_desc() {
        return url_m_desc;
    }

    public String getUrl_m() {
        return url_m;
    }

    public String getUrl_n_title() {
        return url_n_title;
    }

    public String getUrl_n_desc() {
        return url_n_desc;
    }

    public String getUrl_n() {
        return url_n;
    }

    public String getUrl_o_title() {
        return url_o_title;
    }

    public String getUrl_o_desc() {
        return url_o_desc;
    }

    public String getUrl_o() {
        return url_o;
    }

    public String getUrl_p_title() {
        return url_p_title;
    }

    public String getUrl_p_desc() {
        return url_p_desc;
    }

    public String getUrl_p() {
        return url_p;
    }

    public String getUrl_q_title() {
        return url_q_title;
    }

    public String getUrl_q_desc() {
        return url_q_desc;
    }

    public String getUrl_q() {
        return url_q;
    }

    public String getUrl_r_title() {
        return url_r_title;
    }

    public String getUrl_r_desc() {
        return url_r_desc;
    }

    public String getUrl_r() {
        return url_r;
    }

    public String getUrl_s_title() {
        return url_s_title;
    }

    public String getUrl_s_desc() {
        return url_s_desc;
    }

    public String getUrl_s() {
        return url_s;
    }

    public String getUrl_t_title() {
        return url_t_title;
    }

    public String getUrl_t_desc() {
        return url_t_desc;
    }

    public String getUrl_t() {
        return url_t;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }
}