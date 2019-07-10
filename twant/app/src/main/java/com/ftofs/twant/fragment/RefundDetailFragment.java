package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ProcessProgressIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 退款詳情Fragment
 * @author zwm
 */
public class RefundDetailFragment extends BaseFragment implements View.OnClickListener {
    int refundId;
    EasyJSONObject paramsInObj;

    ProcessProgressIndicator indicator;
    // 當前已經處理到哪一步
    int currentStep = 0;

    TextView tvOrderNo;
    ImageView goodsImage;
    TextView tvGoodsName;
    TextView tvGoodsFullSpecs;
    TextView tvGoodsPrice;
    TextView tvBuyNum;
    TextView tvRefundStatus;
    TextView tvReturnNo;
    TextView tvReturnReason;
    TextView tvReturnAmount;
    TextView tvReturnDesc;

    ImageView imgEvidence1;
    ImageView imgEvidence2;
    ImageView imgEvidence3;

    TextView tvAuditStatus;
    TextView tvSellerRemark;

    TextView tvPlatformAudit;
    TextView tvPlatformRemark;

    TextView tvRefundStatus2;
    TextView tvRefundWay;
    TextView tvRefundAmount;


    public static RefundDetailFragment newInstance(int refundId, String paramsIn) {
        Bundle args = new Bundle();

        args.putInt("refundId", refundId);
        args.putString("paramsIn", paramsIn);

        RefundDetailFragment fragment = new RefundDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refund_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        refundId = args.getInt("refundId");
        String paramsIn = args.getString("paramsIn");
        paramsInObj = (EasyJSONObject) EasyJSONObject.parse(paramsIn);

        Util.setOnClickListener(view, R.id.btn_back, this);

        indicator = view.findViewById(R.id.progress_indicator);

        tvOrderNo = view.findViewById(R.id.tv_order_no);
        goodsImage = view.findViewById(R.id.goods_image);
        tvGoodsName = view.findViewById(R.id.tv_goods_name);
        tvGoodsFullSpecs = view.findViewById(R.id.tv_goods_full_specs);
        tvGoodsPrice = view.findViewById(R.id.tv_goods_price);
        tvBuyNum = view.findViewById(R.id.tv_buy_num);
        tvRefundStatus = view.findViewById(R.id.tv_refund_status);
        tvReturnNo = view.findViewById(R.id.tv_return_no);
        tvReturnReason = view.findViewById(R.id.tv_return_reason);
        tvReturnAmount = view.findViewById(R.id.tv_return_amount);
        tvReturnDesc = view.findViewById(R.id.tv_return_desc);

        imgEvidence1 = view.findViewById(R.id.img_evidence1);
        imgEvidence2 = view.findViewById(R.id.img_evidence2);
        imgEvidence3 = view.findViewById(R.id.img_evidence3);

        tvAuditStatus = view.findViewById(R.id.tv_audit_status);
        tvSellerRemark = view.findViewById(R.id.tv_seller_remark);

        tvPlatformAudit = view.findViewById(R.id.tv_platform_audit);
        tvPlatformRemark = view.findViewById(R.id.tv_platform_remark);

        tvRefundStatus2 = view.findViewById(R.id.tv_refund_status2);
        tvRefundWay = view.findViewById(R.id.tv_refund_way);
        tvRefundAmount = view.findViewById(R.id.tv_refund_amount);

        loadRefundDetail();
    }
    
    
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }

    private void loadRefundDetail() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            SLog.info("Error!token 為空");
            return;
        }

        final EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "refundId", refundId);

        SLog.info("params[%s]", params);

        Api.postUI(Api.PATH_REFUND_INFO, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONObject refundItemVo = responseObj.getObject("datas.refundItemVo");

                    int sellerState = refundItemVo.getInt("sellerState");
                    if (sellerState > 1) { // 商家已處理
                        currentStep++;
                        int adminState = refundItemVo.getInt("adminState");
                        if (adminState > 1) {
                            currentStep++;
                        }
                    }

                    List<String> progressList = new ArrayList<>();
                    progressList.add(getString(R.string.text_refund_applied));
                    progressList.add(getString(R.string.text_seller_process));
                    progressList.add(getString(R.string.text_refund_finished));

                    indicator.setData(progressList, currentStep);


                    long ordersSn = refundItemVo.getLong("ordersSn");
                    tvOrderNo.setText(String.valueOf(ordersSn));

                    String goodsImageUrl = refundItemVo.getString("goodsImage");
                    Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(goodsImageUrl)).centerCrop().into(goodsImage);

                    tvGoodsName.setText(refundItemVo.getString("goodsName"));
                    tvGoodsFullSpecs.setText(paramsInObj.getString("goodsFullSpecs"));

                    tvGoodsPrice.setText(StringUtil.formatPrice(_mActivity, (float) paramsInObj.getDouble("goodsPrice"), 0));
                    tvBuyNum.setText(getString(R.string.times_sign) + " " + paramsInObj.getInt("buyNum"));

                    String refundStatus = refundItemVo.getString("currentStateText");
                    tvRefundStatus.setText(refundStatus);

                    long refundSn = refundItemVo.getLong("refundSn");
                    tvReturnNo.setText(String.valueOf(refundSn));

                    tvReturnReason.setText(refundItemVo.getString("reasonInfo"));
                    float refundAmount = (float) refundItemVo.getDouble("refundAmount");
                    tvReturnAmount.setText(StringUtil.formatPrice(_mActivity, refundAmount, 0));

                    tvReturnDesc.setText(refundItemVo.getString("buyerMessage"));

                    String picJson = refundItemVo.getString("picJson");
                    String[] picJsonArr = picJson.split(",");

                    if (picJsonArr.length > 0) {
                        imgEvidence1.setVisibility(View.VISIBLE);
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(picJsonArr[0])).centerCrop().into(imgEvidence1);
                    }
                    if (picJsonArr.length > 1) {
                        imgEvidence2.setVisibility(View.VISIBLE);
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(picJsonArr[1])).centerCrop().into(imgEvidence2);
                    }
                    if (picJsonArr.length > 2) {
                        imgEvidence3.setVisibility(View.VISIBLE);
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(picJsonArr[2])).centerCrop().into(imgEvidence3);
                    }

                    tvAuditStatus.setText(refundItemVo.getString("sellerStateText"));
                    String sellerMessage = refundItemVo.getString("sellerMessage");
                    if (!StringUtil.isEmpty(sellerMessage)) {
                        tvSellerRemark.setText(sellerMessage);
                    }


                    tvPlatformAudit.setText(refundItemVo.getString("refundStateText"));
                    String adminMessage = refundItemVo.getString("adminMessage");
                    if (!StringUtil.isEmpty(adminMessage)) {
                        tvPlatformRemark.setText(adminMessage);
                    }

                    tvRefundStatus2.setText(refundStatus);
                } catch (Exception e) {

                }
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
