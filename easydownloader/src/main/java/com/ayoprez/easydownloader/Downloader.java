package com.ayoprez.easydownloader;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;

public class Downloader implements DownloadUtil.Listener{

    private Context context;
    private DownloadUtil downloadUtil;

    public Downloader(Context context){
        this.context = context;
        downloadUtil = DownloadUtil.newInstance(this);
    }

    public void cancel() {
        downloadUtil.cancel();
    }

    public void unregister(){
        downloadUtil.unregister();
    }

    public void download(String uriString){
        Uri uri = Uri.parse(uriString);
        download(uri);
    }

    public void download(Uri uriString) {
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            downloadUtil.download(uriString);
        } else {
            ActivityCompat.requestPermissions((Activity)context,  new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 0);
        }
    }

    @Override
    public void fileDownloaded(Uri uri, String mimeType) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mimeType);
        context.startActivity(intent);
    }

    @Override
    public Context getContext() {
        return context.getApplicationContext();
    }
}