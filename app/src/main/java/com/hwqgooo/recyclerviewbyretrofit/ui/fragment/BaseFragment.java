package com.hwqgooo.recyclerviewbyretrofit.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;

/**
 * Created by weiqiang on 2016/6/28.
 */
public abstract class BaseFragment extends Fragment {

    final String TAG = getClass().getSimpleName();
    public Context context;
    private boolean isViewInitiated;
    private boolean isVisibleToUser;
    private boolean isDataInitiated;


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        prepareFetchData(false);
        Log.d(TAG, "setUserVisibleHint: " + isVisibleToUser);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        isDataInitiated = false;
        isViewInitiated = false;
        isVisibleToUser = false;
        Log.d(TAG, "onAttach: ");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        isViewInitiated = true;
        prepareFetchData(false);

        Log.d(TAG, "onActivityCreated: ");
    }

    @Override
    public void onResume() {
        super.onResume();
        prepareFetchData(false);
        Log.d(TAG, "onResume: ");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView: ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy: ");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        context = null;
        Log.d(TAG, "onDetach: ");
    }

    private boolean prepareFetchData(boolean forceUpdate) {
        if (isVisibleToUser && isViewInitiated) {
            if (isDataInitiated) {
                Log.d(TAG, "prepareFetchData: restore");
                restoreData();
                return true;
            } else if (!isDataInitiated || forceUpdate) {
                Log.d(TAG, "prepareFetchData: load");
                isDataInitiated = true;
                loadData();
                return true;
            }
        }
        return false;
    }

    public abstract boolean restoreData();

    public abstract boolean loadData();
}
