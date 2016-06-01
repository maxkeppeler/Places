package com.mk.places.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mk.places.R;
import com.mk.places.fragment.FragmentPlaces;
import com.mk.places.utilities.Bookmarks;
import com.mk.places.utilities.Constants;
import com.mk.places.utilities.Dialogs;
import com.mk.places.utilities.Preferences;
import com.mk.places.utilities.Utils;

public class Settings extends PreferenceActivity {

    private Context context;
    private static final String TAG = "Settings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.component_settings_toolbar, root, false);

        // Fixes the odd paddings in portrait and landscape
        ListView lv = (ListView) findViewById(android.R.id.list);
        ViewGroup parent = (ViewGroup) lv.getParent();
        parent.setPadding(0, 0, 0, 0);
        root.setPadding(0, 0, 0, 0);

        toolbar.setTitle(Constants.DRAWER_SETTINGS);
        toolbar.setElevation(15);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setTitleTextColor(ContextCompat.getColor(context, R.color.white));
        root.addView(toolbar, 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addPreferencesFromResource(R.xml.preference);

        Preference prefSuggestPlaces = findPreference("pref_suggest_places");
        prefSuggestPlaces.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                // TODO: Activity for the input instead of a intent

                return true;
            }
        });

        Preference prefSuggestNatureEvents = findPreference("pref_suggest_nature_events");
        prefSuggestNatureEvents.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                // TODO: Activity for the input instead of a intent

                return true;
            }
        });

        Preference prefSuggestionsHall = findPreference("pref_suggest_hall_of_honor");
        prefSuggestionsHall.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                // TODO: Activity for the input instead of a intent

                return true;
            }
        });

        Preference prefChangelog = findPreference("pref_changelog");
        prefChangelog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Dialogs.showChangelog(context);
                return true;
            }
        });

        Preference prefDataBase = findPreference("pref_data_base");
        prefDataBase.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                new MaterialDialog.Builder(context)
                        .content("Are you sure you want to delete all bookmarked Places?")
                        .typeface(Utils.customTypeface(context, 3), Utils.customTypeface(context, 2))
                        .contentColor(ContextCompat.getColor(context, R.color.textLevel1))
                        .backgroundColor(context.getResources().getColor(R.color.cardBackground))
                        .negativeText("Yes")
                        .positiveText("No")
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                Bookmarks.deleteDB();
                            }
                        })
                        .show();

                return true;
            }
        });
    }

}
