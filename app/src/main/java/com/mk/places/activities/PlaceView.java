package com.mk.places.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.Preference;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.transition.Slide;
import android.transition.Transition;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.inquiry.Inquiry;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mk.places.R;
import com.mk.places.adapters.GalleryAdapter;
import com.mk.places.adapters.PlaceInfoAdapter;
import com.mk.places.models.InfoItem;
import com.mk.places.models.Place;
import com.mk.places.threads.DownloadImage;
import com.mk.places.utilities.Preferences;
import com.mk.places.utilities.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

public class PlaceView extends AppCompatActivity {



    @Bind(R.id.placeItemFAB)
    FloatingActionButton fab;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.collapsingToolbar)
    CollapsingToolbarLayout toolbarLayout;
    @Bind(R.id.pDescTitle)
    TextView pDescTitle;
    @Bind(R.id.pDescText)
    TextView pDescText;
    @Bind(R.id.pInfoTitle)
    TextView pInfoTitle;
    @Bind(R.id.pInfoRecycler)
    RecyclerView pInfoRecycler;
    @Bind(R.id.pGalleryRecyler)
    RecyclerView pGalleryRecycler;
    @Bind(R.id.shadowOverlay)
    LinearLayout shadowOverlay;
    @Bind(R.id.pMainImage)
    ImageView pMainImage;
    private int color;
    private Window window;
    private String location, desc;
    private String[] infoTitle, info;
    private Activity context;
    private String[] images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window = this.getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
        }

        context = this;
        setContentView(R.layout.drawer_places_detail);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationIcon(R.drawable.ic_close);

        final Place item = getIntent().getParcelableExtra("item");
        location = item.getLocation();
        desc = item.getDescription();
        images = item.getUrl().replace(" ", "").split("\\|");
        info = item.getInfo().split("\\|");
        infoTitle = item.getInfoTitle().split("\\|");

        Glide.with(context)
                .load(images[0])
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
                        shadowOverlay.setVisibility(View.VISIBLE);

                        new Palette.Builder(resource).generate(new Palette.PaletteAsyncListener() {
                            @Override
                            public void onGenerated(Palette palette) {

                                if (palette == null) return;
                                color = Utils.colorFromPalette(context, palette);
                                fab.setBackgroundTintList(ColorStateList.valueOf(color));
                                fab.setVisibility(View.VISIBLE);
                                Animation animation = AnimationUtils.loadAnimation(context, R.anim.scale_up);
                                fab.startAnimation(animation);
                                toolbarLayout.setContentScrimColor(color);
                                toolbarLayout.setStatusBarScrimColor((Utils.colorVariant(color, 0.92f)));

                                Transition transition = new Slide();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    window.setNavigationBarColor(color);
                                    window.setWindowAnimations(R.style.navBarAnim);
                                }
                            }
                        });

                        return false;
                    }
                })
                .into(pMainImage);


        InfoItem[] infoItems = new InfoItem[infoTitle.length];
        for (int i = 0; i < infoTitle.length; i++)
            infoItems[i] = new InfoItem(infoTitle[i], info[i]);

        pInfoRecycler.setLayoutManager(new LinearLayoutManager(context));
        pInfoRecycler.setAdapter(new PlaceInfoAdapter(infoItems, context));
        pInfoRecycler.setHasFixedSize(true);

        final Typeface typeTitles = Utils.customTypeface(context, 1);
        final Typeface typeTexts = Utils.customTypeface(context, 2);

        pDescTitle.setTypeface(typeTitles);
        pInfoTitle.setTypeface(typeTitles);
        pDescText.setTypeface(typeTexts);

        pDescText.setText(Html.fromHtml(desc).toString().replace("â€“", "–").replace("â€™", "\"").replace("â€™", "\"").replace("â€˜", "\"").replace("\\n", "\n").replace("\\", ""));

        fab.setVisibility(View.INVISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                TODO: Deselecting the bookmarked place does not work correctly

                Bookmarks.init(context);
                Bookmarks.favoriteItem(item.getId());
                Log.d("FAB", " with ID: " + item.getId() + " Selected: " + Bookmarks.isFavorited(item.getId()));


                MainActivity.drawer.updateBadge(1, new StringHolder(new Preferences(context).getFavoSize() + ""));

                Utils.simpleSnackBar(context, color, R.id.coordinatorLayout, R.string.snackbarFavoredText, Snackbar.LENGTH_LONG);
            }
        });

        toolbarLayout.setTitle(location);
        toolbarLayout.setCollapsedTitleTypeface(typeTitles);
        toolbarLayout.setExpandedTitleTypeface(typeTitles);

        createGallery();
    }


    private void createGallery() {


        final GalleryAdapter galleryAdapter = new GalleryAdapter(context, images, new GalleryAdapter.ClickListener() {


            @Override
            public void onClick(GalleryAdapter.ViewHolder view, int index, boolean longOnClick) {

                if (longOnClick) {

                } else if (!longOnClick) {

                    Log.d("PlaceView Short", "long click" + longOnClick);
                    Intent intent = new Intent(context, GalleryView.class);
                    intent.putExtra("imageLink", images);
                    intent.putExtra("index", index);
                    intent.putExtra("location", location);
                    context.startActivity(intent);

                }


            }
        });

        pGalleryRecycler.setLayoutManager(new GridLayoutManager(context, 2, GridLayoutManager.HORIZONTAL, false));
        pGalleryRecycler.setAdapter(galleryAdapter);
        pGalleryRecycler.setHasFixedSize(true);

    }

    @Override
    public void onBackPressed() {
        close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        getMenuInflater().inflate(R.menu.toolbar_place, menu);
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
                Utils.customChromeTab(context, "http://www.google.com/search?q=" + location, color);
                break;
        }
        return true;
    }

    private void close() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            supportFinishAfterTransition();
        else finish();
    }





}
