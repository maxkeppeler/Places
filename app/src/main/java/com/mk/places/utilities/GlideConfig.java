package com.mk.places.utilities;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by Max on 19.03.16.
 */
public class GlideConfig implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // Need, so it uses the same high quality decode format as Picasso uses it at default
          builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        // register ModelLoaders here.
    }
}