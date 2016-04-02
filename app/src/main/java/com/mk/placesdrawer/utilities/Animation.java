package com.mk.placesdrawer.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mk.placesdrawer.R;

/**
 * Created by Max on 23.03.16.
 */
public class Animation extends Activity {

    public static void zoomInAndOut(Context context, final ImageView image) {
        final android.view.animation.Animation zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        final android.view.animation.Animation zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out);

        image.setAnimation(zoomIn);
        image.setAnimation(zoomOut);
        image.startAnimation(zoomIn);

        zoomIn.setAnimationListener(new android.view.animation.Animation.AnimationListener() {
            @Override
            public void onAnimationEnd(android.view.animation.Animation animation) {
                if (animation == zoomIn) {
                    image.startAnimation(zoomOut);
                }
            }
            @Override
            public void onAnimationRepeat(android.view.animation.Animation animation) {
                image.startAnimation(zoomIn);
            }

            @Override
            public void onAnimationStart(android.view.animation.Animation animation) {
                image.startAnimation(zoomIn);
            }
        });
    }


}
