/*
 * Copyright (c) 2016.  Jahir Fiquitiva
 *
 * Licensed under the CreativeCommons Attribution-ShareAlike
 * 4.0 International License. You may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *    http://creativecommons.org/licenses/by-sa/4.0/legalcode
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Big thanks to the project contributors. Check them in the repository.
 *
 */

/*
 *
 */

package com.mk.places.utilities;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.mk.places.R;
import com.mk.places.models.Place;
import com.mk.places.models.Places;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Preferences {

    private static final String
            PREFS_NAME = "DASHBOARD_PREFERENCES",
            PLACES_AMOUNT = "PLACES",
            COLUMNS = "COULMNS";

    private static final boolean[] arrayFavo = new boolean[Places.getPlacesList().size()];

    private final Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public void setPlacesSize(int size) {
        getSharedPreferences().edit().putInt(PLACES_AMOUNT, size).apply();
    }

    public int getPlacesSize() {
        return getSharedPreferences().getInt(PLACES_AMOUNT, 0);
    }

    public void setColumns(int amount) {
        getSharedPreferences().edit().putInt(COLUMNS, amount).apply();
    }

    public int getColumns() {
        return getSharedPreferences().getInt(COLUMNS, 0);
    }

}