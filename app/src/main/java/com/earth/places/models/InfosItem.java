package com.earth.places.models;

public class InfosItem {

    private String text;
    private String title;

    public InfosItem(String title, String text) {
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