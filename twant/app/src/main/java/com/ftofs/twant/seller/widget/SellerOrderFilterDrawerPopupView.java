package com.ftofs.twant.seller.widget;

import android.content.Context;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.seller.entity.SellerOrderFilterParams;
import com.ftofs.twant.seller.entity.TwDate;
import com.gzp.lib_common.base.Jarbon;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.lib_common_ui.popup.ListPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.DrawerPopupView;

import java.util.ArrayList;
import java.util.List;

public class SellerOrderFilterDrawerPopupView extends DrawerPopupView implements View.OnClickListener, OnSelectedListener {
    private PopupType type;
    Context context;
    SimpleCallback simpleCallback;

    EditText etBuyerNickname;
    EditText etBuyerMobile;
    EditText etOrderSN;
    EditText etRefundSN;
    EditText etGoodsName;
    EditText etReceiverMobile;
    EditText etReceiverAddress;

    TextView tvBeginDate;
    TextView tvEndDate;
    TextView tvOrderType;
    TextView tvOrderSource;
    TextView tvSearchType;

    private int orderTypeSelectedIndex = 0;
    List<ListPopupItem> orderTypeList;
    private int orderSourceSelectedIndex = 0;
    List<ListPopupItem> orderSourceList;

    TwDate beginDate;
    TwDate endDate;
    private int refundSearchTypeSelectedIndex=0;
    List<ListPopupItem> refundSearchTypeList;
    public String searchType="all";

    public SellerOrderFilterDrawerPopupView(@NonNull Context context, SimpleCallback simpleCallback) {
        super(context);

        this.context = context;
        this.simpleCallback = simpleCallback;
    }

    public SellerOrderFilterDrawerPopupView(@NonNull Context context,PopupType type, SimpleCallback simpleCallback) {
        super(context);
        this.type = type;
        this.context = context;
        this.simpleCallback = simpleCallback;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.seller_order_filter_drawer_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        orderTypeList = new ArrayList<>();
        orderTypeList.add(new ListPopupItem(0, "所有", null));
        orderTypeList.add(new ListPopupItem(1, "零售", null));
        orderTypeList.add(new ListPopupItem(2, "代客下單", null));
        // orderTypeList.add(new ListPopupItem(3, "跨城購", null));

        orderSourceList = new ArrayList<>();
        orderSourceList.add(new ListPopupItem(0, "所有", null));
        orderSourceList.add(new ListPopupItem(1, "PC", null));
        orderSourceList.add(new ListPopupItem(2, "門店", null));

        refundSearchTypeList = new ArrayList<>();
        refundSearchTypeList.add(new ListPopupItem(0, "全部", "all"));
        refundSearchTypeList.add(new ListPopupItem(1, "待處理", "waiting"));
        refundSearchTypeList.add(new ListPopupItem(2, "同意", "agree"));
        refundSearchTypeList.add(new ListPopupItem(3, "不同意", "disagree"));

        findViewById(R.id.ll_popup_content_view).setOnClickListener(this);
        findViewById(R.id.btn_select_order_type).setOnClickListener(this);
        findViewById(R.id.btn_select_order_source).setOnClickListener(this);
        findViewById(R.id.btn_select_begin_date).setOnClickListener(this);
        findViewById(R.id.btn_select_end_date).setOnClickListener(this);
        findViewById(R.id.btn_select_search_type).setOnClickListener(this);
        findViewById(R.id.btn_reset).setOnClickListener(this);
        findViewById(R.id.btn_ok).setOnClickListener(this);

        etBuyerNickname = findViewById(R.id.et_buyer_nickname);
        etBuyerMobile = findViewById(R.id.et_buyer_mobile);
        etOrderSN = findViewById(R.id.et_order_sn);
        etRefundSN = findViewById(R.id.et_refund_sn);
        etGoodsName = findViewById(R.id.et_goods_name);
        etReceiverMobile = findViewById(R.id.et_receiver_mobile);
        etReceiverAddress = findViewById(R.id.et_receiver_address);

        tvBeginDate = findViewById(R.id.tv_begin_date);
        tvEndDate = findViewById(R.id.tv_end_date);
        tvOrderType = findViewById(R.id.tv_order_type);
        tvOrderSource = findViewById(R.id.tv_order_source);
        tvSearchType = findViewById(R.id.tv_search_type);

        //通过设置topMargin，可以让Drawer弹窗进行局部阴影展示
//        ViewGroup.MarginLayoutParams params = (MarginLayoutParams) getPopupContentView().getLayoutParams();
//        params.topMargin = 450;
        if (PopupType.SELLER_REFUND_FILTER == type) {
            findViewById(R.id.ll_buyer_phone_container).setVisibility(View.GONE);
            findViewById(R.id.ll_refund_number).setVisibility(View.VISIBLE);
            findViewById(R.id.ll_receiver_phone).setVisibility(View.GONE);
            findViewById(R.id.ll_receiver_address).setVisibility(View.GONE);
            findViewById(R.id.ll_order_type).setVisibility(View.GONE);
            findViewById(R.id.ll_order_source).setVisibility(View.GONE);
            findViewById(R.id.ll_refund_search_type).setVisibility(View.VISIBLE);
        }
        reset();
    }

    @Override
    protected void onShow() {
        super.onShow();
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
    }


