package com.mk.places.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mk.places.R;
import com.mk.places.activity.FavoriteUtil;
import com.mk.places.adapters.MemberAdapter;
import com.mk.places.models.MemberItem;
import com.mk.places.utilities.Dialogs;
import com.mk.places.utilities.Utils;
import com.mk.places.views.ButtonLayout;

import butterknife.Bind;
import butterknife.ButterKnife;

public class DrawerSettings extends Activity{


    private Toolbar toolbar;

    public static class FragmentSettings extends PreferenceFragment {

        private Context context;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            context = getActivity();

            // Load the preferences from an XML resource
            addPreferencesFromResource(R.xml.preference);

            addPreferencesFromResource(R.xml.preference);

            final ListPreference prefDownloadType = (ListPreference) findPreference("download_type");
            prefDownloadType.setIcon(
                    new IconicsDrawable(context)
                            .icon(GoogleMaterial.Icon.gmd_cloud_download)
                            .color(Color.WHITE)
                            .sizeDp(24));
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
            prefNotifications.setIcon(
                    new IconicsDrawable(context)
                            .icon(GoogleMaterial.Icon.gmd_stay_current_portrait)
                            .color(Color.WHITE)
                            .sizeDp(24));



            Preference prefChangelog = findPreference("pref_changelog");
            prefChangelog.setIcon(
                    new IconicsDrawable(context)
                            .icon(GoogleMaterial.Icon.gmd_new_releases)
                            .color(Color.WHITE)
                            .sizeDp(24));
            prefChangelog.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Dialogs.showChangelog(context);
                    return true;
                }
            });

            Preference prefDataBase = findPreference("pref_data_base");
            prefDataBase.setIcon(
                    new IconicsDrawable(context)
                            .icon(GoogleMaterial.Icon.gmd_book)
                            .color(Color.WHITE)
                            .sizeDp(24));
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


//        context = getActivity();
//
//        getFragmentManager().beginTransaction().replace(android.R.id.content,
//                new PrefsFragment()).commit();

//        LinearLayout root = (LinearLayout) findViewById(android.R.id.list).getParent().getParent().getParent();
//        toolbar = (Toolbar) LayoutInflater.from(this).inflate(R.layout.settings_toolbar, root, false);
//        toolbar.setTitle("Settings");
//        toolbar.setElevation(15);
//        toolbar.setNavigationIcon(R.drawable.ic_close);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        root.addView(toolbar, 0);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });



    }
}