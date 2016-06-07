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

package com.earth.places.utilities;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

@SuppressWarnings("deprecation")
public class JSONParser {

    private static final String TAG = JSONParser.class.getName();

    public static JSONObject getJSONFromURL(String url) {

        try {

            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(new HttpGet(url));

            if (response.getStatusLine().getStatusCode() == 200) {

                final String data = EntityUtils.toString(response.getEntity());
                return new JSONObject(data);

            }

        } catch (Exception e) {
            Log.d(TAG, "No response from the server, where you have stored your json: " + e.getMessage());
        }

        return null;
    }
}