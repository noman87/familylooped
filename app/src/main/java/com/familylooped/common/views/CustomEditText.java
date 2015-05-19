package com.familylooped.common.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * CustomTextView used for displaying custom fonts in the TextView.
 */
public class CustomEditText extends EditText {
    private static final String TAG = CustomEditText.class.getSimpleName();

    // String FONT = "fonts/brandish_regular.ttf"
    public CustomEditText(Context context) {
        super(context);
        // Typeface face = Typeface.createFromAsset(context.getAssets(), FONT);
        // this.setTypeface(face);
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        // Typeface face = Typeface.createFromAsset(context.getAssets(), FONT);
        // this.setTypeface(face);
        CustomUtils.setCustomFont(context, this, attrs);

    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // Typeface face = Typeface.createFromAsset(context.getAssets(), FONT);
        // this.setTypeface(face);
        CustomUtils.setCustomFont(context, this, attrs);

    }

}

