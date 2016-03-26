package com.mk.placesdrawer.models;

/**
 * Created by Max on 26.03.16.
 */
public class PlacesDetailItem {

    private String title, text;
    private int drawable;

    public PlacesDetailItem(String title, String text, int drawable) {

        this.title = title;
        this.text = text;
        this.drawable = drawable;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }
}