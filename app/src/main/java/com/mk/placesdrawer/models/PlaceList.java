package com.mk.placesdrawer.models;

import java.util.ArrayList;

public class PlaceList {

    private static ArrayList<Place> placesList = new ArrayList<>();
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
                Place place = new Place(

                        locations.get(i),
                        sights.get(i),
                        descriptions.get(i),
                        country.get(i),
                        state.get(i),
                        city.get(i),
                        religion.get(i),
                        urls.get(i)
                );
                placesList.add(place);
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public static void createPlacesList(ArrayList<Place> placesList) {
        PlaceList.placesList = placesList;
    }

    public static ArrayList<Place> getPlacesList() {
        return placesList;
    }

    public static void clearList() {
        placesList.clear();
    }



}
