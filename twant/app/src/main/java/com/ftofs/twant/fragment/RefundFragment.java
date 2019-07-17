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
import com.ftofs.twant.constant.Constant;
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
import com.lxj.xpopup.core.BasePopupView;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 退款、退貨、投訴列表Fragment
 * @author zwm
 */
public class RefundFragment extends BaseFragment implements View.OnClickListener {
    RecyclerView rvRefundList;

    int action = Constant.ACTION_REFUND;

    boolean refundDataLoaded = false;
    boolean returnDataLoaded = false;
    boolean complainDataLoaded = false;

    List<RefundItem> refundItemList = new ArrayList<>();
    List<RefundItem> returnItemList = new ArrayList<>();
    List<RefundItem> complainItemList = new ArrayList<>();

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
                RefundItem refundItem;
                if (action == Constant.ACTION_REFUND) {
                    refundItem = refundItemList.get(position);
                } else if (action == Constant.ACTION_RETURN) {
                    refundItem = returnItemList.get(position);
                } else {
                    refundItem = complainItemList.get(position);
                }


                int id = view.getId();
                if (id == R.id.btn_cancel_refund) {
                    String title;
                    if (action == Constant.ACTION_REFUND) {
                        title = "確認取消退款嗎?";
                    } else if (action == Constant.ACTION_RETURN) {
                        title = "確認取消退貨嗎?";
                    } else {
                        title = "確認取消投訴嗎?";
                    }

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
                            }).asCustom(new TwConfirmPopup(_mActivity, title, null, new OnConfirmCallback() {
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
                            "action", action,
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

                if (id == R.id.btn_refund) {
                    action = Constant.ACTION_REFUND;
                } else if (id == R.id.btn_return) {
                    action = Constant.ACTION_RETURN;
                } else {
                    action = Constant.ACTION_COMPLAIN;
                }
                loadRefundList();
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
        if (action == Constant.ACTION_REFUND && refundDataLoaded) {
            refundListAdapter.setNewData(refundItemList);
            return;
        } else if (action == Constant.ACTION_RETURN && returnDataLoaded) {
            refundListAdapter.setNewData(returnItemList);
            return;
        } else if (action == Constant.ACTION_COMPLAIN && complainDataLoaded) {
            refundListAdapter.setNewData(complainItemList);
            return;
        }

        try {
            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                SLog.info("Error!token 為空");
                return;
            }

            EasyJSONObject params = EasyJSONObject.generate("token", token);

            SLog.info("params[%s]", params);
            String path;

            if (action == Constant.ACTION_REFUND) {
                path = Api.PATH_REFUND_LIST;
            } else if (action == Constant.ACTION_RETURN) {
                path = Api.PATH_RETURN_LIST;
            } else {
                path = Api.PATH_COMPLAIN_LIST;
            }


            final BasePopupView loadingPopup = new XPopup.Builder(_mActivity)
                    .asLoading(getString(R.string.text_loading))
                    .show();
            Api.postUI(path, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity, e);
                    loadingPopup.dismiss();
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    SLog.info("responseStr[%s]", responseStr);
                    loadingPopup.dismiss();

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    try {
                        EasyJSONArray refundItemVoList;

                        if (action == Constant.ACTION_REFUND || action == Constant.ACTION_RETURN) {
                            refundItemVoList = responseObj.getArray("datas.refundItemVoList");
                        } else {
                            refundItemVoList = responseObj.getArray("datas.complainList");
                        }

                        for (Object object : refundItemVoList) {
                            EasyJSONObject item = (EasyJSONObject) object;
                            RefundItem refundItem = new RefundItem();
                            refundItem.action = action;
                            if (action == Constant.ACTION_REFUND || action == Constant.ACTION_RETURN) {
                                refundItem.storeName = item.getString("storeName");
                                refundItem.refundId = item.getInt("refundId");
                                refundItem.goodsName = item.getString("ordersGoodsVoList[0].goodsName");
                                refundItem.goodsFullSpecs = item.getString("ordersGoodsVoList[0].goodsFullSpecs");
                                refundItem.goodsPrice = (float) item.getDouble("ordersGoodsVoList[0].goodsPrice");
                                refundItem.buyNum = item.getInt("ordersGoodsVoList[0].buyNum");
                                refundItem.goodsPayAmount = (float) item.getDouble("ordersGoodsVoList[0].goodsPayAmount");

                                refundItem.orderStatus = item.getString("currentStateText");
                                refundItem.addTime = item.getString("addTime");
                                refundItem.enableMemberCancel = item.getInt("enableMemberCancel");
                            } else {
                                refundItem.storeName = item.getString("accusedName");
                                refundItem.refundId = item.getInt("complainId");
                                refundItem.goodsName = item.getString("goodsName");
                                refundItem.goodsFullSpecs = item.getString("goodsFullSpecs");
                                refundItem.orderStatus = item.getString("complainStateName");
                                refundItem.enableMemberCancel = item.getInt("showMemberClose");
                            }

                            refundItem.goodsImage = item.getString("goodsImage");

                            SLog.info("enableMemberCancel[%d]", refundItem.enableMemberCancel);

                            if (action == Constant.ACTION_REFUND) {
                                refundItemList.add(refundItem);
                            } else if (action == Constant.ACTION_RETURN) {
                                returnItemList.add(refundItem);
                            } else {
                                complainItemList.add(refundItem);
                            }
                        }

                        if (action == Constant.ACTION_REFUND) {
                            refundDataLoaded = true;

                            SLog.info("refundItemList[%d]", refundItemList.size());
                            refundListAdapter.setNewData(refundItemList);
                        } else if (action == Constant.ACTION_RETURN) {
                            returnDataLoaded = true;

                            SLog.info("returnItemList[%d]", returnItemList.size());
                            refundListAdapter.setNewData(returnItemList);
                        } else {
                            complainDataLoaded = true;

                            SLog.info("complainItemList[%d]", complainItemList.size());
                            refundListAdapter.setNewData(complainItemList);
                        }
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

            int refundId = 0;
            String path = "";

            if (action == Constant.ACTION_REFUND) {
                refundId = refundItemList.get(position).refundId;
                path = Api.PATH_CANCEL_REFUND;
            } else if (action == Constant.ACTION_RETURN) {
                refundId = returnItemList.get(position).refundId;
                path = Api.PATH_CANCEL_RETURN;
            } else {
                refundId = complainItemList.get(position).refundId;
                path = Api.PATH_CANCEL_COMPLAIN;
            }

            EasyJSONObject params;
            if (action == Constant.ACTION_REFUND || action == Constant.ACTION_RETURN) {
                params = EasyJSONObject.generate(
                        "token", token,
                        "refundId", refundId);
            } else {
                params = EasyJSONObject.generate(
                        "token", token,
                        "complainId", refundId);
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

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    String msg;

                    if (action == Constant.ACTION_REFUND) {
                        msg = "取消退款成功";
                    } else if (action == Constant.ACTION_RETURN) {
                        msg = "取消退貨成功";
                    } else {
                        msg = "取消投訴成功";
                    }
                    ToastUtil.success(_mActivity, msg);
                }
            });
        } catch (Exception e) {
            SLog.info("Error!%s", e.getMessage());
        }
    }
}
