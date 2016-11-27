package com.ayoprez.easydownloader;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;

/**
 * Created by ayo on 26.11.16.
 */

public class DownloaderBroadcastReceiver extends BroadcastReceiver {

    private DownloadCallback callback;


    public void setCallback(@NonNull DownloadCallback callback){
        this.callback = callback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if(callback != null & intent.getAction() != null && intent.getAction().equals(DownloadManager.ACTION_DOWNLOAD_COMPLETE)) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            callback.downloadCompleted(downloadId);
        }
    }

    public void register(Context context) {
        IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        context.registerReceiver(this, filter);
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    interface DownloadCallback {
        void downloadCompleted(long id);
    }
}
