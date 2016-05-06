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
            ArrayList<String> urls0,
            ArrayList<String> urls1,
            ArrayList<String> urls2,
            ArrayList<String> urls3,
            ArrayList<String> urls4,
            ArrayList<String> urls5,
            ArrayList<String> urls6,
            ArrayList<String> urls7,
            ArrayList<String> urls8,
            ArrayList<String> urls9,
            ArrayList<String> urls10,
            ArrayList<String> urls11,
            ArrayList<String> urls12,
            ArrayList<String> urls13,
            ArrayList<String> urls14,
            ArrayList<String> urls15,
            ArrayList<String> urls16,
            ArrayList<String> urls17,
            ArrayList<String> urls18,
            ArrayList<String> urls19
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
                        urls0.get(i),
                        urls1.get(i),
                        urls2.get(i),
                        urls3.get(i),
                        urls4.get(i),
                        urls5.get(i),
                        urls6.get(i),
                        urls7.get(i),
                        urls8.get(i),
                        urls9.get(i),
                        urls10.get(i),
                        urls11.get(i),
                        urls12.get(i),
                        urls13.get(i),
                        urls14.get(i),
                        urls15.get(i),
                        urls16.get(i),
                        urls17.get(i),
                        urls18.get(i),
                        urls19.get(i)

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
