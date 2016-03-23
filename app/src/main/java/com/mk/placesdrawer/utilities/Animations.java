package com.mk.placesdrawer.utilities;

import android.app.Activity;
import android.content.Context;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.mk.placesdrawer.R;

/**
 * Created by Max on 23.03.16.
 */
public class Animations extends Activity {

    public static void zoomInAndOut(Context context, final ImageView image) {

        final Animation zoomIn = AnimationUtils.loadAnimation(context, R.anim.zoom_in);
        final Animation zoomOut = AnimationUtils.loadAnimation(context, R.anim.zoom_out);

        image.setAnimation(zoomIn);
        image.setAnimation(zoomOut);

        image.startAnimation(zoomIn);

        zoomIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationEnd(Animation animation) {
                if (animation == zoomIn) {
                    image.startAnimation(zoomOut);
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                image.startAnimation(zoomIn);
            }

            @Override
            public void onAnimationStart(Animation animation) {
                image.startAnimation(zoomIn);
            }
        });
    }


}
