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

public class Preferences {

    private static final String
            PREFS_NAME = "DASHBOARD_PREFERENCES",
            FAVO_ARRAY = "ARRAY",
            FIRST_START = "START",
            PLACES_AMOUNT = "PLACES",
            PLACES_FAVO_AMOUNT = "FAVO",
            COLUMNS = "COULMNS";

    private final Context context;

    public Preferences(Context context) {
        this.context = context;
    }

    private SharedPreferences getSharedPreferences() {
        return context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    public boolean getFirstStart() {
        return getSharedPreferences().getBoolean(FIRST_START, true);
    }

    public void setFirstStart(boolean state) {
        getSharedPreferences().edit().putBoolean(FIRST_START, state).apply();
    }

    public int getPlacesSize() {
        return getSharedPreferences().getInt(PLACES_AMOUNT, 0);
    }

    public void setPlacesSize(int size) {
        getSharedPreferences().edit().putInt(PLACES_AMOUNT, size).apply();
    }

    public int getFavoSize() {
        return getSharedPreferences().getInt(PLACES_FAVO_AMOUNT, 0);
    }

    public void setFavoSize(int size) {
        getSharedPreferences().edit().putInt(PLACES_FAVO_AMOUNT, size).apply();
    }

    public int getColumns() {
        return getSharedPreferences().getInt(COLUMNS, 0);
    }

    public void setColumns(int amount) {
        getSharedPreferences().edit().putInt(COLUMNS, amount).apply();
    }

    public boolean storeArray(Boolean[][] array, String arrayName, Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences(FAVO_ARRAY, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(arrayName + "_size", array.length);

        for (int i = 0; i < array.length; i++) {

            for (int j = 0; j < array.length; i++) {

                editor.putBoolean(arrayName + "_" + i, array[i][j]);
            }
        }
        return editor.commit();
    }

    public Boolean[][] loadArray(String arrayName, Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences(FAVO_ARRAY, 0);
        int size = prefs.getInt(arrayName + "_size", 0);

        Boolean array[][] = new Boolean[size][0];

        for (int i = 0; i < size; i++)

            for (int j = 0; j < array.length; i++) {
                array[i][j] = prefs.getBoolean(arrayName + "_" + i, false);
            }

        return array;
    }
}