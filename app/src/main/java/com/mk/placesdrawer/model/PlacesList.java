package com.mk.placesdrawer.model;

import java.util.ArrayList;

/**
 * Created by Max on 16.03.16.
 */
public class PlacesList {

    private static ArrayList<PlacesItem> placesList = new ArrayList<>();

    public static void createPlacesList(ArrayList<String> locations, ArrayList<String> sights, ArrayList<String> descriptions,
                                            ArrayList<String> urls
                                        ) {

        try {
            for (int i = 0; i < locations.size(); i++) {

                PlacesItem wallItem = new PlacesItem(

                                locations.get(i),
                                sights.get(i),
                                urls.get(i),
                                descriptions.get(i));

                placesList.add(wallItem);
            }
        } catch (IndexOutOfBoundsException e) {
            //Do nothing
        }
    }

    public static void createPlacesList(ArrayList<PlacesItem> placesList) {
        PlacesList.placesList = placesList;
    }

    public static ArrayList<PlacesItem> getPlacesList() {
        return placesList;
    }

    public static void clearList() {
        placesList.clear();
    }
}
