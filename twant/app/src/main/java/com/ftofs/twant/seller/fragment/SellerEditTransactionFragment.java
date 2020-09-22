package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.lib_common_ui.popup.ListPopup;
import com.ftofs.twant.widget.ScaledButton;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class SellerEditTransactionFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    TextView tvTitle;
    private int goodsModal=-1;//銷售模式 銷售模式 0零售 1跨城購 2虛擬 【必填】
    private EasyJSONObject publishGoodsInfo= new EasyJSONObject();
    private String unitName;
    private int commonId;
    SellerGoodsDetailFragment parent;
    private int unityIndex;
    private TextView tvAddGoodUnit;
    private ScaledButton sbRetail;
    private View retailContainer;
    private ScaledButton sbVirtual;
    private View VirtualContainer;

    private ScaledButton sbAcross;
    private View AcrossContainer;

    private ScaledButton sbConsult;
    private View consultContainer;

    private int isVirtual;
    private int tariffEnable;
    private int allowTariff;
    private int businessType;

    public static SellerEditTransactionFragment newInstance(SellerGoodsDetailFragment parent) {
        SellerEditTransactionFragment fragment= new SellerEditTransactionFragment();
        fragment.parent = parent;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_goods_transaction_edit, container, false);
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
        tvTitle.setText("基本交易信息");

        tvAddGoodUnit = view.findViewById(R.id.tv_add_good_unit);
        view.findViewById(R.id.ll_bottom_container).setVisibility(View.GONE);
        view.findViewById(R.id.btn_ok).setVisibility(View.VISIBLE);

        Util.setOnClickListener(view, R.id.tv_add_good_unit, this);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.tv_add_good_unit, this);

        sbRetail = view.findViewById(R.id.sb_retail);
        retailContainer = view.findViewById(R.id.ll_retail);
        AcrossContainer = view.findViewById(R.id.ll_across_container);
        consultContainer = view.findViewById(R.id.ll_consult_container);
        sbVirtual = view.findViewById(R.id.sb_virtual);
        sbAcross = view.findViewById(R.id.sb_across);
        sbConsult = view.findViewById(R.id.sb_consult);
        sbConsult.setVisibility(View.VISIBLE);
        sbRetail.setButtonCheckedBlue();
        sbVirtual.setButtonCheckedBlue();
        sbAcross.setButtonCheckedBlue();
        sbRetail.setOnClickListener(v -> {
            if (!sbRetail.isChecked()) {
                goodsModal = 0;

                tariffEnable = 0;
                isVirtual = 0;
                updateView();
            }
        });
        sbVirtual.setOnClickListener(v -> {
            if (!sbVirtual.isChecked()) {
                goodsModal = 0;

                tariffEnable = 0;
                isVirtual = 1;
                updateView();
            }
        });
        sbAcross.setOnClickListener(v -> {
            if (!sbAcross.isChecked()) {
                goodsModal = 0;

                allowTariff = 1;
                tariffEnable = 1;
                updateView();
            }
        });
        sbConsult.setOnClickListener(v->{
            if (!sbConsult.isChecked()) {
                goodsModal = Constant.GOODS_TYPE_CONSULT;
                updateView();
            }
        });
