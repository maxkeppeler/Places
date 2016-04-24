package com.mk.places.utilities;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mk.places.R;

import java.util.Random;

/**
 * Created by Max on 23.03.16.
 */
public class AnimUtils extends Activity {

    public static void zoomInAndOut(Context context, final ImageView image) {


        Random random = new Random();
        Random random2 = new Random();

        final android.view.animation.Animation zoomIn, zoomOut;

        if (random.nextInt(50) <  random2.nextInt(50)) {
            zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in_1);
            zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out_1);
        } else {
           zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in_2);
            zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out_2);
        }

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

    public static  void reveal(View v) {
        int cx = v.getWidth() / 2;
        int cy = v.getHeight() / 2;
        int finalRadius = Math.max(v.getWidth(), v.getHeight());
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Animator anim = android.view.ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, finalRadius);
            v.setVisibility(View.VISIBLE);
            anim.setDuration(800);
            anim.setInterpolator(new AccelerateDecelerateInterpolator());
            anim.start();
        }
    }

}
