package com.hwqgooo.recyclerviewbyretrofit.model;

import retrofit2.http.GET;
import rx.Observable;

/**
 * Created by weiqiang on 2016/6/13.
 */
public interface IHfsService {
    @GET("./")
        // Call<String> getGirls();//单纯retrofit
    Observable<String> getGirls();//retrofit加rxjava
}
