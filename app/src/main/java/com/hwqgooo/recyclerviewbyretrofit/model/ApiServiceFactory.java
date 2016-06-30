package com.hwqgooo.recyclerviewbyretrofit.model;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by weiqiang on 2016/6/26.
 */
public class ApiServiceFactory {
    private static IGirlService gankioService;
    private static IGitHubService gitHubService;
    private static IVideoApiService videoApiService;

    public static IGirlService buildGankioService() {
        if (gankioService == null) {
            Retrofit retfGank = new Retrofit.Builder()
                    .baseUrl("http://gank.io/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            gankioService = retfGank.create(IGirlService.class);
        }
        return gankioService;
    }

    public static IGitHubService buildGitHubService() {
        if (gitHubService == null) {
            Retrofit retroGithub = new Retrofit.Builder().baseUrl("https://api.github.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            gitHubService = retroGithub.create(IGitHubService.class);
        }
        return gitHubService;
    }

    public static IVideoApiService buildVideoApiService() {
        if (videoApiService == null) {
            Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.bmob.cn/1/classes/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();
            videoApiService = retrofit.create(IVideoApiService.class);
        }
        return videoApiService;
    }
}
