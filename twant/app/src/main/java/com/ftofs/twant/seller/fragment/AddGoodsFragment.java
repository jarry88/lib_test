package com.ftofs.twant.seller.fragment;


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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SimpleViewPagerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.domain.AdminCountry;
import com.ftofs.twant.domain.Format;
import com.ftofs.twant.domain.goods.Brand;
import com.ftofs.twant.domain.goods.Category;
import com.ftofs.lib_net.model.StoreLabel;
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.gzp.lib_common.base.BaseFragment;
import com.ftofs.twant.interfaces.EditorResultInterface;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.seller.entity.SellerGoodsPicVo;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.entity.SellerSpecMapItem;
import com.ftofs.twant.seller.entity.SellerSpecPermutation;
import com.ftofs.twant.seller.entity.TwDate;
import com.ftofs.twant.seller.widget.NoScrollViewPager;
import com.ftofs.twant.seller.widget.SellerSelectSpecPopup;
import com.ftofs.twant.seller.widget.StoreLabelPopup;
import com.ftofs.twant.util.AssetsUtil;
import com.gzp.lib_common.base.Jarbon;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.lib_common_ui.popup.ListPopup;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.kyleduo.switchbutton.SwitchButton;
import com.lxj.xpopup.XPopup;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


public class AddGoodsFragment extends BaseFragment
        implements View.OnClickListener, OnSelectedListener, EditorResultInterface {


    private SimpleViewPagerAdapter mPagerAdapter;
    private List<View> mViews = new ArrayList<>();
    //    1是0否允許發佈跨城購商品
    public int allowTariff;
    //    允許添加的最大規格數量
    private int specMax;
    private List<ListPopupItem> unitList = new ArrayList<>();
    //準備要提交的商品信息json
    private EasyJSONObject publishGoodsInfo;
    // private EasyJSONArray goodsJsonVoList = EasyJSONArray.generate();
    // private EasyJSONArray goodsPicVoList = EasyJSONArray.generate();

    private final int PRIMARY_INDEX=0;//基本信息頁
    private final int BASIC_INDEX=1;//交易信息頁
    private final int SPEC_INDEX=2;//規格與圖片頁
    private final int DETAIL_INDEX=3;//詳細信息頁
    private final int FREIGHT_INDEX=4;//物流信息頁
    private final int OTHERS_INDEX=5;//其他信息頁
    private List<StoreLabel> labelList;
    private int categoryId =-1;
    private int goodsCountry=-1;//品牌所在地

    List<ListPopupItem> spinnerLogoItems = new ArrayList<>();

    List<ListPopupItem> spinnerLogoCountryItems = new ArrayList<>();
    private int brandId;

    // 未選中的規格組Id與規格組信息的映射關係
    Map<Integer, SellerSpecMapItem> sellerSpecMap = new HashMap<>();

    // 已選中的規格組Id與規格組信息的映射關係
    List<SellerSpecMapItem> sellerSelectedSpecList = new ArrayList<>();

    // 規格值Id的字符串拼接(例5,12,8)與SKU信息的映射關係
    Map<String, SellerSpecPermutation> specValueIdStringMap = new HashMap<>();
    // 規格值Id的字符串拼接(例5,12,8)的列表
    List<String> specValueIdStringList = new ArrayList<>();

    // colorId與圖片列表的映射關係
    Map<Integer, List<SellerGoodsPicVo>> colorImageMap = new HashMap<>();

    // SpecId 與 SpecName的映射
    Map<Integer, String> specMap = new HashMap<>();
    // SpecValueId 與 SpecValueName的映射
    Map<Integer, String> specValueMap = new HashMap<>();

    LinearLayout llSelectedSpecContainer;

    private int unityIndex;
    private String unitName;
    private int goodsModal=-1;//銷售模式 銷售模式 0零售 1跨城購 2虛擬 【必填】 5咨詢
    private int countryIndex;
    private int logoIndex;
    private boolean useFixedFreight =true;
    private int freightTemplateId =-1;
    private List<ListPopupItem> freightList=new ArrayList<>();
    private int freightRuleIndex;
    private int goodsState;//發佈到 0倉庫 1出售中
    private int joinBigSale;//1是0否參與促銷活動
    private SwitchButton sbJoinActivity;
    private int storeLabelId =-1;


    LinearLayout llDetailBody;

    RelativeLayout rlDetailBody;
    private int formatBottomIndex;
    private int formatTopIndex;
    private int formatBottom =-1;
    private int formatTop=-1;

    TextView etGoodsVideoUrl;
    private List<Category> selectCategoryList;
    private EasyJSONArray storeLabelIdList;
    private int limitBuy;
    private String limitBuyStartTime;
    private String limitBuyEndTime;
    private TwDate beginDate;
    private TwDate endDate;
    private TextView tvBeginDate;
    private TextView tvEndDate;
    private TimePickerView pvTime;
    private boolean isBiginDate;
    private LinearLayout llNoticeContainer;
    private EditText etLimitBuy;
    private int businessType;//經營模式 1資訊模式(只允許發佈資訊類商品) 2交易模式(可發佈所有類型商品)
    private final int TYPE_CONSULT=5 ;//咨詢
    private View consultContainer;
    private ScaledButton sbRetail;
    private ScaledButton sbVirtual;
    private ScaledButton sbAcross;
    private ScaledButton sbConsult;

    public static AddGoodsFragment newInstance() {

        return new AddGoodsFragment();
    }

    /**
     * 接收從Sku編輯器返回的結果
     * @param specValueIdStringMap
     */
    @Override
    public void setEditorResult(Map<String, SellerSpecPermutation> specValueIdStringMap, Map<Integer, List<SellerGoodsPicVo>> colorImageMap) {
        this.specValueIdStringMap = specValueIdStringMap;
        this.colorImageMap = colorImageMap;
        SLog.info("specValueIdStringMap[%s]", Util.specValueIdStringMapToJSONString(specValueIdStringMap));
    }


    void publish() {
        hideAddGuide();
//        testJson();
//        savePublishGoodsInfo();
    }


    void save() {
        ToastUtil.success(_mActivity,String.valueOf(llDetailBody.getChildCount()));
    }

    TextView tvTitle;

    ScaledButton sbNotice;

    NoScrollViewPager vpAddGood;

    void back() {
        new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
               .asCustom(new TwConfirmPopup(_mActivity, "確定要離開商品發佈頁嗎?", null, "離開頁面","繼續編輯",new OnConfirmCallback() {
            @Override
            public void onYes() {
                SLog.info("onYes");
                hideSoftInputPop();

            }

            @Override
            public void onNo() {
                SLog.info("onNo");
            }
        }))
                .show();
    }
//    @OnClick(R.id.et_add_good_name)
//    void showKeybord1() {
//        hideSoftInputPop();
//    }
//
//    @OnClick(R.id.rl_guide_container)
//    void hideGuide() {
//        hideAddGuide();
//
//    }

    private void hideAddGuide() {
        getView().findViewById(R.id.rl_guide_container).setVisibility(View.GONE);
        getView().findViewById(R.id.vp_seller_good_add).setVisibility(View.VISIBLE);
        vpAddGood.setCurrentItem(0);
    }


    void checkNotice() {
        sbNotice.setChecked(!sbNotice.isChecked());
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_seller_goods_publish, container, false);
        llDetailBody = (LinearLayout) view.findViewById(R.id.ll_detail_body);
        rlDetailBody = (RelativeLayout) view.findViewById(R.id.rl_detail_body);
        tvTitle = (TextView) view.findViewById(R.id.tv_title);
        sbNotice = (ScaledButton) view.findViewById(R.id.sb_check_notice);
        llNoticeContainer =  view.findViewById(R.id.ll_notice);
        vpAddGood = (NoScrollViewPager) view.findViewById(R.id.vp_seller_good_add);
        view.findViewById(R.id.sb_check_notice).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNotice();
            }
        });
        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        view.findViewById(R.id.btn_detail_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        view.findViewById(R.id.btn_publish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publish();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        colorImageMap.put(0, new ArrayList<>());  // 初始化無顏色的圖片列表
        initView();
        loadDate();

        Util.setOnClickListener(view, R.id.tv_title, this);
    }

    private void initView() {
        storeLabelIdList = new EasyJSONArray();
        publishGoodsInfo = new EasyJSONObject();
        sbNotice.setButtonCheckedBlue();
        boolean hideNotice = Hawk.get(SPField.SELLER_ADD_GUIDE_HIDE, false);
        SLog.info("%s",hideNotice);
        sbNotice.setChecked(hideNotice);
        if (hideNotice) {
            hideAddGuide();
        }
        sbNotice.setOnClickListener((v)->{
            boolean hide=Hawk.get(SPField.SELLER_ADD_GUIDE_HIDE, false);
            sbNotice.setChecked(!hide);
            SLog.info("%s",sbNotice.isChecked());
            Hawk.put(SPField.SELLER_ADD_GUIDE_HIDE, !hide);
        });
        mViews.add(primaryView());
        mViews.add(basicView());
        mViews.add(specView());
        mViews.add(detailView());
        mViews.add(freightView());
        mViews.add(othersView());
        mPagerAdapter = new SimpleViewPagerAdapter(_mActivity, mViews);
        vpAddGood.setAdapter(mPagerAdapter);
        vpAddGood.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setTitle(position);
                hideSoftInput();
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setTitle(int position) {
        switch (position) {
            case PRIMARY_INDEX:
                tvTitle.setText("基本信息");
                break;
            case BASIC_INDEX:
                tvTitle.setText("交易信息");
                break;
            case SPEC_INDEX:
                tvTitle.setText("規格與圖片");
                break;
            case 3:
                tvTitle.setText("詳情描述");
                break;
            case 4:
                tvTitle.setText("物流信息");
                break;
            case 5:
                tvTitle.setText("其它信息");
                break;
            default:
                break;
        }
    }

    private View othersView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_others_widget, vpAddGood, false);
//        view.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
//            @Override
//            public void onSystemUiVisibilityChange(int visibility) {
//                if (visibility == View.VISIBLE) {
//                    if (goodsModal == Constant.GOODS_TYPE_CONSULT) {
//
        consultContainer = view.findViewById(R.id.ll_consult_container);
        if (goodsModal == Constant.GOODS_TYPE_CONSULT) {
            consultContainer.setVisibility(View.INVISIBLE);
        }
//                    }
//                }
//            }
//        });
        Util.setOnClickListener(view, R.id.btn_others_prev, this);
        Util.setOnClickListener(view, R.id.btn_publish_goods, this);
        ScaledButton sbInstancePublish = view.findViewById(R.id.sb_instance_publish);
        ScaledButton sbAddHub = view.findViewById(R.id.sb_add_hub);

        LinearLayout llAddHub =view.findViewById(R.id.ll_add_hub);
        LinearLayout llInstancePublish =view.findViewById(R.id.ll_instance_publish);
        tvBeginDate = view.findViewById(R.id.tv_begin_date);
        etLimitBuy = view.findViewById(R.id.et_limit_num);
        tvEndDate = view.findViewById(R.id.tv_end_date);
        tvBeginDate.setOnClickListener((view1)->{
            isBiginDate = true;
            if (pvTime != null) {
                pvTime.setTitleText("開始時間");
                pvTime.show();
            }
        });
        tvEndDate.setOnClickListener((view1)->{
            isBiginDate = false;
            if (pvTime != null) {
                pvTime.setTitleText("結束時間");

                pvTime.show();
            }
        });

        sbInstancePublish.setButtonCheckedBlue();
        sbAddHub.setButtonCheckedBlue();
//        sbInstancePublish.setText("立即發佈");
//        sbAddHub.setText("放入倉庫");
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
        sbJoinActivity.setChecked(goodsModal != TYPE_CONSULT);//默认参与活动,除了咨詢型
        sbJoinActivity.setOnClickListener(v->{
            if (goodsModal == TYPE_CONSULT) {
                sbJoinActivity.setChecked(false);
            }
        });
        initTimePicker();

        return view;
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
                .setTitleText("选择時間")
                .setCancelText("取消")//取消按钮文字
                .setSubmitText("確認")//确认按钮文字
//                .setContentSize(18)//滚轮文字大小
                .setTitleSize(20)//标题文字大小
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
    private View freightView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_freight_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_freight_prev, this);
        Util.setOnClickListener(view, R.id.btn_freight_next, this);
        Util.setOnClickListener(view, R.id.tv_add_freight_rule, this);
        EditText fetFreight = view.findViewById(R.id.et_add_fixed_freight);
