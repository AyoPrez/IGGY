package com.ayoprez.iggy.library;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ayoprez.iggy.library.activities.FullScreenImageGalleryActivity;
import com.ayoprez.iggy.library.adapters.ImageGalleryAdapter;
import com.ayoprez.iggy.library.util.ImageGalleryUtils;
import com.ayoprez.iggy.library.view.GridSpacesItemDecoration;

import java.util.ArrayList;

import static com.ayoprez.iggy.library.activities.FullScreenImageGalleryActivity.KEY_DOWNLOAD_BUTTON;
import static com.ayoprez.iggy.library.activities.FullScreenImageGalleryActivity.KEY_SHARE_BUTTON;

/**
 * Created by etiennelawlor on 6/10/16.
 */

public class ImageGalleryFragment extends Fragment implements ImageGalleryAdapter.OnImageClickListener, ImageGalleryAdapter.ImageThumbnailLoader {

    // region Constants
    public static final String KEY_IMAGES = "KEY_IMAGES";
    public static final String KEY_POSITION = "KEY_POSITION";
    public static final String KEY_TITLE = "KEY_TITLE";
    // endregion

    // region Views
    private RecyclerView recyclerView;
    // endregion

    // region Member Variables
    private ArrayList<String> images;
    private String title;
    private GridSpacesItemDecoration gridSpacesItemDecoration;
    private static ImageGalleryAdapter.ImageThumbnailLoader imageThumbnailLoader;
    private boolean downloadButton = false;
    private boolean shareButton = false;
    // endregion

    // region Constructors
    public ImageGalleryFragment() {
    }
    // endregion

    // region Factory Methods
    public static ImageGalleryFragment newInstance(Bundle extras) {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    public static ImageGalleryFragment newInstance() {
        ImageGalleryFragment fragment = new ImageGalleryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    // endregion

    // region Lifecycle Methods
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            images = getArguments().getStringArrayList(KEY_IMAGES);
            title = getArguments().getString(KEY_TITLE);
            downloadButton = getArguments().getBoolean(KEY_DOWNLOAD_BUTTON);
            shareButton = getArguments().getBoolean(KEY_SHARE_BUTTON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_image_gallery, container, false);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindViews();

        setUpRecyclerView();

    }
    // endregion

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        recyclerView.removeItemDecoration(gridSpacesItemDecoration);
        setUpRecyclerView();
    }

    // region ImageGalleryAdapter.OnImageClickListener Methods
    @Override
    public void onImageClick(int position) {
        Intent intent = new Intent(getContext(), FullScreenImageGalleryActivity.class);
        Bundle bundle = new Bundle();
        bundle.putStringArrayList(FullScreenImageGalleryActivity.KEY_IMAGES, images);
        bundle.putInt(FullScreenImageGalleryActivity.KEY_POSITION, position);
        bundle.putBoolean(KEY_SHARE_BUTTON, shareButton);
        bundle.putBoolean(KEY_DOWNLOAD_BUTTON, downloadButton);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    // endregion

    // region ImageGalleryAdapter.ImageThumbnailLoader Methods
    @Override
    public void loadImageThumbnail(ImageView iv, String imageUrl, int dimension) {
        imageThumbnailLoader.loadImageThumbnail(iv, imageUrl, dimension);
    }
    // endregion

    // region Helper Methods
    private void bindViews() {
        recyclerView = (RecyclerView) getActivity().findViewById(R.id.rv);
    }

    private void setUpRecyclerView() {
        int numOfColumns;
        if (ImageGalleryUtils.isInLandscapeMode(getActivity())) {
            numOfColumns = 4;
        } else {
            numOfColumns = 3;
        }

        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numOfColumns));
        gridSpacesItemDecoration = new GridSpacesItemDecoration(ImageGalleryUtils.dp2px(getActivity(), 3), numOfColumns);
        recyclerView.addItemDecoration(gridSpacesItemDecoration);
        ImageGalleryAdapter imageGalleryAdapter = new ImageGalleryAdapter(images);
        imageGalleryAdapter.setOnImageClickListener(this);
        imageGalleryAdapter.setImageThumbnailLoader(this);

        recyclerView.setAdapter(imageGalleryAdapter);
    }

    public static void setImageThumbnailLoader(ImageGalleryAdapter.ImageThumbnailLoader loader) {
        imageThumbnailLoader = loader;
    }
    // endregion
}
