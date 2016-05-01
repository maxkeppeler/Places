package com.mk.places.activity;

import com.afollestad.inquiry.annotations.Column;

/**
 * Created by max on 30.04.16.
 */
public class Favorites {

    @Column(name = "_id", primaryKey = true, notNull = true, autoIncrement = false)
    public String _id;

    public Favorites() {
    }

    public Favorites(String ID) {
        this._id = ID;
    }

    public String getID() {
        return _id;
    }
}
