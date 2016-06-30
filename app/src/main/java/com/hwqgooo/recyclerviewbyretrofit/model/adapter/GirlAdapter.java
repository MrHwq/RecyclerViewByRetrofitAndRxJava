package com.hwqgooo.recyclerviewbyretrofit.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwqgooo.recyclerviewbyretrofit.R;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.Girl;
import com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview.BaseRecyclerViewAdapter;
import com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview.ViewHolderInject;

import butterknife.BindView;

/**
 * Created by weiqiang on 2016/6/11.
 */
public class GirlAdapter extends BaseRecyclerViewAdapter<Girl> {
    public GirlAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolderInject<Girl> getNewHolder(LayoutInflater layoutInflater, ViewGroup parent,
                                               int viewType) {
        return new GirlHolder(layoutInflater.inflate(R.layout.girllayout, parent, false));
    }

    public class GirlHolder extends ViewHolderInject<Girl> {
        public Girl girl;
        int widthPixels;
        int heightPixels;
        @BindView(R.id.iv_girl)
        ImageView imageView;
        @BindView(R.id.tv_date)
        TextView textView;

        public GirlHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.iv_girl);
            textView = (TextView) itemView.findViewById(R.id.tv_date);
//            float scale = (float) (Math.random() + 1);
//            while (scale > 1.6 || scale < 1.1) {
//                scale = (float) (Math.random() + 1);
//            }
            float scale = (float) 1.5;
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            widthPixels = context.getResources().getDisplayMetrics().widthPixels;
            params.height = (int) (widthPixels * scale * 0.448);
            heightPixels = params.height;
            imageView.setLayoutParams(params);
        }

        @Override
        public void loadData(final Girl girl, int position) {
            this.girl = girl;
//            Glide.with(context).load(girl.getUrl()).centerCrop().into(imageView);
//            Picasso.with(context)
//                    .load(girl.getUrl())
//                    .resize(widthPixels, heightPixels)
//                    .centerCrop()
//                    .into(imageView);
//            Glide.with(context)
//                    .load(girl.getUrl())
//                    .override(widthPixels, heightPixels)
//                    .into(imageView);
            Glide.with(context).load(girl.getUrl()).fitCenter().into(imageView);
            textView.setText(girl.getDesc());
        }
    }
}
