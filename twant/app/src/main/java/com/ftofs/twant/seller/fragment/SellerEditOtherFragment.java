package com.ftofs.twant.seller.fragment;

import android.app.ActionBar;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.AdminCountry;
import com.ftofs.twant.domain.goods.Category;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.entity.TwDate;
import com.ftofs.twant.seller.widget.CalendarPopup;
import com.ftofs.twant.seller.widget.StoreLabelPopup;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.ScaledButton;
import com.kyleduo.switchbutton.SwitchButton;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class SellerEditOtherFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    TextView tvTitle;
    private EasyJSONObject publishGoodsInfo= new EasyJSONObject();
    SellerGoodsDetailFragment parent;
    private SwitchButton sbJoinActivity;
    private int joinBigSale;
    private int goodsState;
    private ScaledButton sbInstancePublish;
    private ScaledButton sbAddHub;
    private TextView tvBeginDate;
    private TextView tvEndDate;
    private TwDate beginDate;
    private TwDate endDate;
    private TimePickerView pvTime;
    private boolean isBiginDate;
    private String limitBuyStartTime;
    private String limitBuyEndTime;
    private int limitBuy;

    public static SellerEditOtherFragment newInstance(SellerGoodsDetailFragment parent) {
        SellerEditOtherFragment fragment= new SellerEditOtherFragment();
        fragment.parent = parent;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_goods_other_edit, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initTimePicker();
    }
    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(date);
    }
    private void initTimePicker() {
        pvTime = new TimePickerBuilder(_mActivity, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                if (isBiginDate) {
                    limitBuyStartTime = getTime(date);
                            tvBeginDate.setText(limitBuyStartTime);
                } else {
                    limitBuyEndTime = getTime(date);
                    tvEndDate.setText(limitBuyEndTime);
                }

                Log.i("pvTime", "onTimeSelect");

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setTitleText("选择时间")
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("确认")//确认按钮文字
//                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
                .setTitleText("Title")//标题文字
                .setOutSideCancelable(false)//点击屏幕，点在控件外部范围时，是否取消显示
                .isCyclic(true)//是否循环滚动
                .setTitleColor(Color.BLACK)//标题文字颜色
                .setSubmitColor(Color.BLUE)//确定按钮文字颜色
                .setCancelColor(Color.BLUE)//取消按钮文字颜色
                .setTitleBgColor(getResources().getColor(R.color.tw_dark_white))//标题背景颜色 Night mode
                .setBgColor(getResources().getColor(R.color.tw_white))//滚轮背景颜色 Night mode
//                .setRange(calendar.get(Calendar.YEAR) - 20, calendar.get(Calendar.YEAR) + 20)//默认是1900-2100年
                .setDate(Calendar.getInstance())// 如果不设置的话，默认是系统时间*/
                .setType(new boolean[]{true, true, true, true, true, true})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
            }
        }
    }

    private void initView() {
        View view = getView();
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("编辑其它信息");

        view.findViewById(R.id.ll_bottom_container).setVisibility(View.GONE);
        view.findViewById(R.id.btn_ok).setVisibility(View.VISIBLE);

        tvBeginDate = view.findViewById(R.id.tv_begin_date);
        tvEndDate = view.findViewById(R.id.tv_end_date);
        tvBeginDate.setOnClickListener((view1)->{
            if (pvTime != null) {
                isBiginDate = true;
                pvTime.show(view);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
            }
        });
        tvEndDate.setOnClickListener((view1)->{
            if (pvTime != null) {
                isBiginDate = false;

                pvTime.show(view);//弹出时间选择器，传递参数过去，回调的时候则可以绑定此view
            }
        });
        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_back, this);

        sbInstancePublish = view.findViewById(R.id.sb_instance_publish);
        sbAddHub = view.findViewById(R.id.sb_add_hub);

        LinearLayout llAddHub =view.findViewById(R.id.ll_add_hub);
        LinearLayout llInstancePublish =view.findViewById(R.id.ll_instance_publish);
        sbInstancePublish.setButtonCheckedBlue();
        sbAddHub.setButtonCheckedBlue();
        llAddHub.setOnClickListener(v ->{
            if (!sbAddHub.isChecked()) {
                sbAddHub.setChecked(true);
                goodsState = Constant.FALSE_INT;
                sbInstancePublish.setChecked(false);
            }
        });
        llInstancePublish.setOnClickListener(v ->{
            if (!sbInstancePublish.isChecked()) {
                sbInstancePublish.setChecked(true);
                goodsState = Constant.TRUE_INT;
                sbAddHub.setChecked(false);
            }
        });
        llInstancePublish.performClick();
        sbJoinActivity = view.findViewById(R.id.sb_join_activity);

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (parent.goodsVo == null) {
            loadData();
        } else {
            explainData();
        }
    }

    private void explainData() {
        try{
            joinBigSale = parent.joinBigSale;
            limitBuy = parent.goodsVo.getInt("limitBuy");
            limitBuyStartTime = parent.goodsVo.getSafeString("limitBuyStartTime");
            limitBuyEndTime = parent.goodsVo.getSafeString("limitBuyEndTime");
            updateView();
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void updateView() {
        sbJoinActivity.setChecked(joinBigSale==1);
        EditText etLimitNum = getView().findViewById(R.id.et_limit_num);
        etLimitNum.setText(String.valueOf(limitBuy));
        tvBeginDate.setText(limitBuyStartTime);
        tvEndDate.setText(limitBuyEndTime);
    }
    private void updateOtherView(EasyJSONObject data) throws Exception{

    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_SELLER_GOODS_DETAIL + "/" + parent.commonId;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );
        SLog.info("url[%s], params[%s]", url, params);
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        loadingPopup.show();
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    View contentView = getView();
                    if (contentView == null) {
                        return;
                    }

                    EasyJSONObject goodsVo = responseObj.getSafeObject("datas.GoodsVo");
                    parent.goodsVo = goodsVo;
                    parent.commonId = goodsVo.getInt("commonId");
                    parent.joinBigSale = goodsVo.getInt("joinBigSale");
                    explainData();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private boolean checkOtherInfo() {
        joinBigSale = sbJoinActivity.isChecked() ? Constant.TRUE_INT : Constant.FALSE_INT;
        goodsState = sbInstancePublish.isChecked() ? 1 : 0;

        try {
            publishGoodsInfo.set("joinBigSale", joinBigSale);
            publishGoodsInfo.set("goodsState", goodsState);
            publishGoodsInfo.set("commonId", parent.commonId);
            publishGoodsInfo.set("editType", 6);
            publishGoodsInfo.set("limitBuyStartTime", limitBuyStartTime);
            publishGoodsInfo.set("limitBuyEndTime", limitBuyEndTime);
            publishGoodsInfo.set("limitBuy",limitBuy);
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        return true;
    }
    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
        if (id == R.id.btn_ok) {
            if (checkOtherInfo()) {
                parent.saveGoodsInfo(publishGoodsInfo, new SimpleCallback() {
                    @Override
                    public void onSimpleCall(Object data) {
                        ToastUtil.success(_mActivity,data.toString());
                        hideSoftInputPop();
                    }
                });
            }

        }
        if (id == R.id.tv_add_good_unit) {

        }

    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {

    }
}
