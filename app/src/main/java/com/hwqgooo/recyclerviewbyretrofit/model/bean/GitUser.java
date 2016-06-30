package com.hwqgooo.recyclerviewbyretrofit.model.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by weiqiang on 2016/6/26.
 */
public class GitUser {
    @SerializedName("avatar_url")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
