package com.mk.places.utilities;

import android.Manifest;
import android.support.v4.content.ContextCompat;

import com.mk.places.R;

public class Constants {

    // Database
    public static final String DATABASE_NAME = "BOOKMARKS";
    public static final int DATABASE_VERSION = 6;

    // Permissions
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    // Permission request IDs
    public static final int PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE = 42;

    // Tab Layout
    public static final String TAB_PLACES = "Places";
    public static final String TAB_BOOKMARKS = "Bookmarks";
    public static final String TAB_SINS = "Sins";
    public static final String TAB_GOOD_ACTS = "Good Acts";
    public static final String TAB_PEOPLE = "People";
    public static final String TAB_WEBSITES = "Websites";

    // Drawer Filter Misc
    public static final String NO_FILTER = "Nope";

    // Sights for Drawer Filter
    public static final String SIGHT = "Sight";
    public static final String SIGHT_CITY = "City";
    public static final String SIGHT_COUNTRY = "Country";
    public static final String SIGHT_NATIONAL_PARK = "National Park";
    public static final String SIGHT_PARK = "Park";
    public static final String SIGHT_BEACH = "Beach";
    public static final String SIGHT_LAKE = "Lake";
    public static final String SIGHT_DESERT = "Desert";
    public static final String SIGHT_GEYSER = "Geyser";
    public static final String SIGHT_LANDFORM = "Landform";

    // Continents for Drawer Filter
    public static final String CONTINENT = "Continent";
    public static final String CONTINENT_AFRICA = "Africa";
    public static final String CONTINENT_ANTARCTICA = "Antarctica";
    public static final String CONTINENT_ASIA = "Asia";
    public static final String CONTINENT_AUSTRALIA = "Australia";
    public static final String CONTINENT_EUROPE = "Europe";
    public static final String CONTINENT_NORTH_AMERICA = "North America";
    public static final String CONTINENT_SOUTH_AMERICA = "South America";

    // Material Drawer Sections
    public static final String DRAWER_PLACES = "Places";
    public static final String DRAWER_NATURE = "Nature";
    public static final String DRAWER_HALL = "Hall of Honor";
    public static final String DRAWER_ABOUT = "About";
    public static final String DRAWER_SETTINGS = "Settings";
    public static final String DRAWER_WRONG = "Where are you?";

}
