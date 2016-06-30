package com.hwqgooo.recyclerviewbyretrofit.model.bean;

/**
 * Created by weiqiang on 2016/6/26.
 */
public class Gank {
    GankDetail gankDetail;
    String avatarUrl;

    public Gank(GankDetail gankDetail, String avatarUrl) {
        this.gankDetail = gankDetail;
        this.avatarUrl = avatarUrl;
    }

    public GankDetail getGankDetail() {
        return gankDetail;
    }

    public void setGankDetail(GankDetail gankDetail) {
        this.gankDetail = gankDetail;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}
