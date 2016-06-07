package com.earth.places.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.earth.places.R;

/**
 *  Thanks Aidan Follestad
 */
public class ButtonLayout extends LinearLayout {

    public ButtonLayout(Context context) {
        super(context);
    }

    public ButtonLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ButtonLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setAmount(int amount) {
        setWeightSum(amount);
    }

    public void addButton(String text, String link, boolean horizontal) {

        if (horizontal) setOrientation(HORIZONTAL);
        else setOrientation(VERTICAL);

        final Button button = (Button) LayoutInflater.from(getContext()).inflate(R.layout.component_borderless_button, this, false);

        final LinearLayout.LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.weight = 1;

        button.setText(text);
        button.setTag(link);

        addView(button, params);

    }

}
