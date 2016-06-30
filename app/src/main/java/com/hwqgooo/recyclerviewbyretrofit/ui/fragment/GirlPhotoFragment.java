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
import android.widget.Toast;

import com.hwqgooo.recyclerviewbyretrofit.R;
import com.hwqgooo.recyclerviewbyretrofit.model.bean.Girl;
import com.hwqgooo.recyclerviewbyretrofit.presenter.GirlPhotoPresenter;
import com.hwqgooo.recyclerviewbyretrofit.presenter.IGirlPhotoPresenter;
import com.hwqgooo.recyclerviewbyretrofit.view.IGirlPhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by weiqiang on 2016/6/11.
 */
public class GirlPhotoFragment extends DialogFragment implements IGirlPhotoView {
    @BindView(R.id.iv_fr_girl)
    PhotoView ivFrGirl;

    Girl girl;
    Context context;
    IGirlPhotoPresenter presenter;
    Unbinder unbinder;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.girl_photo_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        Bundle bundle = getArguments();
        girl = bundle.getParcelable("girl");
//        Dart.inject(this, getActivity());
        Log.d("hwqhwq", "onCreateView: " + girl.getDesc() + ":::" + girl.getUrl());
        presenter = new GirlPhotoPresenter(context, this);
        presenter.showGirl();

        setupPhotoEvent();
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

    private void setupPhotoEvent() {
        ivFrGirl.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                Toast.makeText(context, "x:" + x + ",y:" + y, Toast.LENGTH_SHORT).show();
//                dismiss();
            }
        });

        ivFrGirl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Toast.makeText(context, "onlonglick", Toast.LENGTH_SHORT).show();
                return false;
            }
        });
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

    @Override
    public PhotoView getImageView() {
        return ivFrGirl;
    }

    @Override
    public void saveImage() {
        presenter.saveGirl();
    }

    @Override
    public Girl getGirl() {
        return girl;
    }
}
