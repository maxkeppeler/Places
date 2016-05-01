package com.mk.places.activity;

import android.content.Context;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mk.places.R;
import com.mk.places.utilities.Dialogs;

public class Settings extends PreferenceActivity {

    private Context context;
    private Toolbar toolbar;
    private AppCompatDelegate delegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preference);

        Preference prefChangelog = findPreference("pref_changelog");

        context = this;

        prefChangelog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {

                Dialogs.showChangelog(context);

                return true;
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
        toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
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
    }


}