//        ScaledButton sbFixedFreight

        ScaledButton sbFreightTemple = view.findViewById(R.id.sb_freight_temple);

        ScaledButton sbFixedTemple = view.findViewById(R.id.sb_freight_solid);
        sbFixedTemple.setButtonCheckedBlue();
        sbFreightTemple.setButtonCheckedBlue();
        sbFixedTemple.setOnClickListener(v ->  {
            if (!sbFixedTemple.isChecked()) {
                sbFixedTemple.setChecked(true);
                freightTemplateId = -1;
            }
            sbFreightTemple.setChecked(false);
            useFixedFreight = sbFixedTemple.isChecked();
        });
        sbFreightTemple.setOnClickListener(v -> {
            if (!sbFreightTemple.isChecked()) {
                sbFreightTemple.setChecked(true);
            }
            sbFixedTemple.setChecked(false);
            useFixedFreight = !sbFreightTemple.isChecked();
        });
        sbFixedTemple.performClick();
        return view;
    }
    // 初始化 详情描述

    private View detailView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_detail_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_detail_next, this);
        Util.setOnClickListener(view, R.id.btn_detail_prev, this);
        Util.setOnClickListener(view, R.id.btn_select_store_category_id, this);
        TextView tvAddGoodsBody =view.findViewById(R.id.btn_add_app_description);
        TextView tvFormatTop =view.findViewById(R.id.tv_format_top);
        TextView tvFormatBottom =view.findViewById(R.id.tv_format_top);
        tvAddGoodsBody.setOnClickListener(v -> {
            rlDetailBody.setVisibility(View.VISIBLE);
        });

        return view;
    }
