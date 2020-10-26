package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ProcessProgressIndicator;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 退款詳情Fragment
 * @author zwm
 */
public class RefundDetailFragment extends BaseFragment implements View.OnClickListener {
    int action;
    int refundId;
    EasyJSONObject paramsInObj;


    TextView tvFragmentTitle;

    ProcessProgressIndicator indicator;
    // 當前已經處理到哪一步
    int currentStep = 0;

    LinearLayout llWidgetContainer;

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

    TextView tvComplainTime;
    TextView tvComplainSubject;
    TextView tvComplainContent;

    TextView tvFreightAmount;
    TextView tvOrderTotalAmount;
    TextView tvOrderNum;
    TextView tvLogisticsOrderNo;
    TextView tvMerchant;


    public static RefundDetailFragment newInstance(int refundId, String paramsIn) {
        Bundle args = new Bundle();

        args.putInt("refundId", refundId);
        args.putString("paramsIn", paramsIn);

        RefundDetailFragment fragment = new RefundDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;

        Bundle args = getArguments();
        refundId = args.getInt("refundId");
        String paramsIn = args.getString("paramsIn");
        paramsInObj = EasyJSONObject.parse(paramsIn);


        try {
            action = paramsInObj.getInt("action");
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
        if (action == Constant.ACTION_REFUND || action == Constant.ACTION_RETURN) {
            view = inflater.inflate(R.layout.fragment_refund_detail, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_refund_detail_complain, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);

        indicator = view.findViewById(R.id.progress_indicator);

        llWidgetContainer = view.findViewById(R.id.ll_widget_container);
        if (action == Constant.ACTION_REFUND) {
            tvFragmentTitle.setText(R.string.text_refund_detail);
            LayoutInflater.from(_mActivity).inflate(R.layout.refund_detail_widget, llWidgetContainer, true);
        } else if (action == Constant.ACTION_RETURN) {
            tvFragmentTitle.setText(R.string.text_return_detail);
            LayoutInflater.from(_mActivity).inflate(R.layout.return_detail_widget, llWidgetContainer, true);
        } else if (action == Constant.ACTION_COMPLAIN) {
            tvFragmentTitle.setText(R.string.text_complain_detail);
        }


        tvOrderNo = view.findViewById(R.id.tv_order_no);
        goodsImage = view.findViewById(R.id.goods_image);
        tvGoodsName = view.findViewById(R.id.tv_goods_name);
        tvGoodsFullSpecs = view.findViewById(R.id.tv_goods_full_specs);
        tvGoodsPrice = view.findViewById(R.id.tv_goods_price_left);
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

        tvComplainTime = view.findViewById(R.id.tv_complain_time);
        tvComplainSubject = view.findViewById(R.id.tv_complain_subject);
        tvComplainContent = view.findViewById(R.id.tv_complain_content);

        tvFreightAmount = view.findViewById(R.id.tv_freight_amount);
        tvOrderTotalAmount = view.findViewById(R.id.tv_order_total_amount);
        tvOrderNum = view.findViewById(R.id.tv_order_num);
        tvLogisticsOrderNo = view.findViewById(R.id.tv_logistics_order_no);
        tvMerchant = view.findViewById(R.id.tv_merchant);

        loadRefundDetail();
    }
    
    
    
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
    }

    private void loadRefundDetail() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            SLog.info("Error!token 為空");
            return;
        }

        final EasyJSONObject params = EasyJSONObject.generate("token", token);

