package com.hwqgooo.recyclerviewbyretrofit.model;

import com.hwqgooo.recyclerviewbyretrofit.model.bean.GitUser;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by weiqiang on 2016/6/26.
 */
public interface IGitHubService {
    @GET("users/{user}")
    public Observable<GitUser> getAvatar(@Path("user") String user, @Query("client_id") String
            id, @Query("client_secret") String secret);
}
