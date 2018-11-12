package com.wind.photopreview.event;

import com.wind.photopreview.PhotoPreview;

/**
 * Created by wind on 2018/11/12.
 */

public class PhotoRemoveEvent {

    private PhotoPreview photoPreview;
    public PhotoRemoveEvent(PhotoPreview photoPreview){
        this.photoPreview=photoPreview;
    }

    public PhotoPreview getPhotoPreview() {
        return photoPreview;
    }
}
