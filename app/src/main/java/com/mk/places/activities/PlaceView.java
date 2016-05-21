package com.mk.places.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.inquiry.Inquiry;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mk.places.R;
import com.mk.places.adapters.CreditsAdapter;
import com.mk.places.adapters.DetailsAdapter;
import com.mk.places.adapters.GalleryAdapter;
import com.mk.places.fragment.FragmentBookmarks;
import com.mk.places.models.CreditsItem;
import com.mk.places.models.DetailsItem;
import com.mk.places.models.Place;
import com.mk.places.utilities.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaceView extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.collapsingToolbarLayout) CollapsingToolbarLayout collapsingToolbarLayout;
    @Bind(R.id.tvDescTitle) TextView tvDescTitle;
    @Bind(R.id.tvDescText) TextView tvDescText;
    @Bind(R.id.tvInfoTitle) TextView tvInfoTitle;
    @Bind(R.id.tvCreditsTitle) TextView tvCreditsTitle;
    @Bind(R.id.infoRecycler) RecyclerView infoRecycler;
    @Bind(R.id.creditsRecycler) RecyclerView creditsRecycler;
    @Bind(R.id.galleryRecyler) RecyclerView galleryRecycler;
    @Bind(R.id.shadow) LinearLayout shadow;
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

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
        }

        context = this;
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_close);

        item = getIntent().getParcelableExtra("item");

        place = item.getPlace();
        desc = item.getDescription();

        url = item.getUrl().split("\\|");

        info = item.getInfo().split("\\|");
        infoTitle = item.getInfoTitle().split("\\|");

        credits = item.getCredits().split("\\|");
        creditsTitle = item.getCreditsTitle().split("\\|");
        creditsDesc = item.getCreditsDesc().split("\\|");

        Glide.with(context)
                .load(url[0])
                .asBitmap()
                .override(1000, 1000)
                .fitCenter()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        shadow.setVisibility(View.VISIBLE);

                        new Palette.Builder(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                                if (palette == null) return;
                                color = Utils.colorFromPalette(context, palette);
                                fab.setBackgroundTintList(ColorStateList.valueOf(color));
                                fab.setVisibility(View.VISIBLE);
                                Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                                fab.startAnimation(animation);
                                collapsingToolbarLayout.setContentScrimColor(color);
                                collapsingToolbarLayout.setStatusBarScrimColor((Utils.colorVariant(color, 0.92f)));

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    window.setNavigationBarColor(color);
                                    window.setWindowAnimations(R.style.navBarAnim);
                                }
                            }
                        });

                        return false;
                    }
                })
                .into(toolbarImage);


        tvDescText.setText(desc);
        collapsingToolbarLayout.setTitle(place);


        final IconicsDrawable drawable = new IconicsDrawable(context).icon(GoogleMaterial.Icon.gmd_collections_bookmark).color(getResources().getColor(R.color.white)).sizeDp(24);
        fab.setVisibility(View.INVISIBLE);
        fab.setImageDrawable(drawable);
        fab.setOnClickListener(this);


        final Typeface typeTitles = Utils.customTypeface(context, 1);
        final Typeface typeTexts = Utils.customTypeface(context, 2);

        collapsingToolbarLayout.setCollapsedTitleTypeface(typeTitles);
        collapsingToolbarLayout.setExpandedTitleTypeface(typeTitles);
        tvDescTitle.setTypeface(typeTitles);
        tvInfoTitle.setTypeface(typeTitles);
        tvCreditsTitle.setTypeface(typeTitles);
        tvDescText.setTypeface(typeTexts);


        createCreditsCard();

        createDetailsCard();

        createGallery();

    }

    private void createCreditsCard() {

        CreditsItem[] creditsCard = new CreditsItem[creditsTitle.length];
        for (int i = 0; i < creditsTitle.length; i++)
            creditsCard[i] = new CreditsItem(creditsTitle[i], creditsDesc[i], credits[i]);

        creditsRecycler.setLayoutManager(new LinearLayoutManager(context));
        creditsRecycler.setAdapter(new CreditsAdapter(creditsCard, context));
        creditsRecycler.setHasFixedSize(true);
    }

    private void createDetailsCard() {

        DetailsItem[] detailsCard = new DetailsItem[infoTitle.length];
        for (int i = 0; i < infoTitle.length; i++)
            detailsCard[i] = new DetailsItem(infoTitle[i], info[i]);

        infoRecycler.setLayoutManager(new LinearLayoutManager(context));
        infoRecycler.setAdapter(new DetailsAdapter(detailsCard, context));
        infoRecycler.setHasFixedSize(true);

    }

    private void createGallery() {

        final GalleryAdapter galleryAdapter = new GalleryAdapter(context, url, new GalleryAdapter.ClickListener() {

            @Override
            public void onClick(GalleryAdapter.ViewHolder view, int index, boolean longOnClick) {

                if (longOnClick) {
//                    TODO: For what could the long click be used?

                } else {
                    Intent intent = new Intent(context, GalleryView.class);
                    intent.putExtra("url", url);
                    intent.putExtra("index", index);
                    intent.putExtra("place", place);
                    startActivity(intent);
                    finish();
                }
            }
        });

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
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                close();
                break;
            case R.id.share:
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("*/*");
                intent.putExtra(Intent.EXTRA_EMAIL, "chvent94@gmail.com");
                intent.putExtra(Intent.EXTRA_SUBJECT, "Places - Support Mail");
                intent.putExtra(Intent.EXTRA_TEXT, desc);
                startActivity(Intent.createChooser(intent, "Send Email with"));
                break;

            case R.id.launch:
                Utils.customChromeTab(context, "http://www.google.com/search?q=" + place, color);
                break;
        }
        return true;
    }

    private void close() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            supportFinishAfterTransition();
        else finish();
    }


    @Override
    public void onClick(View v) {

        if (Bookmarks.favoriteItem(item.getId()))
            Utils.simpleSnackBar(context, Utils.colorVariant(color, 1.07f), R.id.coordinatorLayout, R.string.bookmarkedPlace, Snackbar.LENGTH_LONG);
        else
            Utils.simpleSnackBar(context, Utils.colorVariant(color, 1.07f), R.id.coordinatorLayout, R.string.removedPlace, Snackbar.LENGTH_LONG);


        Inquiry.deinit();

        FragmentBookmarks.loadBookmarks(context);



    }
}
