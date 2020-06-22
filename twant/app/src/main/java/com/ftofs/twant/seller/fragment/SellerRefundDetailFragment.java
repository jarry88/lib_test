package com.ftofs.twant.seller.fragment;

import android.graphics.Color;
import android.opengl.Visibility;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.AssetsUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ProcessProgressIndicator;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SquareGridLayout;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 訂單退貨退款詳情頁
 * @author gzp
 */
public class SellerRefundDetailFragment extends BaseFragment {

    private int ordersId;
    private boolean sellerAgree;
    private int currentStep=0;
    private int refundId;
    private int FragmentType=Constant.SELLER_REFUND;//Constant.SELLER_REFUND表示refund 其它表示return
    private int showStoreHandel;//1是0否可處理

    LinearLayout llAdiminContainer;

    TextView tvAdminState;

    TextView tvAdminInfo;

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }


    LinearLayout llButtonContainer;

    LinearLayout llReturnGoodContainer;

    ScaledButton btnReturnGood;

    ScaledButton btnReturnWithoutGood;


    void setReturn() {
        returnType = 2;
        btnReturnGood.setChecked(true);
        btnReturnWithoutGood.setChecked(false);
    }

    void setReturnNo() {
        returnType = 1;
        btnReturnGood.setChecked(false);
        btnReturnWithoutGood.setChecked(true);
    }
    private int returnType=2;//退貨類型 1棄貨 2退貨;

    public static SellerRefundDetailFragment newInstance(int refundId) {

        return newInstance(refundId,Constant.SELLER_REFUND);
    }

    public static SellerRefundDetailFragment newInstance(int refundId, int sellerRefund) {
        SellerRefundDetailFragment fragment = new SellerRefundDetailFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.refundId = refundId;
        fragment.FragmentType = sellerRefund;
        return fragment;

    }


    void back() {
        hideSoftInputPop();
    }


    void goBack() {
        hideSoftInputPop();
    }

    void commitForm(){
        collectFormInfo();
    }

    void toOrderInfo(){
        Util.startFragment(SellerOrderInfoFragment.newInstance(refundId,FragmentType!=Constant.SELLER_REFUND));
    }


    ScaledButton btnOk;

    ScaledButton btnNo;

    ProcessProgressIndicator indicator;

    TextView tvRefundState;

    TextView tvRefundSn;

    TextView tvBuyer;

    TextView tvRefundReason;

    TextView tvRefundAmount;

    TextView tvRefundDescribe;

    TextView tvSellerHandleState;

    TextView tvSellerReason;

    LinearLayout llSellerHandleInfo;

    LinearLayout llSellerHandleContainer;

    SquareGridLayout refundContentImageContainer;

    EditText etSellerMessage;

    void checkOk() {
        sellerAgree = true;
        btnOk.setChecked(true);
        btnOk.setIconResource(R.drawable.icon_checked);
        btnNo.setIconResource(R.drawable.icon_cart_item_unchecked);
        if (btnNo.isChecked()) {
            btnNo.setChecked(false);
        }
    }


    void checkNo(){
        btnNo.setChecked(true);
        btnNo.setIconResource(R.drawable.icon_checked);
        btnOk.setIconResource(R.drawable.icon_cart_item_unchecked);
        sellerAgree = false;
        if (btnOk.isChecked()) {
            btnOk.setChecked(false);
        }
    }

    private void collectFormInfo() {
        SLog.info("%s,%s",btnOk.isChecked(),btnNo.isChecked());
        if (btnOk.isChecked() ||btnNo.isChecked()) {

        }else{
            ToastUtil.error(_mActivity,"請選擇是否同意");
            return;
        }

        if (StringUtil.isEmpty(etSellerMessage.getText().toString())) {
            ToastUtil.error(_mActivity,"請填寫備注信息");
            return;
        }

        int sellerState = sellerAgree ? 2 : 3;
        String sellerMessage = etSellerMessage.getText().toString();
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(), "refundId", refundId, "sellerState", sellerState, "sellerMessage", sellerMessage);
        if (FragmentType != Constant.SELLER_REFUND) {
            try {
                params.set("returnType", returnType);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        String path =FragmentType==Constant.SELLER_REFUND?Api.PATH_SELLER_REFUND_HANDLE:Api.PATH_SELLER_RETURN_HANDLE;
          SLog.info("params[%s]", params);
             Api.postUI(path, params, new UICallback() {
                 @Override
                 public void onFailure(Call call, IOException e) {
                     ToastUtil.showNetworkError(_mActivity, e);
                 }
         
                 @Override
                 public void onResponse(Call call, String responseStr) throws IOException {
                     try {
                         SLog.info("responseStr[%s]", responseStr);
         
                         EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
//                         if (!Config.DEVELOPER_MODE) {

                             if (ToastUtil.checkError(_mActivity, responseObj)) {
                                 return;
                             }
                             ToastUtil.success(_mActivity, responseObj.getSafeString("datas.success"));
////                         }
//                         llSellerHandleContainer.setVisibility(View.GONE);
//                         llSellerHandleInfo.setVisibility(View.VISIBLE);
//                         tvSellerHandleState.setText(sellerAgree?"同意":"不同意");
//                         tvSellerReason.setText(sellerMessage);
//                         llAdiminContainer.setVisibility(View.VISIBLE);
//                         tvAdminState.setText("待審核");
//                         llButtonContainer.setVisibility(View.GONE);

//                         currentStep++;
//                         updateIndicator();
                         loadDate();
                     } catch (Exception e) {
                         SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                     }
                 }
             });

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_refund_detail, container, false);
        llAdiminContainer = (LinearLayout) view.findViewById(R.id.ll_admin_container);
        tvAdminState = (TextView) view.findViewById(R.id.tv_admin_handle_state);
        tvAdminInfo = (TextView) view.findViewById(R.id.tv_admin_info);
        llButtonContainer = (LinearLayout) view.findViewById(R.id.ll_button_cotainer);
        llReturnGoodContainer = (LinearLayout) view.findViewById(R.id.ll_widget_good_container);
        btnReturnGood = (ScaledButton) view.findViewById(R.id.btn_return_good);
        btnReturnWithoutGood = (ScaledButton) view.findViewById(R.id.btn_return_without_good);
        btnOk = (ScaledButton) view.findViewById(R.id.btn_check_ok);
        btnNo = (ScaledButton) view.findViewById(R.id.btn_check_no);
        indicator = (ProcessProgressIndicator) view.findViewById(R.id.progress_indicator);
        tvRefundState = (TextView) view.findViewById(R.id.tv_refund_state);
        tvRefundSn = (TextView) view.findViewById(R.id.tv_refund_sn);
        tvBuyer = (TextView) view.findViewById(R.id.tv_buyer);
        tvRefundReason = (TextView) view.findViewById(R.id.tv_refund_reason);
        tvRefundAmount = (TextView) view.findViewById(R.id.tv_refund_amount);
        tvRefundDescribe = (TextView) view.findViewById(R.id.tv_refund_describe);
        tvSellerHandleState = (TextView) view.findViewById(R.id.tv_seller_handle_state);
        tvSellerReason = (TextView) view.findViewById(R.id.tv_seller_reason);
        llSellerHandleInfo = (LinearLayout) view.findViewById(R.id.ll_seller_refund_info_container);
        llSellerHandleContainer = (LinearLayout) view.findViewById(R.id.ll_seller_handle_container);
        refundContentImageContainer = (SquareGridLayout) view.findViewById(R.id.refund_content_image_container);
        etSellerMessage = (EditText) view.findViewById(R.id.et_refund_seller_note);
        view.findViewById(R.id.btn_check_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkNo();
            }
        });
        view.findViewById(R.id.btn_check_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkOk();
            }
        });
        view.findViewById(R.id.btn_menu).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toOrderInfo();
            }
        });
        view.findViewById(R.id.btn_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commitForm();
            }
        });
        view.findViewById(R.id.btn_return).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        view.findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
        view.findViewById(R.id.btn_return_without_good).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReturnNo();
            }
        });
        view.findViewById(R.id.btn_return_good).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReturn();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        indicator.setTheme();
        btnReturnGood.setButtonCheckedBlue();
        btnReturnWithoutGood.setButtonCheckedBlue();
        llReturnGoodContainer.setVisibility(FragmentType == Constant.SELLER_REFUND?View.GONE:View.VISIBLE );
        btnReturnGood.performClick();
        btnOk.performClick();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        loadDate();
    }

    private void loadDate() {
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(),"refundId",refundId);
          SLog.info("params[%s]", params);
        String path = FragmentType == Constant.SELLER_REFUND ? Api.PATH_SELLER_REFUND_INFO : Api.PATH_SELLER_RETURN_INFO;
          path=path+"/"+refundId;
             Api.getUI(path, params, new UICallback() {
                 @Override
                 public void onFailure(Call call, IOException e) {
                     ToastUtil.showNetworkError(_mActivity, e);
                 }

                 @Override
                 public void onResponse(Call call, String responseStr) throws IOException {
                     try {
                         SLog.info("responseStr[%s]", responseStr);
//                         responseStr= AssetsUtil.loadText(_mActivity,"tangram/test.json");
                         EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                         if (ToastUtil.checkError(_mActivity, responseObj)) {
                             return;
                         }
                         EasyJSONObject refundInfo = responseObj.getObject("datas.refundInfo");
                         updateView(refundInfo);
                     } catch (Exception e) {
                         SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                     }
                 }
             });
    }

    private void updateView(EasyJSONObject refundInfo) throws Exception {
        int sellerState = refundInfo.getInt("sellerState");
        currentStep = 0;
        if (sellerState > 1) { // 商家已處理
            currentStep++;
            int adminState = refundInfo.getInt("adminState");
            if (adminState > 1) {
                currentStep++;
            }
        }
//        currentStep = refundInfo.getInt("refundState");

        updateIndicator();
        showStoreHandel = refundInfo.getInt("showStoreHandel");
//        if (Config.DEVELOPER_MODE) {
//            currentStep = 3;
//            showStoreHandel = 1;
//        }
        refundId = refundInfo.getInt("refundId");
        tvRefundSn.setText(String.valueOf(refundInfo.getLong("refundSn")));
        tvRefundAmount.setText(StringUtil.formatPrice(_mActivity,refundInfo.getDouble("refundAmount"),1));
        tvRefundAmount.setTextColor(Color.RED);
//        tvRefundState.setText( refundInfo.getSafeString("refundStateTextForSelf"));
        tvRefundState.setText( refundInfo.getSafeString("currentStateText"));
        tvRefundReason.setText(refundInfo.getSafeString("reasonInfo"));
        tvRefundDescribe.setText(refundInfo.getSafeString("buyerMessage"));
        tvBuyer.setText(refundInfo.getSafeString("memberName"));
        tvSellerHandleState.setText(refundInfo.getSafeString("sellerStateText"));
        String picJson = refundInfo.getSafeString("picJson");

        if (!StringUtil.isEmpty(picJson)) {
            String[] imageList=picJson.split(",");
            for (String object : imageList) {
                View imageItem =LayoutInflater.from(_mActivity).inflate(R.layout.post_content_image_widget, refundContentImageContainer, false);
                imageItem.findViewById(R.id.btn_remove_image).setVisibility(View.GONE);
                Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(object)).into((ImageView) imageItem.findViewById(R.id.post_image));
                refundContentImageContainer.addView(imageItem);
            }
        }

        if (showStoreHandel == 1) {
            llSellerHandleContainer.setVisibility(View.VISIBLE);
            llSellerHandleInfo.setVisibility(View.GONE);
        } else {
            tvSellerHandleState.setText(refundInfo.getSafeString("sellerStateText"));
            tvSellerReason.setText(refundInfo.getSafeString("sellerMessage"));
            llButtonContainer.setVisibility(View.GONE);
//            if (currentStep == 3) {
                llAdiminContainer.setVisibility(View.VISIBLE);
                tvAdminState.setText(refundInfo.getSafeString("adminStateText"));
                tvAdminInfo.setText(refundInfo.getSafeString("adminMessage"));
//            }
        }
        updateIndicator();
    }

    private void updateIndicator() {
        List<String> progressList = new ArrayList<>();
        progressList.add("申請退款");//1 處理中
        progressList.add("商家處理");//2、帶管理員處理

        if (FragmentType == Constant.SELLER_RETURN) {
            progressList.add("買家退貨");
        }
        progressList.add("完成退款");//3、已完成
        indicator.setData(progressList,currentStep);

    }
}
