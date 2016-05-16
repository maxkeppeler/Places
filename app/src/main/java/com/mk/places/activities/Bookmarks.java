package com.mk.places.activities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.afollestad.inquiry.Inquiry;
import com.afollestad.inquiry.annotations.Column;

import java.util.Arrays;

public final class Bookmarks {

    private static final String TABLE_NAME = "BOOKMARKS";
    private static Activity context;

    public static void init(Activity ctxt) {
        context = ctxt;
        Inquiry.init(ctxt, TABLE_NAME, 1);
    }


    /**
     * Returns true if the item is currently favorited.
     */
    public static boolean isFavorited(String id) {
        return Inquiry.get()
                .selectFrom(TABLE_NAME, BookmarksDB.class)
                .where("_id = ?", id)
                .one() != null;
    }

    /**
     * Returns true if the item was favorited successfully.
     */
    public static boolean favoriteItem(String id) {

        init(context);

        if (!isFavorited(id)) {
            Inquiry.get()
                    .insertInto(TABLE_NAME, BookmarksDB.class)
                    .values(new BookmarksDB(id))
                    .run();
            return true;
        } else unfavoriteItem(id);

        return false;
    }

    /**
     * Returns Array with all IDs
     */
    public static BookmarksDB[] getDB() {
        return Inquiry.get().selectFrom(TABLE_NAME, BookmarksDB.class).all();
    }

    /**
     * Deletes Data Base
     */
    public static void deleteDB() {

        init(context);

        Inquiry.get().dropTable(TABLE_NAME);
        Inquiry.deinit();
    }


    /**
     * Returns true if the item was unfavorited successfully.
     */
    public static boolean unfavoriteItem(String id) {
        if (isFavorited(id)) {
            Inquiry.get()
                    .deleteFrom(TABLE_NAME, BookmarksDB.class)
                    .where("_id = ?", id)
                    .run();
            return true;
        }

        favoriteItem(id);

        return false;
    }

    public static class BookmarksDB {

        @Column(name = "_id", primaryKey = true, notNull = true, autoIncrement = false)
        public String _id;

        public BookmarksDB() {
        }

        public BookmarksDB(String ID) {
            this._id = ID;
        }

        public String getID() {
            return _id;
        }
    }
}