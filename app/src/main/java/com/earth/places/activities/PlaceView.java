package com.earth.places.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.inquiry.Inquiry;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.earth.places.R;
import com.earth.places.adapters.CreditsAdapter;
import com.earth.places.adapters.GalleryAdapter;
import com.earth.places.adapters.InfosAdapter;
import com.earth.places.fragment.FragmentBookmarks;
import com.earth.places.models.CreditsItem;
import com.earth.places.models.InfosItem;
import com.earth.places.models.Place;
import com.earth.places.utilities.Bookmarks;
import com.earth.places.utilities.Dialogs;
import com.earth.places.utilities.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaceView extends AppCompatActivity {

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.creditsCardView) CardView creditsCardView;
    @Bind(R.id.infoCardView) CardView infoCardView;
    @Bind(R.id.tvDescTitle) TextView tvDescTitle;
    @Bind(R.id.tvDescText) TextView tvDescText;
    @Bind(R.id.tvInfoTitle) TextView tvInfoTitle;
    @Bind(R.id.tvCreditsTitle) TextView tvCreditsTitle;
    @Bind(R.id.infoRecycler) RecyclerView infoRecycler;
    @Bind(R.id.creditsRecycler) RecyclerView creditsRecycler;
    @Bind(R.id.galleryRecyler) RecyclerView galleryRecycler;
    @Bind(R.id.toolbarImage) ImageView toolbarImage;

    private int color;
    private Window window;
    private String place, desc;
    private String[] infoTitle, info;
    private String[] creditsTitle, creditsDesc, credits;
    private Activity context;
    private String[] url;
    private Place item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.place_item);

        window = this.getWindow();

        context = this;
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_close);

        item = getIntent().getParcelableExtra("item");
        place = item.getPlace();
        desc = item.getDescription();

        // Make strings to arrays
        url = item.getUrl().split("\\|");
        info = item.getInfo().split("\\|");
        infoTitle = item.getInfoTitle().split("\\|");
        credits = item.getCredits().split("\\|");
        creditsTitle = item.getCreditsTitle().split("\\|");
        creditsDesc = item.getCreditsDesc().split("\\|");
        color = getIntent().getIntExtra("color", -1);

                Glide.with(context)
                        .load(url[0])
                        .crossFade()
                        .override(900, 900)
                        .centerCrop()
                        .priority(Priority.HIGH)
                        .into(toolbarImage);

        collapsingToolbarLayout.setContentScrimColor(color);
        collapsingToolbarLayout.setStatusBarScrimColor((Utils.colorVariant(color, 0.92f)));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setNavigationBarColor(color);
            window.setWindowAnimations(R.style.navBarAnim);
        }

        tvDescText.setText(desc);
        collapsingToolbarLayout.setTitle(place);

        final Typeface typeTitles = Utils.customTypeface(context, 1);
        final Typeface typeTexts = Utils.customTypeface(context, 2);

        collapsingToolbarLayout.setCollapsedTitleTypeface(typeTitles);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeTitles);
        tvDescTitle.setTypeface(typeTitles);
        tvInfoTitle.setTypeface(typeTitles);
        tvCreditsTitle.setTypeface(typeTitles);
        tvDescText.setTypeface(typeTexts);

        createGallery();

        // Just display the card, when there's content
        if (item.getInfoTitle().length() > 2) {
            infoCardView.setVisibility(View.VISIBLE);
            createInfosCard();
        }

        // Just display the card, when there's content
        if (item.getCredits().length() > 2) {
            creditsCardView.setVisibility(View.VISIBLE);
            createCreditsCard();
        }

    }

    /**
     *  Setup the card for giving credits to the sources of the place.
     */
    private void createCreditsCard() {

        CreditsItem[] creditsCard = new CreditsItem[creditsTitle.length];
        for (int i = 0; i < creditsTitle.length; i++)
            creditsCard[i] = new CreditsItem(creditsTitle[i], creditsDesc[i], credits[i]);

        creditsRecycler.setLayoutManager(new LinearLayoutManager(context));
        creditsRecycler.setAdapter(new CreditsAdapter(creditsCard, context));
        creditsRecycler.setHasFixedSize(true);
    }

    /**
     *  Setup the card for giving users info about the place.
     */
    private void createInfosCard() {

        InfosItem[] infosCard = new InfosItem[infoTitle.length];
        for (int i = 0; i < infoTitle.length; i++)
            infosCard[i] = new InfosItem(infoTitle[i], info[i]);

        infoRecycler.setLayoutManager(new LinearLayoutManager(context));
        infoRecycler.setAdapter(new InfosAdapter(infosCard, context));
        infoRecycler.setHasFixedSize(true);

    }

    /**
     *  Setup the image gallery
     */
    private void createGallery() {

        final GalleryAdapter galleryAdapter = new GalleryAdapter(context, url, new GalleryAdapter.ClickListener() {

            @Override
            public void onClick(GalleryAdapter.ViewHolder view, int index, boolean longOnClick) {

                if (longOnClick) {
//                    TODO: For what could the long click be used?

                } else {
                    Intent intent = new Intent(context, GalleryView.class);
                    intent.putExtra("urls", url);
                    intent.putExtra("index", index);
                    intent.putExtra("place", place);
                    startActivity(intent);
                }
            }
        });

        if (url.length <= 4)
            galleryRecycler.setLayoutManager(new GridLayoutManager(context, 2, LinearLayoutManager.VERTICAL, false));
        else
            galleryRecycler.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false));

        galleryRecycler.setAdapter(galleryAdapter);
        galleryRecycler.setHasFixedSize(true);

    }

    @Override
    public void onBackPressed() {
        close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.action_place, menu);

        Bookmarks.init(context);

        if (Bookmarks.isBookmarked(item.getId()))
            menu.findItem(R.id.bookmark).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_bookmark).paddingDp(1).color(Color.WHITE).sizeDp(24));
        else menu.findItem(R.id.bookmark).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_bookmark_border).paddingDp(1).color(Color.WHITE).sizeDp(24));

        Inquiry.deinit();

        menu.findItem(R.id.launch).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_open_in_browser).paddingDp(1).color(Color.WHITE).actionBar());
        menu.findItem(R.id.report).setIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_bug_report).paddingDp(1).color(Color.WHITE).actionBar());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:  // Back (close) button
                close();
                break;

            case R.id.bookmark:       // Bookmark function
                bookmark();
                break;

            case R.id.launch:       // Search in a chrome tab after the location without a specific link
                Utils.openChromeTab(context, getResources().getString(R.string.launch_word_search) + place, color);
                break;

            case R.id.report:       // Let users report any issues on the respective place
                Dialogs.mailReport(context);
                break;
        }
        return true;
    }

    /**
     * Bookmark the respective Place. Drawable transition between the bookmark states.
     */
    private void bookmark() {

//        TODO: Drawable Transition

        if (Bookmarks.bookmarkItem(item.getId()))
            Utils.showSnackBar(context, Utils.colorVariant(color, 1.07f), R.id.coordinatorLayout, R.string.bookmarkedPlace, Snackbar.LENGTH_LONG);
        else Utils.showSnackBar(context, Utils.colorVariant(color, 1.07f), R.id.coordinatorLayout, R.string.removedPlace, Snackbar.LENGTH_LONG);

        FragmentBookmarks.loadBookmarks(context);

        Inquiry.deinit();

    }

    /**
     * Close current activity correctly
     */
    private void close() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            this.supportFinishAfterTransition();
        else finish();
    }

}
