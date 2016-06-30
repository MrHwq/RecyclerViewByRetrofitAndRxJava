package com.hwqgooo.recyclerviewbyretrofit.model.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwqgooo.recyclerviewbyretrofit.R;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.Gank;
import com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview.BaseRecyclerViewAdapter;
import com.hwqgooo.recyclerviewbyretrofit.utils.recyclerview.ViewHolderInject;

import de.hdodenhof.circleimageview.CircleImageView;
import me.gujun.android.taggroup.TagGroup;

/**
 * Created by weiqiang on 2016/6/26.
 */
public class GankAdapter extends BaseRecyclerViewAdapter<Gank> {
    public GankAdapter(Context context) {
        super(context);
    }

    @Override
    public ViewHolderInject<Gank> getNewHolder(LayoutInflater layoutInflater, ViewGroup parent,
                                               int viewType) {
        return new GankHolder(layoutInflater.inflate(R.layout.ganklayout, parent, false));
    }

    public class GankHolder extends ViewHolderInject<Gank> {
        public Gank gank;
        CircleImageView imageView;
        TextView author;
        TextView title;
        TagGroup tagGroup;

        public GankHolder(View itemView) {
            super(itemView);
            imageView = (CircleImageView) itemView.findViewById(R.id.iv_author);
            author = (TextView) itemView.findViewById(R.id.tv_author);
            title = (TextView) itemView.findViewById(R.id.tv_title);
            tagGroup = (TagGroup) itemView.findViewById(R.id.tag_group);
        }

        @Override
        public void loadData(final Gank gank, int position) {
            this.gank = gank;
            author.setText(gank.getGankDetail().getWho() == null ? "无名好汉" : gank.getGankDetail()
                    .getWho());
            title.setText(gank.getGankDetail().getDesc());

            // 给每篇干货 设置标签
            setTag(gank.getGankDetail().getDesc(), gank.getGankDetail().getUrl());

            // 作者的头像
            setAvatar(gank);
        }

        public void setTag(String title, String url) {
            if (title.contains("源码解析") || title.contains("分析源代码") || title.contains("源代码分析")) {
                tagGroup.setTags(new String("源码解析"));
            } else if (url.contains("https://github.com/") && title.contains("项目")) {
                tagGroup.setTags(new String("开源项目"));
            } else if (url.contains("https://github.com/")) {
                tagGroup.setTags(new String("开源库"));
            } else if (url.contains("https://zhuanlan.zhihu.com/")) {
                tagGroup.setTags(new String("知乎专栏"));
            } else {
                tagGroup.setTags(new String("干货"));
            }
        }

        private void setAvatar(Gank wrapper) {
            imageView.setImageResource(android.R.color.transparent);
            if (wrapper.getAvatarUrl() == null) {
                final String url = wrapper.getGankDetail().getUrl();
                if (url.contains("https://www.sdk.cn/")) {
                    imageView.setImageResource(R.drawable.sdkcn_logo);
                } else {
                    imageView.setImageResource(R.drawable.ic_person_black_24dp);
                }
            } else {
                Glide.with(context).load(wrapper.getAvatarUrl()).into(imageView);
            }
        }
    }
}