    private void reset() {
        etBuyerNickname.setText("");
        etBuyerMobile.setText("");
        etOrderSN.setText("");
        etRefundSN.setText("");
        etGoodsName.setText("");
        etReceiverMobile.setText("");
        etReceiverAddress.setText("");


        Jarbon now = new Jarbon();

        // 查詢結束日期默認為今天
        Jarbon endDateJarbon = Jarbon.create(now.getYear(), now.getMonth(), now.getDay());
        // 查詢結束日期默認為7天前
        Jarbon beginDateJarbon = new Jarbon(endDateJarbon.getTimestampMillis() - 7L * 86400L * 1000L);

        beginDate = new TwDate(beginDateJarbon.getYear(), beginDateJarbon.getMonth(), beginDateJarbon.getDay());
        endDate = new TwDate(endDateJarbon.getYear(), endDateJarbon.getMonth(), endDateJarbon.getDay());
        tvBeginDate.setText(beginDateJarbon.toDateString());
        tvEndDate.setText(endDateJarbon.toDateString());

        orderTypeSelectedIndex = 0;
        tvOrderType.setText(orderTypeList.get(orderTypeSelectedIndex).title);

        orderSourceSelectedIndex = 0;
        tvOrderSource.setText(orderSourceList.get(orderSourceSelectedIndex).title);

        refundSearchTypeSelectedIndex = 0;
        tvOrderSource.setText(refundSearchTypeList.get(refundSearchTypeSelectedIndex).title);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_select_order_type) {
            simpleCallback.onSimpleCall(null);
            new XPopup.Builder(context)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(context, "訂單類型",
                            PopupType.SELECT_ORDER_TYPE, orderTypeList, orderTypeSelectedIndex, this))
                    .show();
        } else if (id == R.id.btn_select_order_source) {
            simpleCallback.onSimpleCall(null);
            new XPopup.Builder(context)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .autoOpenSoftInput(false)
                    .asCustom(new ListPopup(context, "訂單來源",
                            PopupType.SELECT_ORDER_SOURCE, orderSourceList, orderSourceSelectedIndex, this))
                    .show();
        } else if (id == R.id.btn_select_search_type) {
            new XPopup.Builder(context)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(context, "搜索類型",
                            PopupType.SELLER_REFUND_FILTER, refundSearchTypeList, refundSearchTypeSelectedIndex, this))
                    .show();
        } else if (id == R.id.btn_select_begin_date || id == R.id.btn_select_end_date) {
            new XPopup.Builder(context)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new CalendarPopup(context,
                            id == R.id.btn_select_begin_date ? PopupType.SELECT_BEGIN_DATE : PopupType.SELECT_END_DATE,
                            id == R.id.btn_select_begin_date ? beginDate : endDate, this))
                    .show();
        } else if (id == R.id.btn_reset) {
            reset();
        } else if (id == R.id.btn_ok) {
            if (TwDate.compare(beginDate, endDate) > 0) {
                ToastUtil.error(context, "下單時間【開始日期】不能晚於【結束日期】");
                return;
            }

            SellerOrderFilterParams filterParams = new SellerOrderFilterParams();
            filterParams.buyerNickname = etBuyerNickname.getText().toString().trim();
            filterParams.buyerMobile = etBuyerMobile.getText().toString().trim();
            filterParams.orderSN = etOrderSN.getText().toString().trim();
            filterParams.refundSN = etRefundSN.getText().toString().trim();
            filterParams.goodsName = etGoodsName.getText().toString().trim();
            filterParams.receiverMobile = etReceiverMobile.getText().toString().trim();
            filterParams.receiverAddress = etReceiverAddress.getText().toString().trim();
            filterParams.beginDate = beginDate;
            filterParams.endDate = endDate;
            filterParams.orderType = getOrderTypeDesc();
            filterParams.orderSource = getOrderSourceDesc();
            filterParams.searchType = searchType;

            if (simpleCallback != null) {
                simpleCallback.onSimpleCall(filterParams);
            }

            dismiss();
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.SELECT_ORDER_TYPE) {
            orderTypeSelectedIndex = id;
            tvOrderType.setText(orderTypeList.get(orderTypeSelectedIndex).title);
        } else if (type == PopupType.SELECT_ORDER_SOURCE) {
            orderSourceSelectedIndex = id;
            tvOrderSource.setText(orderSourceList.get(orderSourceSelectedIndex).title);
        }else if (type == PopupType.SELLER_REFUND_FILTER) {
            refundSearchTypeSelectedIndex = id;
            tvSearchType.setText(refundSearchTypeList.get(refundSearchTypeSelectedIndex).title);
            searchType = extra.toString();
        } else if (type == PopupType.SELECT_BEGIN_DATE) {
            beginDate = (TwDate) extra;
            tvBeginDate.setText(beginDate.toString());
        } else if (type == PopupType.SELECT_END_DATE) {
            endDate = (TwDate) extra;
            tvEndDate.setText(endDate.toString());
        }
    }

    private String getOrderTypeDesc() {
        if (orderTypeSelectedIndex == 0) { // 所有
            return "";
        } else if (orderTypeSelectedIndex == 1) { // 零售
            return "retail";
        } else if (orderTypeSelectedIndex == 2) { // 代客下单
            return "proxy";
        }
        return null;
    }

    private String getOrderSourceDesc() {
        if (orderTypeSelectedIndex == 0) { // 所有
            return "";
        } else if (orderTypeSelectedIndex == 1) { // PC
            return "web";
        } else if (orderTypeSelectedIndex == 2) { // 门店
            return "chain";
        }
        return null;
    }
}

