package com.hwqgooo.recyclerviewbyretrofit.presenter.gank;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.hwqgooo.recyclerviewbyretrofit.model.ApiServiceFactory;
import com.hwqgooo.recyclerviewbyretrofit.model.IGirlService;
import com.hwqgooo.recyclerviewbyretrofit.model.IGitHubService;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.Gank;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.GankData;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.GankDetail;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.GitUser;
import com.hwqgooo.recyclerviewbyretrofit.view.IGirlView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
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
 * Created by weiqiang on 2016/6/26.
 */
public class GankPresenter implements IGankPresenter {
    final String TAG = getClass().getSimpleName();
    final String baseUrl = "http://gank.io/api/";
    private final String clientID = "b78af009a1b1cfe46317";
    private final String clientSecret = "6d96f809338d479ed86614dd09983195119d338c";
    Context context;
    IGirlView girlView;
    Retrofit mRetrofit;
    IGirlService gankService;
    int page;
    List<Gank> gankList = new LinkedList<>();
    CompositeSubscription compositeSubscription;
    private Object CSDNAvatar;

    public GankPresenter(Context context, IGirlView view) {
        this.girlView = view;
        this.context = context;
        mRetrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        gankService = mRetrofit.create(IGirlService.class);
        compositeSubscription = new CompositeSubscription();
    }

    void loadPage(int p) {
        compositeSubscription.add(gankService
                .getGankDatas(p)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<GankData, List<GankDetail>>() {
                    @Override
                    public List<GankDetail> call(GankData gankData) {
                        return gankData.getData();
                    }
                })
                .flatMap(new Func1<List<GankDetail>, Observable<GankDetail>>() {
                    @Override
                    public Observable<GankDetail> call(List<GankDetail> gankDetails) {
                        return Observable.from(gankDetails);
                    }
                })
                .subscribe(new Subscriber<GankDetail>() {
                    @Override
                    public void onCompleted() {
                        girlView.stopRefresh();
                        page++;
                    }

                    @Override
                    public void onError(Throwable e) {
                        girlView.stopRefresh();
                        if (e instanceof SocketTimeoutException) {
                            girlView.showSnackBar("网络超时")
                                    .setAction("重试", new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            girlView.startRefresh();
                                            refresh();
                                        }
                                    })
                                    .show();
                        } else {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onNext(GankDetail gankDetail) {
                        setAvatarUrlAndShow(gankDetail);
                    }
                }));
    }

    @Override
    public void restore() {
        if (!gankList.isEmpty()) {
            girlView.getAdapter().setData(gankList);
        }
    }

    @Override
    public void refresh() {
        page = 0;
        loadPage(page);
    }

    @Override
    public void loadMore() {
        loadPage(page);
    }

    private void setAvatarUrlAndShow(GankDetail gankDetail) {
        String url = gankDetail.getUrl();
        if (url.contains("https://github.com/")) {
            setGitHubAvatar(url, gankDetail);
        } else if (url.contains("http://blog.csdn.net/")) {
            setCSDNAvatar(url, gankDetail);
        } else if (url.contains("http://www.jianshu.com")) {
            setJianShuAvatar(url, gankDetail);
        } else if (url.contains("http://gankDetail.jobbole.com")) {
            setJobboleAvatar(url, gankDetail);
        } else {
//            AndroidWrapper wrapper = new AndroidWrapper(gankDetail, null);
//            wrapperList.add(wrapper);
        }
    }

    private void setJobboleAvatar(final String url, final GankDetail gankDetail) {
        Observable observable = Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            Document document = Jsoup.connect(url)
                                    .userAgent("Mozilla")
                                    .timeout(8000)
                                    .get();
                            Element container = document.getElementsByClass("copyright-area").get
                                    (0);
                            String author = container.select("a[href]").get(1).text();
                            gankDetail.setWho(author);
                            subscriber.onNext(author);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        compositeSubscription.add(observable
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(final String s) {
                        Observable avatarObsevable = Observable
                                .create(new Observable.OnSubscribe<String>() {
                                    @Override
                                    public void call(Subscriber<? super String> subscriber) {
                                        String baseUrl = "http://www.jobbole.com/members/";
                                        try {
                                            Document document = Jsoup.connect(baseUrl + s)
                                                    .userAgent("Mozilla")
                                                    .timeout(8000)
                                                    .get();
                                            Element profileImg = document.getElementsByClass
                                                    ("profile-img").get(0);
                                            String avatarUrl = profileImg.select("[src]").get(0)
                                                    .toString();
                                            avatarUrl = avatarUrl.substring(51, avatarUrl.length
                                                    () - 2);
                                            Gank androidWrapper = new Gank(gankDetail, avatarUrl);
                                            subscriber.onNext(avatarUrl);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                })
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread());

                        avatarObsevable.subscribe(getAction(gankDetail));

                    }
                }));
    }

    private void setJianShuAvatar(final String url, final GankDetail gankDetail) {
        Observable observable = Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            Document document = Jsoup.connect(url)
                                    .userAgent("Mozilla")
                                    .timeout(8000)
                                    .get();
                            Element container = document.getElementsByClass("container").get(0);
                            Element avatar = container.getElementsByClass("avatar").get(0);
                            String imgSrc = avatar.select("[src]").toString();
                            int end = imgSrc.indexOf("\"", 10);
                            String avatarUrl = imgSrc.substring(10, end);
                            gankDetail.setWho(container.select("a[class^=author-name blue-link]")
                                    .text());
                            subscriber.onNext(avatarUrl);

                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        compositeSubscription.add(observable.subscribe(getAction(gankDetail)));
    }

    Action1<String> getAction(final GankDetail data) {
        return new Action1<String>() {
            @Override
            public void call(String s) {
                if (girlView != null) {
                    Gank gank = new Gank(data, s);
                    girlView.getAdapter().addData(gank);
                    gankList.add(gank);
                }
            }
        };
    }

    private void setCSDNAvatar(final String url, final GankDetail gankDetail) {
        Observable observable = Observable
                .create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        try {
                            Document document = Jsoup.connect(url)
                                    .userAgent("Mozilla")
                                    .timeout(8000)
                                    .get();
                            Element author = document.getElementById("blog_title");
                            Element avatar = document.getElementById("blog_userface");
                            gankDetail.setWho(author.select("a[href]").text().toString());
                            String imgSrc = avatar.select("[src]").toString();
                            int end = imgSrc.indexOf("\"", 10);
                            String avatarUrl = imgSrc.substring(10, end);
                            subscriber.onNext(avatarUrl);
                        } catch (MalformedURLException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        compositeSubscription.add(observable.subscribe(getAction(gankDetail)));
    }

    private void setGitHubAvatar(String url, final GankDetail gankDetail) {
        IGitHubService gitHubService = ApiServiceFactory.buildGitHubService();
        int start = url.indexOf("/", 19);
        String author = url.substring(19, start);
        gankDetail.setWho(author);
        compositeSubscription.add(gitHubService
                .getAvatar(author, clientID, clientSecret)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GitUser>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("TAG", e.toString());
                    }

                    @Override
                    public void onNext(GitUser gitUser) {
                        if (girlView != null) {
                            Gank gank = new Gank(gankDetail, gitUser.getImageUrl());
                            girlView.getAdapter().addData(gank);
                            gankList.add(gank);
                        }
                    }
                }));
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
}
