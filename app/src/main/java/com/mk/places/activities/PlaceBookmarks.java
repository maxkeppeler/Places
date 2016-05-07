package com.mk.places.activities;

import com.afollestad.inquiry.annotations.Column;

/**
 * Created by max on 30.04.16.
 */
public class PlaceBookmarks {

    @Column(name = "_id", primaryKey = true, notNull = true, autoIncrement = false)
    public String _id;

    public PlaceBookmarks() {
    }

    public PlaceBookmarks(String ID) {
        this._id = ID;
    }

    public String getID() {
        return _id;
    }
}
