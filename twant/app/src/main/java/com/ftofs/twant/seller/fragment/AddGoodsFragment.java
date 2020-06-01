package com.ftofs.twant.seller.fragment;

import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SimpleViewPagerAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.domain.AdminCountry;
import com.ftofs.twant.domain.Format;
import com.ftofs.twant.domain.goods.Brand;
import com.ftofs.twant.domain.goods.Category;
import com.ftofs.twant.domain.store.StoreLabel;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.entity.SellerSpecItem;
import com.ftofs.twant.seller.widget.NoScrollViewPager;
import com.ftofs.twant.seller.widget.StoreLabelPopup;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.FixedEditText;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.ScaledButton;
import com.kyleduo.switchbutton.SwitchButton;
import com.lxj.xpopup.XPopup;
import com.orhanobut.hawk.Hawk;
import com.umeng.commonsdk.debug.E;

import org.json.JSONArray;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class AddGoodsFragment extends BaseFragment implements View.OnClickListener , OnSelectedListener {
    private Unbinder unbinder;
    private SimpleViewPagerAdapter mPagerAdapter;
    private List<View> mViews = new ArrayList<>();
    //    1是0否允許發佈跨城購商品
    private int allowTariff;
    //    允許添加的最大規格數量
    private int specMax;
    private List<ListPopupItem> unitList = new ArrayList<>();
    //準備要提交的商品信息json
    private EasyJSONObject publishGoodsInfo;
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
    private int brandId =-1;

    List<SellerSpecItem> sellerSpecItemList = new ArrayList<>();
    JSONArray goodsMobileBodyVoList =new JSONArray();
    private int unityIndex;
    private String unitName;
    private int goodsModal=-1;//銷售模式 銷售模式 0零售 1跨城購 2虛擬 【必填】
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

    @BindView(R.id.ll_detail_body)
    LinearLayout llDetailBody;
    @BindView(R.id.rl_detail_body)
    RelativeLayout rlDetailBody;
    private int formatBottomIndex;
    private int formatTopIndex;
    private int formatBottom =-1;
    private int formatTop=-1;

    public static AddGoodsFragment newInstance() {

        return new AddGoodsFragment();
    }

    @OnClick(R.id.btn_publish)
    void publish() {
        hideAddGuide();
    }

    @OnClick(R.id.btn_detail_save)
    void save() {
        ToastUtil.success(_mActivity,String.valueOf(llDetailBody.getChildCount()));
    }
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.sb_check_notice)
    ScaledButton sbNotice;
    @BindView(R.id.vp_seller_good_add)
    NoScrollViewPager vpAddGood;
    @OnClick(R.id.btn_back)
    void back() {
        hideSoftInputPop();
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
        Hawk.put(SPField.SELLER_ADD_GUIDE_HIDE, sbNotice.isChecked());
        getView().findViewById(R.id.rl_guide_container).setVisibility(View.GONE);
        getView().findViewById(R.id.vp_seller_good_add).setVisibility(View.VISIBLE);
        vpAddGood.setCurrentItem(0);
    }

    @OnClick(R.id.sb_check_notice)
    void checkNotice() {
        sbNotice.setChecked(!sbNotice.isChecked());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_seller_goods_publish, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        loadDate();
    }

    private void initView() {
        publishGoodsInfo = new EasyJSONObject();
        sbNotice.setButtonCheckedBlue();
        sbNotice.setChecked(Hawk.get(SPField.SELLER_ADD_GUIDE_HIDE, false));
        if (sbNotice.isChecked()) {
            hideAddGuide();
        }
        sbNotice.setIconResource(R.drawable.icon_cart_item_unchecked);
        sbNotice.setChecked(false);
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
        Util.setOnClickListener(view, R.id.btn_others_prev, this);
        Util.setOnClickListener(view, R.id.btn_publish_goods, this);
        ScaledButton sbInstancePublish = view.findViewById(R.id.sb_instance_publish);
        ScaledButton sbAddHub = view.findViewById(R.id.sb_add_hub);

        LinearLayout llAddHub =view.findViewById(R.id.ll_add_hub);
        LinearLayout llInstancePublish =view.findViewById(R.id.ll_instance_publish);
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
//        goodsState
//                joinBigSale
        sbJoinActivity = view.findViewById(R.id.sb_join_activity);
        return view;
    }

    private View freightView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_freight_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_freight_prev, this);
        Util.setOnClickListener(view, R.id.btn_freight_next, this);
        Util.setOnClickListener(view, R.id.tv_add_freight_rule, this);
        FixedEditText fetFreight = view.findViewById(R.id.et_add_fixed_freight);
        fetFreight.setFixedText("$ ");
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
        return view;
    }

    private View basicView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_basic_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_basic_next, this);
        Util.setOnClickListener(view, R.id.btn_basic_prev, this);
        Util.setOnClickListener(view, R.id.tv_add_good_unit, this);
        ScaledButton sbRetail = view.findViewById(R.id.sb_retail);
        ScaledButton sbVirtual = view.findViewById(R.id.sb_virtual);
        ScaledButton sbAcross = view.findViewById(R.id.sb_across);
        sbRetail.setButtonCheckedBlue();
        sbVirtual.setButtonCheckedBlue();
        sbAcross.setButtonCheckedBlue();
        sbRetail.setOnClickListener(v -> {
            sbRetail.setChecked(!sbRetail.isChecked());
            if (sbRetail.isChecked()) {
                goodsModal = 0;
                sbVirtual.setChecked(false);
                sbAcross.setChecked(false);
            } else {
                if (sbVirtual.isChecked() || sbAcross.isChecked()) {

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
            }else {
                if (sbRetail.isChecked() || sbAcross.isChecked()) {
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
            }else {
                if (sbRetail.isChecked() || sbVirtual.isChecked()) {
                } else {
                    goodsModal = -1;
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
        EasyJSONObject params =EasyJSONObject.generate("token", User.getToken());
         SLog.info("params[%s]", params);
         Api.getUI(Api.PATH_SELLER_QUERY_COUNTRY_ALL, params, new UICallback() {
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
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private void loadDate() {
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken());
        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_SELLER_GOODS_PUBLISH_PAGE, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    EasyJSONObject data = responseObj.getObject("datas");
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        hideSoftInput();
                        return;
                    }
                    updateView(data);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateView(EasyJSONObject data) throws Exception {
        allowTariff = data.getInt("allowTariff");//1是0否允許發佈跨城購商品
        specMax = data.getInt("specMax");//允許添加的最大規格數量
        int specValueMax = data.getInt("specValueMax");//允許添加的最大規格數量值
        EasyJSONArray formatBottomList = data.getArray("formatBottomList");//底部關聯版式列表
        EasyJSONArray formatTopList = data.getArray("formatTopList");//頂部關聯版式列表
        EasyJSONArray countryList = data.getArray("countyrList");//品牌所在地列表
        EasyJSONArray freightTemplateList = data.getArray("freightTemplateList");//物流模板列表
        EasyJSONArray specListArr = data.getArray("specList");//規格列表

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
            OnSelectedListener listener = this;
            tvSelect.setOnClickListener(v ->{
                new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(
                        new StoreLabelPopup(_mActivity, PopupType.STORE_CATEGORY, list, listener)
                ).show();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updatePrimaryView(EasyJSONObject data) throws Exception {
        EasyJSONArray storeLabelList = data.getArray("storeLabelList");//店内分類列表
        labelList = new ArrayList<>();
        for (Object storelabel : storeLabelList) {
            labelList.add(StoreLabel.parse((EasyJSONObject) storelabel));
            SLog.info(labelList.get(0).getStoreLabelName());
        }
        
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_primary_next:
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
                new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(new ListPopup(_mActivity, "品牌所在地", PopupType.GOODS_LOCATION, spinnerLogoCountryItems, countryIndex, this)).show();
                break;
            case R.id.tv_add_good_logo:
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
                new XPopup.Builder(_mActivity)
                        .moveUpToKeyboard(false)
                        .asCustom(new ListPopup(_mActivity,"計量單位",
                                PopupType.GOODS_UNITY,unitList, unityIndex,this))
                        .show();
                break;
            case R.id.btn_spec_next:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() + 1);

                break;
            case R.id.btn_spec_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() - 1);

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
                new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(new ListPopup(_mActivity, "物流規則", PopupType.GOODS_FREIGHT_RULE, spinnerLogoItems, freightRuleIndex, this)).show();
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
            default:
                break;
        }

    }

    private boolean saveDetailInfo() {
        EditText etVideos = mViews.get(DETAIL_INDEX).findViewById(R.id.et_goods_video_url);
        String detailVideo = "";
        if (etVideos.getText() != null) {
            detailVideo = etVideos.getText().toString();
        }
        try{
            publishGoodsInfo.set("storeLabelId", storeLabelId);
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
        try{
            publishGoodsInfo.set("joinBigSale", joinBigSale);
            publishGoodsInfo.set("goodsState", goodsState);
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private boolean saveFreightInfo() {
        View view = mViews.get(FREIGHT_INDEX);
        FixedEditText etFreight = view.findViewById(R.id.et_add_fixed_freight);
        String freightText = etFreight.getText()==null?"":etFreight.getText().toString();
        if (useFixedFreight) {
            if (StringUtil.isEmpty(freightText)) {
                ToastUtil.error(_mActivity, "請填寫運費");
                return false;
            }
        } else {
            if (freightTemplateId < 0) {
                ToastUtil.error(_mActivity, "請選擇運費模板");
                return false;
            }
        }
        EditText etW=view.findViewById(R.id.et_freight_weight);
        EditText etV=view.findViewById(R.id.et_freight_v);
        double goodsFreight = Double.parseDouble(freightText);
        String freightWeightStr = etW.getText()==null?"":etW.getText().toString();
        String freightVolumeStr = etV.getText()==null?"":etV.getText().toString();
        double freightWeight =Double.parseDouble(etW.getText().toString());
        double freightVolume=Double.parseDouble(etV.getText().toString());

        try{
            if (freightTemplateId >= 0) {
                publishGoodsInfo.set("freightTemplateId", freightTemplateId);
            } else {
                publishGoodsInfo.set("goodsFreight", goodsFreight);
            }
            if (!StringUtil.isEmpty(freightWeightStr)) {

                publishGoodsInfo.set("freightWeight", Double.parseDouble(freightWeightStr));
            }
            if (!StringUtil.isEmpty(freightVolumeStr)) {

                publishGoodsInfo.set("freightWeight", Double.parseDouble(freightVolumeStr));
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
            publishGoodsInfo.set("unityName", unitName);
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
        if (StringUtil.isEmpty(jingle)) {
            ToastUtil.error(_mActivity,"請填寫商品賣點");
            return false;
        }
        try {
            publishGoodsInfo.set("goodsName", goodsName);
            publishGoodsInfo.set("categoryId", categoryId);
            publishGoodsInfo.set("jingle", jingle);
            if (brandId >= 0) {
                publishGoodsInfo.set("brandId", brandId);
            }
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

    private void savePublishGoodsInfo() {
        String token = User.getToken();
        try{
            publishGoodsInfo.set("token", token);
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        SLog.info("paramas[%s]",publishGoodsInfo.toString());
          Api.postUI(Api.PATH_SELLER_GOODS_PUBLISH_SAVE, publishGoodsInfo, new UICallback() {
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
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("type[%s], id[%d], extra[%s]", type, id, extra);
        if (type == PopupType.STORE_LABEL) {
            List<Category> selectCategoryList =(List<Category>)extra;
            Category categoryLast = new Category();
            StringBuilder selectCategoryName=new StringBuilder();
            for (Category category : selectCategoryList) {
                categoryLast = category;
                 selectCategoryName.append(category.getCategoryName()).append( " -- ");
            }
            if (!StringUtil.isEmpty(selectCategoryName.toString())) {
                selectCategoryName.delete(selectCategoryName.length() - 4,selectCategoryName.length()-1);
                ((TextView)(mViews.get(PRIMARY_INDEX).findViewById(R.id.tv_category_id))).setText(selectCategoryName.toString());
                categoryId = categoryLast.getCategoryId();
            }
            updateLogoInfo();
        } else if(type==PopupType.GOODS_UNITY){
            TextView tvUnit = mViews.get(BASIC_INDEX).findViewById(R.id.tv_add_good_unit);
            unityIndex = id;
            unitName = extra.toString();
            tvUnit.setText(unitName);
        } else if(type==PopupType.GOODS_LOCATION){
            TextView tvLocation = mViews.get(PRIMARY_INDEX).findViewById(R.id.tv_add_good_location);
            countryIndex = id;
            AdminCountry item = (AdminCountry) extra;
            goodsCountry= item.getCountryId();
            tvLocation.setText(item.getCountryCn());

        }else if(type==PopupType.GOODS_LOGO){
            TextView tvLogo = mViews.get(PRIMARY_INDEX).findViewById(R.id.tv_add_good_logo);
            logoIndex = id;
            Brand item = (Brand) extra;
            brandId = item.getBrandId();
            tvLogo.setText(item.getBrandName());

        } else if(type ==PopupType.GOODS_FREIGHT_RULE){
            TextView tvRule = mViews.get(FREIGHT_INDEX).findViewById(R.id.tv_add_freight_rule);
            freightRuleIndex = id;
            tvRule.setText(extra.toString());
        }else if(type==PopupType.SELLER_FORMAT_TOP){
            TextView tvFormatTop=mViews.get(DETAIL_INDEX).findViewById(R.id.tv_format_top);
            tvFormatTop.setText(((Format)extra).getFormatName());
            formatTopIndex = id;
            formatTop = ((Format) extra).getFormatId();
        }else if(type==PopupType.SELLER_FORMAT_BOTTOM){
            TextView tvFormatBottom=mViews.get(DETAIL_INDEX).findViewById(R.id.tv_format_bottom);
            tvFormatBottom.setText(((Format)extra).getFormatName());
            formatBottomIndex = id;
            formatBottom = ((Format) extra).getFormatId();
        }
        else if(type == PopupType.DEFAULT){
            updateLogoInfo();
//        String[] spinnerItems = new String[labelList.size()];
            //自定义选择填充后的字体样式
            //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        }
    }

    private void updateLogoInfo() {
        TextView tvLogo = mViews.get(PRIMARY_INDEX).findViewById(R.id.tv_add_good_logo);
        tvLogo.setText("");
        logoIndex = 0;
        brandId = -1;
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(), "categoryId", categoryId);
        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_SELLER_QUERY_BIND_BRANDS, params, new UICallback() {
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
}
