package com.ftofs.twant.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.EditText;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.util.EditTextUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

public class BudgetPricePopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    String budgetPrice;
    OnSelectedListener onSelectedListener;

    EditText etBudgetPrice;

    /**
     * 列表彈框的構造方法
     * @param context
     * @param onSelectedListener
     */
    public BudgetPricePopup(@NonNull Context context, String budgetPrice, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.budgetPrice = budgetPrice;
        this.onSelectedListener = onSelectedListener;
    }



    @Override
    protected int getImplLayoutId() {
        return R.layout.budget_price_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);
        etBudgetPrice = findViewById(R.id.et_budget_price);
        etBudgetPrice.setText(budgetPrice);
        EditTextUtil.cursorSeekToEnd(etBudgetPrice);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();

        Util.showSoftInput(etBudgetPrice);
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
        } else if (id == R.id.btn_ok) {
            budgetPrice = etBudgetPrice.getText().toString().trim();

            if (StringUtil.isEmpty(budgetPrice)) {
                ToastUtil.error(context, context.getString(R.string.input_budget_price_hint));
                return;
            }

            onSelectedListener.onSelected(Constant.POPUP_TYPE_BUDGET_PRICE, 0, budgetPrice);
            dismiss();
        }
    }
}

