package com.ftofs.twant.fragment;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alipay.android.app.IAlixPay;
import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.kyleduo.switchbutton.SwitchButton;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cc.ibooker.ztextviewlib.AutoVerticalScrollTextView;
import cn.snailpad.easyjson.EasyJSONObject;
import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.fragmentation.ISupportFragment;
import okhttp3.Call;

/**
 * @author gzp
 * Created 2020-4-28 上午 10:21
 * 商家首頁
 */

public class SellerHomeFragment extends BaseFragment{

    @BindView(R.id.img_seller_avatar)
    CircleImageView imgSellerAvatar;
    @BindView(R.id.tv_orders_wait_send_count)
    TextView tvOrderWaitSendCount;
    @BindView(R.id.tv_refund_waiting_count)
    TextView tvRefundWaitingCount;
    @BindView(R.id.tv_goods_common_fail_count)
    TextView tvGoodsCommonFailCount;
    @BindView(R.id.tv_bill_count)
    TextView tvBillCount;

    @OnClick(R.id.btn_goto_member)
    void popBack(){
        pop();
    }
    @BindView(R.id.sw_seller_business_state)
    SwitchButton swBusinessState;
    @BindView(R.id.img_seller_home_background)
    ImageView sellerHomeBackground;
    @BindView(R.id.tv_store_name)
    TextView tvStoreName;

    @BindView(R.id.img_avatar)
    CircleImageView imgStoreAvatar;
    @BindView(R.id.tv_store_signature)
    TextView tvStoreSignature;
    @BindView(R.id.tv_vertical_scroll)
    AutoVerticalScrollTextView tvVerticalScroll;
    @BindView(R.id.tv_today_amount)
    TextView tvTodayAmount;

    @BindView(R.id.tv_today_order_count)
    TextView tvTodayOrderCount;
    @BindView(R.id.tv_today_view_count)
    TextView tvTodayViewCount;
    @BindView(R.id.tv_message_count)
    TextView tvMessageCount;
    @BindView(R.id.tv_new_comment_count)
    TextView tvTodayCommentedCount;

    @OnClick(R.id.btn_goods_info)
    void showGoodsInfo() {

    }

    @OnClick(R.id.btn_orders_info)
    void showOrdersInfo() {

    }

    @BindView(R.id.tv_goods_common_onsale_count)
    TextView tvOnSaleCount;

    public static SellerHomeFragment newInstance() {

        SellerHomeFragment fragment = new SellerHomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private Unbinder unbinder;

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        loadSellerData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_home, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view,savedInstanceState);
        initView();
        loadSellerData();
    }

    private void initView() {
        swBusinessState.setText("營業","休息");
        swBusinessState.setHighlightColor(getResources().getColor(R.color.tw_blue));

    }

    private void loadSellerData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        Api.getUI(Api.PATH_SELLER_INDEX, EasyJSONObject.generate("token", token), new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responsetr[%s]",responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }
                updateView(responseObj);
            }
        });
    }

    private void updateView(EasyJSONObject responseObj) {
        try {
            String todayViewCount = responseObj.getSafeString("datas.todayViewCount");
            tvTodayViewCount.setText(String.valueOf(todayViewCount));
            int billCount = responseObj.getInt("datas.billCount");
            tvBillCount.setText(String.valueOf(billCount));
            double ordersAmount = responseObj.getDouble("datas.orderAmount");
            tvTodayAmount.setText(String.valueOf(ordersAmount));
            int storeState = responseObj.getInt("datas.storeState");
            updateSwitchButton(storeState == Constant.TRUE_INT);

            String storeWeekDayEndTime = responseObj.getSafeString("datas.storeWeekDayEndTime");
            String storeAvatar = responseObj.getSafeString("datas.storeAvatar");
            if (!StringUtil.isEmpty(storeAvatar)) {
                Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(storeAvatar)).centerCrop().into(imgStoreAvatar);
            }
            String storeVideo = responseObj.getSafeString("datas.storeVideo");
            String storeRestDayStart = responseObj.getSafeString("datas.storeRestDayStart");
            int ordersCount = responseObj.getInt("datas.ordersCount");
            tvTodayOrderCount.setText(String.valueOf(ordersCount));
            int ordersWaitSendCount = responseObj.getInt("datas.ordersWaitSendCount");
            tvOrderWaitSendCount.setText(String.valueOf(ordersWaitSendCount));
            int refundWaitingCount = responseObj.getInt("datas.refundWaitingCount");
            tvRefundWaitingCount.setText(String.valueOf(refundWaitingCount));
            int goodsCommonVerifyFailCount = responseObj.getInt("datas.goodsCommonVerifyFailCount");
            tvGoodsCommonFailCount.setText(String.valueOf(goodsCommonVerifyFailCount));
            int newCommonCount = responseObj.getInt("datas.newCommonCount");
            tvTodayCommentedCount.setText(String.valueOf(newCommonCount));
            int complainAccessCount = responseObj.getInt("datas.complainAccessCount");
            int complainTalkCount = responseObj.getInt("datas.complainTalkCount");
            int goodsStockAlarmCount = responseObj.getInt("datas.goodsStockAlarmCount");
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void updateSwitchButton(boolean enable) {
        swBusinessState.setChecked(enable);
        swBusinessState.setBackgroundColor(getResources().getColor(enable?R.color.tw_blue:R.color.tw_slight_grey));
    }
}
