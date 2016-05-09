package com.mk.places.models;

import java.util.ArrayList;

public class Places {

    private static ArrayList<Place> placesList = new ArrayList<>();

    public static void createPlaceList(
            ArrayList<String> id,
            ArrayList<String> locations,
            ArrayList<String> sights,
            ArrayList<String> continents,
            ArrayList<String> religions,
            ArrayList<String> descriptions,
            ArrayList<String> urls,
            ArrayList<String> mUrls

    ) {
        try {

            for (int i = 0; i < locations.size(); i++) {
                Place place = new Place(

                        id.get(i),
                        locations.get(i),
                        sights.get(i),
                        continents.get(i),
                        religions.get(i),
                        descriptions.get(i),
                        urls.get(i),
                        mUrls.get(i)

                );
                placesList.add(place);
            }
        } catch (IndexOutOfBoundsException e) {
        }
    }

    public static void createPlaceList(ArrayList<Place> placesList) {
        Places.placesList = placesList;
    }

    public static ArrayList<Place> getPlacesList() {
        return placesList;
    }

    public static void clearList() {
        placesList.clear();
    }


}
