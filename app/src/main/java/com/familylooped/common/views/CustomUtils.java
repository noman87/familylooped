package com.familylooped.common.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.familylooped.R;


public class CustomUtils {

    public static void setCustomFont(Context ctx, TextView customView, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomTextView);
        String customFont = a.getString(R.styleable.CustomTextView_customFont);
        setCustomFont(ctx, customView, customFont);
        a.recycle();
    }

    public static boolean setCustomFont(Context ctx, TextView customView, String asset) {
        Typeface tf;
        try {
            tf = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            return false;
        }
        customView.setTypeface(tf);
        return true;
    }

}
