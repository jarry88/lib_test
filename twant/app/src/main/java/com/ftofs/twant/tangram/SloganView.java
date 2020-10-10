package com.ftofs.twant.tangram;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.entity.ShoppingZoneItem;
import com.ftofs.twant.entity.StickyCellData;
import com.ftofs.twant.fragment.BargainListFragment;
import com.ftofs.twant.fragment.GroupInfoListFragment;
import com.ftofs.twant.fragment.SecKillFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.tmall.wireless.tangram.structure.BaseCell;
import com.tmall.wireless.tangram.structure.view.ITangramViewLifeCycle;

public class SloganView extends LinearLayout implements ITangramViewLifeCycle {
    Context context;
    View vwSloganHighLighter;
    boolean animInitialized = false;  // 动画是否已经初始化

    public static final int ANIM_COUNT = 4;
    ObjectAnimator[] animatorArr = new ObjectAnimator[ANIM_COUNT];
    private LinearLayout clFirst;
    private LinearLayout clSecondLine;
    private LinearLayout clFirstLine;
    private LinearLayout llContainer;
    private LinearLayout llFirstLine;
    private LinearLayout llSecondLine;
    private  ConstraintLayout clGroup;
    private  ConstraintLayout clBargain;
    private boolean themeLoaded =false;
    private ConstraintLayout clSecKillEntrance;

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
        llContainer =contentView.findViewById(R.id.ll_shopping_container);
        clFirst = contentView.findViewById(R.id.cl_single_container);
        clFirstLine = contentView.findViewById(R.id.cl_first_line);
        llFirstLine = contentView.findViewById(R.id.ll_zone_first);
        llSecondLine = contentView.findViewById(R.id.ll_zone_second);
        clSecondLine = contentView.findViewById(R.id.cl_second_line);
        clGroup = contentView.findViewById(R.id.cl_group_entrance);
        clBargain = contentView.findViewById(R.id.cl_bargain_entrance);
        clSecKillEntrance = contentView.findViewById(R.id.cl_sec_kill_entrance);
        ImageView imgGroup = clGroup.findViewById(R.id.img_group_entrance);
        imgGroup.setOnClickListener(v -> Util.startFragment(GroupInfoListFragment.newInstance()));
        ImageView imgBargain = clBargain.findViewById(R.id.img_bargain_entrance);
        imgBargain.setOnClickListener(v -> Util.startFragment(BargainListFragment.newInstance()));
        ImageView imgSecKill = clSecKillEntrance.findViewById(R.id.img_sec_kill_entrance);
        imgSecKill.setOnClickListener(v -> Util.startFragment(SecKillFragment.newInstance()));
        addView(contentView);
    }

    @Override
    public void cellInited(BaseCell cell) {

    }

    @Override
    public void postBindView(BaseCell cell) {
        loadWantTheme(cell);
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

    private void loadWantTheme(BaseCell cell) {
        Object data = cell.optParam("data");
        if (themeLoaded) {
            return;
        }
        if (data != null) {
            StickyCellData stickyCellData = (StickyCellData) data;
            int size = stickyCellData.zoneItemList.size();
            ImageView singleImage =clFirst.findViewById(R.id.img_zone_single);
            ImageView firstImage =llFirstLine.findViewById(R.id.img_zone_first);
            ImageView secondImage =llFirstLine.findViewById(R.id.img_zone_second);
            ImageView thirdImage =llSecondLine.findViewById(R.id.img_zone_third);
            ImageView fourthImage =llSecondLine.findViewById(R.id.img_zone_fourth);
            llContainer.setVisibility(VISIBLE);

            try{
                if (size >= 5) {
                    size = 4;
                }
                if (size == 1||size==3||size==5) {
                    SLog.info("slogan 1,3");
                    clSecondLine.setVisibility(View.GONE);
                    Glide.with(context).load(StringUtil.normalizeImageUrl(stickyCellData.zoneItemList.get(0).appLogo)).centerCrop().into(singleImage);
                    singleImage.setOnClickListener(v -> Util.startFragment(NewShoppingSpecialFragment.newInstance(stickyCellData.zoneItemList.get(0).zoneId)));

                    clFirstLine.setVisibility(View.GONE);
                    if (size == 3) {
                        clFirstLine.setVisibility(View.VISIBLE);
                        Glide.with(context).load(StringUtil.normalizeImageUrl(stickyCellData.zoneItemList.get(1).appLogo)).centerCrop().into(firstImage);
                        firstImage.setOnClickListener(v -> Util.startFragment(NewShoppingSpecialFragment.newInstance(stickyCellData.zoneItemList.get(1).zoneId)));
                        secondImage.setOnClickListener(v -> Util.startFragment(NewShoppingSpecialFragment.newInstance(stickyCellData.zoneItemList.get(2).zoneId)));
                        Glide.with(context).load(StringUtil.normalizeImageUrl(stickyCellData.zoneItemList.get(2).appLogo)).centerCrop().into(secondImage);
                    }
                }else if(size==2||size==4) {
                    SLog.info("slogan 2,4");

                    clFirst.setVisibility(View.GONE);
                    clSecondLine.setVisibility(View.GONE);
                    if (size == 4) {
                        clSecondLine.setVisibility(View.VISIBLE);
                        Glide.with(context).load(StringUtil.normalizeImageUrl(stickyCellData.zoneItemList.get(2).appLogo)).centerCrop().into(thirdImage);
                        thirdImage.setOnClickListener(v -> Util.startFragment(NewShoppingSpecialFragment.newInstance(stickyCellData.zoneItemList.get(2).zoneId)));
                        Glide.with(context).load(StringUtil.normalizeImageUrl(stickyCellData.zoneItemList.get(3).appLogo)).centerCrop().into(fourthImage);
                        fourthImage.setOnClickListener(v -> Util.startFragment(NewShoppingSpecialFragment.newInstance(stickyCellData.zoneItemList.get(3).zoneId)));
                    }
                    clFirstLine.setVisibility(View.VISIBLE);
                    Glide.with(context).load(StringUtil.normalizeImageUrl(stickyCellData.zoneItemList.get(0).appLogo)).centerCrop().into(firstImage);
                    firstImage.setOnClickListener(v -> Util.startFragment(NewShoppingSpecialFragment.newInstance(stickyCellData.zoneItemList.get(0).zoneId)));
                    Glide.with(context).load(StringUtil.normalizeImageUrl(stickyCellData.zoneItemList.get(1).appLogo)).centerCrop().into(secondImage);
                    secondImage.setOnClickListener(v -> Util.startFragment(NewShoppingSpecialFragment.newInstance(stickyCellData.zoneItemList.get(1).zoneId)));
                }else {
                    SLog.info("slogan 0");

                    llContainer.setVisibility(GONE);
                }
                themeLoaded = true;
            }catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }

            for (ShoppingZoneItem item : stickyCellData.zoneItemList) {
                SLog.info(item.toString());
            }

        }else {
            llContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void postUnBindView(BaseCell cell) {
        // SLog.info("ANII_postUnBindView");
    }
}

