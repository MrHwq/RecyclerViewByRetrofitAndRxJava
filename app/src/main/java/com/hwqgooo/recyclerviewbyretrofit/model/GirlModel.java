package com.hwqgooo.recyclerviewbyretrofit.model;

import android.content.Context;

/**
 * Created by weiqiang on 2016/6/10.
 */
public class GirlModel implements IGirlModel {
    final Context context;

    private GirlModel(Builder builder) {
        this.context = builder.context;
    }

    public static class Builder {
        public Context context;

        public Builder(Context context) {
            this.context = context;
        }

        public GirlModel build() {
            return new GirlModel(this);
        }
    }
}
