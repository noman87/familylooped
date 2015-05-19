package com.familylooped.downloadPhotos;

import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.familylooped.R;

public class DownloadActivity extends ActionBarActivity {

    private Receiver mReceve;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
        mReceve = new Receiver();
        ((Button)findViewById(R.id.btn_done)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DownloadActivity.this, DownloadService.class);
                // add infos for the service which file to download and where to store
                intent.putExtra(DownloadService.FILENAME, "shaid.jpg");
                intent.putExtra(DownloadService.URL,
                        "http://www.familylooped.com//app//uploads//gallery//thumbs//1430475503_1.jpg");
                startService(intent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mReceve);
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mReceve,new IntentFilter(DownloadService.NOTIFICATION));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_download, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
