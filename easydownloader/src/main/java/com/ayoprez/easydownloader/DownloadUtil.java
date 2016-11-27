package com.ayoprez.easydownloader;

import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;

/**
 * Created by ayo on 26.11.16.
 */

public class DownloadUtil implements DownloaderBroadcastReceiver.DownloadCallback {
    private final Listener listener;
    private final DownloadManager downloadManager;

    private DownloaderBroadcastReceiver receiver = null;

    private long downloadId = -1;

    static DownloadUtil newInstance(Listener listener) {
        Context context = listener.getContext();
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return new DownloadUtil(downloadManager, listener);
    }

    public DownloadUtil(DownloadManager downloadManager, Listener listener) {
        this.downloadManager = downloadManager;
        this.listener = listener;
    }

    public void download(Uri uri) {
        if (!isDownloading()) {
            register();
            DownloadManager.Request request = new DownloadManager.Request(uri);
            request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, uri.getPath() +".jpg");
            downloadId = downloadManager.enqueue(request);
        }
    }

    public boolean isDownloading() {
        return downloadId >= 0;
    }

    private void register() {
        if (receiver == null && !isDownloading()) {
            receiver = new DownloaderBroadcastReceiver();
            receiver.setCallback(this);
            receiver.register(listener.getContext());
        }
    }

    @Override
    public void downloadCompleted(long completedDownloadId) {
        if (downloadId == completedDownloadId) {
            DownloadManager.Query query = new DownloadManager.Query();
            query.setFilterById(downloadId);
            downloadId = -1;
            unregister();
            Cursor cursor = downloadManager.query(query);
            while (cursor.moveToNext()) {
                getFileInfo(cursor);
            }
            cursor.close();
        }
    }

    public void unregister() {
        if (receiver != null) {
            receiver.unregister(listener.getContext());
        }
        receiver = null;
    }

    private void getFileInfo(Cursor cursor) {
        int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
        if (status == DownloadManager.STATUS_SUCCESSFUL) {
            Long id = cursor.getLong(cursor.getColumnIndex(DownloadManager.COLUMN_ID));
            Uri uri = downloadManager.getUriForDownloadedFile(id);
            String mimeType = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_MEDIA_TYPE));
            listener.fileDownloaded(uri, mimeType);
        }
    }

    void cancel() {
        if (isDownloading()) {
            downloadManager.remove(downloadId);
            downloadId = -1;
            unregister();
        }
    }

    interface Listener {
        void fileDownloaded(Uri uri, String mimeType);
        Context getContext();
    }
}
