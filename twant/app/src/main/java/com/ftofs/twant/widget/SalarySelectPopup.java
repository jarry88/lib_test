package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.ftofs.twant.R;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.core.BottomPopupView;

import java.util.List;


public class SalarySelectPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    PopupType popupType;
    List<ListPopupItem> hrSalaryTypeList;
    List<ListPopupItem> hrSalaryRangeList;
    String salary;
    OnSelectedListener onSelectedListener;

    FrameLayout flSalaryPickerContainer;

    public SalarySelectPopup(@Nullable Context context, PopupType popupType, List<ListPopupItem> hrSalaryTypeList, List<ListPopupItem> hrSalaryRangeList, OnSelectedListener onSelectedListener) {
        super(context);
        this.context = context;
        this.popupType = popupType;
        this.hrSalaryTypeList = hrSalaryTypeList;
        this.hrSalaryRangeList = hrSalaryRangeList;
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        flSalaryPickerContainer = findViewById(R.id.fl_date_picker_container);

        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = hrSalaryTypeList.get(options1).toString()
                        + hrSalaryRangeList.get(options1).toString()
                        /* + options3Items.get(options1).get(options2).get(options3).getPickerViewText()*/;
                SLog.info("tx[%s]", tx);
            }
        })
                .isDialog(false)
                .setTitleText("城市选择")
                .setContentTextSize(20)//设置滚轮文字大小
                .setDividerColor(Color.LTGRAY)//设置分割线的颜色
                /*
                .setSelectOptions(0, 1)//默认选中项
                .setBgColor(Color.BLACK)
                .setTitleBgColor(Color.DKGRAY)
                .setTitleColor(Color.LTGRAY)
                .setCancelColor(Color.YELLOW)
                .setSubmitColor(Color.YELLOW)
                .setTextColorCenter(Color.LTGRAY)
                .isRestoreItem(true)//切换时是否还原，设置默认选中第一项。

                 */

                // .setDecorView(flSalaryPickerContainer)//非dialog模式下,设置ViewGroup, pickerView将会添加到这个ViewGroup中
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .isCenterLabel(false) //是否只显示中间选中项的label文字，false则每项item全部都带有label。
                .setOptionsSelectChangeListener(new OnOptionsSelectChangeListener() {
                    @Override
                    public void onOptionsSelectChanged(int options1, int options2, int options3) {
                        String str = "options1: " + options1 + "\noptions2: " + options2 + "\noptions3: " + options3;
                        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
                    }
                })
                .build();

//        pvOptions.setSelectOptions(1,1);
        /*pvOptions.setPicker(options1Items);//一级选择器*/
        pvOptions.setNPicker(hrSalaryTypeList, hrSalaryRangeList, null);//二级选择器
        pvOptions.show();
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.salary_select_popup;
    }

    @Override
    public void onClick(View v) {
        int id=getId();
        switch (id) {
            case R.id.btn_dismiss:
                dismiss();
                break;
            case R.id.btn_ok:
                onSelectedListener.onSelected(PopupType.SELECT_SALARY,0,salary);
                dismiss();
                break;
            default:
                break;
        }
    }

}
