package com.mk.places.models;

import java.util.ArrayList;

public class Places {

    private static ArrayList<Place> placesList = new ArrayList<>();

    public static void createPlaceList(
            ArrayList<String> id,
            ArrayList<String> locations,
            ArrayList<String> sights,
            ArrayList<String> continents,
            ArrayList<String> religions,
            ArrayList<String> descriptions,
            ArrayList<String> urls,
            ArrayList<String> url_a_title, ArrayList<String> url_a_desc, ArrayList<String> url_a,
            ArrayList<String> url_b_title, ArrayList<String> url_b_desc, ArrayList<String> url_b,
            ArrayList<String> url_c_title, ArrayList<String> url_c_desc, ArrayList<String> url_c,
            ArrayList<String> url_d_title, ArrayList<String> url_d_desc, ArrayList<String> url_d,
            ArrayList<String> url_e_title, ArrayList<String> url_e_desc, ArrayList<String> url_e,
            ArrayList<String> url_f_title, ArrayList<String> url_f_desc, ArrayList<String> url_f,
            ArrayList<String> url_g_title, ArrayList<String> url_g_desc, ArrayList<String> url_g,
            ArrayList<String> url_h_title, ArrayList<String> url_h_desc, ArrayList<String> url_h,
            ArrayList<String> url_i_title, ArrayList<String> url_i_desc, ArrayList<String> url_i,
            ArrayList<String> url_j_title, ArrayList<String> url_j_desc, ArrayList<String> url_j,
            ArrayList<String> url_k_title, ArrayList<String> url_k_desc, ArrayList<String> url_k,
            ArrayList<String> url_l_title, ArrayList<String> url_l_desc, ArrayList<String> url_l,
            ArrayList<String> url_m_title, ArrayList<String> url_m_desc, ArrayList<String> url_m,
            ArrayList<String> url_n_title, ArrayList<String> url_n_desc, ArrayList<String> url_n,
            ArrayList<String> url_o_title, ArrayList<String> url_o_desc, ArrayList<String> url_o,
            ArrayList<String> url_p_title, ArrayList<String> url_p_desc, ArrayList<String> url_p,
            ArrayList<String> url_q_title, ArrayList<String> url_q_desc, ArrayList<String> url_q,
            ArrayList<String> url_r_title, ArrayList<String> url_r_desc, ArrayList<String> url_r,
            ArrayList<String> url_s_title, ArrayList<String> url_s_desc, ArrayList<String> url_s,
            ArrayList<String> url_t_title, ArrayList<String> url_t_desc, ArrayList<String> url_t,
            ArrayList<Integer> favorite
    ) {
        try {

            for (int i = 0; i < locations.size(); i++) {
                Place place = new Place(

                        id.get(i),
                        locations.get(i),
                        sights.get(i),
                        continents.get(i),
                        religions.get(i),
                        descriptions.get(i),
                        urls.get(i),
                        url_a_title.get(i), url_a_desc.get(i), url_a.get(i),
                        url_b_title.get(i), url_b_desc.get(i), url_b.get(i),
                        url_c_title.get(i), url_c_desc.get(i), url_c.get(i),
                        url_d_title.get(i), url_d_desc.get(i), url_d.get(i),
                        url_e_title.get(i), url_e_desc.get(i), url_e.get(i),
                        url_f_title.get(i), url_f_desc.get(i), url_f.get(i),
                        url_g_title.get(i), url_g_desc.get(i), url_g.get(i),
                        url_h_title.get(i), url_h_desc.get(i), url_h.get(i),
                        url_i_title.get(i), url_i_desc.get(i), url_i.get(i),
                        url_j_title.get(i), url_j_desc.get(i), url_j.get(i),
                        url_k_title.get(i), url_k_desc.get(i), url_k.get(i),
                        url_l_title.get(i), url_l_desc.get(i), url_l.get(i),
                        url_m_title.get(i), url_m_desc.get(i), url_m.get(i),
                        url_n_title.get(i), url_n_desc.get(i), url_n.get(i),
                        url_o_title.get(i), url_o_desc.get(i), url_o.get(i),
                        url_p_title.get(i), url_p_desc.get(i), url_p.get(i),
                        url_q_title.get(i), url_q_desc.get(i), url_q.get(i),
                        url_r_title.get(i), url_r_desc.get(i), url_r.get(i),
                        url_s_title.get(i), url_s_desc.get(i), url_s.get(i),
                        url_t_title.get(i), url_t_desc.get(i), url_t.get(i)
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
