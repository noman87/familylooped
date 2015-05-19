package com.familylooped.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * CustomTextView used for displaying custom fonts in the TextView.
 */
public class CustomAutoCompleteTextView extends AutoCompleteTextView {
    private static final String TAG = CustomAutoCompleteTextView.class.getSimpleName();

    // String FONT = "fonts/brandish_regular.ttf"
    public CustomAutoCompleteTextView(Context context) {
        super(context);
        // Typeface face = Typeface.createFromAsset(context.getAssets(), FONT);
        // this.setTypeface(face);
    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Typeface face = Typeface.createFromAsset(context.getAssets(), FONT);
        // this.setTypeface(face);
        CustomUtils.setCustomFont(context, this, attrs);

    }

    public CustomAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // Typeface face = Typeface.createFromAsset(context.getAssets(), FONT);
        // this.setTypeface(face);
        CustomUtils.setCustomFont(context, this, attrs);

    }

}

