package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ArticleCategory;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.nex3z.flowlayout.FlowLayout;

import java.util.List;

public class ArticleCategoryPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    List<ArticleCategory> articleCategoryList;
    int selectedId;
    OnSelectedListener onSelectedListener;

    int twBlue;
    int twBlack;

    // 上一次選中的分類按鈕
    TextView prevCategoryButton;

    /**
     * 列表彈框的構造方法
     * @param context
     * @param onSelectedListener
     */
    public ArticleCategoryPopup(@NonNull Context context, int selectedId, List<ArticleCategory> articleCategoryList, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.selectedId = selectedId;
        this.articleCategoryList = articleCategoryList;

        this.onSelectedListener = onSelectedListener;

        this.twBlack = context.getColor(R.color.tw_black);
        this.twBlue = context.getColor(R.color.tw_blue);
    }



    @Override
    protected int getImplLayoutId() {
        return R.layout.article_category_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        FlowLayout flCategoryButtonContainer = findViewById(R.id.fl_category_button_container);
        boolean first = true;
        for (ArticleCategory articleCategory : articleCategoryList) {
            TextView tvCategoryButton = new TextView(context);
            tvCategoryButton.setTextSize(16);
            if (first && selectedId == -1 || selectedId == articleCategory.categoryId) {
                tvCategoryButton.setTextColor(twBlue);
                prevCategoryButton = tvCategoryButton;
                selectedId = articleCategory.categoryId;
            } else {
                tvCategoryButton.setTextColor(twBlack);
            }
            tvCategoryButton.setText(articleCategory.categoryName);
            tvCategoryButton.setBackgroundResource(R.drawable.price_room_edit_text_bg);
            tvCategoryButton.setGravity(Gravity.CENTER);

            tvCategoryButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    prevCategoryButton.setTextColor(twBlack);

                    prevCategoryButton = (TextView) v;
                    prevCategoryButton.setTextColor(twBlue);
                    selectedId = (int) prevCategoryButton.getTag();

                    onSelectedListener.onSelected(PopupType.ARTICLE_CATEGORY, selectedId, null);
                    dismiss();
                }
            });

            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Util.dip2px(context, 30));

            tvCategoryButton.setPadding(Util.dip2px(context, 18), 0, Util.dip2px(context, 18), 0);
            tvCategoryButton.setTag(articleCategory.categoryId);
            flCategoryButtonContainer.addView(tvCategoryButton, layoutParams);

            first = false;
        }
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        }
    }
}

