package com.ftofs.twant.seller.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * @author gzp
 */
public class SellerRefundDetailFragment extends BaseFragment {

    private int ordersId;
    private boolean sellerAgree;
    private int currentStep=0;
    private int refundId;

    public static SellerRefundDetailFragment newInstance(int refundId) {
        SellerRefundDetailFragment fragment = new SellerRefundDetailFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.refundId = refundId;
        return fragment;
    }
    @OnClick(R.id.btn_back)
    void back() {
        hideSoftInputPop();
    }

    @OnClick(R.id.btn_return)
    void goBack() {
        hideSoftInputPop();
    }
    @OnClick(R.id.btn_commit)
    void commitForm(){
        collectFormInfo();
    }
    @OnClick(R.id.btn_menu)
    void toOrderInfo(){
        Util.startFragment(SellerOrderInfoFragment.newInstance(refundId));
    }

    @BindView(R.id.btn_check_ok)
    ScaledButton btnOk;
    @BindView(R.id.btn_check_no)
    ScaledButton btnNo;
    @BindView(R.id.progress_indicator)
    ProcessProgressIndicator indicator;
    @BindView(R.id.tv_refund_state)
    TextView tvRefundState;
    @BindView(R.id.tv_refund_sn)
    TextView tvRefundSn;
    @BindView(R.id.tv_buyer)
    TextView tvBuyer;
    @BindView(R.id.tv_refund_reason)
    TextView tvRefundReason;
    @BindView(R.id.tv_refund_amount)
    TextView tvRefundAmount;
    @BindView(R.id.tv_refund_describe)
    TextView tvRefundDescribe;
    @BindView(R.id.tv_seller_handle_state)
    TextView tvSellerHandleState;
    @BindView(R.id.tv_seller_reason)
    TextView tvSellerReason;
    @BindView(R.id.ll_seller_refund_info_container)
    LinearLayout llSellerHandleInfo;
    @BindView(R.id.ll_seller_handle_container)
    LinearLayout llSellerHandleContainer;
    @BindView(R.id.refund_content_image_container)
    SquareGridLayout refundContentImageContainer;
    @BindView(R.id.et_refund_seller_note)
    EditText etSellerMessage;
    @OnClick(R.id.btn_check_ok)
    void checkOk() {
        sellerAgree = true;
        btnOk.setChecked(true);
        btnOk.setIconResource(R.drawable.icon_checked);
        btnNo.setIconResource(R.drawable.icon_cart_item_unchecked);
        if (btnNo.isChecked()) {
            btnNo.setChecked(false);
        }
    }

    @OnClick(R.id.btn_check_no)
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
          SLog.info("params[%s]", params);
             Api.postUI(Api.PATH_SELLER_REFUND_HANDLE, params, new UICallback() {
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
                         llSellerHandleContainer.setVisibility(View.GONE);
                         llSellerHandleInfo.setVisibility(View.VISIBLE);
                         tvSellerHandleState.setText(sellerAgree?"同意":"不同意");
                         tvSellerReason.setText(sellerMessage);
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

    private Unbinder unbinder;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_refund_detail, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        indicator.setTheme();

        loadDate();
    }

    private void loadDate() {
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(),"refundId",refundId);
          SLog.info("params[%s]", params);
          String path =Api.PATH_SELLER_REFUND_INFO+"/"+refundId;
             Api.getUI(path, params, new UICallback() {
                 @Override
                 public void onFailure(Call call, IOException e) {
                     ToastUtil.showNetworkError(_mActivity, e);
                 }

                 @Override
                 public void onResponse(Call call, String responseStr) throws IOException {
                     try {
                         SLog.info("responseStr[%s]", responseStr);
                         responseStr= AssetsUtil.loadText(_mActivity,"tangram/test.json");
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
        List<String> progressList = new ArrayList<>();
        progressList.add("  買家\n申請退款");//1 處理中
        progressList.add("商家處理\n退款申請");//2、帶管理員處理
        progressList.add("平臺審核\n退款完成");//3、已完成
        currentStep = refundInfo.getInt("refundState");
        indicator.setData(progressList,currentStep-1);
        refundId = refundInfo.getInt("refundId");
        tvRefundSn.setText(String.valueOf(refundInfo.getLong("refundSn")));
        tvRefundAmount.setText(StringUtil.formatPrice(_mActivity,refundInfo.getDouble("refundAmount"),1));
        tvRefundAmount.setTextColor(Color.RED);
        tvRefundState.setText( refundInfo.getSafeString("refundStateTextForSelf"));
        tvRefundReason.setText(refundInfo.getSafeString("reasonInfo"));
        tvBuyer.setText(refundInfo.getSafeString("memberName"));
        String picJson = refundInfo.getSafeString("picJson");

        if (!StringUtil.isEmpty(picJson)) {
            String[] imageList=picJson.split(",");
            for (String object : imageList) {
                View imageItem =LayoutInflater.from(_mActivity).inflate(R.layout.post_content_image_widget, refundContentImageContainer, false);
                Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(object)).into((ImageView) imageItem.findViewById(R.id.post_image));
                refundContentImageContainer.addView(imageItem);
            }
        }

        if (currentStep == 1) {
            llSellerHandleContainer.setVisibility(View.VISIBLE);
            llSellerHandleInfo.setVisibility(View.GONE);
        } else {
            tvSellerHandleState.setText(refundInfo.getSafeString("sellerState"));
            tvSellerReason.setText(refundInfo.getSafeString("sellerMessage"));
        }
    }
}
