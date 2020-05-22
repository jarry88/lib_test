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
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.fragment.SellerRefundFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.widget.ProcessProgressIndicator;
import com.ftofs.twant.widget.ScaledButton;
import com.github.piasy.biv.indicator.ProgressIndicator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.ISupportFragment;
import okhttp3.Call;

/**
 * @author gzp
 */
public class SellerRefundDetailFragment extends BaseFragment {

    private int ordersId;
    private boolean sellerAgree;
    private int currentStep=0;
    private long refundSn;

    public static SellerRefundDetailFragment newInstance(long ordersId) {
        SellerRefundDetailFragment fragment = new SellerRefundDetailFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragment.refundSn = ordersId;
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
    @OnClick(R.id.btn_check_ok)
    void checkOk() {
        sellerAgree = true;
        btnOk.setIconResource(R.drawable.icon_checked);
        btnNo.setIconResource(R.drawable.icon_cart_item_unchecked);
        if (btnNo.isChecked()) {
            btnNo.setChecked(false);
        }
    }

    @OnClick(R.id.btn_check_no)
    void checkNo(){
        btnNo.setIconResource(R.drawable.icon_checked);
        btnOk.setIconResource(R.drawable.icon_cart_item_unchecked);
        sellerAgree = false;
        if (btnOk.isChecked()) {
            btnOk.setChecked(false);
        }
    }
    private void collectFormInfo() {

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
        List<String> progressList = new ArrayList<>();
        progressList.add("買家\n申請退款");
        progressList.add("商家處理\n退款申請");
        progressList.add("平臺審核\n退款完成");
        indicator.setData(progressList,currentStep);
        loadDate();
    }

    private void loadDate() {
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(),"refundId",refundSn);
          SLog.info("params[%s]", params);
          String path =Api.PATH_SELLER_REFUND_INFO+"/"+refundSn;
             Api.getUI(path, params, new UICallback() {
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
                     } catch (Exception e) {
                         SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                     }
                 }
             });
    }
}
