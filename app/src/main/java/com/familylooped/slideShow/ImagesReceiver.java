package com.familylooped.slideShow;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.familylooped.MainActivity;
import com.familylooped.common.Utilities;

/**
 * Created by Noman on 5/29/2015.
 */
public class ImagesReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
    }

}
