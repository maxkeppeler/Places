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

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.customtabs.CustomTabsClient;
import android.support.customtabs.CustomTabsIntent;
import android.support.customtabs.CustomTabsServiceConnection;
import android.support.customtabs.CustomTabsSession;
import android.support.design.widget.Snackbar;
import android.support.v7.graphics.Palette;
import android.view.View;

import com.mk.places.R;

public class Utils extends Activity {

    public static Typeface customTypeface(Context context, int index) {
        Typeface typeface = null;
        if (index == 1) typeface = Typeface.createFromAsset(context.getAssets(), "fonts/BreeSerif-Regular.ttf");
        if (index == 2) typeface = Typeface.createFromAsset(context.getAssets(), "fonts/OpenSans-Regular.ttf");
        return typeface;
    }

    public static void customChromeTab(Context context, String link, int color) {
        final CustomTabsClient[] mClient = new CustomTabsClient[1];
        final CustomTabsSession[] mCustomTabsSession = new CustomTabsSession[1];

        CustomTabsServiceConnection mCustomTabsServiceConnection = new CustomTabsServiceConnection() {
            @Override
            public void onCustomTabsServiceConnected(ComponentName componentName, CustomTabsClient customTabsClient) {
                mClient[0] = customTabsClient;
                mClient[0].warmup(0L);
                mCustomTabsSession[0] = mClient[0].newSession(null);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                mClient[0] = null;
            }
        };

        CustomTabsClient.bindCustomTabsService(context, "com.android.chrome", mCustomTabsServiceConnection);
        CustomTabsIntent customTabsIntent = new CustomTabsIntent.Builder(mCustomTabsSession[0])
                .setToolbarColor(color)
                .setShowTitle(true)
                .addDefaultShareMenuItem()
                .build();

        customTabsIntent.launchUrl((Activity) context, Uri.parse(link));
        context.unbindService(mCustomTabsServiceConnection);
    }

    public static String getStringFromResources(Context context, int id) {
        return context.getResources().getString(id);
    }

    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }

    public static int colorVariant(int color, float intensity) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= intensity;
        color = Color.HSVToColor(hsv);
        return color;
    }

    public static void intentOpen(Uri uri, Context context, String text) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, "image/*");
        context.startActivity(Intent.createChooser(intent, text));
    }

    public static int colorFromPalette(Context context, Palette palette) {

        final int defaultColor = context.getResources().getColor(R.color.colorPrimary);
        int mutedLight = palette.getLightMutedColor(defaultColor);
        int vibrantLight = palette.getLightVibrantColor(mutedLight);
        int mutedDark = palette.getDarkMutedColor(vibrantLight);
        int muted = palette.getMutedColor(mutedDark);
        int vibrantDark = palette.getDarkVibrantColor(muted);
        return palette.getVibrantColor(vibrantDark);
    }

    public static void simpleSnackBar(Activity activity, int color, int view, int text, int length) {

        View layout = activity.findViewById(view);
        Snackbar snackbar = Snackbar.make(layout, text, length)
                .setActionTextColor(activity.getResources().getColor(R.color.white));

        View snackBarView = snackbar.getView();
        if (color == 0) color = activity.getResources().getColor(R.color.colorPrimary);
        snackBarView.setBackgroundColor(color);
        snackbar.show();
    }
}