//    初始化 规格与图片页

    private View specView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_spec_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_spec_next, this);
        Util.setOnClickListener(view, R.id.btn_spec_prev, this);
        Util.setOnClickListener(view, R.id.btn_add_spec, this);

        llSelectedSpecContainer = view.findViewById(R.id.ll_selected_spec_container);
        etGoodsVideoUrl = view.findViewById(R.id.et_goods_video_url);
        view.findViewById(R.id.btn_view_sku_detail).setOnClickListener(this);
        return view;
    }


    /**
     * 更新選中的規格列表的顯示
     */
    private void updateSelectedSpecView() {
        // 先清空所有子View
        llSelectedSpecContainer.removeAllViews();


        for (SellerSpecMapItem sellerSpecMapItem : sellerSelectedSpecList) {
            ViewGroup selectedSpecItemView = (ViewGroup) LayoutInflater.from(_mActivity)
                    .inflate(R.layout.seller_selected_spec_value_desc, llSelectedSpecContainer, false);

            TextView textView = new TextView(_mActivity);
            textView.setTextSize(13);
            textView.setText("【" + sellerSpecMapItem.specName + "】   ");
            selectedSpecItemView.addView(textView, 0);


            for (SellerSpecItem sellerSpecItem : sellerSpecMapItem.sellerSpecItemList) {
                textView = new TextView(_mActivity);
                textView.setTextSize(13);
                textView.setText(sellerSpecItem.name + "   ");
                selectedSpecItemView.addView(textView, 1);
            }

            View btnEdit = selectedSpecItemView.findViewById(R.id.btn_edit);
            btnEdit.setTag(sellerSpecMapItem.specId);

            btnEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int specId = (int) v.getTag();
                    SLog.info("specId[%d]", specId);


                    SellerSpecMapItem item = sellerSpecMap.get(specId);
                    if (item == null) {
                        return;
                    }


                    new XPopup.Builder(_mActivity)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new SellerSelectSpecPopup(_mActivity, Constant.ACTION_EDIT, specId, item.sellerSpecItemList, null, AddGoodsFragment.this))
                            .show();
                }
            });

            LinearLayout.MarginLayoutParams layoutParams = new LinearLayout.MarginLayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.topMargin = Util.dip2px(_mActivity, 8);
            llSelectedSpecContainer.addView(selectedSpecItemView, layoutParams);
        }
    }

    private View basicView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_basic_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_basic_next, this);
        Util.setOnClickListener(view, R.id.btn_basic_prev, this);
        Util.setOnClickListener(view, R.id.tv_add_good_unit, this);
         sbRetail = view.findViewById(R.id.sb_retail);
         sbVirtual = view.findViewById(R.id.sb_virtual);
         sbAcross = view.findViewById(R.id.sb_across);
         sbConsult = view.findViewById(R.id.sb_consult);
        view.findViewById(R.id.ll_consult_container).setVisibility(View.VISIBLE);
        sbRetail.setButtonCheckedBlue();
        sbVirtual.setButtonCheckedBlue();
        sbAcross.setButtonCheckedBlue();
        sbConsult.setButtonCheckedBlue();
        sbRetail.setOnClickListener(v -> {
            sbRetail.setChecked(!sbRetail.isChecked());
            if (sbRetail.isChecked()) {
                goodsModal = 0;
                sbVirtual.setChecked(false);
                sbAcross.setChecked(false);
                sbConsult.setChecked(false);

            } else {
                if (sbVirtual.isChecked() || sbAcross.isChecked()||sbConsult.isChecked()) {

                } else {
                    goodsModal = -1;
                }
            }
        });
        sbVirtual.setOnClickListener(v -> {
            sbVirtual.setChecked(!sbVirtual.isChecked());
            if (sbVirtual.isChecked()) {
                goodsModal = 2;
                sbRetail.setChecked(false);
                sbAcross.setChecked(false);
                sbConsult.setChecked(false);

            }else {
                if (sbRetail.isChecked() || sbAcross.isChecked()||sbConsult.isChecked()) {
                } else {
                    goodsModal = -1;
                }
            }
        });
        sbAcross.setOnClickListener(v -> {
            sbAcross.setChecked(!sbAcross.isChecked());

            if (sbAcross.isChecked()) {
                goodsModal = 1;

                sbRetail.setChecked(false);
                sbVirtual.setChecked(false);
                sbConsult.setChecked(false);
            }else {
                if (sbRetail.isChecked() || sbVirtual.isChecked()||sbConsult.isChecked()) {
                } else {
                    goodsModal = -1;
                }
            }
        });
        sbConsult.setOnClickListener(v->{
            sbConsult.setChecked(!sbConsult.isChecked());
            if( sbConsult.isChecked()){
                goodsModal = TYPE_CONSULT;
                if (sbJoinActivity != null&&sbJoinActivity.isChecked()) {
                    sbJoinActivity.setChecked(false);
                }
                if (consultContainer != null) {
                    consultContainer.setVisibility(View.INVISIBLE);
                }
                if (sbRetail.isChecked()) {
                    sbRetail.setChecked(false);
                }
                if (sbAcross.isChecked()) {
                    sbAcross.setChecked(false);
                }
                if (sbVirtual.isChecked()) {
                    sbVirtual.setChecked(false);
                }
            }

        });
        return view;
    }


    private View primaryView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_primary_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_primary_next, this);
        Util.setOnClickListener(view, R.id.tv_add_good_location, this);
        Util.setOnClickListener(view, R.id.tv_add_good_logo, this);
        TextView btnGoodCategory =view.findViewById(R.id.btn_select_category_id);
//        Util.setOnClickListener(view,R.id.sp_add_good_logo,this);
        EditText etName =view.findViewById(R.id.et_add_good_name);
        etName.setOnClickListener(v -> showSoftInput(view));
        btnGoodCategory.setOnClickListener(v -> {
            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new StoreLabelPopup(_mActivity, PopupType.STORE_LABEL,this))
                    .show();
        });
        loadGoodsCountry(view);
