package com.familylooped.common.async;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.familylooped.R;

import static android.widget.LinearLayout.LayoutParams.MATCH_PARENT;
import static android.widget.LinearLayout.LayoutParams.WRAP_CONTENT;

public class TransparentProgressDialog extends Dialog {

    private AnimationDrawable frameAnimation;
    private ProgressWheel progressWheel;

    public TransparentProgressDialog(Context context) {
        super(context, R.style.TransparentProgressDialog);
        WindowManager.LayoutParams windowAttrs = getWindow().getAttributes();
        windowAttrs.gravity = Gravity.CENTER;
        getWindow().setAttributes(windowAttrs);
        setTitle(null);
        setCancelable(false);
        setOnCancelListener(null);
        LinearLayout layout = new LinearLayout(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.custome_dialog, null);
        progressWheel = (ProgressWheel) view.findViewById(R.id.progress);
        progressWheel.spin();
        layout.addView(view, params);
        addContentView(layout, params);
        /*ImageView img = new ImageView(context);
        img.setBackgroundResource(R.drawable.progress_bar_anim);

        // Get the background, which has been compiled to an AnimationDrawable object.
        frameAnimation = (AnimationDrawable) img.getBackground();
        layout.addView(img, params);
        addContentView(layout, params); */
    }

    @Override
    public void show() {
        super.show();
    }


    @Override
    public void dismiss() {
        super.dismiss();

    }
}
