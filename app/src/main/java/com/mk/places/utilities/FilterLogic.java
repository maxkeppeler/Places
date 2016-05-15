package com.mk.places.utilities;

import com.mk.places.fragment.DrawerBookmarks;
import com.mk.places.fragment.DrawerPlaces;
import com.mk.places.models.Place;
import com.mk.places.models.Places;

import java.util.ArrayList;

public class FilterLogic {

    public static void filterList(int index, String key) {

        ArrayList<Place> filter = new ArrayList<>();


        if (index == 0)
            if (!key.equals("All")) {
                int x = 0;
                for (int j = 0; j < Places.getPlacesList().size(); j++) {
                    if (key.equals(Places.getPlacesList().get(j).getSight())
                            || key.equals(Places.getPlacesList().get(j).getContinent())) {
                        filter.add(x, Places.getPlacesList().get(j));
                        x++;
                    }
                }
                DrawerPlaces.createLayout(true, filter);
            } else DrawerPlaces.createLayout(false, filter);


        if (index == 1)
            if (!key.equals("All")) {
                int x = 0;
                for (int j = 0; j < DrawerBookmarks.getBookmarks().size(); j++) {
                    if (key.equals(DrawerBookmarks.getBookmarks().get(j).getSight())
                            || key.equals(DrawerBookmarks.getBookmarks().get(j).getContinent())) {

                        filter.add(x, DrawerBookmarks.getBookmarks().get(j));
                        x++;
                    }
                }
                DrawerBookmarks.createLayout(true, filter);
            } else DrawerBookmarks.createLayout(false, null);


    }
}
