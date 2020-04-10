package com.ftofs.twant.tangram;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.util.Util;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

public class SloganView extends LinearLayout implements ITangramViewLifeCycle {
    Context context;
    View vwSloganHighLighter;
    boolean animInitialized = false;  // 动画是否已经初始化

    public static final int ANIM_COUNT = 4;
    ObjectAnimator[] animatorArr = new ObjectAnimator[ANIM_COUNT];

    public SloganView(Context context) {
        this(context, null);
    }

    public SloganView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SloganView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);


        View contentView = LayoutInflater.from(context).inflate(R.layout.tangram_layout_home_slogan_view, this, false);
        vwSloganHighLighter = contentView.findViewById(R.id.vw_slogan_highlighter);

        addView(contentView);
    }

    @Override
    public void cellInited(BaseCell cell) {

    }

    @Override
    public void postBindView(BaseCell cell) {
        // SLog.info("ANII_postBindView, animInitialized[%s]", animInitialized);
        if (animInitialized) {
            return;
        }
        animInitialized = true;

        long animDuration = 1500;
        int blockWidth = Util.getScreenDimension(getContext()).first / 3; // 分3塊
        animatorArr[0] = ObjectAnimator.ofFloat(vwSloganHighLighter,"translationX",-blockWidth,0).setDuration(animDuration);
        animatorArr[1] = ObjectAnimator.ofFloat(vwSloganHighLighter,"translationX",0,blockWidth).setDuration(animDuration);
        animatorArr[2] = ObjectAnimator.ofFloat(vwSloganHighLighter,"translationX",blockWidth,2*blockWidth).setDuration(animDuration);
        animatorArr[3] = ObjectAnimator.ofFloat(vwSloganHighLighter,"translationX",2*blockWidth,3*blockWidth).setDuration(animDuration);

        for (int i = 0; i < ANIM_COUNT; i++) {
            int finalI = i;
            animatorArr[i].addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    int index = (finalI + 1) % ANIM_COUNT;
                    // SLog.info("index[%d]", index);
                    vwSloganHighLighter.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            animatorArr[index].start();
                        }
                    }, 1000);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        vwSloganHighLighter.postDelayed(new Runnable() {
            @Override
            public void run() {
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) vwSloganHighLighter.getLayoutParams();
                layoutParams.width = blockWidth;
                vwSloganHighLighter.setLayoutParams(layoutParams);
                vwSloganHighLighter.setVisibility(View.VISIBLE);
                animatorArr[0].start();
            }
        }, 500);
    }

    @Override
    public void postUnBindView(BaseCell cell) {
        // SLog.info("ANII_postUnBindView");
    }
}

