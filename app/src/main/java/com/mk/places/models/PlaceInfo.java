package com.mk.places.models;

public class PlaceInfo {

    private String text;
    private String title;
    private int drawable;

    public PlaceInfo(String title, String text, int drawable) {
        this.text = text;
        this.title = title;
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