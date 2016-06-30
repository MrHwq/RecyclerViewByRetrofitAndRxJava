package com.hwqgooo.recyclerviewbyretrofit.model.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by weiqiang on 2016/6/10.
 */
public class GirlData {

    /**
     * error : false
     * results : [{"_id":"56eb5db867765933d9b0a8fc",
     * "_ns":"ganhuo",
     * "createdAt":"2016-03-18T09:45:28.259Z",
     * "desc":"3.18",
     * "publishedAt":"2016-03-18T12:18:39.928Z",
     * "source":"chrome",
     * "type":"福利",
     * "url":"http://ww1.sinaimg.cn/large/7a8aed7bjw1f20ruz456sj20go0p0wi3.jpg",
     * "used":true,
     * "who":"张涵宇"},]
     */
    private boolean error;

    @SerializedName("results")
    private List<Girl> girls;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public List<Girl> getGirls() {
        return girls;
    }

    public void setGirls(List<Girl> girls) {
        this.girls = girls;
    }
}
