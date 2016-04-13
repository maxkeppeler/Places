package com.mk.placesdrawer.models;

import java.util.ArrayList;

public class PlaceList {

    private static ArrayList<Place> placesList = new ArrayList<>();

    public static void createPlaceList(ArrayList<String> locations,
                                       ArrayList<String> sights,
                                       ArrayList<String> descriptions,
                                       ArrayList<String> position,
                                       ArrayList<String> religion,
                                       ArrayList<String> urls,
                                       ArrayList<Integer> favorite
    ) {
        try {

            for (int i = 0; i < locations.size(); i++) {
                Place place = new Place(

                        locations.get(i),
                        sights.get(i),
                        descriptions.get(i),
                        position.get(i),
                        religion.get(i),
                        urls.get(i),
                        favorite.get(i)

                );
                placesList.add(place);
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public static void createPlaceList(ArrayList<Place> placesList) {
        PlaceList.placesList = placesList;
    }

    public static ArrayList<Place> getPlacesList() {
        return placesList;
    }

    public static void clearList() {
        placesList.clear();
    }



}
