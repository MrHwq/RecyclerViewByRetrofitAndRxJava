package com.hwqgooo.recyclerviewbyretrofit.ui.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hwqgooo.recyclerviewbyretrofit.R;
import com.hwqgooo.recyclerviewbyretrofit.ui.TabsPagerAdapter;
import com.hwqgooo.recyclerviewbyretrofit.ui.fragment.GankFragment;
import com.hwqgooo.recyclerviewbyretrofit.ui.fragment.GirlFragment;
import com.hwqgooo.recyclerviewbyretrofit.ui.fragment.HfsFragment;
import com.hwqgooo.recyclerviewbyretrofit.utils.rxutils.RxBus;

import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class MainActivity extends AppCompatActivity {
    final String TAG = getClass().getSimpleName();
    final int EXPANDED = 0;
    final int COLLAPSED = 1;
    final int INTERNEDIATE = 2;
    Unbinder unbinder;
    Subscription subscription;
    @BindView(R.id.tablayout)
    TabLayout tablayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.content_bar)
    AppBarLayout contentBar;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbarImage)
    ImageView toolbarImage;
    @BindView(R.id.colayout)
    CoordinatorLayout colayout;
    @BindView(R.id.collapsingtoolbar)
    CollapsingToolbarLayout collapsingtoolbar;
    int state = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        unbinder = ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("R&R");
        initTab();
        initTextColor();
        initAppBarLayout();
    }

    void initTab() {
        final List<Fragment> lists = new LinkedList<>();

        lists.add(new GankFragment());
        lists.add(new HfsFragment());
        lists.add(new GirlFragment());

        TabsPagerAdapter tabs = new TabsPagerAdapter(getSupportFragmentManager(), lists);
        viewpager.setAdapter(tabs);
        tablayout.setupWithViewPager(viewpager);
    }

    void initTextColor() {
        subscription = RxBus.getInstance()
                .toObserverable(String.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d("hwqhwq", s);
                        if (!TextUtils.isEmpty(s)) {
                            // Caused by: java.lang.IllegalArgumentException: You must
                            // call this method on the main thread
                            Glide.with(MainActivity.this)
                                    .load(s)
                                    .asBitmap()
                                    .centerCrop()
                                    .listener(new RequestListener<String, Bitmap>() {
                                        @Override
                                        public boolean onException(Exception e, String
                                                model, Target<Bitmap> target, boolean
                                                                           isFirstResource) {
                                            Log.d(TAG, "onException: ");
                                            collapsingtoolbar.setExpandedTitleColor(
                                                    MainActivity.this.getResources()
                                                            .getColor(android.R.color.white));
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(Bitmap resource,
                                                                       String model,
                                                                       Target<Bitmap> target,
                                                                       boolean isFromMemoryCache,
                                                                       boolean isFirstResource) {
                                            Palette.from(resource).generate(new Palette
                                                    .PaletteAsyncListener() {
                                                @Override
                                                public void onGenerated(Palette palette) {
                                                    Palette.Swatch vibrant = palette
                                                            .getMutedSwatch();
                                                    //有活力
                                                    if (vibrant != null) {
                                                        Log.d(TAG, "onGenerated: vibrant " +
                                                                "not null");
                                                        collapsingtoolbar
                                                                .setExpandedTitleColor
                                                                        (vibrant.getRgb());
//                                                        toolbar.setTitleTextColor(vibrant
//                                                                .getRgb());
//                                                        toolbar.setSubtitleTextColor(vibrant
//                                                                .getRgb());
                                                        tablayout.setTabTextColors(vibrant
                                                                        .getTitleTextColor(),
                                                                vibrant.getRgb());
                                                    } else {
                                                        Log.d(TAG, "onGenerated: vibrant " +
                                                                " null");
                                                    }
                                                }
                                            });
                                            Log.d(TAG, "onResourceReady: ");
                                            return false;
                                        }
                                    })
                                    .into(toolbarImage);
                        } else {
                            toolbarImage.setImageDrawable(null);
                        }
                    }
                });
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.d(TAG, "onRestoreInstanceState: ");
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle
            persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);
        Log.d(TAG, "onRestoreInstanceState: ");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState: ");
    }

    void initAppBarLayout() {
        contentBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    if (state != EXPANDED) {
                        state = EXPANDED;
                        collapsingtoolbar.setTitle("完全展开");
                    }
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    if (state != COLLAPSED) {
                        state = COLLAPSED;
                        collapsingtoolbar.setTitle("完全收缩");
                    }
                } else {
                    if (state != INTERNEDIATE) {
                        if (state == COLLAPSED) {
                            //折叠变成隐藏
                        }
                        collapsingtoolbar.setTitle("中间态");
                        state = INTERNEDIATE;
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
