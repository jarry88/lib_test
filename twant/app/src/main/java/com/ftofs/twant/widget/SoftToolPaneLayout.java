package com.ftofs.twant.widget;


import android.content.Context;
import android.content.res.TypedArray;
import androidx.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.SoftInputInfo;
import com.ftofs.twant.log.SLog;
import com.orhanobut.hawk.Hawk;


/**
 * 軟鍵盤與工具面板的混合輸入佈局(仿照微信輸入表情的鍵盤交互，參考SmoothInputLayout)
 * 所在Activity需要設置
 *      android:windowSoftInputMode="adjustResize"
 * 才會生效
 */
public class SoftToolPaneLayout extends LinearLayout {
    /*
    各種狀態
     */
    public static final int STATUS_NONE_SHOWN = 0;  // 0 -- 什麼都沒顯示
    public static final int STATUS_SOFT_INPUT_SHOWN = 1; // 1 -- 軟鍵盤顯示了
    public static final int STATUS_TOOL_PANE_SHOWN = 2;  // 2 -- 工具面板顯示了

    int prevHeight = -1; // 本ViewGroup的最近一次的高度

    int softInputHeight = -1; // 軟鍵盤的高度
    int height1 = -1; // 本ViewGroup的高度1
    int height2 = -1; // 本ViewGroup的高度2

    /**
     * 本ViewGroup的状态
     */
    int status = STATUS_NONE_SHOWN;

    boolean toolPaneToBeShown;  // 將要顯示工具面板

    View vwToolPane;  // 工具面板View
    int toolPaneId;  // 工具面板View的Id

    OnStatusChangedListener statusChangedListener;

    public SoftToolPaneLayout(Context context) {
        this(context, null);
    }

    public SoftToolPaneLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SoftToolPaneLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView(attrs);
    }

    private void initView(AttributeSet attrs) {
        TypedArray custom = getContext().obtainStyledAttributes(attrs,
                R.styleable.SoftToolPaneLayout);
        toolPaneId = custom.getResourceId(R.styleable.SoftToolPaneLayout_stpl_tool_pane, NO_ID);
        custom.recycle();

        getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int currHeight = getHeight();

                // prevHeight必須在這裏初始化，因為onMeasure首次獲取的高度不太准
                if (prevHeight == -1) {
                    prevHeight = currHeight;
                    setHeight(currHeight);
                }
                /*
                SLog.info("___onGlobalLayout, width[%d], height[%d], y[%d]",
                        getWidth(), currHeight, Util.getYOnScreen(SoftToolPaneLayout.this));

                 */
            }
        });
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        SLog.info("onMeasure, heightSize[%d]", heightSize);

        if (prevHeight != -1) {
            setHeight(heightSize);
            int diff = heightSize - prevHeight;  // 本次與上次的高度差
            SLog.info("diff[%d]", diff);
            if (diff < 0) {  // 顯示軟鍵盤
                SLog.info("顯示軟鍵盤");
                setStatus(STATUS_SOFT_INPUT_SHOWN);
                vwToolPane.setVisibility(GONE);
            } else if (diff > 0) { // 隱藏軟鍵盤
                SLog.info("隱藏軟鍵盤");
                if (toolPaneToBeShown) { // 如果是為了顯示工具面板而隱藏軟鍵盤，則顯示工具面板
                    vwToolPane.setVisibility(VISIBLE);
                    toolPaneToBeShown = false;
                    setStatus(STATUS_TOOL_PANE_SHOWN);
                } else { // 單純隱藏軟鍵盤
                    setStatus(STATUS_NONE_SHOWN);
                }
            }

            prevHeight = heightSize;
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (toolPaneId != NO_ID) {
            vwToolPane = findViewById(toolPaneId);
        }
    }

    /**
     * 設置高度1、高度2，並得出軟鍵盤高度
     * @param height
     */
    private void setHeight(int height) {
        if (height1 == height) {
            return;
        }

        if (height2 == height) {
            return;
        }

        if (height1 == -1) {
            height1 = height;
            return;
        }

        if (height2 == -1) {
            height2 = height;
            softInputHeight = Math.abs(height2 - height1);
            return;
        }
    }

    public int getSoftInputHeight() {
        SLog.info("height1[%d], height2[%d], softInputHeight[%d]", height1, height2, softInputHeight);
        return softInputHeight;
    }

    /**
     * 顯示工具面板
     */
    public void showToolPane() {
        SLog.info("status[%d]", status);
        if (status == STATUS_TOOL_PANE_SHOWN) { // 如果工具面板已經顯示，則忽略
            return;
        }

        // 設置工具面板的高度與軟鍵盤的高度一致
        ViewGroup.LayoutParams layoutParams = vwToolPane.getLayoutParams();
        if (softInputHeight != SoftInputInfo.INVALID_HEIGHT) {
            layoutParams.height = softInputHeight;
        } else {
            layoutParams.height = Hawk.get(SPField.FIELD_SOFT_INPUT_HEIGHT, SoftInputInfo.INVALID_HEIGHT);
        }

        vwToolPane.setLayoutParams(layoutParams);

        if (status == STATUS_NONE_SHOWN) { // 如果一開始什麼都沒顯示，則直接顯示工具面板即可
            vwToolPane.setVisibility(VISIBLE);
        } else if (status == STATUS_SOFT_INPUT_SHOWN) {
            /*
            如果一開始軟鍵盤顯示了，則首先隱藏軟鍵盤，然後將 toolPaneToBeShown 設置為true，
            軟鍵盤隱藏，會引起佈局變化，則會調用onMeasure回調，在onMeasure回調中，發現是隱藏
            軟鍵盤，並且toolPaneToBeShown為true，則設置工具面板為VISIBLE
             */
            InputMethodManager imm = ((InputMethodManager) (getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE)));
            if (imm != null) {
                imm.hideSoftInputFromWindow(getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
            }

            // 設置toolPaneToBeShown, 以在後面的onMeasure回調中顯示工具面板
            toolPaneToBeShown = true;
        }
    }

    /**
     * 顯示軟鍵盤
     * @param view 獲取輸入焦點的View
     */
    public void showSoftInput(View view) {
        // 初始狀態下，需要獲取focus才會彈出軟鍵盤
        view.requestFocus();
        view.requestFocusFromTouch();

        InputMethodManager imm = ((InputMethodManager) (getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE)));
        if (imm != null) {
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    public void setStatus(int newStatus) {
        if (statusChangedListener != null) {
            statusChangedListener.onStatusChanged(status, newStatus);
        }
        status = newStatus;
    }

    public int getStatus() {
        return status;
    }

    public void hideToolPane() {
        vwToolPane.setVisibility(GONE);
    }

    public interface OnStatusChangedListener {
        void onStatusChanged(int oldStatus, int newStatus);
    }

    public void setStatusChangedListener(OnStatusChangedListener statusChangedListener) {
        this.statusChangedListener = statusChangedListener;
    }
}

