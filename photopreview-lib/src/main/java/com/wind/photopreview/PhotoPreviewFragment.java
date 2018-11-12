package com.wind.photopreview;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.github.chrisbanes.photoview.OnPhotoTapListener;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;


public class PhotoPreviewFragment extends Fragment {

    protected static final String ARG_PHOTO_POSITION = "args_photo_position";
    protected static final String ARG_STARTING_PHOTO_POSITION = "args_starting_photo_position";
    public static final String ARGS_KEY_PHOTO = "args_photo";

    protected PhotoView mPhotoView;
    RequestOptions options = new RequestOptions();


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayoutRes(), container, false);
    }

    public int getLayoutRes() {
        return R.layout.fragment_photo_preview;

    }

    protected int mStartingPosition, mPhotoPosition;
    private boolean mIsTransitioning;
    protected PhotoPreview mPhoto;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStartingPosition = getArguments().getInt(ARG_STARTING_PHOTO_POSITION);
        mPhotoPosition = getArguments().getInt(ARG_PHOTO_POSITION);
        mIsTransitioning = savedInstanceState == null && mStartingPosition == mPhotoPosition;
        mPhoto = getArguments().getParcelable(ARGS_KEY_PHOTO);

        options
                .placeholder(R.drawable.placeholder_bg)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true);
    }

    public ImageView getPreviewView() {
        return mPhotoView;
    }

    private PhotoViewAttacher attacher;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView(view);

        if (TextUtils.isEmpty(mPhoto.getPath())) {
            ViewCompat.setTransitionName(mPhotoView, mPhoto.getResId() + "");
            inflateImage(mPhoto.getResId());

        } else {
            ViewCompat.setTransitionName(mPhotoView, mPhoto.getPath());
            inflateImage(mPhoto.getPath());
        }


    }

    private void initView(View view) {
        mPhotoView = view.findViewById(R.id.photo_view);
        attacher = new PhotoViewAttacher(mPhotoView);
        attacher.setOnPhotoTapListener(new OnPhotoTapListener() {
            @Override
            public void onPhotoTap(ImageView view, float x, float y) {
                getActivity().finish();
            }

        });
    }

    public static Fragment newInstance(int position, int startingPosition, PhotoPreview photo) {
        Bundle args = new Bundle();
        args.putInt(ARG_PHOTO_POSITION, position);
        args.putInt(ARG_STARTING_PHOTO_POSITION, startingPosition);
        args.putParcelable(ARGS_KEY_PHOTO, photo);
        PhotoPreviewFragment fragment = new PhotoPreviewFragment();
        fragment.setArguments(args);
        return fragment;
    }


    private void startPostponedEnterTransition(final View view) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                ActivityCompat.startPostponedEnterTransition(getActivity());
                return true;
            }
        });
    }



    private void inflateImage(Object path) {

        Glide.with(getActivity())
                .load(path)
                .apply(options)
                .into(new ImageViewTarget<Drawable>(mPhotoView) {

                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        mPhotoView.setImageDrawable(resource);
                        attacher.update();
                        if (mStartingPosition == mPhotoPosition) {
                            startPostponedEnterTransition(mPhotoView);
                        }
                    }
                });
    }

}