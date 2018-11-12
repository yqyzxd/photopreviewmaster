package com.wind.photopreview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.wind.photopreview.event.PhotoRemoveEvent;

import org.greenrobot.eventbus.EventBus;

public class PhotoPreviewWithLocalDelFragment extends PhotoPreviewFragment {


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_photo_preview_with_local_del;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.iv_del).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new PhotoRemoveEvent(mPhoto));
            }
        });
    }

    public static Fragment newInstance(int position, int startingPosition, PhotoPreview photo) {
        Bundle args = new Bundle();
        args.putInt(ARG_PHOTO_POSITION, position);
        args.putInt(ARG_STARTING_PHOTO_POSITION, startingPosition);
        args.putParcelable(ARGS_KEY_PHOTO, photo);
        PhotoPreviewWithLocalDelFragment fragment = new PhotoPreviewWithLocalDelFragment();
        fragment.setArguments(args);
        return fragment;
    }


}