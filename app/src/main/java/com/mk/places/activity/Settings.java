package com.mk.places.activity;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mk.places.R;
import com.mk.places.utilities.Dialogs;

public class Settings extends PreferenceActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        Toolbar toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);

        ListView lv = (ListView) findViewById(android.R.id.list);
        ViewGroup parent = (ViewGroup) lv.getParent();
        parent.setPadding(0, 0, 0, 0);
        root.setPadding(0, 0, 0, 0);

        toolbar.setTitle("Settings");
        toolbar.setElevation(15);
        toolbar.setNavigationIcon(R.drawable.ic_close);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        root.addView(toolbar, 0);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        addPreferencesFromResource(R.xml.preference);

        final ListPreference prefDownloadType = (ListPreference) findPreference("download_type");
        prefDownloadType.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                String[] array = getResources().getStringArray(R.array.listArray);
                prefDownloadType.setSummary(array[Integer.parseInt(newValue.toString())]);
                prefDownloadType.setValue(newValue.toString());
                return false;
            }
        });


        SwitchPreference prefNotifications = (SwitchPreference) findPreference("pref_notifications");


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
                        .negativeText("Yes")
                        .positiveText("No").positiveColor(getResources().getColor(R.color.white))
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                FavoriteUtil.deleteDB(context);
                            }
                        })
                        .show();

                return true;
            }
        });
    }

}