//        btnGoodCategory.setBackground(UiUtil.tvButtonBackGround());

        return view;
    }

    private void loadGoodsCountry(View view) {
        String url = Api.PATH_SELLER_QUERY_COUNTRY_ALL;
         EasyJSONObject params =EasyJSONObject.generate("token", User.getToken());
         SLog.info("params[%s]", params);
         Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }
        
            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
        
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }
                    EasyJSONArray countryList = responseObj.getArray("datas.countryList");
                    if (countryList != null) {
                        spinnerLogoCountryItems.clear();
                        for (Object o : countryList) {
                            AdminCountry item = AdminCountry.parase(o);
                            spinnerLogoCountryItems.add(new ListPopupItem(item.getCountryId(),item.getCountryCn(),item));
                        }
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
         });     
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void loadDate() {
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(),"isPublish",1);
        String url = Api.PATH_SELLER_GOODS_PUBLISH_PAGE;
        if (Config.DEVELOPER_MODE) {
            // url = "https://test.snailpad.cn/tmp/3.json";
        }

        SLog.info("url[%s], params[%s]", url, params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }


            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    EasyJSONObject data = responseObj.getObject("datas");
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        hideSoftInputPop();
                        return;
                    }
                    updateView(data);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    private void updateView(EasyJSONObject data) throws Exception {
        allowTariff = data.getInt("allowTariff");//1是0否允許發佈跨城購商品
        businessType  = data.getInt("businessType");//經營模式 1資訊模式(只允許發佈資訊類商品) 2交易模式(可發佈所有類型商品)
        specMax = data.getInt("specMax");//允許添加的最大規格數量
        int specValueMax = data.getInt("specValueMax");//允許添加的最大規格數量值
        EasyJSONArray formatBottomList = data.getArray("formatBottomList");//底部關聯版式列表
        EasyJSONArray formatTopList = data.getArray("formatTopList");//頂部關聯版式列表
        EasyJSONArray countryList = data.getArray("countyrList");//品牌所在地列表
        EasyJSONArray specListArr = data.getArray("specList");//規格列表


        // 處理規格列表
        for (Object object : specListArr) {
            EasyJSONObject spec = (EasyJSONObject) object;
            int specId = spec.getInt("specId");

            SellerSpecMapItem sellerSpecMapItem = new SellerSpecMapItem();
            sellerSpecMapItem.specId = specId;
            sellerSpecMapItem.specName = spec.getSafeString("specName");

            specMap.put(sellerSpecMapItem.specId, sellerSpecMapItem.specName);


            EasyJSONArray specValueList = spec.getSafeArray("specValueList");
            for (Object object2 : specValueList) {
                EasyJSONObject specValue = (EasyJSONObject) object2;

                SellerSpecItem sellerSpecItem = new SellerSpecItem();
                sellerSpecItem.id = specValue.getInt("specValueId");
                sellerSpecItem.name = specValue.getSafeString("specValueName");

                specValueMap.put(sellerSpecItem.id, sellerSpecItem.name);

                sellerSpecMapItem.sellerSpecItemList.add(sellerSpecItem);
            }

            sellerSpecMap.put(specId, sellerSpecMapItem);
        }

        updatePrimaryView(data);
        updateBasicView(data);
        updateSpecView(data);
        updateDetailView(data);
        updateFreightView(data);
    }




    private void updateFreightView(EasyJSONObject data)throws Exception {
        EasyJSONArray freightTemplateList = data.getArray("freightTemplateList");
        if(freightTemplateList!=null){
            freightList.clear();

            for (Object object : freightTemplateList) {
                int freightId =((EasyJSONObject)object).getInt("freightId");
                String title = ((EasyJSONObject) object).getSafeString("title");
                freightList.add(new ListPopupItem(freightId, title, title));
            }
        }

    }

    private void updateBasicView(EasyJSONObject data) throws Exception{
        EasyJSONArray unitListJson =data.getArray("unitList");
        if (unitListJson != null) {
            unitList.clear();
            for (Object object : unitListJson) {
                ListPopupItem item = new ListPopupItem(0,"",null);
                item.id = ((EasyJSONObject) object).getInt("id");
                item.title = ((EasyJSONObject) object).getSafeString("name");
                item.data = item.title;
                unitList.add(item);
            }
            onSelected(PopupType.GOODS_UNITY,unityIndex,unitList.get(unityIndex));
        }

            if (businessType == Constant.CONSULT_STORE) {
                View view = mViews.get(BASIC_INDEX);
                view.findViewById(R.id.ll_retail).setVisibility(View.GONE);
                view.findViewById(R.id.ll_across_container).setVisibility(View.GONE);
                goodsModal = Constant.GOODS_TYPE_CONSULT;
                sbConsult.performClick();
            } else {
                sbRetail.performClick();
            }


    }

    //    更新商品详情页数据
    private void updateDetailView(EasyJSONObject data) throws Exception{
        EasyJSONArray storeLabelList = data.getArray("storeLabelList");
        if (storeLabelList != null) {
            List<StoreLabel> list = new ArrayList<>();
            for (Object o : storeLabelList) {
                list.add(StoreLabel.parse(((EasyJSONObject) o)));
            }
            TextView tvSelect=mViews.get(DETAIL_INDEX).findViewById(R.id.btn_select_store_category_id);
            tvSelect.setOnClickListener(v ->{
                hideSoftInput();
                if (list != null && list.size() != 0) {
                    new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(
                            new StoreLabelPopup(_mActivity, PopupType.STORE_CATEGORY, list, AddGoodsFragment.this)
                    ).show();
                } else {
                    ToastUtil.error(_mActivity,"當前店鋪沒有分類數據");
                    storeLabelId = 0;
                }
            });
        }

        EasyJSONArray formatBottomList = data.getArray("formatBottomList");

        if (formatBottomList != null) {
            List<ListPopupItem> list = new ArrayList<>();
            for (Object o : formatBottomList) {
                Format format = Format.parse(((EasyJSONObject) o));
                ListPopupItem item = new ListPopupItem(format.getFormatId(),format.getFormatName(),format);
                list.add(item);
            }
            TextView tvBottom=mViews.get(DETAIL_INDEX).findViewById(R.id.tv_format_bottom);
            OnSelectedListener listener = this;
            tvBottom.setOnClickListener(v ->{
                new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(
                        new ListPopup(_mActivity,"底部版式", PopupType.SELLER_FORMAT_BOTTOM, list, formatBottomIndex, this)
                ).show();
            });
        }
        EasyJSONArray formatTopList = data.getArray("formatTopList");


        if (formatTopList != null) {
            List<ListPopupItem> list = new ArrayList<>();
            for (Object o : formatTopList) {
                Format format = Format.parse(((EasyJSONObject) o));
                ListPopupItem item = new ListPopupItem(format.getFormatId(),format.getFormatName(),format);

                list.add(item);
            }
            TextView tvFormatTop=mViews.get(DETAIL_INDEX).findViewById(R.id.tv_format_top);
            OnSelectedListener listener = this;
            tvFormatTop.setOnClickListener(v ->{
                new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(
                        new ListPopup(_mActivity, "頂部版式",PopupType.SELLER_FORMAT_TOP, list,formatTopIndex,listener)
                ).show();
            });
        }
    }

    //    更新商品规格页数据
    private void updateSpecView(EasyJSONObject data) {

    }


    private void updatePrimaryView(EasyJSONObject data) throws Exception {
        EasyJSONArray storeLabelList = data.getArray("storeLabelList");//店内分類列表
        labelList = new ArrayList<>();
        for (Object storelabel : storeLabelList) {
            labelList.add(StoreLabel.parse((EasyJSONObject) storelabel));
            SLog.info(labelList.get(0).getStoreLabelName());
        }
        
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
        switch (id) {
            case R.id.tv_title:
                SLog.info("publishGoodsInfo[%s]", publishGoodsInfo.toString());
                break;
            case R.id.btn_primary_next:
                if (Config.DEVELOPER_MODE) {
                    // vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() + 2);
                }
                if (!savePrimaryInfo()) {
                    break;
                }
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() + 1);
                break;
            case R.id.sp_add_good_logo:
                if (categoryId < 0) {
                    ToastUtil.error(_mActivity,"先選擇商品類別");
                }
                break;
            case R.id.tv_add_good_location:
                hideSoftInput();

                new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(new ListPopup(_mActivity, "品牌所在地", PopupType.GOODS_LOCATION, spinnerLogoCountryItems, countryIndex, this)).show();
                break;
            case R.id.tv_add_good_logo:
                if (spinnerLogoItems.size() == 0) {
                    ToastUtil.error(_mActivity,"該分類暫時沒有可選品牌");
                    break;
                }
                hideSoftInput();
                new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(new ListPopup(_mActivity, "品牌", PopupType.GOODS_LOGO, spinnerLogoItems, logoIndex, this)).show();
                break;
            case R.id.btn_basic_next:
                if (!saveBasicInfo()) {
                    break;
                }
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() + 1);

                break;
            case R.id.btn_basic_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() - 1);

                break;
            case R.id.tv_add_good_unit:
                if (unitList == null || unitList.size() == 0) {
                    ToastUtil.success(_mActivity,"未獲取單位列表數據");
                    return;
                }
                new XPopup.Builder(_mActivity)
                        .moveUpToKeyboard(false)
                        .asCustom(new ListPopup(_mActivity,"計量單位",
                                PopupType.GOODS_UNITY,unitList, unityIndex,this))
                        .show();
                break;
            case R.id.btn_spec_next:
                String errMsg = saveSpecInfo();
                if (!StringUtil.isEmpty(errMsg)) {
                    ToastUtil.error(_mActivity, errMsg);
                    break;
                }

                if (Config.USE_DEVELOPER_TEST_DATA) {
                    break;
                }
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() + 1);

                break;
            case R.id.btn_spec_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() - 1);

                break;
            case R.id.btn_view_sku_detail:
                if (goodsModal == Constant.GOODS_TYPE_CONSULT) {  // 諮詢型商品，沒有規格
                    start(SellerSkuEditorFragment.newInstance(this,
                            specValueIdStringList,
                            specValueIdStringMap,
                            null,
                            colorImageMap, true));

                    return;
                }

                // 查看SKU詳情
                if (sellerSelectedSpecList.size() < 1) {
                    ToastUtil.error(_mActivity, "請先添加規格");
                    return;
                }

                // 查看是否有选【颜色】规格
                SellerSpecMapItem colorItem = sellerSpecMap.get(Constant.COLOR_SPEC_ID);
                SellerSpecMapItem colorSpecMapItem = null;
                if (colorItem != null && colorItem.selected) { // 如果有選顏色
                    colorSpecMapItem = new SellerSpecMapItem();

                    colorSpecMapItem.specId = colorItem.specId;
                    colorSpecMapItem.specName = colorItem.specName;

                    for (SellerSpecItem elem : colorItem.sellerSpecItemList) {
                        if (elem.selected) {
                            colorSpecMapItem.sellerSpecItemList.add(elem);
                        }
                    }
                }

                start(SellerSkuEditorFragment.newInstance(this,
                        specValueIdStringList,
                        specValueIdStringMap,
                        colorSpecMapItem,
                        colorImageMap));
                break;
            case R.id.btn_add_address:
                ToastUtil.success(_mActivity, "添加商品描述");
                break;
            case R.id.btn_detail_next:
                if (!saveDetailInfo()) {
                    break;
                }
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() + 1);
                break;
            case R.id.btn_detail_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() - 1);

                break;
            case R.id.btn_freight_next:
                if (!saveFreightInfo()) {
                    break;
                }
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() + 1);
                break;

            case R.id.tv_add_freight_rule:
                hideSoftInput();
                new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(new ListPopup(_mActivity, "物流規則", PopupType.GOODS_FREIGHT_RULE, freightList, freightRuleIndex, this)).show();
                break;
            case R.id.btn_freight_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() - 1);

                break;
            case R.id.btn_publish_goods:
                saveOthersInfo();
                commitGoodsInfo();
                break;
            case R.id.btn_others_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() - 1);
                break;
            case R.id.btn_add_spec:
                if (goodsModal == Constant.GOODS_TYPE_CONSULT) {
                    ToastUtil.error(_mActivity, "諮詢型商品不可添加規格");
                    return;
                }

                if (sellerSpecMap.size() < 1) {
                    ToastUtil.error(_mActivity, "沒有可用的規格");
                    return;
                }

                List<SellerSpecItem> sellerSpecItemList = new ArrayList<>();
                Map<Integer, List<SellerSpecItem>> sellerSpecValueMap = new HashMap<>();

                for (Map.Entry<Integer, SellerSpecMapItem> entry : sellerSpecMap.entrySet()) {
                    if (entry.getValue().selected) {
                        continue;
                    }

                    SellerSpecItem sellerSpecItem = new SellerSpecItem();
                    sellerSpecItem.id = entry.getKey();
                    sellerSpecItem.name = entry.getValue().specName;

                    sellerSpecItemList.add(sellerSpecItem);
                    sellerSpecValueMap.put(entry.getKey(), entry.getValue().sellerSpecItemList);
                }


                if (sellerSpecItemList.size() < 1) {
                    ToastUtil.error(_mActivity, "已添加所有的規格");
                    return;
                }

                new XPopup.Builder(_mActivity)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new SellerSelectSpecPopup(_mActivity, Constant.ACTION_ADD, 0, sellerSpecItemList, sellerSpecValueMap, this))
                        .show();
                break;
            default:
                break;
        }

    }


    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        SLog.info("onFragmentResult, requestCode[%d], resultCode[%d]", requestCode, resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }

