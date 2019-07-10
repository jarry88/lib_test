package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.RefundListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.RefundItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class RefundFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvRefundList;

    List<RefundItem> refundItemList = new ArrayList<>();
    RefundListAdapter refundListAdapter;

    public static RefundFragment newInstance() {
        Bundle args = new Bundle();
        RefundFragment fragment = new RefundFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_refund, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Util.setOnClickListener(view, R.id.btn_back, this);

        rvRefundList = view.findViewById(R.id.rv_refund_list);
        refundListAdapter = new RefundListAdapter(R.layout.refund_list_item, refundItemList);
        refundListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, final int position) {
                RefundItem refundItem = refundItemList.get(position);

                int id = view.getId();
                if (id == R.id.btn_cancel_refund) {
                    new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                            // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                            .setPopupCallback(new XPopupCallback() {
                                @Override
                                public void onShow() {
                                }
                                @Override
                                public void onDismiss() {
                                }
                            }).asCustom(new TwConfirmPopup(_mActivity, "確認取消退款嗎?", null, new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("onYes");
                            cancelRefund(position);
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    }))
                            .show();
                } else if (id == R.id.btn_view_refund_info) {
                    MainFragment mainFragment = MainFragment.getInstance();
                    mainFragment.start(RefundDetailFragment.newInstance(refundItem.refundId, EasyJSONObject.generate(
                            "goodsFullSpecs", refundItem.goodsFullSpecs,
                            "goodsPrice", refundItem.goodsPrice,
                            "buyNum", refundItem.buyNum).toString()));
                }
            }
        });

        LinearLayoutManager layoutManager = new LinearLayoutManager(_mActivity);
        rvRefundList.setLayoutManager(layoutManager);
        rvRefundList.setAdapter(refundListAdapter);

        SimpleTabManager simpleTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                if (isRepeat) {
                    SLog.info("重復點擊");
                    return;
                }
            }
        };
        simpleTabManager.add(view.findViewById(R.id.btn_refund));
        simpleTabManager.add(view.findViewById(R.id.btn_return));
        simpleTabManager.add(view.findViewById(R.id.btn_complain));

        loadRefundList();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_back) {
            pop();
        }
    }

    private void loadRefundList() {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                SLog.info("Error!token 為空");
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("token", token);

            SLog.info("params[%s]", params);

            Api.postUI(Api.PATH_REFUND_LIST, params, new UICallback() {
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
                        EasyJSONArray refundItemVoList = responseObj.getArray("datas.refundItemVoList");
                        for (Object object : refundItemVoList) {
                            EasyJSONObject item = (EasyJSONObject) object;
                            RefundItem refundItem = new RefundItem();
                            refundItem.refundId = item.getInt("refundId");
                            refundItem.storeName = item.getString("storeName");
                            refundItem.orderStatus = item.getString("currentStateText");
                            refundItem.goodsImage = item.getString("goodsImage");
                            refundItem.goodsName = item.getString("ordersGoodsVoList[0].goodsName");
                            refundItem.goodsFullSpecs = item.getString("ordersGoodsVoList[0].goodsFullSpecs");
                            refundItem.goodsPrice = (float) item.getDouble("ordersGoodsVoList[0].goodsPrice");
                            refundItem.buyNum = item.getInt("ordersGoodsVoList[0].buyNum");
                            refundItem.addTime = item.getString("addTime");
                            refundItem.goodsPayAmount = (float) item.getDouble("ordersGoodsVoList[0].goodsPayAmount");
                            refundItem.enableMemberCancel = item.getInt("enableMemberCancel");

                            refundItemList.add(refundItem);
                        }

                        SLog.info("refundItemList[%d]", refundItemList.size());
                        refundListAdapter.setNewData(refundItemList);
                    } catch (EasyJSONException e) {
                        e.printStackTrace();
                        SLog.info("Error!loadRefundList failed");
                    }
                }
            });
        } catch (Exception e) {

        }
    }

    /**
     * 取消退款
     * @param position
     */
    private void cancelRefund(int position) {
        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                SLog.info("Error!token 為空");
                return;
            }

            int refundId = refundItemList.get(position).refundId;
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "refundId", refundId);

            SLog.info("params[%s]", params);

            Api.postUI(Api.PATH_CANCEL_REFUND, params, new UICallback() {
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

                    ToastUtil.show(_mActivity, "取消退款成功");
                }
            });
        } catch (Exception e) {

        }
    }
}
