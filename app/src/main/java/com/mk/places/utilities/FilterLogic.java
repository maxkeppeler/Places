package com.mk.places.utilities;

import com.mk.places.fragment.FragmentBookmarks;
import com.mk.places.fragment.FragmentPlaces;
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
            for (int j = 0; j < FragmentBookmarks.getBookmarks().size(); j++) {
                if (key.equals(FragmentBookmarks.getBookmarks().get(j).getSight())
                        || key.equals(FragmentBookmarks.getBookmarks().get(j).getContinent())) {

                    filterBookmarks.add(x, FragmentBookmarks.getBookmarks().get(j));
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
            FragmentBookmarks.createLayout(true, filterBookmarks);
            FragmentPlaces.createLayout(true, filterPlaces);
        } else {
            FragmentBookmarks.createLayout(false, null);
            FragmentPlaces.createLayout(false, null);
        }


    }
}