//        try {
//            if (requestCode == RequestCode.SELLER_EDIT_SKU_INFO.ordinal()) {
//                SLog.info("data[%s]", data.toString());
//                String result = data.getString("result");
//                if (StringUtil.isEmpty(result)) {
//                    return;
//                }
//                SLog.info("result[%s]", result);
//
//                EasyJSONObject resultObj = EasyJSONBase.parse(result);
//
//                goodsJsonVoList = resultObj.getArray("goodsJsonVoList");
//                goodsPicVoList = resultObj.getArray("goodsPicVoList");
//            }
//        } catch (Exception e) {
//            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
//        }
    }


    /**
     * 保存规格信息
     * @return  错误消息： 如果成功，返回null
     *                   如果失败，返回具体的错误消息
     */
    private String saveSpecInfo() {
        try {
            SLog.info("sellerSelectedSpecList.size[%d]", sellerSelectedSpecList.size());
            if (goodsModal != Constant.GOODS_TYPE_CONSULT  // 諮詢型商品沒有規格
                    && sellerSelectedSpecList.size() < 1) {
                return "請添加商品規格";
            }

            /*
            "specJsonVoList": [ #商品規格
                {
                    "specId": 1, #規格ID ，選擇了specId=1時，添加規格圖片
                    "specName": "顔色", #規格名稱
                    "specValueList": [ #規格值
                        {
                            "specValueId": 16, #規格值ID
                            "specValueName": "A+B" #規格值名稱
                        },
                        {
                            "specValueId": 18,
                            "specValueName": "A"
                        }
                    ]
                }
            ]
             */

            EasyJSONArray specJsonVoList = EasyJSONArray.generate();
            for (SellerSpecMapItem item : sellerSelectedSpecList) {
                EasyJSONArray specValueArr = EasyJSONArray.generate();
                for (SellerSpecItem specItem : item.sellerSpecItemList) {
                    specValueArr.append(
                            EasyJSONObject.generate(
                                    "specValueId", specItem.id,
                                    "specValueName", specItem.name
                            )
                    );
                }

                specJsonVoList.append(EasyJSONObject.generate(
                        "specId", item.specId,
                        "specName", item.specName,
                        "specValueList", specValueArr));
            }
            SLog.info("specJsonVoList[%s]", specJsonVoList.toString());
            publishGoodsInfo.set("specJsonVoList", specJsonVoList);

            EasyJSONArray goodsJsonVoList = EasyJSONArray.generate();
            // 生成PermutationList
            for (String specValueIdString : specValueIdStringList) {
                SellerSpecPermutation permutation = specValueIdStringMap.get(specValueIdString);
                /*
                "goodsSpecs": "A+B,,,XXS", #規格值 多個用三個英文逗號,,,隔開
            "goodsFullSpecs": "顔色：A+B；尺碼：XXS", #完整規格
            "specValueIds": "16,35", #規格值ID，多個用英文逗號隔開
            "goodsPrice0": 10, #sku價格
            "goodsSerial": "", #商家編號
            "goodsStorage": 20, #縂庫存
            "colorId": 16, #主規格值ID
            "reserveStorage": #預留庫存
                 */
                goodsJsonVoList.append(EasyJSONObject.generate(
                        "specValueIds", permutation.specValueIdString,
                        "goodsPrice0", permutation.price,
                        "goodsSerial", permutation.goodsSN,
                        "goodsStorage", permutation.storage,
                        "colorId", permutation.colorId,
                        "reserveStorage", permutation.reserved
                ));
            }


            EasyJSONArray goodsPicVoList = EasyJSONArray.generate();
            for (Map.Entry<Integer, List<SellerGoodsPicVo>> entry: colorImageMap.entrySet()) {
                /*
                {
                    "colorId": 16, #規格值ID
                    "imageName": "image/a8/5a/a85a133391e94416decc6668f5334bdf.jpg", #圖片路徑
                    "imageSort": 0, #圖片排序
                    "isDefault": 1 #1是0否主圖，同一組(colorId相同)規格只有一張主圖
                }
                 */
                List<SellerGoodsPicVo> picVoList = entry.getValue();
                for (SellerGoodsPicVo vo : picVoList) {
                    goodsPicVoList.append(EasyJSONObject.generate(
                            "colorId", vo.colorId,
                            "imageName", vo.imageName,
                            "imageSort", vo.imageSort,
                            "isDefault", vo.isDefault
                    ));
                }

            }
            publishGoodsInfo.set("goodsPicVoList", goodsPicVoList);

            if (specValueIdStringList.size() == 0&&goodsModal==Constant.GOODS_TYPE_CONSULT) {
//                for(int i=0;i<goodsPicVoList.length();i++)
//                goodsJsonVoList.append(EasyJSONObject.generate(
//                        "specValueIds", "0",
//                        "goodsPrice0", 0,
//                        "goodsStorage", 0,
//                        "reserveStorage", 0
//                ));
                goodsJsonVoList.append(EasyJSONObject.generate(
                        "specValueIds", "0",
                        "goodsPrice0", 0,
                        "goodsStorage", 0,
                        "reserveStorage", 0
                ));
            }
            publishGoodsInfo.set("goodsJsonVoList", goodsJsonVoList);


            // 處理商品視頻
            String goodsVideoUrl = etGoodsVideoUrl.getText().toString().trim();
            if (!StringUtil.isEmpty(goodsVideoUrl)) {
                publishGoodsInfo.set("goodsVideo", goodsVideoUrl);
            }

            return null;
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));

            return "保存規格數據失敗";
        }
    }

    private boolean saveDetailInfo() {
        EditText etVideos = mViews.get(DETAIL_INDEX).findViewById(R.id.et_goods_video_url);
        String detailVideo = "";
        if (etVideos.getText() != null) {
            detailVideo = etVideos.getText().toString();
        }
        if (storeLabelId < 0) {
            ToastUtil.error(_mActivity,"請選擇店内分類");
            return false;
        }
        try{
            publishGoodsInfo.set("storeLabelIdList", storeLabelIdList);
            publishGoodsInfo.set("detailVideo", detailVideo);
            if (formatBottom >= 0) {
                publishGoodsInfo.set("formatBottom", formatBottom);
            }
            if (formatTop >= 0) {
                publishGoodsInfo.set("formatTop", formatTop);
            }
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        return true;
    }

    private void saveOthersInfo() {
        joinBigSale = sbJoinActivity.isChecked() ? Constant.TRUE_INT : Constant.FALSE_INT;
        String limitbuyStr = etLimitBuy.getText().toString();
        if (!StringUtil.isEmpty(limitbuyStr)) {
            limitBuy = Integer.parseInt(etLimitBuy.getText().toString());
        } else {
            limitBuy=0;//不限購
        }
        if (limitBuy > 0) {
            if (StringUtil.isEmpty(limitBuyStartTime)) {
                ToastUtil.success(_mContext, "請填寫限購開始時間");
            } else if (StringUtil.isEmpty(limitBuyEndTime)) {
                ToastUtil.success(_mContext, "請填寫限購結束時間");
            }
            if (!StringUtil.isEmpty(limitBuyStartTime) && !StringUtil.isEmpty(limitBuyEndTime)) {
                if(Jarbon.parse(limitBuyEndTime).getTimestamp() <= Jarbon.parse(limitBuyStartTime).getTimestamp()){
                    ToastUtil.success(_mContext, "結束時間必須大於開始時間");
                };
            }
        }
        try{
            publishGoodsInfo.set("joinBigSale", joinBigSale);
            publishGoodsInfo.set("goodsState", goodsState);
            publishGoodsInfo.set("limitBuy", limitBuy);
            if (limitBuy > 0) {
                publishGoodsInfo.set("limitBuyStartTime", limitBuyStartTime);
                publishGoodsInfo.set("limitBuyEndTime", limitBuyEndTime);
            }
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private boolean saveFreightInfo() {
        View view = mViews.get(FREIGHT_INDEX);
        EditText etFreight = view.findViewById(R.id.et_add_fixed_freight);
        String freightText = etFreight.getText()==null?"":etFreight.getText().toString();
        if (useFixedFreight) {
            if (StringUtil.isEmpty(freightText)) {
                freightText = "0";
                etFreight.setText(freightText);
//                ToastUtil.error(_mActivity, "請填寫運費");
//                return false;
            }
        } else {
            if (freightTemplateId <= 0) {
                ToastUtil.error(_mActivity, "請選擇運費模板");
                return false;
            }
        }
        EditText etW=view.findViewById(R.id.et_freight_weight);
        EditText etV=view.findViewById(R.id.et_freight_v);
        String freightWeightStr = etW.getText()==null?"":etW.getText().toString();
        String freightVolumeStr = etV.getText()==null?"":etV.getText().toString();

        try{
            if (freightTemplateId >= 0) {
                publishGoodsInfo.set("freightTemplateId", freightTemplateId);
            } else {
                double goodsFreight = Double.parseDouble(freightText);
                publishGoodsInfo.set("goodsFreight", goodsFreight);
            }
            if (!StringUtil.isEmpty(freightWeightStr)) {

                publishGoodsInfo.set("freightWeight", Double.parseDouble(freightWeightStr));
            }
            if (!StringUtil.isEmpty(freightVolumeStr)) {

                publishGoodsInfo.set("freightVolume", Double.parseDouble(freightVolumeStr));
            }
        }catch (Exception e) {
           SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }


        return true;
    }

    private boolean saveBasicInfo() {
        if (StringUtil.isEmpty(unitName)) {
            ToastUtil.error(_mActivity,"請選擇計量單位");
            return false;
        }
        if (goodsModal < 0) {
            ToastUtil.error(_mActivity,"請選擇銷售模式");
            return false;
        }
        try{
            publishGoodsInfo.set("unitName", unitName);
            publishGoodsInfo.set("goodsModal", goodsModal);
        }catch (Exception e) {
           SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        return true;
    }

    private boolean savePrimaryInfo() {
        View primaryView = mViews.get(PRIMARY_INDEX);
        EditText etName=primaryView.findViewById(R.id.et_add_good_name);
        String goodsName =etName.getText().toString();
        if (StringUtil.isEmpty(goodsName)) {
            ToastUtil.error(_mActivity,"請填寫商品名稱");
            return false;
        } else if (goodsName.length()<3||goodsName.length()>50) {
            ToastUtil.error(_mActivity,"商品名稱為3到50個字符");
            //暫無可選品牌
            return false;
        }
        if (categoryId < 0) {
            ToastUtil.error(_mActivity,"請選擇商品分類");
            return false;
        }
        if (goodsCountry < 0) {
            ToastUtil.error(_mActivity,"請選擇商品品牌所在地");
            return false;
        }
        EditText etJingle=primaryView.findViewById(R.id.et_add_good_description);
        String jingle =etJingle.getText().toString();
//        if (StringUtil.isEmpty(jingle)) {
//            ToastUtil.error(_mActivity,"請填寫商品賣點");
//            return false;
//        }
        try {
            publishGoodsInfo.set("goodsName", goodsName);
            publishGoodsInfo.set("categoryId", categoryId);
            publishGoodsInfo.set("jingle", jingle);

            for (int i = 0; i < 3; i++) {
                String keyName = String.format("categoryId%d", (i + 1));
                if (i < selectCategoryList.size()) {
                    Category category = selectCategoryList.get(i);
                    publishGoodsInfo.set(keyName, category.getCategoryId());
                } else { // 固定有3個category，如果不夠，填0
                    publishGoodsInfo.set(keyName, 0);
                }
            }

            publishGoodsInfo.set("brandId", brandId);
            publishGoodsInfo.set("goodsCountry", goodsCountry);
            SLog.info("publishInfo [%s]",publishGoodsInfo.toString());
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            return false;
        }
        return true;
    }

//    收集表單信息，驗證信息符合要求后提交

    private void commitGoodsInfo() {
        savePublishGoodsInfo();
    }

    private void testJson() {
        String token = User.getToken();
        String url = Api.PATH_SELLER_GOODS_PUBLISH_SAVE + "?token=" + token;
        String testJson = "";
        testJson = AssetsUtil.loadText(_mActivity, "tangram/seller_order.json");
        testJson = AssetsUtil.loadText(_mActivity, "tangram/test.json");
        SLog.info("params[%s]",testJson);
        Api.postJsonUi(url, testJson ,new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }
                    ToastUtil.success(_mActivity,responseObj.getSafeString("datas.success"));
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
        try{
        }catch (Exception e) {
           SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

    }
    private void savePublishGoodsInfo() {
        String token = User.getToken();
//        token = "e6b1594f0ce04d7cbf01163121a44fcd";
        String url = Api.PATH_SELLER_GOODS_PUBLISH_SAVE + "?token=" + token;
        SLog.info("url[%s]\nparams[%s]",url,publishGoodsInfo.toString());
        Api.postJsonUi(url, publishGoodsInfo.toString() ,new UICallback() {
             @Override
             public void onFailure(Call call, IOException e) {
                 LogUtil.uploadAppLog(url, publishGoodsInfo.toString(), "", e.getMessage());
                 ToastUtil.showNetworkError(_mActivity, e);
             }

             @Override
             public void onResponse(Call call, String responseStr) throws IOException {
                 try {
                     SLog.info("responseStr[%s]", responseStr);
                     EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                     if (ToastUtil.checkError(_mActivity, responseObj)) {
                         LogUtil.uploadAppLog(url, publishGoodsInfo.toString(), responseStr, "");
                         return;
                     }
                     ToastUtil.success(_mActivity,responseObj.getSafeString("datas.success"));
                     hideSoftInputPop();
                 } catch (Exception e) {
                     SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                 }
             }
          });
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("type[%s], id[%d], extra[%s]", type, id, extra);
        try {
            if (type == PopupType.SELECT_BEGIN_DATE) {
                beginDate = (TwDate) extra;
                tvBeginDate.setText(beginDate.toString());
            } else if (type == PopupType.SELECT_END_DATE) {
                endDate = (TwDate) extra;
                tvEndDate.setText(endDate.toString());
            }
            if (type == PopupType.SELLER_SELECT_SPEC) {
                EasyJSONObject dataObj = (EasyJSONObject) extra;
                int specId = dataObj.getInt("specId");
                int action = dataObj.getInt("action");
                EasyJSONArray specValueIdList = dataObj.getArray("specValueIdList");

                // 设置选中状态
                SellerSpecMapItem selectedItem = sellerSpecMap.get(specId);
                if (selectedItem == null) {
                    return;
                }

                selectedItem.selected = true;
                for (SellerSpecItem elem : selectedItem.sellerSpecItemList) {
                    elem.selected = false;
                }

                SellerSpecMapItem item = null;
                if (action == Constant.ACTION_ADD) {
                    item = new SellerSpecMapItem();
                } else {
                    for (SellerSpecMapItem elem : sellerSelectedSpecList) {
                        if (elem.specId == specId) {
                            item = elem;
                            item.sellerSpecItemList.clear();
                            break;
                        }
                    }
                }

                if (item == null) {
                    return;
                }

                item.specId = specId;
                item.specName = specMap.get(specId);
                for (Object object : specValueIdList) {
                    int specValueId = (int) object;

                    SellerSpecItem sellerSpecItem = new SellerSpecItem();
                    sellerSpecItem.id = specValueId;
                    sellerSpecItem.name = specValueMap.get(specValueId);
                    item.sellerSpecItemList.add(sellerSpecItem);

                    for (SellerSpecItem elem : selectedItem.sellerSpecItemList) {
                        if (elem.id == specValueId) {
                            elem.selected = true;
                            break;
                        }
                    }
                }

                if (action == Constant.ACTION_ADD) {
                    sellerSelectedSpecList.add(item);
                }

                updateSelectedSpecView();

                generateSpecPermutation();
            } else if (type == PopupType.STORE_LABEL) {
                selectCategoryList = (List<Category>) extra;
                Category categoryLast = new Category();
                StringBuilder selectCategoryName = new StringBuilder();
                for (Category category : selectCategoryList) {
                    categoryLast = category;
                    selectCategoryName.append(category.getCategoryName()).append(" -- ");
                }
                if (!StringUtil.isEmpty(selectCategoryName.toString())) {
                    selectCategoryName.delete(selectCategoryName.length() - 4, selectCategoryName.length() - 1);
                    if (vpAddGood.getCurrentItem() == PRIMARY_INDEX) {
                        ((TextView) (mViews.get(PRIMARY_INDEX).findViewById(R.id.tv_category_id))).setText(selectCategoryName.toString());
                        categoryId = categoryLast.getCategoryId();
                        updateLogoInfo();
                    } else if (vpAddGood.getCurrentItem() == DETAIL_INDEX) {
                        ((TextView) (mViews.get(DETAIL_INDEX).findViewById(R.id.tv_select_store_category))).setText(selectCategoryName.toString());
                        storeLabelId = categoryLast.getCategoryId();
                        EasyJSONArray array = new EasyJSONArray();
                        for (Category category : selectCategoryList) {
                            array.append(category.getCategoryId());
                        }
                        storeLabelIdList = array;
                    }
                }

            } else if (type == PopupType.GOODS_UNITY) {
                TextView tvUnit = mViews.get(BASIC_INDEX).findViewById(R.id.tv_add_good_unit);
                unityIndex = id;
                unitName = extra.toString();
                tvUnit.setText(unitName);
            } else if (type == PopupType.GOODS_LOCATION) {
                TextView tvLocation = mViews.get(PRIMARY_INDEX).findViewById(R.id.tv_add_good_location);
                countryIndex = id;
                AdminCountry item = (AdminCountry) extra;
                goodsCountry = item.getCountryId();
                tvLocation.setText(item.getCountryCn());

            } else if (type == PopupType.GOODS_LOGO) {
                TextView tvLogo = mViews.get(PRIMARY_INDEX).findViewById(R.id.tv_add_good_logo);
                logoIndex = id;
                Brand item = (Brand) extra;
                brandId = item.getBrandId();
                tvLogo.setText(item.getBrandName());

            } else if (type == PopupType.GOODS_FREIGHT_RULE) {
                TextView tvRule = mViews.get(FREIGHT_INDEX).findViewById(R.id.tv_add_freight_rule);
                freightRuleIndex = id;
                freightTemplateId = freightList.get(id).id;
                tvRule.setText(extra.toString());
            } else if (type == PopupType.SELLER_FORMAT_TOP) {
                TextView tvFormatTop = mViews.get(DETAIL_INDEX).findViewById(R.id.tv_format_top);
                tvFormatTop.setText(((Format) extra).getFormatName());
                formatTopIndex = id;
                formatTop = ((Format) extra).getFormatId();
            } else if (type == PopupType.SELLER_FORMAT_BOTTOM) {
                TextView tvFormatBottom = mViews.get(DETAIL_INDEX).findViewById(R.id.tv_format_bottom);
                tvFormatBottom.setText(((Format) extra).getFormatName());
                formatBottomIndex = id;
                formatBottom = ((Format) extra).getFormatId();
            } else if (type == PopupType.DEFAULT) {
                updateLogoInfo();
//        String[] spinnerItems = new String[labelList.size()];
                //自定义选择填充后的字体样式
                //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    private void updateLogoInfo() {
        TextView tvLogo = mViews.get(PRIMARY_INDEX).findViewById(R.id.tv_add_good_logo);
        tvLogo.setText("");
        logoIndex = 0;
        brandId = 0;
        String url = Api.PATH_SELLER_QUERY_BIND_BRANDS;
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(), "categoryId", categoryId);
        SLog.info("params[%s]", params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
            }


            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }
                    EasyJSONArray brandList = responseObj.getArray("datas.brandList");
                    spinnerLogoItems.clear();
                    for (Object object : brandList) {
                        Brand item = Brand.parase((EasyJSONObject) object);
                        spinnerLogoItems.add(new ListPopupItem(item.getBrandId(),item.getBrandName(),item));
                    }

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    /**
     * 根據當前已選中的Spec值生成SKU枚舉值
     */
    private void generateSpecPermutation() {
        // 先清空列表
        specValueIdStringList.clear();

        int totalSkuCount = 1;

        for (SellerSpecMapItem item : sellerSelectedSpecList) {
            totalSkuCount *= item.sellerSpecItemList.size();
        }
        SLog.info("totalSkuCount[%d]", totalSkuCount);

        for (int i = 0; i < totalSkuCount; i++) {
            int n = i;
            StringBuilder specValueIdStringSB = new StringBuilder();
            StringBuilder skuDesc = new StringBuilder();  // SKU規格描述
            List<SellerSpecItem> sellerSpecItemList = new ArrayList<>();  // 規格值列表

            int colorId = 0;
            for (int j = 0; j < sellerSelectedSpecList.size(); j++) {
                SellerSpecMapItem sellerSpecMapItem = sellerSelectedSpecList.get(j);

                int index = n % sellerSpecMapItem.sellerSpecItemList.size();
                SellerSpecItem specItem = sellerSpecMapItem.sellerSpecItemList.get(index);
                if (sellerSpecMapItem.specId == Constant.COLOR_SPEC_ID) {
                    colorId = specItem.id;
                }

                sellerSpecItemList.add(specItem);

                specValueIdStringSB.append(specItem.id).append(",");
                skuDesc.append(specItem.name).append("/");

                n /= sellerSpecMapItem.sellerSpecItemList.size();
            }

            SLog.info("skuDesc[%s]", skuDesc.toString());
            String specValueIdString = StringUtil.trim(specValueIdStringSB.toString(), new char[] {','});
            specValueIdStringList.add(specValueIdString);

            SellerSpecPermutation permutation = specValueIdStringMap.get(specValueIdString);
            if (permutation == null) {  // 如果不在Map裏面，則新建一個
                permutation = new SellerSpecPermutation();

                permutation.colorId = colorId;
                permutation.specValueIdString = specValueIdString;
                permutation.specValueNameString = StringUtil.trim(skuDesc.toString(), new char[] {'/'});
                permutation.sellerSpecItemList = sellerSpecItemList;

                specValueIdStringMap.put(specValueIdString, permutation);
            }
        }
    }
}
