package com.mk.placesdrawer.models;

import java.util.ArrayList;

/**
 * Created by Max on 16.03.16.
 */
public class PlacesList {

    private static ArrayList<PlacesItem> placesList = new ArrayList<>();

    // I understood the principle of this method
    public static void createPlacesList(ArrayList<String> locations,
                                        ArrayList<String> sights,
                                       // ArrayList<String> descriptions,
                                        ArrayList<String> urls
                                        )
    {

        try {

            for (int i = 0; i < locations.size(); i++) {

                //
                PlacesItem placesItem = new PlacesItem(

                                locations.get(i),
                                sights.get(i),
                             //   descriptions.get(i),
                                urls.get(i)
                                );

                // ArrayList placesList receives each placesItem
                placesList.add(placesItem);
            }
        } catch (IndexOutOfBoundsException e) {
            //Do nothing
        }
    }

    public static void createPlacesList(ArrayList<PlacesItem> placesList) {
        // Local ArrayList<PlacesItem> variable gets the value from the parameter variable
        PlacesList.placesList = placesList;
    }

    public static ArrayList<PlacesItem> getPlacesList() {
        return placesList;
    }

    public static void clearList() {
        placesList.clear();
    }

}
