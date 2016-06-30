package com.hwqgooo.recyclerviewbyretrofit.view;

import com.hwqgooo.recyclerviewbyretrofit.model.bean.Girl;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by weiqiang on 2016/6/13.
 */
public interface IGirlPhotoView {
    PhotoView getImageView();

    void saveImage();

    Girl getGirl();
}
