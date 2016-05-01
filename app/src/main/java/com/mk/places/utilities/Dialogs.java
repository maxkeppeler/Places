package com.mk.places.utilities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.Html;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mk.places.R;
import com.mk.places.fragment.DrawerPlaces;

/**
 * Created by Max on 30.03.16.
 */
public class Dialogs {



    public static void showChangelog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.changelogTitle)
                .content(Html.fromHtml(context.getResources().getString(R.string.changelogContent)))
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
//                .backgroundColor(context.getResources().getColor(R.color.colorPrimary))
                .show();


    }

    public static void columnsDialog(final Context context) {

        new MaterialDialog.Builder(context)
                .title(R.string.columnsTitle)
                .items(R.array.columnsArray)
                .backgroundColor(context.getResources().getColor(R.color.dialogs))
                .itemsCallback(new MaterialDialog.ListCallback() {
                    @Override
                    public void onSelection(MaterialDialog dialog, View view, int i, CharSequence text) {
                        DrawerPlaces drawer = new DrawerPlaces();

                        Preferences mPrefs = new Preferences(context);
                        mPrefs.setColumns(i+1);

                        drawer.setColumns(i + 1);
                    }
                })
                .show();
    }

}
