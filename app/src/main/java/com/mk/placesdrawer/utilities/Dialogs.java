package com.mk.placesdrawer.utilities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.fragment.DrawerPlaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * Created by Max on 30.03.16.
 */
public class Dialogs {

    public static void showChangelog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.changelogTitle)
                .content(R.string.changelogContent)
                .positiveText(R.string.changelogPositive)
                .negativeText(R.string.changelogNegative)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
                        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                        try {
                            context.startActivity(goToMarket);
                        } catch (ActivityNotFoundException e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
                        }

                    }
                })
                .backgroundColor(context.getResources().getColor(R.color.colorPrimary))
                .show();
    }

    public static void filterDialog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.filterTitle)
                .items(R.array.filterContentArray)
                .backgroundColor(context.getResources().getColor(R.color.colorPrimary))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {
//                     There are 2 filter options: After sight and after country
                        if (i == 0) sightDialog(context);
                        if (i == 1) countryDialog(context);

                    }
                })
                .show();

    }

    public static void sightDialog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.sightTitle)
                .items(R.array.sightContentArray)
                .backgroundColor(context.getResources().getColor(R.color.colorPrimary))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {

//                        TODO on selection, filter the current placesList and return just the objects where the category (Sight) is correct.

                    }
                })
                .show();
    }

    public static void countryDialog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.filterTitle)
                .items(R.array.filterContentArray)
                .backgroundColor(context.getResources().getColor(R.color.colorPrimary))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {

//                        TODO on selection, filter the current placesList and return just the objects where the category (country) is correct.

                    }
                })
                .show();
    }

    public static void columnsDialog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.columnsTitle)
                .items(R.array.columnsArray)
                .backgroundColor(context.getResources().getColor(R.color.colorPrimary))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {
                        DrawerPlaces.changeColumns(i + 1);
                    }
                })
                .show();
    }

    public static void saveImageDialog(final Context context, final Bitmap bitmap, final String location) {

//        TODO Why the fuck does the download image replace the current bitmap, download from the url with glide? I never have used the variable
        new MaterialDialog.Builder(context)
                .title(R.string.saveImageTitle)
                .items(R.array.saveImageContentArray)
                .backgroundColor(context.getResources().getColor(R.color.colorPrimary))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {

                        if (i == 0) saveImage(bitmap, 1, context, location); // Image
                        if (i == 1) saveImage(bitmap, 2, context, location); // Image with text overlay
                        if (i == 2) saveImage(bitmap, 3, context, location); // Page as Image

                    }
                })
                .show();
    }

    private static void saveImage(Bitmap bitmap, int index, Context context, String location) {

        //        TODO Why the fuck does the download image replace the current bitmap, download from the url with glide? I never have used the variable

        OutputStream fOut = null;
        String imageName = location + " .jpg";
        File path = new File(Environment.getExternalStorageDirectory().toString());
        File myDir = new File(path, context.getResources().getString(R.string.app_name));

        if (index == 2) {  // with text overlay
            imageName = location + " lo.jpg";

            float textSize = 0.111979f * (bitmap.getWidth()) / 1.6f;
            float marginLeft = 0.07421875f * bitmap.getWidth();

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            paint.setColor(context.getResources().getColor(R.color.white));
            paint.setAlpha(255);
            paint.setTypeface(Utils.getTypeface(context, 1));
            paint.setTextSize(textSize);
            paint.setShadowLayer(12, 0, 12, context.getResources().getColor(R.color.textShadow));
            paint.setTextAlign(Paint.Align.LEFT);
            canvas.drawText(location, marginLeft, bitmap.getHeight() - paint.getTextSize(), paint);
        }

        if (!myDir.exists()) myDir.mkdir();
        File file = new File(myDir, imageName);
        try {
            fOut = new FileOutputStream(file);                                       Log.d("1", "Download: Image was saved.");
            if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut)) {          Log.d("2", "Download: Image was NOT saved: " + path + imageName);
            }
        } catch (FileNotFoundException e) {                                          Log.d("3", "Download: Image: Exception");
            e.printStackTrace();
        }
    }
}
