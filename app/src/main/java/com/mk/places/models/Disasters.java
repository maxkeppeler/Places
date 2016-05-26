package com.mk.places.models;

import java.util.ArrayList;

public class Disasters {

    private static ArrayList<Disaster> disastersList = new ArrayList<>();

    public static void createDisastersList(
            ArrayList<String> title,
            ArrayList<String> image,
            ArrayList<String> url
    ) {
        try {

            for (int i = 0; i < title.size(); i++) {
                Disaster disaster = new Disaster(

                        title.get(i),
                        image.get(i),
                        url.get(i)
                );
                disastersList.add(disaster);
            }
        } catch (IndexOutOfBoundsException e) {
            e.getStackTrace();
        }
    }

    public static void createDisastersList(ArrayList<Disaster> placesList) {
        Disasters.disastersList = placesList;
    }

    public static ArrayList<Disaster> getDisastersList() {
        return disastersList;
    }

    public static void clearList() {
        disastersList.clear();
    }

}