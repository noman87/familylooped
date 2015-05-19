package com.familylooped.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * CustomTextView used for displaying custom fonts in the TextView.
 */
public class CustomTextView extends TextView {
    private static final String TAG = CustomTextView.class.getSimpleName();

    // String FONT = "fonts/brandish_regular.ttf"
    public CustomTextView(Context context) {
        super(context);
        // Typeface face = Typeface.createFromAsset(context.getAssets(), FONT);
        // this.setTypeface(face);
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Typeface face = Typeface.createFromAsset(context.getAssets(), FONT);
        // this.setTypeface(face);
        CustomUtils.setCustomFont(context, this, attrs);

    }

    public CustomTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // Typeface face = Typeface.createFromAsset(context.getAssets(), FONT);
        // this.setTypeface(face);
        CustomUtils.setCustomFont(context, this, attrs);

    }

}

