package com.familylooped.downloadPhotos;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.familylooped.common.Utilities;

/**
 * Created by Noman on 5/6/2015.
 */
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String string = bundle.getString(DownloadService.FILEPATH);
            int resultCode = bundle.getInt(DownloadService.RESULT);
            if (resultCode == Activity.RESULT_OK) {
                Utilities.toast(context, "Download complete. Download URI: " + string);
            } else {
                Utilities.toast(context, "Download failed");
            }
        }
    }

}
