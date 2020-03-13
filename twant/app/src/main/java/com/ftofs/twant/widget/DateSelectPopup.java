package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import androidx.annotation.NonNull;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.CustomListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 日期選擇彈窗
 * @author zwm
 */
public class DateSelectPopup extends BottomPopupView implements View.OnClickListener, OnSelectedListener {
    Context context;
    PopupType popupType;
    OnSelectedListener onSelectedListener;
    String dateStr = "2019-01-01";
    boolean showDay=true;
    String dayLabel = "日";

    TimePickerView timePickerView;

    /**
     * 列表彈框的構造方法
     * @param context
     * @param dateStr 初始的日期，格式 2019-06-06，如果為null或空串，則默認為 1992-01-01
     * @param onSelectedListener
     */
    public DateSelectPopup(@NonNull Context context, PopupType popupType, String dateStr, OnSelectedListener onSelectedListener) {
        super(context);

        this.context = context;
        this.popupType = popupType;
        this.onSelectedListener = onSelectedListener;
        if (!StringUtil.isEmpty(dateStr)) {
            this.dateStr = dateStr;
        }
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.date_select_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        TextView tvPopupTitle = findViewById(R.id.tv_popup_title);
        if (popupType == PopupType.BIRTH_DAY) {
            tvPopupTitle.setText(context.getString(R.string.text_birthday));
        } else {
            tvPopupTitle.setText(context.getString(R.string.text_deadline));
        }

        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

        FrameLayout flDatePickerContainer = findViewById(R.id.fl_date_picker_container);
        Calendar calendar = Calendar.getInstance();

        int year = Integer.parseInt(dateStr.substring(0, 4));
        int month = Integer.parseInt(dateStr.substring(5, 7));
        int day=1;
        if (dateStr.length() > 8) {
            day= Integer.parseInt(dateStr.substring(8, 10));
        }
        // 設置初始日期
        calendar.set(year, month - 1, day);
        SLog.info("calendar[%s]",calendar.getTime());
        SLog.info("");

        if (popupType == PopupType.SELECT_START_MONTH||popupType == PopupType.SELECT_END_MONTH) {
            showDay=false;
            dayLabel="";
        }
        timePickerView = new TimePickerBuilder(context, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                int year = date.getYear() + 1900;
                int month = date.getMonth() + 1;
                int day = date.getDate();
                String dateStr = String.format("%04d-%02d-%02d 09:06:03", year, month, day);
                SLog.info("dateStr[%s]", dateStr);
                if (popupType == PopupType.BIRTH_DAY) {
                    setBirthday(year, month, day);
                } else if(popupType == PopupType.SELECT_START_MONTH||popupType == PopupType.SELECT_END_MONTH){
                    dismiss();
                    onSelectedListener.onSelected(popupType, 0, dateStr);
                }else {
                    dismiss();
                    onSelectedListener.onSelected(popupType, 0, String.format("%d-%02d-%02d", year, month, day));
                }
            }
        })
                .setLayoutRes(R.layout.date_picker_view, new CustomListener() {

                    @Override
                    public void customLayout(View v) {
                        /*
                        TextView tvSubmit = (TextView) v.findViewById(R.id.tv_finish);
                        tvSubmit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                pvTime.returnData();
                            }
                        });
                        */
                    }
                })
                .setType(new boolean[]{true, true,showDay, false, false, false})
                .setLabel("年", "月", dayLabel, "", "", "") //设置空字符串以隐藏单位提示   hide label
                .setDividerColor(Color.DKGRAY)
                .setContentTextSize(20)
                .setDate(calendar)
                // .setRangDate(startDate, endDate)
                .setDecorView(flDatePickerContainer)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                .setBackgroundId(0x00000000)
                .setOutSideCancelable(false)
                .build();
        timePickerView.setKeyBackCancelable(false);//系统返回键监听屏蔽掉
        timePickerView.show(false);
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
        } else if (id == R.id.btn_ok) {
            timePickerView.returnData();
        }
    }

    /**
     * 設置城友生日
     */
    private void setBirthday(int year, int month, int day) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            ToastUtil.error(context, context.getString(R.string.text_not_login));
            return;
        }

        final String birthday = String.format("%04d-%02d-%02d 00:00:00", year, month, day);
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "birthday", birthday);
        Api.postUI(Api.PATH_SET_BIRTHDAY, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(getContext(), e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(context, responseObj)) {
                        return;
                    }

                    dismiss();
                    onSelectedListener.onSelected(popupType, 0, birthday.substring(0, 10));
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("onSelected, type[%d], id[%d], extra[%s]", type, id, extra);

        onSelectedListener.onSelected(type, id, extra);
    }
}
