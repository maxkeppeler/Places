package com.mk.placesdrawer.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mk.placesdrawer.R;
import com.mk.placesdrawer.fragment.Fragment_1;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean currentProfile) {
                        return false;
                    }
                })
                .build();


        new DrawerBuilder().withActivity(this).build();

        //if you want to update the items at a later time it is recommended to keep it in a variable
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(R.string.drawer_item_home).withIcon(GoogleMaterial.Icon.gmd_home).withIdentifier(1);
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName(R.string.drawer_item_submit).withIcon(GoogleMaterial.Icon.gmd_mail_send);

        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName(R.string.drawer_item_about).withIcon(GoogleMaterial.Icon.gmd_face);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName(R.string.drawer_item_settings).withIcon(GoogleMaterial.Icon.gmd_settings);

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withAccountHeader(headerResult)
                .withActivity(this)
                .withToolbar(toolbar)
                .addDrawerItems(
                        item1,
                        item2,
                        new DividerDrawerItem(),
                        item3,
                        item4
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {

                        FragmentManager manager = getSupportFragmentManager();
                        FragmentTransaction transaction = manager.beginTransaction();
                        Fragment fragment;

                        if (drawerItem != null) {

                            Intent intent = null;

                            switch ((int) drawerItem.getIdentifier()) {

                                case 1:   fragment = new Fragment_1();    break;
                                case 2:   fragment = new Fragment_1();    break;
                                case 3:   fragment = new Fragment_1();    break;
                                case 4:   fragment = new Fragment_1();    break;
                                case 5:   fragment = new Fragment_1();    break;

                                default:  fragment = new Fragment_1();
                            }

                            transaction.replace(R.id.container, fragment);
                            transaction.commit();

                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }


                        return false;

                    }
                })
                .build();

        //Default selection for the app drawer, when the app starts.
        //(Otherwise you have to go again in the home section in order to let the content load)
        result.setSelection(1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
