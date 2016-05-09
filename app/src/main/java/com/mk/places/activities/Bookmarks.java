package com.mk.places.activities;

import android.content.Context;

import com.afollestad.inquiry.Inquiry;

public final class Bookmarks {

    private static final String TABLE_NAME = "bookmarks";
    private static final String TAG = "Bookmarks";

    public static void init(Context context) {
        Inquiry.init(context, TABLE_NAME, 1);
    }


    /**
     * Returns true if the item is currently favorited.
     */
    public static boolean isFavorited(String id) {
        return Inquiry.get()
                .selectFrom(TABLE_NAME, DataBaseBookmarks.class)
                .where("_id = ?", id)
                .one() != null;
    }

    /**
     * Returns true if the item was favorited successfully.
     */
    public static boolean favoriteItem(String id) {
        if (!isFavorited(id)) {
            Inquiry.get()
                    .insertInto(TABLE_NAME, DataBaseBookmarks.class)
                    .values(new DataBaseBookmarks(id))
                    .run();
            return true;
        }

        unfavoriteItem(id);

        return false;
    }

    /**
     * Returns Array with all IDs
     */
    public static DataBaseBookmarks[] getDB() {

        return Inquiry.get().selectFrom(TABLE_NAME, DataBaseBookmarks.class).all();
    }

    /**
     * Deletes Data Base
     */
    public static void deleteDB(Context context) {
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
                    .deleteFrom(TABLE_NAME, DataBaseBookmarks.class)
                    .where("_id = ?", id)
                    .run();
            return true;
        }

        favoriteItem(id);

        return false;
    }
}