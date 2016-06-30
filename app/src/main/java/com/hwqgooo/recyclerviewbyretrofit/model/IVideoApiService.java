package com.hwqgooo.recyclerviewbyretrofit.model;

import com.hwqgooo.recyclerviewbyretrofit.model.bean.VideoData;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import rx.Observable;

/**
 * Created by weiqiang on 2016/6/26.
 */
public interface IVideoApiService {
    @Headers({
            "X-Bmob-Application-Id: 82c75d3435a1a68a58df1574ba7e235a",
            "X-Bmob-REST-API-Key: f72a1c058aee798da27a3e39085e4155",
            "Content-Type: application/json"
    })
    @GET("Video")
    Observable<VideoData> getVideoList();
}
