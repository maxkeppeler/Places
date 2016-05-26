package com.mk.places.models;

import java.util.ArrayList;

public class GoodActs {

    private static ArrayList<GoodAct> goodActsList = new ArrayList<>();

    public static void createGoodActsList(
            ArrayList<String> title,
            ArrayList<String> image,
            ArrayList<String> url
    ) {
        try {

            for (int i = 0; i < title.size(); i++) {
                GoodAct goodAct = new GoodAct(

                        title.get(i),
                        image.get(i),
                        url.get(i)
                );
                goodActsList.add(goodAct);
            }
        } catch (IndexOutOfBoundsException e) {
            e.getStackTrace();
        }
    }

    public static void createGoodActsList(ArrayList<GoodAct> placesList) {
        GoodActs.goodActsList = placesList;
    }

    public static ArrayList<GoodAct> getGoodActsList() {
        return goodActsList;
    }

    public static void clearList() {
        goodActsList.clear();
    }

}