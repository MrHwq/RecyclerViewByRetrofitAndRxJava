package com.hwqgooo.recyclerviewbyretrofit.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hwqgooo.recyclerviewbyretrofit.view.IGirlPhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by weiqiang on 2016/6/14.
 */
public class GirlPhotoPresenter implements IGirlPhotoPresenter, Picasso.Listener {
    Context context;
    IGirlPhotoView girlPhotoView;

    public GirlPhotoPresenter(Context context, IGirlPhotoView girlPhotoView) {
        this.context = context;
        this.girlPhotoView = girlPhotoView;
    }

    @Override
    public void showGirl() {
//        ViewGroup.LayoutParams params = girlPhotoView.getImageView().getLayoutParams();
//        params.width = context.getResources().getDisplayMetrics().widthPixels;
//        params.height = context.getResources().getDisplayMetrics().heightPixels;
//        girlPhotoView.getImageView().setLayoutParams(params);

//        final PhotoViewAttacher attacher = new PhotoViewAttacher(girlPhotoView.getImageView());
//        Picasso.with(context)
//                .load(girlPhotoView.getGirl().getUrl()).into(girlPhotoView.getImageView(), new
//                Callback() {
//                    @Override
//                    public void onSuccess() {
//                        attacher.update();
//                        Log.d("hwqhwq", "onSuccess: ");
//                    }
//
//                    @Override
//                    public void onError() {
//                        Log.d("hwqhwq", "onError: ");
//                    }
//                });

        Glide.with(context)
                .load(girlPhotoView.getGirl().getUrl())
                .fitCenter()
                .into(girlPhotoView.getImageView());
    }

    @Override
    public void saveGirl() {

        Observable observable = Observable
                .just(girlPhotoView.getGirl().getUrl())
                .map(new Func1<String, Bitmap>() {
                    @Override
                    public Bitmap call(String s) {
                        try {
                            return Picasso.with(context).load(s).get();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                }).map(new Func1<Bitmap, Uri>() {
                    @Override
                    public Uri call(Bitmap bitmap) {
                        if (bitmap != null) {
                            File appDir = new File(Environment.getExternalStorageDirectory(),
                                    "Girl");
                            if (!appDir.exists()) {
                                appDir.mkdir();
                            }
                            File file = new File(appDir, girlPhotoView.getGirl().getDesc() + "" +
                                    ".jpg");
                            try {
                                FileOutputStream fos = new FileOutputStream(file);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                                fos.flush();
                                fos.close();
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            Uri uri = Uri.fromFile(file);
                            // 通知图库更新
                            Intent scannerIntent = new Intent(Intent
                                    .ACTION_MEDIA_SCANNER_SCAN_FILE, uri);
                            context.sendBroadcast(scannerIntent);

                            return uri;
                        }
                        return null;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

        observable.subscribe(new Action1<Uri>() {
            @Override
            public void call(Uri uri) {
                Toast.makeText(context, "保存至" + uri.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
        Log.d("hwqhwq", "onImageLoadFailed: ");
    }
}
