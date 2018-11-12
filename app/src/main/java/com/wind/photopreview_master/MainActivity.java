package com.wind.photopreview_master;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.wind.photopreview.PhotoPreview;
import com.wind.photopreview.PhotoPreviewActivity;
import com.wind.photopreview.PhotoPreviewWithLocalDelFragment;

import java.util.ArrayList;

/**
 * Created by wind on 2018/11/9.
 */

public class MainActivity extends FragmentActivity {

    private ArrayList<PhotoPreview> previews = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        PhotoPreview photoPreview=new PhotoPreview();
        photoPreview.setPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1542356648&di=ef054ca2c49310bf5615a977105df96d&imgtype=jpg&er=1&src=http%3A%2F%2Fs13.sinaimg.cn%2Fmw690%2F0041rhFZzy7o022uw1Kdc%26amp%3B690");

        PhotoPreview photoPreview2=new PhotoPreview();
        photoPreview2.setPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541761930003&di=01577ddeebe17e57a8bbf0990f202506&imgtype=0&src=http%3A%2F%2Fs6.sinaimg.cn%2Fmw690%2F0041rhFZzy7nYF78gOF15%26690");
        PhotoPreview photoPreview3=new PhotoPreview();
        photoPreview3.setPath("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1541761930003&di=5234494e60363a477b6e0237c5177aa7&imgtype=0&src=http%3A%2F%2Fwww.xgsy188.com%2Fuploadfile%2F20131140433138440.jpg");

        previews.add(photoPreview);
        previews.add(photoPreview2);
        previews.add(photoPreview3);
    }

    public void preview(View view) {


        Intent intent = new Intent(this, PhotoPreviewActivity.class);
        intent.putExtra(PhotoPreviewActivity.EXTRA_KEY_PHOTOS, previews);
        intent.putExtra(PhotoPreviewActivity.EXTRA_KEY_FRAGMENT_NAME, PhotoPreviewWithLocalDelFragment.class.getCanonicalName());
        intent.putExtra(PhotoPreviewActivity.EXTRA_KEY_START_POSITION, 0);

       /* Pair pair1 = Pair.create(shareElement, ViewCompat.getTransitionName(shareElement));
        ActivityOptionsCompat options = ActivityOptionsCompat
                .makeSceneTransitionAnimation(getActivity(), pair1);
        ActivityCompat.startActivity(getActivity(), intent, options.toBundle());*/

        startActivity(intent);
    }
}
