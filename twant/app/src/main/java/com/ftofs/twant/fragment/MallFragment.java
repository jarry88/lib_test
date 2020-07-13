package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.GoodsSearchResultAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.GoodsSearchItem;
import com.ftofs.twant.entity.GoodsSearchItemPair;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.MaxHeightRecyclerView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商城Fragment
 * @author zwm
 */
public class MallFragment extends BaseFragment implements View.OnClickListener {
    int walletStatus = Constant.WANT_PAY_WALLET_STATUS_UNKNOWN;

    NestedScrollView containerView;
    int containerViewHeight = -1;


    /**
     * 猜你喜歡列表
     */
    MaxHeightRecyclerView rvGuessList;

    GoodsSearchResultAdapter goodsAdapter;
    List<GoodsSearchItemPair> goodsItemPairList = new ArrayList<>();

    // id: tv_to_be_paid_count, tv_to_be_shipped_count, tv_to_be_received_count, tv_to_be_commented_count;
    TextView tvToBePaidCount, tvToBeShippedCount, tvToBeReceivedCount, tvToBeCommentedCount;

    public static MallFragment newInstance() {
        Bundle args = new Bundle();

        MallFragment fragment = new MallFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mall, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvToBePaidCount = view.findViewById(R.id.tv_to_be_paid_count);
        tvToBeShippedCount = view.findViewById(R.id.tv_to_be_shipped_count);
        tvToBeReceivedCount = view.findViewById(R.id.tv_to_be_received_count);
        tvToBeCommentedCount = view.findViewById(R.id.tv_to_be_commented_count);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_my_bill, this);

        Util.setOnClickListener(view, R.id.btn_to_be_paid, this);
        Util.setOnClickListener(view, R.id.btn_to_be_shipped, this);
        Util.setOnClickListener(view, R.id.btn_to_be_received, this);
        Util.setOnClickListener(view, R.id.btn_to_be_commented, this);
        Util.setOnClickListener(view, R.id.icon_return_or_exchange, this);

        Util.setOnClickListener(view, R.id.btn_my_express, this);
        Util.setOnClickListener(view, R.id.btn_my_store_coupon, this);
        //我的足迹隐藏
//        Util.setOnClickListener(view, R.id.btn_my_footprint, this);
        Util.setOnClickListener(view, R.id.btn_wallet, this);
        Util.setOnClickListener(view, R.id.btn_my_bonus, this);
        Util.setOnClickListener(view, R.id.btn_my_trust_value, this);
        Util.setOnClickListener(view, R.id.btn_more_price, this);
        Util.setOnClickListener(view, R.id.btn_handle_price, this);
        Util.setOnClickListener(view, R.id.btn_help_price, this);


        rvGuessList = view.findViewById(R.id.rv_guess_list);
        rvGuessList.setLayoutManager(new LinearLayoutManager(_mActivity));
        goodsAdapter = new GoodsSearchResultAdapter(_mActivity, goodsItemPairList);
        goodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                GoodsSearchItemPair pair = goodsItemPairList.get(position);

