package com.mk.places.utilities;

import com.mk.places.activities.MainActivity;
import com.mk.places.fragment.FragmentBookmarks;
import com.mk.places.fragment.FragmentPlaces;
import com.mk.places.models.Place;
import com.mk.places.models.Places;

import java.util.ArrayList;

public class FilterLogic {

    public static void filterList(String key) {

        ArrayList<Place> filterPlaces = new ArrayList<>();
        ArrayList<Place> filterBookmarks = new ArrayList<>();

        if (!key.equals(Constants.NO_FILTER)) {
            int x = 0;
            int y = 0;
            for (int j = 0; j < FragmentBookmarks.bookmarks.size(); j++) {
                if (key.equals(FragmentBookmarks.bookmarks.get(j).getSight())
                        || key.equals(FragmentBookmarks.bookmarks.get(j).getContinent())) {

                    filterBookmarks.add(x, FragmentBookmarks.bookmarks.get(j));
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

            FragmentPlaces.updateLayout(true, filterPlaces);
            FragmentBookmarks.updateLayout(true, filterBookmarks);

            MainActivity.updateTabTexts(filterPlaces.size(), filterBookmarks.size());

        } else {

            FragmentPlaces.updateLayout(false, null);
            FragmentBookmarks.updateLayout(false, null);

            MainActivity.updateTabTexts(Places.getPlacesList().size(), FragmentBookmarks.bookmarks.size());
        }


    }
}
