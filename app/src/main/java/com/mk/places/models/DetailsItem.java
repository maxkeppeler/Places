package com.mk.places.models;

public class DetailsItem {

    private String text;
    private String title;

    public DetailsItem(String title, String text) {
        this.text = text;
        this.title = title;
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

}