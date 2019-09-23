package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;

import org.w3c.dom.Text;

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
    ScrollView svContainer;

    /**
     * 猜你喜歡列表
     */
    RecyclerView rvGuessList;

    GoodsSearchResultAdapter goodsAdapter;
    List<GoodsSearchItem> goodsItemList = new ArrayList<>();

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
        Util.setOnClickListener(view, R.id.btn_my_footprint, this);
        Util.setOnClickListener(view, R.id.btn_my_address, this);
        Util.setOnClickListener(view, R.id.btn_my_bonus, this);
        Util.setOnClickListener(view, R.id.btn_my_trust_value, this);

        svContainer = view.findViewById(R.id.sv_container);
        rvGuessList = view.findViewById(R.id.rv_guess_list);
        svContainer.post(new Runnable() {
            @Override
            public void run() {
                int height = svContainer.getHeight();
                ViewGroup.LayoutParams layoutParams = rvGuessList.getLayoutParams();
                layoutParams.height = height;
                rvGuessList.setLayoutParams(layoutParams);
            }
        });

        GridLayoutManager layoutManager = new GridLayoutManager(_mActivity, 2, GridLayoutManager.VERTICAL, false);
        rvGuessList.setLayoutManager(layoutManager);
        goodsAdapter = new GoodsSearchResultAdapter(_mActivity, R.layout.goods_search_item, goodsItemList);
        goodsAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_goto_store) {
                    GoodsSearchItem goodsSearchItem = goodsItemList.get(position);
                    start(ShopMainFragment.newInstance(goodsSearchItem.storeId));
                }
            }
        });
        goodsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                GoodsSearchItem goodsSearchItem = goodsItemList.get(position);
                start(GoodsDetailFragment.newInstance(goodsSearchItem.commonId, 0));
            }
        });
        rvGuessList.setAdapter(goodsAdapter);

        loadGuessData();
        loadOrderCountData();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_my_bill:
                Util.startFragment(OrderFragment.newInstance(Constant.ORDER_STATUS_ALL));
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
                Util.startFragment(OrderFragment.newInstance(orderStatus));
                break;

            case R.id.icon_return_or_exchange:
                Util.startFragment(RefundFragment.newInstance());
                break;

            case R.id.btn_my_express:
                Util.startFragment(SendPackageFragment.newInstance());
                break;

            case R.id.btn_my_store_coupon:
                Util.startFragment(StoreCouponFragment.newInstance());
                break;

            case R.id.btn_my_footprint:
                Util.startFragment(FootprintFragment.newInstance());
                break;

            case R.id.btn_my_address:
                Util.startFragment(AddrManageFragment.newInstance());
                break;

            case R.id.btn_my_bonus:
                Util.startFragment(TrustValueFragment.newInstance(TrustValueFragment.DATA_TYPE_BONUS));
                break;

            case R.id.btn_my_trust_value:
                Util.startFragment(TrustValueFragment.newInstance(TrustValueFragment.DATA_TYPE_TRUST_VALUE));
                break;
            default:
                break;
        }
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

                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONArray goodsLiteVoList = responseObj.getArray("datas.goodsLiteVoList");
                    for (Object object : goodsLiteVoList) {
                        EasyJSONObject goodsLiteVo = (EasyJSONObject) object;

                        /*
                        String imageSrc, String storeAvatarUrl, int storeId, String storeName, int commonId,
                           String goodsName, String jingle, float price, String nationalFlag
                         */

                        GoodsSearchItem item = new GoodsSearchItem(
                                goodsLiteVo.getString("imageName"),
                                goodsLiteVo.getString("storeAvatarUrl"),
                                goodsLiteVo.getInt("storeId"),
                                goodsLiteVo.getString("storeName"),
                                goodsLiteVo.getInt("commonId"),
                                goodsLiteVo.getString("goodsName"),
                                goodsLiteVo.getString("jingle"),
                                Util.getSpuPrice(goodsLiteVo),
                                goodsLiteVo.getString("adminCountry.nationalFlag"));

                        goodsItemList.add(item);

                        goodsAdapter.setNewData(goodsItemList);
                    }
                } catch (EasyJSONException e) {
                    e.printStackTrace();
                    SLog.info("Error!%s", e.getMessage());
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

                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
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
        pop();
        return true;
    }
}
