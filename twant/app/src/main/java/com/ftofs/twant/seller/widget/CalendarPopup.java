package com.ftofs.twant.seller.widget;

import android.content.Context;
import android.view.View;
import android.widget.CalendarView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.gzp.lib_common.constant.PopupType;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.ftofs.twant.seller.entity.TwDate;
import com.gzp.lib_common.base.Jarbon;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;


/**
 * 日曆選擇彈窗
 * @author zwm
 */
public class CalendarPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    PopupType popupType;
    TwDate twDate;

    CalendarView calendarView;
    OnSelectedListener onSelectedListener;

    public CalendarPopup(@NonNull Context context, PopupType popupType, TwDate twDate, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.popupType = popupType;
        this.twDate = twDate;
        this.onSelectedListener = onSelectedListener;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.calendar_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        calendarView = findViewById(R.id.calendar_view);
        calendarView.setDate(Jarbon.create(twDate.year, twDate.month, twDate.day).getTimestampMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                CalendarPopup.this.twDate.year = year;
                CalendarPopup.this.twDate.month = month + 1; // month的取值範圍為[0-11]，所以要加1
                CalendarPopup.this.twDate.day = dayOfMonth;
            }
        });

        findViewById(R.id.btn_ok).setOnClickListener(this);
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
        return (int) (XPopupUtils.getWindowHeight(getContext()) * .85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_ok) {
            if (onSelectedListener != null) {
                onSelectedListener.onSelected(popupType, 0, twDate);
            }
            dismiss();
        }
    }
}
