package com.hwqgooo.recyclerviewbyretrofit.model.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by weiqiang on 2016/6/10.
 */
public class Girl implements Parcelable {
    public static final Creator<Girl> CREATOR = new Creator<Girl>() {
        @Override
        public Girl createFromParcel(Parcel in) {
            return new Girl(in);
        }

        @Override
        public Girl[] newArray(int size) {
            return new Girl[size];
        }
    };
    private String desc;
    private String url;

    public Girl(String desc, String url) {
        this.desc = desc;
        this.url = url;
    }

    protected Girl(Parcel in) {
        desc = in.readString();
        url = in.readString();
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(desc);
        parcel.writeString(url);
    }
}
