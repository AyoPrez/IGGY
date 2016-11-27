package com.ayoprez.easydownloader;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

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
        downloadUtil.download(uriString);
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