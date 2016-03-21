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

package com.mk.placesdrawer.utilities;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;

import com.mk.placesdrawer.activity.MainActivity;
import com.mk.placesdrawer.fragment.DrawerPlaces;
import com.mk.placesdrawer.models.PlacesList;


public class ApplicationBase extends Application {

    private Context context;
    private Preferences mPrefs;

    @Override
    public void onCreate() {
        super.onCreate();

        this.context = getApplicationContext();
        mPrefs = new Preferences(context);
        loadPlacesList();
    }

    private void loadPlacesList() {

        if (mPrefs.getPlacesListLoaded()) {
            PlacesList.clearList();
            mPrefs.setPlacesListLoaded(!mPrefs.getPlacesListLoaded());

        }

        new DrawerPlaces.DownloadJSON(new MainActivity.PlacesListInterface() {
            @Override
            public void checkPlacesListCreation(boolean result) {
                mPrefs.setPlacesListLoaded(result);

                if (DrawerPlaces.mAdapter != null) {
                    DrawerPlaces.mAdapter.notifyDataSetChanged();
                }
            }
        }, context).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

}