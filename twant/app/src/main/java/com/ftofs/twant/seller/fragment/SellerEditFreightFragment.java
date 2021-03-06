package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.ftofs.lib_common_ui.popup.ListPopup;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ScaledButton;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.gzp.lib_common.constant.PopupType;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * @author gzp
 */
public class SellerEditFreightFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    TextView tvTitle;
    private EasyJSONObject publishGoodsInfo= new EasyJSONObject();
    SellerGoodsDetailFragment parent;
    private int freightTemplateId;
    private boolean useFixedFreight;
    private int freightRuleIndex;
    private List<ListPopupItem> freightList=new ArrayList<>();
    private String freightTitle;
    private double goodsFreight;
//    private FixedEditText  fetFreight;
    private EditText  fetFreight;
    private ScaledButton sbFreightTemple;
    private ScaledButton sbFixedTemple;
    private EditText etW;
    private EditText etV;
    private TextView tvRule;

    public static SellerEditFreightFragment newInstance(SellerGoodsDetailFragment parent) {
        SellerEditFreightFragment fragment= new SellerEditFreightFragment();
        fragment.parent = parent;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_goods_freight_edit, container, false);

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

        tvTitle.setText("編輯物流信息");

        etW=view.findViewById(R.id.et_freight_weight);
        etV=view.findViewById(R.id.et_freight_v);
        tvRule = getView().findViewById(R.id.tv_add_freight_rule);


        view.findViewById(R.id.ll_bottom_container).setVisibility(View.GONE);
        view.findViewById(R.id.btn_ok).setVisibility(View.VISIBLE);
        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.tv_add_freight_rule, this);
        fetFreight = view.findViewById(R.id.et_add_fixed_freight);
//        ScaledButton sbFixedFreight

        sbFreightTemple = view.findViewById(R.id.sb_freight_temple);

        sbFixedTemple = view.findViewById(R.id.sb_freight_solid);
        sbFixedTemple.setButtonCheckedBlue();
        sbFreightTemple.setButtonCheckedBlue();
        sbFixedTemple.setOnClickListener(v ->  {
            if (!sbFixedTemple.isChecked()) {
                useFixedFreight = true;
                updateCheckView();
            }
        });
        sbFreightTemple.setOnClickListener(v -> {
            if (!sbFreightTemple.isChecked()) {
                useFixedFreight = false;
                updateCheckView();
            }
        });
    }

    private void updateCheckView() {
        sbFixedTemple.setChecked(useFixedFreight);
        sbFreightTemple.setChecked(!useFixedFreight);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (parent.goodsVo == null) {
            loadData();
        } else {
            explainData();
        }
        loadFreightListDate();
    }

    private void explainData() {
        freightTemplateId = parent.freightTemplateId;
        goodsFreight = parent.goodsFreight;
        useFixedFreight = freightTemplateId <= 0;

        if (!useFixedFreight) {
            onSelected(PopupType.GOODS_FREIGHT_RULE, freightTemplateId, null);
        } else{
            fetFreight.setText(String.valueOf(goodsFreight));
        }
        if (parent.freightVolume > 0) {
            etV.setText(String.valueOf(parent.freightVolume));
        }if (parent.freightVolume > 0) {
            etW.setText(String.valueOf(parent.freightWeight));
        }

        updateCheckView();
    }
    private void loadFreightListDate() {
        if (!freightList.isEmpty()) {
            for (ListPopupItem item : freightList) {
                item.title.equals(freightTitle);
                freightRuleIndex = item.id;
            }
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(),"isPublish",0);
        String url = Api.PATH_SELLER_GOODS_PUBLISH_PAGE;

        SLog.info("url[%s], params[%s]", url, params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }


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
                    updateFreightView(data);
                    if (!useFixedFreight) {
                        onSelected(PopupType.GOODS_FREIGHT_RULE,freightTemplateId,null);
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
    private void updateFreightView(EasyJSONObject data)throws Exception {
        EasyJSONArray freightTemplateList = data.getArray("freightTemplateList");
        if(freightTemplateList!=null){
            freightList.clear();

            for (Object object : freightTemplateList) {
                int freightId =((EasyJSONObject)object).getInt("freightId");

                String title = ((EasyJSONObject) object).getSafeString("title");
                if (freightId == freightTemplateId) {
                    tvRule.setText(title);
                }
                freightList.add(new ListPopupItem(freightId, title, title));
            }
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
                    parent.updateDataFromJson(responseObj);
                    explainData();

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
    private boolean checkFreightInfo() {
        try{

            String freightText = fetFreight.getText()==null?"":fetFreight.getText().toString();
            if (useFixedFreight) {
                if (StringUtil.isEmpty(freightText)) {
                    ToastUtil.error(_mActivity, "請填寫運費");
                    return false;
                }
            } else {
                if (freightTemplateId<=0) {
                    ToastUtil.error(_mActivity, "請選擇運費模板");
                    return false;
                }
            }
            String freightWeightStr = etW.getText()==null?"":etW.getText().toString();
            String freightVolumeStr = etV.getText()==null?"":etV.getText().toString();

            try{
                publishGoodsInfo.set("commonId", parent.commonId);
                publishGoodsInfo.set("editType", 5);
                if (useFixedFreight) {
                    double goodsFreight = Double.parseDouble(freightText);
                    publishGoodsInfo.set("goodsFreight", goodsFreight);
                } else {
                    publishGoodsInfo.set("freightTemplateId", freightTemplateId);
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
            if (checkFreightInfo()) {
                parent.saveGoodsInfo(publishGoodsInfo, new SimpleCallback() {
                    @Override
                    public void onSimpleCall(Object data) {
                        ToastUtil.success(_mActivity,data.toString());
                        hideSoftInputPop();
                    }
                });
            }

        }
        if (id == R.id.tv_add_freight_rule) {

            hideSoftInput();
            new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(new ListPopup(_mActivity, "物流規則", PopupType.GOODS_FREIGHT_RULE, freightList, freightRuleIndex, this)).show();
        }

    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.GOODS_FREIGHT_RULE) {
            freightRuleIndex = id;
            if (freightList != null && id < freightList.size()) {
                freightTemplateId = freightList.get(id).id;
                if (extra != null) {
                    tvRule.setText(extra.toString());
                }
            }
        }
    }
}

