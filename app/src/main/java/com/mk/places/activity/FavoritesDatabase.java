package com.mk.places.activity;

import com.afollestad.inquiry.annotations.Column;

public class FavoritesDatabase {

    @Column(name = "_id", primaryKey = true, notNull = true, autoIncrement = true)
    public long id;

    @Column
    public boolean favorite;


    public FavoritesDatabase(long id, boolean favorite) {
        this.id = id;
        this.favorite = favorite;
    }

    public FavoritesDatabase() {

    }


    public boolean isFavorite() {
        return favorite;
    }
}
