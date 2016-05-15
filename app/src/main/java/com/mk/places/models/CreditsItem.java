package com.mk.places.models;

public class CreditsItem {

    private String desc;
    private String title;
    private String tag;

    public CreditsItem(String title, String desc, String tag) {
        this.title = title;
        this.desc = desc;
        this.tag = tag;
    }

    public String getTag() {
        return tag;
    }

    public String getTitle() {
        return title;
    }

    public String getDesc() {
        return desc;
    }

}