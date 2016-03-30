package com.mk.placesdrawer.models;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Max on 16.03.16.
 */
public class PlacesList {

    private static ArrayList<PlacesItem> placesList = new ArrayList<>();
    private static String category;

    public static void createPlacesList(ArrayList<String> locations,
                                        ArrayList<String> sights,
                                        ArrayList<String> descriptions,
                                        ArrayList<String> country,
                                        ArrayList<String> state,
                                        ArrayList<String> city,
                                        ArrayList<String> religion,
                                        ArrayList<String> urls
    ) {

        try {

            for (int i = 0; i < locations.size(); i++) {
                PlacesItem placesItem = new PlacesItem(

                        locations.get(i),
                        sights.get(i),
                        descriptions.get(i),
                        country.get(i),
                        state.get(i),
                        city.get(i),
                        religion.get(i),
                        urls.get(i)
                );
                placesList.add(placesItem);
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public static void createPlacesList(ArrayList<PlacesItem> placesList, String category) {
        PlacesList.placesList = placesList;
        PlacesList.category = category;
    }

    public static ArrayList<PlacesItem> getPlacesList() {
        return placesList;
    }

    public static void clearList() {
        placesList.clear();
    }



}
