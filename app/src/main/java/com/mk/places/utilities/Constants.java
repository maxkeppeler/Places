package com.mk.places.utilities;

import android.Manifest;
import android.support.v4.content.ContextCompat;

import com.mk.places.R;

public class Constants {

    // Database
    public static final String DATABASE_NAME = "BOOKMARKS";
    public static final int DATABASE_VERSION = 42;

    // Permissions
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    // Permission request IDs
    public static final int PERMISSIONS_REQUEST_ID_WRITE_EXTERNAL_STORAGE = 42;

    // Tab Layout
    public static final String TAB_PLACES = "Places";
    public static final String TAB_BOOKMARKS = "Bookmarks";
    public static final String TAB_DISASTERS = "disasters";
    public static final String TAB_GOOD_ACTS = "Good Acts";
    public static final String TAB_PEOPLE = "People";
    public static final String TAB_WEBSITES = "Websites";

    // Drawer Filter Misc
    public static final String NO_FILTER = "Nope";
    public static final int NO_SELECTION = 42424242;

    // Sights for Drawer Filter
    public static final String SIGHT = "Sight";
    public static final String SIGHT_CITY = "City";
    public static final String SIGHT_COUNTRY = "Country";
    public static final String SIGHT_NATIONAL_PARK = "National Park";
    public static final String SIGHT_GARDEN = "Garden";
    public static final String SIGHT_CAVE = "Cave";
    public static final String SIGHT_BEACH = "Beach";
    public static final String SIGHT_LAKE = "Lake";
    public static final String SIGHT_RIVER = "River";
    public static final String SIGHT_DESERT = "Desert";
    public static final String SIGHT_GEYSER = "Geyser";
    public static final String SIGHT_LANDFORM = "Landform";
    public static final String SIGHT_ISLAND = "Island";

    // Continents for Drawer Filter
    public static final String CONTINENT = "Continent";
    public static final String CONTINENT_AFRICA = "Africa";
    public static final String CONTINENT_ANTARCTICA = "Antarctica";
    public static final String CONTINENT_ASIA = "Asia";
    public static final String CONTINENT_AUSTRALIA = "Australia";
    public static final String CONTINENT_EUROPE = "Europe";
    public static final String CONTINENT_NORTH_AMERICA = "North America";
    public static final String CONTINENT_SOUTH_AMERICA = "South America";

    // Identifiers of Drawer Filters
    public static final long ID_CITY = 0;
    public static final long ID_COUNTRY = 1;
    public static final long ID_NATIONALR_PARK = 2;
    public static final long ID_GARDEN = 3;
    public static final long ID_CAVE = 4;
    public static final long ID_BEACH = 5;
    public static final long ID_LAKE = 6;
    public static final long ID_RIVER = 7;
    public static final long ID_DESERT = 8;
    public static final long ID_GEYSER = 9;
    public static final long ID_LANDFORM = 10;
    public static final long ID_ISLAND = 11;

    public static final long ID_AFRICA = 200;
    public static final long ID_ANTARCTICA = 201;
    public static final long ID_ASIA = 202;
    public static final long ID_AUSTRALIA = 203;
    public static final long ID_EUROPE = 204;
    public static final long ID_NORTH_AMERICA = 205;
    public static final long ID_SOUTH_AMERICA = 206;

    public static final long ID_ALL = 424242424;

    // Material Drawer Sections
    public static final String DRAWER_PLACES = "Places";
    public static final String DRAWER_NATURE = "Nature";
    public static final String DRAWER_HALL = "Hall of Honor";
    public static final String DRAWER_ABOUT = "About";
    public static final String DRAWER_SETTINGS = "Settings";
    public static final String DRAWER_WRONG = "Where are you?";

    // Identifier Material Drawer Sections
    public static final int ID_DRAWER_PLACES = 0;
    public static final int ID_DRAWER_NATURE = 1;
    public static final int ID_DRAWER_HALL = 2;
    public static final int ID_DRAWER_ABOUT = 3;
    public static final int ID_DRAWER_SETTINGS = 4;


}
