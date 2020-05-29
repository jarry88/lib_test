package com.ftofs.twant.seller.fragment;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
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
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.domain.goods.Category;
import com.ftofs.twant.domain.store.StoreLabel;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.widget.StoreLabelPopup;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.UiUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AreaPopup;
import com.ftofs.twant.widget.ScaledButton;
import com.lxj.xpopup.XPopup;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
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
    private List<String> unitList = new ArrayList<>();
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

    public static AddGoodsFragment newInstance() {
        return new AddGoodsFragment();
    }

    @OnClick(R.id.btn_publish)
    void publish() {
        hideAddGuide();
    }

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.sb_check_notice)
    ScaledButton sbNotice;
    @BindView(R.id.vp_seller_good_add)
    ViewPager vpAddGood;

    @OnClick(R.id.btn_back)
    void back() {
        hideSoftInputPop();
    }

    @OnClick(R.id.rl_guide_container)
    void hideGuide() {
        hideAddGuide();

    }

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
        sbInstancePublish.setButtonCheckedBlue();
        sbAddHub.setButtonCheckedBlue();
        sbInstancePublish.setText("立即發佈");
        sbAddHub.setText("放入倉庫");
        return view;
    }

    private View freightView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_freight_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_freight_prev, this);
        Util.setOnClickListener(view, R.id.btn_freight_next, this);
        return view;
    }
    // 初始化 详情描述

    private View detailView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_detail_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_detail_next, this);
        Util.setOnClickListener(view, R.id.btn_detail_prev, this);
        Util.setOnClickListener(view, R.id.btn_add_address, this);
        return view;
    }
//    初始化 规格与图片页

    private View specView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_spec_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_spec_next, this);
        Util.setOnClickListener(view, R.id.btn_spec_prev, this);
        return view;
    }

    private View basicView() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_basic_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_basic_next, this);
        Util.setOnClickListener(view, R.id.btn_basic_prev, this);
        ScaledButton sbRetail = view.findViewById(R.id.sb_retail);
        ScaledButton sbVirtual = view.findViewById(R.id.sb_virtual);
        ScaledButton sbAcross = view.findViewById(R.id.sb_across);
        sbRetail.setText("零售型");
        sbVirtual.setText("虛擬型");
        sbAcross.setText("跨城型");
        return view;
    }

    private View primaryView() {

        View view = LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_primary_widget, vpAddGood, false);
        Util.setOnClickListener(view, R.id.btn_primary_next, this);
        TextView btnGoodCategory =view.findViewById(R.id.btn_select_category_id);
        btnGoodCategory.setOnClickListener(v -> {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new StoreLabelPopup(_mActivity, PopupType.STORE_LABEL,this))
                    .show();
        });
//        btnGoodCategory.setBackground(UiUtil.tvButtonBackGround());

        return view;
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
        EasyJSONArray specList = data.getArray("specList");//規格列表
        if (unitList != null) {
            for (Object object : unitList) {
                this.unitList.add(object.toString());
            }
        }
        updatePrimaryView(data);
        updateSpecView(data);
        updateDetailView(data);

    }

    //    更新商品详情页数据
    private void updateDetailView(EasyJSONObject data) {

    }

    //    更新商品规格页数据
    private void updateSpecView(EasyJSONObject data) {


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updatePrimaryView(EasyJSONObject data) throws Exception {
        EasyJSONArray unitList = data.getArray("unitList");//計量單位列表
        EasyJSONArray storeLabelList = data.getArray("storeLabelList");//店内分類列表
        labelList = new ArrayList<>();
        for (Object storelabel : storeLabelList) {
            labelList.add(StoreLabel.parse((EasyJSONObject) storelabel));
            SLog.info(labelList.get(0).getStoreLabelName());
        }

        View view = mViews.get(0);

        Spinner spGoodLogo = view.findViewById(R.id.sp_add_good_logo);
        Spinner spGoodCategory = view.findViewById(R.id.sp_goods_category);
        TextView btnGoodCategory = view.findViewById(R.id.btn_select_category_id);
        Spinner spGoodLocation = view.findViewById(R.id.sp_add_good_location);
        List<String> listLogo = new ArrayList<>();
        List<String> listLocation = new ArrayList<>();
//        String[] spinnerItems = new String[labelList.size()];
        List<String> spinnerItems = new ArrayList<>();
        labelList.forEach((storeLabel) -> spinnerItems.add(storeLabel.getStoreLabelName()));
        //自定义选择填充后的字体样式
        //只能是textview样式，否则报错：ArrayAdapter requires the resource ID to be a TextView
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(_mActivity,
                R.layout.spinner_item_select, spinnerItems);
        //自定义下拉的字体样式
        spinnerAdapter.setDropDownViewResource(R.layout.item_drop);
        //这个在不同的Theme下，显示的效果是不同的
        //spinnerAdapter.setDropDownViewTheme(Theme.LIGHT);
        spGoodCategory.setAdapter(spinnerAdapter);
        listLogo.add("s");
        listLogo.add("s");
        listLogo.add("s");
        listLocation.addAll(listLogo);
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
            case R.id.btn_basic_next:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() + 1);

                break;
            case R.id.btn_basic_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() - 1);

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
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() + 1);

                tvTitle.setText("詳情描述");
                break;
            case R.id.btn_detail_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() - 1);

                tvTitle.setText("規格與圖片");
                break;
            case R.id.btn_freight_next:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() + 1);

                tvTitle.setText("物流信息");
                break;
            case R.id.btn_freight_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() - 1);

                tvTitle.setText("詳情描述");
                break;
            case R.id.btn_publish_goods:
                commitGoodsInfo();
                break;
            case R.id.btn_others_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem() - 1);
                tvTitle.setText("物流信息");
                break;
            default:
                break;
        }

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
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        return true;
    }

//    收集表單信息，驗證信息符合要求后提交

    private void commitGoodsInfo() {
        savePublishGoodsInfo();
    }

    private void savePublishGoodsInfo() {
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
                selectCategoryName.deleteCharAt(selectCategoryName.length() - 4);
                ((TextView)(mViews.get(PRIMARY_INDEX).findViewById(R.id.tv_category_id))).setText(selectCategoryName.toString());
                categoryId = categoryLast.getCategoryId();
            }
        }
    }
}
