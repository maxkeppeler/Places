package com.earth.places.utilities;

import com.earth.places.activities.MainActivity;
import com.earth.places.fragment.FragmentBookmarks;
import com.earth.places.fragment.FragmentPlaces;
import com.earth.places.models.Place;
import com.earth.places.models.Places;

import java.util.ArrayList;

public class FilterLogic {

    public static void filterList(String key) {

        ArrayList<Place> filterPlaces = new ArrayList<>();
        ArrayList<Place> filterBookmarks = new ArrayList<>();

        if (!key.equals(Constants.NO_FILTER)) {
            int x = 0;
            int y = 0;
            for (int j = 0; j < FragmentBookmarks.getBookmarks().size(); j++) {
                if (Utils.stringIsContained(key, FragmentBookmarks.getBookmarks().get(j).getSight()) || Utils.stringIsContained(key, FragmentBookmarks.getBookmarks().get(j).getContinent())) {

                    filterBookmarks.add(x, FragmentBookmarks.getBookmarks().get(j));
                    x++;
                }
            }

            for (int j = 0; j < Places.getPlacesList().size(); j++) {
                if (Utils.stringIsContained(key, Places.getPlacesList().get(j).getSight()) || Utils.stringIsContained(key, Places.getPlacesList().get(j).getContinent())) {

                    filterPlaces.add(y, Places.getPlacesList().get(j));
                    y++;
                }
            }

            FragmentPlaces.updateLayout(true, filterPlaces);
            FragmentBookmarks.updateLayout(true, filterBookmarks);

            MainActivity.updateTabTexts(0, filterPlaces.size(), filterBookmarks.size());

        } else {

            FragmentPlaces.updateLayout(false, null);
            FragmentBookmarks.updateLayout(false, null);
            MainActivity.updateTabTexts(0, Places.getPlacesList().size(), FragmentBookmarks.getBookmarks().size());
        }


    }
}
