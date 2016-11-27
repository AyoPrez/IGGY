package com.etiennelawlor.imagegallery.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ayoprez.iggy.library.ImageGalleryFragment;
import com.ayoprez.iggy.library.PaletteHelper;
import com.ayoprez.iggy.library.activities.FullScreenImageGalleryActivity;
import com.ayoprez.iggy.library.activities.ImageGalleryActivity;
import com.ayoprez.iggy.library.adapters.FullScreenImageGalleryAdapter;
import com.ayoprez.iggy.library.adapters.ImageGalleryAdapter;
import com.ayoprez.iggy.library.enums.PaletteColorType;
import com.etiennelawlor.imagegallery.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by etiennelawlor on 8/20/15.
 */
public class MainActivity extends AppCompatActivity implements ImageGalleryAdapter.ImageThumbnailLoader, FullScreenImageGalleryAdapter.FullScreenImageLoader {

    // region Listeners
    @OnClick(R.id.image_gallery_activity_btn)
    public void onImageGalleryActivityButtonClicked() {
        Intent intent = new Intent(MainActivity.this, ImageGalleryActivity.class);

        String[] images = getResources().getStringArray(R.array.unsplash_images);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ImageGalleryActivity.KEY_IMAGES, new ArrayList<>(Arrays.asList(images)));
        bundle.putString(ImageGalleryActivity.KEY_TITLE, "Unsplash Images");
        intent.putExtras(bundle);

        startActivity(intent);
    }

    @OnClick(R.id.image_gallery_fragment_btn)
    public void onImageGalleryFragmentButtonClicked() {
        Intent intent = new Intent(MainActivity.this, HostImageGalleryActivity.class);

        String[] images = getResources().getStringArray(R.array.unsplash_images);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(ImageGalleryFragment.KEY_IMAGES, new ArrayList<>(Arrays.asList(images)));
        bundle.putString(ImageGalleryActivity.KEY_TITLE, "Unsplash Images");
        intent.putExtras(bundle);

        startActivity(intent);
    }
    // endregion

    // region Lifecycle Methods
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        ImageGalleryActivity.setImageThumbnailLoader(this);
        ImageGalleryFragment.setImageThumbnailLoader(this);
        FullScreenImageGalleryActivity.setFullScreenImageLoader(this);
    }
    // endregion

    // region ImageGalleryAdapter.ImageThumbnailLoader Methods
    @Override
    public void loadImageThumbnail(ImageView iv, String imageUrl, int dimension) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(iv.getContext())
                    .load(imageUrl)
                    .resize(dimension, dimension)
                    .centerCrop()
                    .into(iv);
        } else {
            iv.setImageDrawable(null);
        }
    }
    // endregion

    // region FullScreenImageGalleryAdapter.FullScreenImageLoader
    @Override
    public void loadFullScreenImage(final ImageView iv, String imageUrl, int width, final LinearLayout bgLinearLayout) {
        if (!TextUtils.isEmpty(imageUrl)) {
            Picasso.with(iv.getContext())
                    .load(imageUrl)
                    .resize(width, 0)
                    .into(iv, new Callback() {
                        @Override
                        public void onSuccess() {
                            Bitmap bitmap = ((BitmapDrawable) iv.getDrawable()).getBitmap();

                            PaletteHelper.applyPalette(bitmap, PaletteColorType.VIBRANT, bgLinearLayout);
                        }

                        @Override
                        public void onError() {

                        }
                    });
        } else {
            iv.setImageDrawable(null);
        }
    }
    // endregion

}
