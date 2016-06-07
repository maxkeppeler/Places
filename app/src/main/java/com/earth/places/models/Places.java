package com.earth.places.models;

import java.util.ArrayList;

public class Places {

    private static ArrayList<Place> placesList = new ArrayList<>();

    public static void createPlaceList(
            ArrayList<String> id,
            ArrayList<String> locations,
            ArrayList<String> sights,
            ArrayList<String> continents,
            ArrayList<String> infoTitle,
            ArrayList<String> info,
            ArrayList<String> creditsTitle,
            ArrayList<String> creditsDesc,
            ArrayList<String> credits,
            ArrayList<String> descriptions,
            ArrayList<String> urls
    ) {
        try {

            for (int i = 0; i < id.size(); i++) {
                Place place = new Place(

                        id.get(i),
                        locations.get(i),
                        sights.get(i),
                        continents.get(i),
                        infoTitle.get(i),
                        info.get(i),
                        creditsTitle.get(i),
                        creditsDesc.get(i),
                        credits.get(i),
                        descriptions.get(i),
                        urls.get(i)
                );
                placesList.add(place);
            }
        } catch (IndexOutOfBoundsException e) {
            e.getStackTrace();
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
