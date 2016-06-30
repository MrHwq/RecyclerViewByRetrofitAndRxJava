package com.hwqgooo.recyclerviewbyretrofit.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hwqgooo.recyclerviewbyretrofit.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by weiqiang on 2016/6/11.
 */
public class EntryActivity extends AppCompatActivity {
    @BindView(R.id.text_label)
    TextView textLabel;
    @BindView(R.id.iv_logo)
    ImageView ivLogo;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
            return;
        }
        startAnim();
        super.onWindowFocusChanged(hasFocus);
    }

    private void startAnim() {
        ObjectAnimator moveUp = ObjectAnimator.ofFloat(ivLogo, "translationY", 0f, -150f);
        moveUp.setDuration(1000);
        moveUp.setStartDelay(500);
        moveUp.start();

        ObjectAnimator moveDown = ObjectAnimator.ofFloat(textLabel, "translationY", 200f, 510f);
        ObjectAnimator fadeInOut = ObjectAnimator.ofFloat(textLabel, "alpha", 0f, 1f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(moveDown).with(fadeInOut);
        animatorSet.setDuration(1500);
        animatorSet.setStartDelay(700);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Intent intent = new Intent(EntryActivity.this, MainActivity.class);
                startActivity(intent);
                EntryActivity.this.finish();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        setContentView(R.layout.activity_entry);
        ButterKnife.bind(this);
    }
}
