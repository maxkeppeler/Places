package com.mk.places.models;

public class PlaceInfoGallery {

    private String
            imageLink,
            imageName,
            imageDesc;


    public PlaceInfoGallery(String imageLink, String imageName, String imageDesc) {
        this.imageLink = imageLink;
        this.imageName = imageName;
        this.imageDesc = imageDesc;
    }

    public String getImageLink() {
        return imageLink;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageDesc() {
        return imageDesc;
    }
}