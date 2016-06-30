package com.hwqgooo.recyclerviewbyretrofit.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.hwqgooo.recyclerviewbyretrofit.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by weiqiang on 2016/6/27.
 */
public class GankWebFragment extends DialogFragment {
    final String TAG = getClass().getSimpleName();
    @BindView(R.id.wv_gank)
    WebView wvGank;
    Unbinder unbinder;
    Context context;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.gank_web_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        String url = bundle.getString("url");
        Log.d(TAG, "onCreateView: " + url);
        WebSettings settings = wvGank.getSettings();
        settings.setJavaScriptEnabled(true);
        wvGank.setWebViewClient(new MyWebViewClient());
        wvGank.setWebChromeClient(new WebChromeClient());
        wvGank.loadUrl(url);
        return view;
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        // Assign window properties to fill the parent
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        if (unbinder != null) {
            unbinder.unbind();
            unbinder = null;
        }
    }

    class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url != null) view.loadUrl(url);
            return true;
        }
    }
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK && wvGank.canGoBack()) {
//            wvGank.goBack();
//            return true;
//        }
//        return false;
//    }
}
