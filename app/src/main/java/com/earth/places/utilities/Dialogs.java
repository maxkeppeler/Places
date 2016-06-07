package com.earth.places.utilities;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Html;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.earth.places.R;

public class Dialogs {


    public static void showChangelog(final Context context) {

        new MaterialDialog.Builder(context)

                .typeface(Utils.customTypeface(context, 2), Utils.customTypeface(context, 2))
                .contentColor(ContextCompat.getColor(context, R.color.textLevel1))
                .backgroundColor(ContextCompat.getColor(context, R.color.cardBackground))
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
                .show();


    }

    public static void mailReport(final Context context) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.putExtra(Intent.EXTRA_EMAIL, new String[]{context.getResources().getString(R.string.mail_adress)});
        intent.setData(Uri.parse(context.getResources().getString(R.string.mail_uri)));
        intent.putExtra(Intent.EXTRA_SUBJECT, context.getResources().getString(R.string.mail_subject));
        intent.putExtra(Intent.EXTRA_TEXT, context.getResources().getString(R.string.mail_text));
        context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.mail_title)));
    }
}