                if (id == R.id.btn_goto_store_left || id == R.id.btn_goto_store_right) {
                    int storeId;
                    if (id == R.id.btn_goto_store_left) {
                        storeId = pair.left.storeId;
                    } else {
                        storeId = pair.right.storeId;
                    }
                    start(ShopMainFragment.newInstance(storeId));
                } else if (id == R.id.cl_container_left || id == R.id.cl_container_right) {
                    int commonId;
                    if (id == R.id.cl_container_left) {
                        commonId = pair.left.commonId;
                    } else {
                        commonId = pair.right.commonId;
                    }
                    start(GoodsDetailFragment.newInstance(commonId, 0));
                }
            }
        });
        rvGuessList.setAdapter(goodsAdapter);

        rvGuessList.setNestedScrollingEnabled(false);

        containerView = view.findViewById(R.id.container_view);
        containerView.setOnScrollChangeListener((View.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            int rvListY = Util.getYOnScreen(rvGuessList);
            int containerViewY = Util.getYOnScreen(containerView);

            if (rvListY <= containerViewY) {
                rvGuessList.setNestedScrollingEnabled(true);
            } else {
                rvGuessList.setNestedScrollingEnabled(false);
            }
        });

        loadGuessData();
        loadOrderCountData();

        checkWalletStatus(false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_my_bill:
                Util.startFragment(OrderFragment.newInstance(Constant.ORDER_STATUS_ALL, OrderFragment.USAGE_LIST));
                break;

            case R.id.btn_to_be_paid:
            case R.id.btn_to_be_shipped:
            case R.id.btn_to_be_received:
            case R.id.btn_to_be_commented:
                int orderStatus;
                if (id == R.id.btn_to_be_paid) {
                    orderStatus = Constant.ORDER_STATUS_TO_BE_PAID;
                } else if (id == R.id.btn_to_be_shipped) {
                    orderStatus = Constant.ORDER_STATUS_TO_BE_SHIPPED;
                } else if (id == R.id.btn_to_be_received) {
                    orderStatus = Constant.ORDER_STATUS_TO_BE_RECEIVED;
                } else {
                    orderStatus = Constant.ORDER_STATUS_TO_BE_COMMENTED;
                }
                Util.startFragment(OrderFragment.newInstance(orderStatus, OrderFragment.USAGE_LIST));
                break;

            case R.id.icon_return_or_exchange:
                Util.startFragment(RefundFragment.newInstance());
                break;

            case R.id.btn_my_express:
                Util.startFragment(SendPackageFragment.newInstance());
                break;

            case R.id.btn_my_store_coupon:
                Util.startFragment(CouponFragment.newInstance());
                break;

//            case R.id.btn_my_footprint:
//                Util.startFragment(FootprintFragment.newInstance());
//                break;

            case R.id.btn_wallet:
                startWantPayWallet();
                break;

            case R.id.btn_my_bonus:
                Util.startFragment(TrustValueFragment.newInstance(TrustValueFragment.DATA_TYPE_BONUS));
                break;

            case R.id.btn_my_trust_value:
                Util.startFragment(TrustValueFragment.newInstance(TrustValueFragment.DATA_TYPE_TRUST_VALUE));
                break;
            case R.id.btn_more_price:
                Util.startFragment(BargainListFragment.newInstance());
                break;
            case R.id.btn_handle_price:
            case R.id.btn_help_price:
                int dataType;
                if (id == R.id.btn_handle_price) {
                    dataType = MyBargainListFragment.DATA_TYPE_INITIATE;
                } else {
                    dataType = MyBargainListFragment.DATA_TYPE_PARTICIPATE;
                }
                Util.startFragment(MyBargainListFragment.newInstance(dataType));
                break;
            default:
                break;
        }
    }

    private void startWantPayWallet() {
        if (walletStatus == Constant.WANT_PAY_WALLET_STATUS_ACTIVATED) {
            start(WalletFragment.newInstance());
        } else if (walletStatus == Constant.WANT_PAY_WALLET_STATUS_NOT_ACTIVATED) { // 如果錢包未激活，則重定向到激活頁面
            start(WalletFragment.newInstance());
//            start(ResetPasswordFragment.newInstance(Constant.USAGE_SET_PAYMENT_PASSWORD, false));
        } else if (walletStatus == Constant.WANT_PAY_WALLET_STATUS_UNKNOWN) {
            checkWalletStatus(true);
        }
    }

    /**
     * 檢查想付錢包的狀態
     * @param startFragment 是否根據結果啟動想付錢包頁面或激活頁面
     */
    private void checkWalletStatus(boolean startFragment) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token);
        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_WALLET_INFO, params, new UICallback() {
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

                    EasyJSONObject wantWallet = responseObj.getObject("datas.wantWallet");
                    if (Util.isJsonNull(wantWallet)) { // 如果為null，表示未激活
                        walletStatus = Constant.WANT_PAY_WALLET_STATUS_NOT_ACTIVATED;
                        if (startFragment) {
                            // 如果未激活，啟動激活頁面
                            start(ResetPasswordFragment.newInstance(Constant.USAGE_SET_PAYMENT_PASSWORD, false));
                        }
                    } else {
                        walletStatus = Constant.WANT_PAY_WALLET_STATUS_ACTIVATED;
                        if (startFragment) {
                            start(WalletFragment.newInstance());
                        }
                    }
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void loadGuessData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            SLog.info("Error!token 為空");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token);

        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_GUESS_YOUR_LIKE, params, new UICallback() {
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
                    EasyJSONArray goodsLiteVoList = responseObj.getSafeArray("datas.goodsLiteVoList");
                    GoodsSearchItemPair pair = null;
                    for (Object object : goodsLiteVoList) {
                        EasyJSONObject goodsLiteVo = (EasyJSONObject) object;

                        /*
                        String imageSrc, String storeAvatarUrl, int storeId, String storeName, int commonId,
                           String goodsName, String jingle, float price, String nationalFlag
                         */

                        GoodsSearchItem item = new GoodsSearchItem(
                                goodsLiteVo.getSafeString("imageName"),
                                goodsLiteVo.getSafeString("storeAvatarUrl"),
                                goodsLiteVo.getInt("storeId"),
                                goodsLiteVo.getSafeString("storeName"),
                                goodsLiteVo.getInt("commonId"),
                                goodsLiteVo.getSafeString("goodsName"),
                                goodsLiteVo.getSafeString("jingle"),
                                Util.getSpuPrice(goodsLiteVo),
                                goodsLiteVo.getSafeString("adminCountry.nationalFlag"));

                        if (pair == null) {
                            pair = new GoodsSearchItemPair(Constant.ITEM_TYPE_NORMAL);
                            pair.left = item;
                        } else {
                            pair.right = item;
                            goodsItemPairList.add(pair);
                            pair = null;
                        }
                    }
                    if (pair != null) { // 如果是奇数项， 并且是最后一项，则满足这个条件
                        goodsItemPairList.add(pair);
                        pair = null;
                    }

                    goodsAdapter.setNewData(goodsItemPairList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void loadOrderCountData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            SLog.info("Error!token 為空");
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate("token", token);

        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_ORDER_COUNT, params, new UICallback() {
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

                /*
                new: 待付款
                pay: 待發貨
                finish: 已完成
                send: 待收貨
                noeval: 待評論
                 */
                try {
                    int newCount = responseObj.getInt("datas.new");
                    int payCount = responseObj.getInt("datas.pay");
                    int sendCount = responseObj.getInt("datas.send");
                    int noevalCount = responseObj.getInt("datas.noeval");

                    if (newCount > 0) {
                        tvToBePaidCount.setText(String.valueOf(newCount));
                        tvToBePaidCount.setVisibility(View.VISIBLE);
                    } else {
                        tvToBePaidCount.setVisibility(View.GONE);
                    }

                    if (payCount > 0) {
                        tvToBeShippedCount.setText(String.valueOf(payCount));
                        tvToBeShippedCount.setVisibility(View.VISIBLE);
                    } else {
                        tvToBeShippedCount.setVisibility(View.GONE);
                    }

                    if (sendCount > 0) {
                        tvToBeReceivedCount.setText(String.valueOf(sendCount));
                        tvToBeReceivedCount.setVisibility(View.VISIBLE);
                    } else {
                        tvToBeReceivedCount.setVisibility(View.GONE);
                    }

                    if (noevalCount > 0) {
                        tvToBeCommentedCount.setText(String.valueOf(noevalCount));
                        tvToBeCommentedCount.setVisibility(View.VISIBLE);
                    } else {
                        tvToBeCommentedCount.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

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

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        if (containerViewHeight == -1) {
            containerViewHeight = containerView.getHeight();
            SLog.info("containerViewHeight[%d]", containerViewHeight);
            rvGuessList.setMaxHeight(containerViewHeight);
        }
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }
}
