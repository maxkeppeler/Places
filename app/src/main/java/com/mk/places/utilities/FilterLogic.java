package com.mk.places.utilities;

import com.mk.places.fragment.TabBookmarks;
import com.mk.places.fragment.TabPlaces;
import com.mk.places.models.Place;
import com.mk.places.models.Places;

import java.util.ArrayList;

public class FilterLogic {

    public static void filterList(int index, String key) {

        ArrayList<Place> filter = new ArrayList<>();


        if (index == 0) {


            if (!key.equals("All")) {

                int x = 0;

                for (int j = 0; j < Places.getPlacesList().size(); j++) {

                    if (key.equals(Places.getPlacesList().get(j).getSight())
                            || key.equals(Places.getPlacesList().get(j).getContinent())) {

                        filter.add(x, Places.getPlacesList().get(j));
                        x++;
                    }

                }

                TabPlaces.createLayout(true, filter);

            } else TabPlaces.createLayout(false, filter);


        }


        if (index == 1) {

            if (!key.equals("All")) {

                int x = 0;

                for (int j = 0; j < TabBookmarks.getBookmarks().size(); j++) {

                    if (key.equals(TabBookmarks.getBookmarks().get(j).getSight())
                            || key.equals(TabBookmarks.getBookmarks().get(j).getContinent())) {

                        filter.add(x, TabBookmarks.getBookmarks().get(j));
                        x++;
                    }

                }

                TabBookmarks.createLayout(true, filter);

            } else TabBookmarks.createLayout(false, null);


        }
    }
}
