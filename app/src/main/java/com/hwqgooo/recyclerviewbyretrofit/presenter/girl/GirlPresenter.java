package com.hwqgooo.recyclerviewbyretrofit.presenter.girl;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.hwqgooo.recyclerviewbyretrofit.model.GirlModel;
import com.hwqgooo.recyclerviewbyretrofit.model.IGirlModel;
import com.hwqgooo.recyclerviewbyretrofit.model.IGirlService;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.Girl;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.GirlData;
import com.hwqgooo.recyclerviewbyretrofit.utils.rxutils.RxBus;
import com.hwqgooo.recyclerviewbyretrofit.view.IGirlView;

import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by weiqiang on 2016/6/10.
 */
public class GirlPresenter implements IGirlPresenter {
    final String TAG = getClass().getSimpleName();
    final String baseUrl = "http://gank.io/api/";
    final List<Girl> girlLists = new LinkedList<>();
    Context context;
    IGirlView girlView;
    IGirlModel girlModel;
    Retrofit mRetrofit;
    IGirlService girlService;
    int page = 0;
    CompositeSubscription compositeSubscription;

    public GirlPresenter(Context context, IGirlView view) {
        this.girlView = view;
        this.context = context;
        girlModel = new GirlModel.Builder(context).build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        girlService = mRetrofit.create(IGirlService.class);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void create() {
        compositeSubscription.add(Observable
                .create(new Observable.OnSubscribe<Void>() {
                    @Override
                    public void call(Subscriber<? super Void> subscriber) {
                        Log.d(TAG, "create call: doBackground");
                        if (girlView != null) {
                            girlView.doBackground();
                            subscriber.onNext(null);
                            subscriber.onCompleted();
                        } else {
                            subscriber.onError(new Throwable("View is null"));
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        if (girlView != null) {
                            girlView.onPostExecute();
                        }
                    }
                }));
    }

    @Override
    public void destory() {
        compositeSubscription.unsubscribe();
        girlView = null;
    }

    @Override
    public void restore() {
        Log.d(TAG, "restore: ");
        if (!girlLists.isEmpty()) {
            Log.d(TAG, "restore: is not empty");
            RxBus.getInstance().post(girlLists.get((int) (Math.random() * girlLists.size()))
                    .getUrl());
            girlView.getAdapter().setData(girlLists);
        }
    }

    @Override
    public void refresh() {
        Log.d(TAG, "refresh: ==========");
        page = 0;
        girlLists.clear();
        final Observable<GirlData> observable = girlService.getGirls(page);
        compositeSubscription.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<GirlData, GirlData>() {
                    @Override
                    public GirlData call(GirlData girlData) {
                        girlView.getAdapter().removeAllData();
                        return girlData;
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<GirlData, List<Girl>>() {
                    @Override
                    public List<Girl> call(GirlData girlData) {
                        if (girlLists == null) {
                            Log.d(TAG, "call: lists is null");
                        }
                        girlLists.addAll(girlData.getGirls());
                        return girlData.getGirls();
                    }
                })
                .flatMap(new Func1<List<Girl>, Observable<Girl>>() {
                    @Override
                    public Observable<Girl> call(List<Girl> girls) {
                        RxBus.getInstance().post(girls.get((int) (Math.random() * girls.size()))
                                .getUrl());
                        return Observable.from(girls);
                    }
                })
                .filter(new Func1<Girl, Boolean>() {
                    @Override
                    public Boolean call(Girl girl) {
                        return girl.getDesc().indexOf(".jpg") >= 0 ? Boolean.TRUE : Boolean.FALSE;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Girl>() {
                    @Override
                    public void onCompleted() {
                        girlView.stopRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        girlView.stopRefresh();
                        if (e instanceof SocketTimeoutException) {
                            girlView.showSnackBar("网络超时")
                                    .setAction("重试", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            refresh();
                                        }
                                    })
                                    .show();
                        } else {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(Girl girl) {
                        girlView.getAdapter().addData(girl);
                    }
                }));
    }

    @Override
    public void loadMore() {
        ++page;
        Log.d(TAG, "loadMore: " + page);
        final Observable<GirlData> observable = girlService.getGirls(page);
        compositeSubscription.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<GirlData, List<Girl>>() {
                    @Override
                    public List<Girl> call(GirlData girlData) {
                        girlLists.addAll(girlData.getGirls());
                        return girlData.getGirls();
                    }
                })
                .subscribe(new Subscriber<List<Girl>>() {
                    @Override
                    public void onCompleted() {
                        girlView.stopRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        girlView.stopRefresh();
                    }

                    @Override
                    public void onNext(List<Girl> girls) {
                        RxBus.getInstance().post(girls.get((int) (Math.random() * girls.size()))
                                .getUrl());
                        girlView.getAdapter().addData(girls);
                    }
                }));
    }
}
