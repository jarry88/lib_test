package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.AdminCountry;
import com.ftofs.twant.domain.goods.Brand;
import com.ftofs.twant.domain.goods.Category;
import com.ftofs.twant.entity.ListPopupItem;
import com.gzp.lib_common.base.BaseFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.seller.widget.StoreLabelPopup;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 *
 * 需求
 * @author gzp
 */
public class SellerEditBasicFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    TextView tvTitle;
    private EasyJSONObject publishGoodsInfo= new EasyJSONObject();
    private int commonId;
    SellerGoodsDetailFragment parent;
    private int categoryId=-1;
    private int goodsCountry=-1;
    private List<Category> selectCategoryList;
    private int brandId;
    private int logoIndex;
    private int countryIndex;
    private EditText etName;
    private EditText etJingle;
    private TextView tvAddGoodLogo;
    private TextView tvAddGoodLocation;
    private TextView tvCategoryId;
    private int categoryId1;
    private int categoryId2;
    private int categoryId3;
    private List<ListPopupItem> spinnerLogoItems;
    private String brandName;
    private String goodsCountryName;

    public static SellerEditBasicFragment newInstance(SellerGoodsDetailFragment parent) {
        SellerEditBasicFragment fragment= new SellerEditBasicFragment();
        fragment.parent = parent;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seller_add_good_basic_layout, container, false);
        Util.setOnClickListener(view,R.id.btn_back,this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        View view = getView();
        tvTitle = view.findViewById(R.id.tv_title);

        tvTitle.setText("編輯基本信息");
        etName=view.findViewById(R.id.et_add_good_name);
        etJingle=view.findViewById(R.id.et_add_good_description);
        tvAddGoodLogo = view.findViewById(R.id.tv_add_good_logo);
        tvAddGoodLocation = view.findViewById(R.id.tv_add_good_location);
        tvCategoryId = view.findViewById(R.id.tv_category_id);
        spinnerLogoItems = new ArrayList<>();

        view.findViewById(R.id.ll_bottom_container).setVisibility(View.GONE);
        view.findViewById(R.id.btn_ok).setVisibility(View.VISIBLE);
        Util.setOnClickListener(view, R.id.tv_add_good_unit, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
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

    }

    private void explainData() {
        try{
            commonId = parent.commonId;
            brandId = parent.goodsVo.getInt("brandId");
            brandName = parent.goodsVo.getSafeString("brandName");
            if (!StringUtil.isEmpty(brandName)) {
                tvAddGoodLogo.setText(brandName);
            }
            goodsCountryName=parent.goodsVo.getSafeString("goodsCountryName");
            goodsCountry=parent.goodsVo.getInt("goodsCountry");
            if (!StringUtil.isEmpty(goodsCountryName)) {
                updateInitCountryIndex();
                tvAddGoodLocation.setText(goodsCountryName);
            }

            etName.setText(parent.goodsVo.getString("goodsName"));
            etJingle.setText(parent.goodsVo.getString("jingle"));

            tvCategoryId.setText(parent.goodsVo.getSafeString("categoryNames"));

            categoryId = parent.goodsVo.getInt("categoryId");
            clearCategory();
            categoryId1 = parent.goodsVo.getInt("categoryId1");
            categoryId2 = parent.goodsVo.getInt("categoryId2");
            categoryId3 = parent.goodsVo.getInt("categoryId3");
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void clearCategory() {
        categoryId1 = 0;
        categoryId2 = 0;
        categoryId3 = 0;
    }

    private void loadGoodsCountry(View view) {
        if (!parent.spinnerLogoCountryItems.isEmpty()) {
            updateInitCountryIndex();
            return;
        }
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
                        parent.spinnerLogoCountryItems.clear();
                        for (Object o : countryList) {
                            AdminCountry item = AdminCountry.parase(o);
                            parent.spinnerLogoCountryItems.add(new ListPopupItem(item.getCountryId(),item.getCountryCn(),item));
                        }
                        updateInitCountryIndex();
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void updateInitCountryIndex() {
        if (StringUtil.isEmpty(goodsCountryName)||parent.spinnerLogoCountryItems==null) {
            return;
        }
        int i = 0;
        for (ListPopupItem item : parent.spinnerLogoCountryItems) {
//            SLog.info("%s，%s,cn %s",goodsCountryName,((AdminCountry)item.data).getCountryContinents(),((AdminCountry)item.data).getCountryCn());
            if (goodsCountryName.equals(item.title)||goodsCountryName.contains(item.title)) {
                countryIndex = i;
                break;
            }
            i++;
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (parent.goodsVo != null) {
            explainData();
        } else {
            loadData();
        }
        if (spinnerLogoItems.size() == 0) {
            updateLogoInfo();
        }
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

                    parent.goodsVo = responseObj.getSafeObject("datas.GoodsVo");
                    commonId = parent.goodsVo.getInt("commonId");
                    ((TextView) contentView.findViewById(R.id.tv_spu_id)).setText(String.valueOf(commonId));
                    ((TextView) contentView.findViewById(R.id.tv_goods_name)).setText(parent.goodsVo.getSafeString("goodsName"));
                    ImageView goodsImage = contentView.findViewById(R.id.goods_image);

                    int tariffEnable = parent.goodsVo.getInt("tariffEnable");
                    SLog.info("tariffEnable__[%d]", tariffEnable);
                    contentView.findViewById(R.id.cross_border_indicator).setVisibility(tariffEnable == Constant.TRUE_INT ? View.VISIBLE : View.GONE);

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private boolean checkBasicInfo() {
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
        String jingle =etJingle.getText().toString();
//        if (StringUtil.isEmpty(jingle)) {
//            ToastUtil.error(_mActivity,"請填寫商品賣點");
//            return false;
//        }
        if (jingle.length() > 140) {
            ToastUtil.error(_mActivity,"商品賣點長度為0到140個字符");
            return false;
        }
        try {
            publishGoodsInfo.set("commonId", commonId);

            publishGoodsInfo.set("editType",1);//1為基本信息
            publishGoodsInfo.set("goodsName", goodsName);
            publishGoodsInfo.set("categoryId", categoryId);
            publishGoodsInfo.set("jingle", jingle);
            clearCategory();
            if (selectCategoryList!=null) {
                int i = 1;
                for (Category category : selectCategoryList) {
                    if (i == 1) {
                        categoryId1 = category.getCategoryId();
                        publishGoodsInfo.set("categoryId1", category.getCategoryId());
                        categoryId = categoryId1;
                    }if (i == 2) {
                        categoryId2 = category.getCategoryId();
                        categoryId = categoryId2;
                        publishGoodsInfo.set("categoryId2", category.getCategoryId());

                    }if (i == 3) {
                        categoryId3 = category.getCategoryId();
                        categoryId = categoryId3;
                        publishGoodsInfo.set("categoryId3", category.getCategoryId());

                    }
                    publishGoodsInfo.set("categoryId", categoryId);
                    i++;
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
            if (checkBasicInfo()) {
                // TODO: 2020/6/26  所在地選擇后 商品詳情頁未更新
                parent.saveGoodsInfo(publishGoodsInfo, new SimpleCallback() {
                    @Override
                    public void onSimpleCall(Object data) {
                        ToastUtil.success(_mActivity,data.toString());
                        hideSoftInputPop();
                    }
                });
            }
                
        }
        if (id == R.id.tv_add_good_location) {
            hideSoftInput();
            new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(new ListPopup(_mActivity, "品牌所在地", PopupType.GOODS_LOCATION, parent.spinnerLogoCountryItems, countryIndex, this)).show();
        }
        if (id == R.id.tv_add_good_logo) {
            if (spinnerLogoItems==null||spinnerLogoItems.size() == 0) {
                ToastUtil.error(_mActivity,"該分類暫時沒有可選品牌");
                return;
            }
            hideSoftInput();
            if (logoIndex >= spinnerLogoItems.size()) {
                logoIndex = 0;
            }
            new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(new ListPopup(_mActivity, "品牌", PopupType.GOODS_LOGO, spinnerLogoItems, logoIndex, this)).show();
        }

        if (id == R.id.tv_add_good_unit) {
            SLog.info("添加單位");
        }

    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
          if (type == PopupType.STORE_LABEL) {
            selectCategoryList = (List<Category>) extra;
            Category categoryLast = new Category();
            StringBuilder selectCategoryName = new StringBuilder();

            for (Category category : selectCategoryList) {
                categoryLast = category;
                selectCategoryName.append(category.getCategoryName()).append(" -- ");
            }
            if (!StringUtil.isEmpty(selectCategoryName.toString())) {
                selectCategoryName.delete(selectCategoryName.length() - 4, selectCategoryName.length() - 1);
                    ((TextView)( getView().findViewById(R.id.tv_category_id))).setText(selectCategoryName.toString());
                    categoryId = categoryLast.getCategoryId();

            }

              TextView tvLogo = getView().findViewById(R.id.tv_add_good_logo);
              tvLogo.setText("");
              logoIndex = 0;
              brandId = 0;
             updateLogoInfo();


          }
          else if (type == PopupType.GOODS_LOCATION) {
              countryIndex = id;
              AdminCountry item = (AdminCountry) extra;
              goodsCountry = item.getCountryId();
              goodsCountryName = item.getCountryCn();
              tvAddGoodLocation.setText(goodsCountryName);

          } else if (type == PopupType.GOODS_LOGO) {
              TextView tvLogo = getView().findViewById(R.id.tv_add_good_logo);
              logoIndex = id;
              Brand item = (Brand) extra;
              brandId = item.getBrandId();
              tvLogo.setText(item.getBrandName());

          }
    }
    private void updateLogoInfo() {
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
                    if (spinnerLogoItems.size() > 0) {
                        int i = 0;
                        for (ListPopupItem item : spinnerLogoItems) {
                            if (tvAddGoodLogo.getText().toString().equals(item.title)) {
                                logoIndex = i;
                                break;
                            }

                            i++;
                        }
                    }

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

}


