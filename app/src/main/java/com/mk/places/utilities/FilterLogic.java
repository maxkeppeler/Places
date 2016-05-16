package com.mk.places.utilities;

import com.mk.places.fragment.DrawerBookmarks;
import com.mk.places.fragment.DrawerPlaces;
import com.mk.places.models.Place;
import com.mk.places.models.Places;

import java.util.ArrayList;

public class FilterLogic {

    public static void filterList(String key) {

        ArrayList<Place> filterPlaces = new ArrayList<>();
        ArrayList<Place> filterBookmarks = new ArrayList<>();

        if (!key.equals("All")) {
            int x = 0;
            int y = 0;
            for (int j = 0; j < DrawerBookmarks.getBookmarks().size(); j++) {
                if (key.equals(DrawerBookmarks.getBookmarks().get(j).getSight())
                        || key.equals(DrawerBookmarks.getBookmarks().get(j).getContinent())) {

                    filterBookmarks.add(x, DrawerBookmarks.getBookmarks().get(j));
                    x++;
                }
            }

            for (int j = 0; j < Places.getPlacesList().size(); j++) {
                if (key.equals(Places.getPlacesList().get(j).getSight())
                        || key.equals(Places.getPlacesList().get(j).getContinent())) {
                    filterPlaces.add(y, Places.getPlacesList().get(j));
                    y++;
                }
            }
            DrawerBookmarks.createLayout(true, filterBookmarks);
            DrawerPlaces.createLayout(true, filterPlaces);
        } else {
            DrawerBookmarks.createLayout(false, null);
            DrawerPlaces.createLayout(false, null);
        }


    }
}
