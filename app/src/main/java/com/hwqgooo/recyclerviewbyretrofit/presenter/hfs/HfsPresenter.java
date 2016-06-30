package com.hwqgooo.recyclerviewbyretrofit.presenter.hfs;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.hwqgooo.recyclerviewbyretrofit.model.GirlModel;
import com.hwqgooo.recyclerviewbyretrofit.model.IGirlModel;
import com.hwqgooo.recyclerviewbyretrofit.model.IHfsService;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.Girl;
import com.hwqgooo.recyclerviewbyretrofit.utils.rxutils.RxBus;
import com.hwqgooo.recyclerviewbyretrofit.view.IGirlView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.SocketTimeoutException;
import java.util.LinkedList;
import java.util.List;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by weiqiang on 2016/6/13.
 */
public class HfsPresenter implements IHfsPresenter {
    final String TAG = getClass().getSimpleName();
    final String baseUrl = "http://192.168.1.100/wallpaper//./";
    Context context;
    IGirlView girlView;
    IGirlModel girlModel;
    Retrofit mRetrofit;
    IHfsService girlService;
    List<Girl> mGirlData = new LinkedList<Girl>();
    CompositeSubscription compositeSubscription;
    int pageSize = 10;
    int current = 0;

    public HfsPresenter(final Context context, IGirlView view) {
        this.girlView = view;
        this.context = context;
        girlModel = new GirlModel.Builder(context).build();

        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        girlService = mRetrofit.create(IHfsService.class);
        compositeSubscription = new CompositeSubscription();
    }

    @Override
    public void create() {
        compositeSubscription.add(Observable
                .create(new Observable.OnSubscribe<Void>() {
                    @Override
                    public void call(Subscriber<? super Void> subscriber) {
                        if (girlView != null) {
                            girlView.doBackground();
                        }
                        subscriber.onNext(null);
                        subscriber.onCompleted();
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
        girlView = null;
        compositeSubscription.unsubscribe();
    }

    @Override
    public void restore() {
        if (!mGirlData.isEmpty()) {
            girlView.getAdapter().setData(mGirlData);
        }
    }

    @Override
    public void refresh() {
        Log.d(TAG, "refresh: ==========");
        compositeSubscription.add(girlService
                .getGirls()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mGirlData.clear();
                    }
                })
                .observeOn(Schedulers.io())
                .map(new Func1<String, List<Girl>>() {
                    @Override
                    public List<Girl> call(String s) {
                        Document doc = Jsoup.parse(s);
                        Elements table = doc.select("tr");
                        if (table.size() == 0) {
                            return null;
                        }
                        for (Element link : table) {
                            //第一项td
                            Element element = link.child(0);
                            //获取 <a href="">
                            Elements href = element.select("a");
                            String desc = href.attr("href");
                            if (desc.endsWith(".jpg")) {
                                mGirlData.add(new Girl(desc, baseUrl + desc));
                            }
                        }
                        return mGirlData;
                    }
                })
                .observeOn(Schedulers.io())
                .flatMap(new Func1<List<Girl>, Observable<Girl>>() {
                    @Override
                    public Observable<Girl> call(List<Girl> girls) {
                        return Observable.from(girls);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Girl>() {
                    @Override
                    public void onCompleted() {
                        Log.d(TAG, "onCompleted: ");
                        int end;
                        if (mGirlData.size() > pageSize) {
                            end = pageSize;
                        } else {
                            end = mGirlData.size();
                        }
                        RxBus.getInstance().post(mGirlData.get((int) (Math.random() * mGirlData
                                .size()))
                                .getUrl());
                        girlView.getAdapter().setData(mGirlData.subList(0, end));
                        current = end;
                        girlView.stopRefresh();
                    }

                    @Override
                    public void onError(Throwable e) {
                        girlView.stopRefresh();
                        if (e instanceof HttpException) {
                            girlView.showSnackBar("网络错误").show();
                        } else if (e instanceof SocketTimeoutException) {
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
// girlView.getAdapter().addData(girl);
                    }
                }));
    }


    @Override
    public void loadMore() {
        if (mGirlData == null) {
            return;
        }
        if (current < mGirlData.size()) {
            RxBus.getInstance().post(mGirlData.get((int) (Math.random() * mGirlData
                    .size()))
                    .getUrl());
            if (current + pageSize >= mGirlData.size()) {
                girlView.getAdapter().addData(mGirlData.subList(current, mGirlData.size() - 1));
                current = mGirlData.size();
            } else {
                girlView.getAdapter().addData(mGirlData.subList(current, current + pageSize));
                current += pageSize;
            }
        }
        girlView.stopRefresh();
    }
}
