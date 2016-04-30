package com.mk.places.models;

public class MemberItem {

    private String mImage;
    private String mName;
    private String mTitle;
    private String mDesc;
    private String[] mButtomNames;
    private String[] mButtomLinks;

    public MemberItem(String mImage, String mName, String mTitle, String mDesc, String[] mButtomNames, String[] mButtomLinks) {
        this.mImage = mImage;
        this.mName = mName;
        this.mTitle = mTitle;
        this.mDesc = mDesc;
        this.mButtomNames = mButtomNames;
        this.mButtomLinks = mButtomLinks;
    }

    public String getmImage() {
        return mImage;
    }

    public String getmName() {
        return mName;
    }

    public String getmTitle() {
        return mTitle;
    }

    public String getmDesc() {
        return mDesc;
    }

    public String[] getmButtomNames() {
        return mButtomNames;
    }

    public String[] getmButtomLinks() {
        return mButtomLinks;
    }
}

