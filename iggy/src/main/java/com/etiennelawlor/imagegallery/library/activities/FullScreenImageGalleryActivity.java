package com.etiennelawlor.imagegallery.library.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.etiennelawlor.imagegallery.library.R;
import com.etiennelawlor.imagegallery.library.adapters.FullScreenImageGalleryAdapter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FullScreenImageGalleryActivity extends AppCompatActivity implements FullScreenImageGalleryAdapter.FullScreenImageLoader {

    // region Constants
    public static final String KEY_IMAGES = "KEY_IMAGES";
    public static final String KEY_POSITION = "KEY_POSITION";
    // endregion

    // region Views
    private Toolbar toolbar;
    private ViewPager viewPager;
    private ImageView imageView;
    private String imageUrl;
    // endregion

    // region Member Variables
    private List<String> images;
    private int position;
    private static FullScreenImageGalleryAdapter.FullScreenImageLoader fullScreenImageLoader;
    // endregion

    // region Listeners
    private final ViewPager.OnPageChangeListener viewPagerOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            if (viewPager != null) {
                viewPager.setCurrentItem(position);

                setActionBarTitle(position);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
    // endregion

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_full_screen_image_gallery);

        bindViews();

        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null)
            actionBar.setDisplayHomeAsUpEnabled(true);


        Intent intent = getIntent();
        if (intent != null) {
            Bundle extras = intent.getExtras();
            if (extras != null) {
                images = extras.getStringArrayList(KEY_IMAGES);
                position = extras.getInt(KEY_POSITION);
            }
        }

        setUpViewPager();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        removeListeners();
    }
    // endregion

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.full_screen_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if(item.getItemId() == R.id.download) {
//            downloadImage(imageUrl);
            fullScreenImageLoader.imageDownloadOption(imageUrl);
            return true;
        } else if(item.getItemId() == R.id.share_image) {
            if (imageView != null){
                fullScreenImageLoader.imageShareOption(getLocalBitmapUri(imageView));
                return true;
            }else{
                return false;
            }
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // region FullScreenImageGalleryAdapter.FullScreenImageLoader Methods
    @Override
    public void loadFullScreenImage(ImageView iv, String imageUrl, int width, LinearLayout bglinearLayout) {
        fullScreenImageLoader.loadFullScreenImage(iv, imageUrl, width, bglinearLayout);
        imageView = iv;
        imageUrl = imageUrl;
    }

    @Override
    public void imageShareOption(Uri image) {
        fullScreenImageLoader.imageShareOption(image);
    }

    @Override
    public void imageDownloadOption(String imageUrl) {
        fullScreenImageLoader.imageDownloadOption(imageUrl);
    }

    private void downloadImage(String url){

    }
    // endregion

    // region Helper Methods
    private void bindViews() {
        viewPager = (ViewPager) findViewById(R.id.vp);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void setUpViewPager() {
        ArrayList<String> imageList = new ArrayList<>();
        imageList.addAll(images);

        FullScreenImageGalleryAdapter fullScreenImageGalleryAdapter = new FullScreenImageGalleryAdapter(imageList);
        fullScreenImageGalleryAdapter.setFullScreenImageLoader(this);
        viewPager.setAdapter(fullScreenImageGalleryAdapter);
        viewPager.addOnPageChangeListener(viewPagerOnPageChangeListener);
        viewPager.setCurrentItem(position);

        setActionBarTitle(position);
    }

    // Returns the URI path to the Bitmap displayed in specified ImageView
    private Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable){
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            // Use methods on Context to access package-specific directories on external storage.
            // This way, you don't need to request external read/write permission.
            // See https://youtu.be/5xVh-7ywKpE?t=25m25s
            File file =  new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    private void setActionBarTitle(int position) {
        if (viewPager != null && images.size() > 1) {
            int totalPages = viewPager.getAdapter().getCount();

            ActionBar actionBar = getSupportActionBar();
            if(actionBar != null){
                actionBar.setTitle(String.format("%d/%d", (position + 1), totalPages));
            }
        }
    }

    private void removeListeners() {
        viewPager.removeOnPageChangeListener(viewPagerOnPageChangeListener);
    }

    public static void setFullScreenImageLoader(FullScreenImageGalleryAdapter.FullScreenImageLoader loader) {
        fullScreenImageLoader = loader;
    }
    // endregion
}
