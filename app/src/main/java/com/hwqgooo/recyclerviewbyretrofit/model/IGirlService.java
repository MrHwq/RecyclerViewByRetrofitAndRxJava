package com.hwqgooo.recyclerviewbyretrofit.model;

import com.hwqgooo.recyclerviewbyretrofit.model.bean.GankData;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.GirlData;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by weiqiang on 2016/6/10.
 */
public interface IGirlService {
    @GET("data/福利/10/{page}")
//    Call<GirlData> getGirls(@Path("page") int page); //单纯retrofit
    Observable<GirlData> getGirls(@Path("page") int page);//retrofit加rxjava

    @GET("data/Android/10/{page}")
    Observable<GankData> getGankDatas(@Path("page") int page);
}