        String path = null;
        try {
            if (action == Constant.ACTION_REFUND) {
                path = Api.PATH_REFUND_INFO;
                params.set("refundId", refundId);
            } else if (action == Constant.ACTION_RETURN) {
                path = Api.PATH_RETURN_INFO;
                params.set("refundId", refundId);
            } else {
                path = Api.PATH_COMPLAIN_INFO;
                params.set("complainId", refundId);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }


        SLog.info("path[%s], params[%s]", path, params);
        Api.postUI(path, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);

                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    List<String> progressList = new ArrayList<>();
                    if (action == Constant.ACTION_REFUND) {
                        progressList.add(getString(R.string.text_refund_applied));
                        progressList.add(getString(R.string.text_seller_process));
                        progressList.add(getString(R.string.text_refund_finished));
                    } else if (action == Constant.ACTION_RETURN) {
                        progressList.add(getString(R.string.text_return_applied));
                        progressList.add(getString(R.string.text_seller_process));
                        progressList.add(getString(R.string.text_buyer_returned));
                        progressList.add(getString(R.string.text_refund_finished));
                    } else {
                        progressList.add(getString(R.string.text_new_complain));
                        progressList.add(getString(R.string.text_to_be_applied));
                        progressList.add(getString(R.string.text_in_conversation));
                        progressList.add(getString(R.string.text_to_be_arbitrated));
                        progressList.add(getString(R.string.text_finished));
                    }

                    if (action == Constant.ACTION_REFUND || action == Constant.ACTION_RETURN) {
                        EasyJSONObject refundItemVo = responseObj.getSafeObject("datas.refundItemVo");

                        int sellerState = refundItemVo.getInt("sellerState");
                        if (sellerState > 1) { // 商家已處理
                            currentStep++;
                            int adminState = refundItemVo.getInt("adminState");
                            if (adminState > 1) {
                                currentStep++;
                            }
                        }

                        long ordersSn = refundItemVo.getLong("ordersSn");
                        tvOrderNo.setText(String.valueOf(ordersSn));

                        String goodsImageUrl = refundItemVo.getSafeString("goodsImage");
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(goodsImageUrl)).centerCrop().into(goodsImage);

                        tvGoodsName.setText(refundItemVo.getSafeString("goodsName"));
                        tvGoodsFullSpecs.setText(paramsInObj.getSafeString("goodsFullSpecs"));

                        tvGoodsPrice.setText(StringUtil.formatPrice(_mActivity, paramsInObj.getDouble("goodsPrice"), 0));
                        tvBuyNum.setText(getString(R.string.times_sign) + " " + paramsInObj.getInt("buyNum"));

                        String refundStatus = refundItemVo.getSafeString("currentStateText");
                        tvRefundStatus.setText(refundStatus);

                        long refundSn = refundItemVo.getLong("refundSn");
                        tvReturnNo.setText(String.valueOf(refundSn));

                        tvReturnReason.setText(refundItemVo.getSafeString("reasonInfo"));
                        float refundAmount = (float) refundItemVo.getDouble("refundAmount");
                        tvReturnAmount.setText(StringUtil.formatPrice(_mActivity, refundAmount, 0));

                        tvReturnDesc.setText(refundItemVo.getSafeString("buyerMessage"));

                        String picJson = refundItemVo.getSafeString("picJson");
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

                        tvAuditStatus.setText(refundItemVo.getSafeString("sellerStateText"));
                        String sellerMessage = refundItemVo.getSafeString("sellerMessage");
                        if (!StringUtil.isEmpty(sellerMessage)) {
                            tvSellerRemark.setText(sellerMessage);
                        }


                        tvPlatformAudit.setText(refundItemVo.getSafeString("refundStateText"));
                        String adminMessage = refundItemVo.getSafeString("adminMessage");
                        if (!StringUtil.isEmpty(adminMessage)) {
                            tvPlatformRemark.setText(adminMessage);
                        }

                        tvRefundStatus2.setText(refundStatus);
                    } else {
                        EasyJSONObject complain = responseObj.getSafeObject("datas.complain");
                        String complainTime = complain.getSafeString("accuserTime");
                        tvComplainTime.setText(complainTime);

                        String complainSubject = complain.getSafeString("subjectTitle");
                        tvComplainSubject.setText(complainSubject);

                        String complainContent = complain.getSafeString("accuserContent");
                        tvComplainContent.setText(complainContent);

                        String accuserImages = complain.getSafeString("accuserImages");

                        String[] evidenceArr = accuserImages.split(",");
                        if (evidenceArr.length > 0) {
                            imgEvidence1.setVisibility(View.VISIBLE);
                            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(evidenceArr[0])).centerCrop().into(imgEvidence1);
                        }
                        if (evidenceArr.length > 1) {
                            imgEvidence2.setVisibility(View.VISIBLE);
                            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(evidenceArr[1])).centerCrop().into(imgEvidence2);
                        }
                        if (evidenceArr.length > 2) {
                            imgEvidence3.setVisibility(View.VISIBLE);
                            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(evidenceArr[2])).centerCrop().into(imgEvidence3);
                        }


                        String goodsName = complain.getSafeString("goodsName");
                        tvGoodsName.setText(goodsName);

                        String goodsImageUrl = complain.getSafeString("goodsImage");
                        Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(goodsImageUrl)).centerCrop().into(goodsImage);

                        String goodsFullSpecs = complain.getSafeString("goodsFullSpecs");
                        tvGoodsFullSpecs.setText(goodsFullSpecs);

                        EasyJSONObject ordersVo = responseObj.getSafeObject("datas.ordersVo");
                        // EasyJSONObject ordersGoodsVo = ordersVo.getSafeObject("ordersGoodsVoList[0]");
                        float freightAmount = (float) ordersVo.getDouble("freightAmount");
                        float ordersAmount = (float) ordersVo.getDouble("ordersAmount");

                        EasyJSONObject ordersGoodsVo = responseObj.getSafeObject("datas.ordersGoodsVo");
                        float goodsPrice = (float) ordersGoodsVo.getDouble("goodsPrice");
                        tvGoodsPrice.setText(StringUtil.formatPrice(_mActivity, goodsPrice, 0));
                        int buyNum = ordersGoodsVo.getInt("buyNum");
                        tvBuyNum.setText(getString(R.string.times_sign) + " " + buyNum);

                        tvFreightAmount.setText(StringUtil.formatPrice(_mActivity, freightAmount, 1));
                        tvOrderTotalAmount.setText(StringUtil.formatPrice(_mActivity, ordersAmount, 1));
                        tvOrderNum.setText(ordersVo.getSafeString("ordersSnStr"));
                        tvLogisticsOrderNo.setText(ordersVo.getSafeString("shipSn"));
                        tvMerchant.setText(ordersVo.getSafeString("storeName"));
                    }

                    indicator.setData(progressList, currentStep);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