//        sbRetail.performClick();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (businessType == 0) {
            loadBusinessType();
        }
        if (parent.goodsVo == null) {
            loadData();
        } else {
            explainData();
        }
        loadUnitListDate();
    }

    private void explainData() {
        try{
            SLog.info("%d",parent.allowTariff);
            tariffEnable = parent.tariffEnable;
            isVirtual = parent.isVirtual;
            unitName = parent.unitName;
            commonId = parent.commonId;
            allowTariff = parent.allowTariff;
            goodsModal = parent.goodsModal;
            if (businessType >=0 ) {

                businessType = parent.businessType;
            }
            updateView();
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void updateView() {
        tvAddGoodUnit.setText(unitName);

        if (businessType == Constant.CONSULT_STORE||goodsModal == Constant.GOODS_TYPE_CONSULT) {
            consultContainer.setVisibility(View.VISIBLE);
            retailContainer.setVisibility(View.GONE);
            AcrossContainer.setVisibility(View.GONE);
            goodsModal = Constant.GOODS_TYPE_CONSULT;            sbConsult.setChecked(true);
            return;


        } else {
            consultContainer.setVisibility(View.GONE);
        }
            if (allowTariff != 1) {

                getView().findViewById(R.id.ll_across_container).setVisibility(View.GONE);
                withoutTariff();
            } else if (tariffEnable == 1) {
                goodsModal = 2;
                sbRetail.setChecked(false);
                sbVirtual.setChecked(false);
                sbAcross.setChecked(true);
            } else {
                sbAcross.setChecked(false);
                withoutTariff();
            }

    }

    private void withoutTariff() {
        goodsModal = isVirtual;
        sbVirtual.setChecked(isVirtual == 1);
        sbRetail.setChecked(isVirtual != 1);
    }

    private void loadBusinessType() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_SELLER_GOODS_PUBLISH_PAGE ;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "isPublish",0

        );
        SLog.info("url[%s], params[%s]", url, params);
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        loadingPopup.show();
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
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
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    EasyJSONObject data = responseObj.getObject("datas");
                    businessType = data.getInt("businessType");
                    parent.businessType = businessType;
                    updateView();

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void updateBasicView(EasyJSONObject data) throws Exception{

        EasyJSONArray unitListJson =data.getArray("unitList");
        if (unitListJson != null) {
            parent.unitList.clear();
            for (Object object : unitListJson) {
                ListPopupItem item = new ListPopupItem(0,"",null);
                item.id = ((EasyJSONObject) object).getInt("id");
                item.title = ((EasyJSONObject) object).getSafeString("name");
                item.data = item.title;
                parent.unitList.add(item);
            }
            updateUnitList();
        }
    }

    private void updateUnitList() {

        if (!StringUtil.isEmpty(unitName)) {
            int i = 0;
            for (ListPopupItem item : parent.unitList) {
                if (unitName.equals(item.title)) {
                    unityIndex = i;
                    break;
                }
                i++;
            }
        }
        onSelected(PopupType.GOODS_UNITY,unityIndex,parent.unitList.get(unityIndex));
    }

    private void loadUnitListDate() {
        if (!parent.unitList.isEmpty()) {
           updateUnitList();
//            onSelected(PopupType.GOODS_UNITY,unityIndex,parent.unitList.get(unityIndex));
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(),"isPublish",0);
        String url = Api.PATH_SELLER_GOODS_PUBLISH_PAGE;

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
                        hideSoftInput();
                        return;
                    }
                    updateBasicView(data);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
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
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
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
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    View contentView = getView();
                    if (contentView == null) {
                        return;
                    }
                    parent.updateDataFromJson(responseObj);
                    explainData();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private boolean checkTransactionInfo() {

        if (StringUtil.isEmpty(unitName)) {
            ToastUtil.error(_mActivity,"請選擇計量單位");
            return false;
        }
        if (goodsModal < 0) {
            ToastUtil.error(_mActivity,"請選擇銷售模式");
            return false;
        }
        try{
            publishGoodsInfo.set("commonId", parent.commonId);
            publishGoodsInfo.set("editType", 2);
            publishGoodsInfo.set("unitName", unitName);
            publishGoodsInfo.set("goodsModal", goodsModal);
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
            if (checkTransactionInfo()) {
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
            SLog.info("添加單位");
            if (parent.unitList.isEmpty()) {
                // TODO: 2020/6/26  当商家已经商家超过100款商品后，访问列表接口会报错，逻辑上有问题，4.1版本会调整接口
                ToastUtil.error(_mActivity, "暂未获取到单位数据");
            } else {
                new XPopup.Builder(_mActivity)
                        .moveUpToKeyboard(false)
                        .asCustom(new ListPopup(_mActivity,"計量單位",
                                PopupType.GOODS_UNITY,parent.unitList, unityIndex,this))
                        .show();
            }
        }

    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.GOODS_UNITY) {
            TextView tvUnit = getView().findViewById(R.id.tv_add_good_unit);
            unityIndex = id;
            unitName = extra.toString();
            tvUnit.setText(unitName);
        }
    }
}
