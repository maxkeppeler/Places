package com.mk.places.models;

import java.util.ArrayList;

public class Places {

    private static ArrayList<Place> placesList = new ArrayList<>();

    public static void createPlaceList(ArrayList<String> locations,
                                       ArrayList<String> sights,
                                       ArrayList<String> continents,
                                       ArrayList<String> religions,
                                       ArrayList<String> descriptions,
                                       ArrayList<String> urls,
                                       ArrayList<String> urls_a,
                                       ArrayList<String> urls_b,
                                       ArrayList<String> urls_c,
                                       ArrayList<String> urls_d,
                                       ArrayList<String> urls_e,
                                       ArrayList<String> urls_f,
                                       ArrayList<String> urls_g,
                                       ArrayList<String> urls_h,
                                       ArrayList<String> urls_i,
                                       ArrayList<String> urls_j,
                                       ArrayList<String> urls_k,
                                       ArrayList<String> urls_l,
                                       ArrayList<String> urls_m,
                                       ArrayList<String> urls_n,
                                       ArrayList<String> urls_o,
                                       ArrayList<String> urls_p,
                                       ArrayList<String> urls_q,
                                       ArrayList<String> urls_r,
                                       ArrayList<String> urls_s,
                                       ArrayList<String> urls_t,
                                       ArrayList<Integer> favorite
    ) {
        try {

            for (int i = 0; i < locations.size(); i++) {
                Place place = new Place(

                        locations.get(i),
                        sights.get(i),
                        continents.get(i),
                        religions.get(i),
                        descriptions.get(i),
                        urls.get(i),
                        urls_a.get(i),
                        urls_b.get(i),
                        urls_c.get(i),
                        urls_d.get(i),
                        urls_e.get(i),
                        urls_f.get(i),
                        urls_g.get(i),
                        urls_h.get(i),
                        urls_i.get(i),
                        urls_j.get(i),
                        urls_k.get(i),
                        urls_l.get(i),
                        urls_m.get(i),
                        urls_n.get(i),
                        urls_o.get(i),
                        urls_p.get(i),
                        urls_q.get(i),
                        urls_r.get(i),
                        urls_s.get(i),
                        urls_t.get(i),
                        favorite.get(i)
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
