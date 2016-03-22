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

package com.mk.placesdrawer.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.models.PlacesItem;
import com.mk.placesdrawer.utilities.Preferences;
import com.mk.placesdrawer.utilities.Utils;
import com.mk.placesdrawer.view.TouchImageView;


public class PlacesViewerActivity extends AppCompatActivity {

    private PlacesItem item;


    private static Preferences mPrefs;
    private ScrollView layout;
    private Toolbar toolbar;

    private MainActivity mainActivity;

    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        if (Utils.newerThan(Build.VERSION_CODES.LOLLIPOP)) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        if (Utils.newerThan(Build.VERSION_CODES.KITKAT)) {
            Window window = this.getWindow();
            window.setNavigationBarColor(getResources().getColor(R.color.navigationBar));
        }

        super.onCreate(savedInstanceState);

        context = this;

        mPrefs = new Preferences(context);

        Intent intent = getIntent();
        String transitionName = intent.getStringExtra("transitionName");

        item = intent.getParcelableExtra("item");

        setContentView(R.layout.drawer_places_list_item_page);

        toolbar = (Toolbar) findViewById(R.id.toolbar_transparent);

        if (toolbar!= null) {
            toolbar.setAlpha(0);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(false);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayUseLogoEnabled(false);
        }

        TouchImageView mPhoto = (TouchImageView) findViewById(R.id.bigImageView);

        layout = (ScrollView) findViewById(R.id.viewerLayout);

        TextView location = (TextView)findViewById(R.id.textViewLocationViewer);

        if (location != null) {
            location.setText(item.getLocation());
        }

        Glide.with(context)
                .load(item.getImgPlaceUrl())
                .override(3000, 2000)
                .placeholder(R.drawable.placeholder)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .centerCrop()
                .into(mPhoto);

    }


    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        closeViewer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_actions, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeViewer();
                break;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            //Crop request
        }
    }


    private void closeViewer() {
        if (Utils.newerThan(Build.VERSION_CODES.LOLLIPOP)) {
            supportFinishAfterTransition();
        } else {
            finish();
        }
    }


}