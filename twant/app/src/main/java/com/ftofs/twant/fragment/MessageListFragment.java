package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.NoticeListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.NoticeItem;
import com.ftofs.twant.entity.UnreadCount;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ReadMessagePopup;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 消息列表Fragment
 * @author zwm
 */
public class MessageListFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.RequestLoadMoreListener {
    int tplClass;
    int currPage = 0;

    boolean hasMore;

    TextView tvFragmentTitle;
    List<NoticeItem> noticeItemList = new ArrayList<>();
    NoticeListAdapter noticeListAdapter;

    public static MessageListFragment newInstance(int tplClass) {
        Bundle args = new Bundle();

        args.putInt("tplClass", tplClass);
        MessageListFragment fragment = new MessageListFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_list, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        tplClass = args.getInt("tplClass", tplClass);

        String fragmentTitle = "";
        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        if (tplClass == Constant.TPL_CLASS_TRANSACT) {
            fragmentTitle = "交易提醒";
        } else if (tplClass == Constant.TPL_CLASS_ASSET) {
            fragmentTitle = "資產提醒";
        } else if (tplClass == Constant.TPL_CLASS_SOCIAL) {
            fragmentTitle = "社交提醒";
        } else if (tplClass == Constant.TPL_CLASS_BARGAIN) {
            fragmentTitle = "促銷提醒";
        } else if (tplClass == Constant.TPL_CLASS_NOTICE) {
            fragmentTitle = "通知提醒";
        }
        tvFragmentTitle.setText(fragmentTitle);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_clear_all, this);

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        noticeListAdapter = new NoticeListAdapter(noticeItemList);

        noticeListAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                NoticeItem noticeItem = noticeItemList.get(position);

                if (id == R.id.btn_delete_message_item) {
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
                            }).asCustom(new TwConfirmPopup(_mActivity, "確定要刪除嗎?", null, new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("onYes");

                            String token = User.getToken();

                            EasyJSONObject params = EasyJSONObject.generate(
                                    "token", token,
                                    "messageId", noticeItem.id);

                            SLog.info("params[%s]", params);

                            Api.postUI(Api.PATH_DELETE_MESSAGE, params, new UICallback() {
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

                                    noticeItemList.remove(position);
                                    adapter.notifyItemRemoved(position);

                                    try {
                                        UnreadCount unreadCount = UnreadCount.processUnreadList(responseObj.getSafeArray("datas.unreadList"));
                                        if (unreadCount != null) {
                                            UnreadCount.save(unreadCount);
                                        }
                                    } catch (Exception e) {
                                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                                    }
                                }
                            });
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    }))
                            .show();
                } else if (id == R.id.ll_swipe_content) {
                    SLog.info("HERE");
                    String token = User.getToken();

                    EasyJSONObject params = EasyJSONObject.generate(
                            "token", token,
                            "messageId", noticeItem.id);

                    Api.postUI(Api.PATH_MARK_ALL_MESSAGE_READ, params, new UICallback() {
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

                                noticeItem.isRead = true;
                                adapter.notifyItemChanged(position);

                                try {
                                    UnreadCount unreadCount = UnreadCount.processUnreadList(responseObj.getSafeArray("datas.unreadList"));
                                    if (unreadCount != null) {
                                        UnreadCount.save(unreadCount);
                                    }
                                } catch (Exception e) {

                                }
                            } catch (Exception e) {

                            }
                        }
                    });


                    SLog.info("tplCode[%s]", noticeItem.tplCode);
                    if (StringUtil.equalsOne(noticeItem.tplCode, new String[] {
                            "memberOrdersBookFinalPaymentNotice", "memberOrdersCancel", "memberVirtualOrdersCancel",
                            "memberOrdersPay", "memberVirtualOrdersPay", "memberOrdersSend", "memberOrdersModifyFreight",
                            "storeOrdersReceive", "memberOrdersReceive", "memberOrdersEvaluateExplain"
                    })) {
                        int ordersId = Integer.valueOf(noticeItem.sn);
                        SLog.info("ordersId[%d]", ordersId);
                        start(OrderDetailFragment.newInstance(ordersId));
                    } else if (StringUtil.equalsOne(noticeItem.tplCode, new String[] {
                            "memberReturnAutoCancelNotice", "memberReturnUpdate"
                    })) {

                        /*
                        Util.startFragment(RefundDetailFragment.newInstance(refundItem.refundId, EasyJSONObject.generate(
                                "action", action,
                                "goodsFullSpecs", refundItem.goodsFullSpecs,
                                "goodsPrice", refundItem.goodsPrice,
                                "buyNum", refundItem.buyNum).toString()));
                                */
                    } else if (StringUtil.equalsOne(noticeItem.tplCode, new String[] {
                            "storeGoodsCommonUpdate", "storeAnnouncement", "storeHr", "storeInfoUpdate",
                            "storeOpen", "storeClose", "storeSalesPromotion"
                    })) {
                        int storeId = Integer.valueOf(noticeItem.sn);
                        SLog.info("storeId[%d]", storeId);
                        start(ShopMainFragment.newInstance(storeId));
                    } else if (StringUtil.equalsOne(noticeItem.tplCode, new String[] {
                            "memberFriendsApply", "memberAgreeFriendsApply"
                    })) {
                        Object iconRes = Util.tplCodeToResId(noticeItem.tplCode);
                        if (iconRes == null && !StringUtil.isEmpty(noticeItem.imageUrl)) {
                            iconRes = noticeItem.imageUrl;
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
                                }).asCustom(new ReadMessagePopup(_mActivity, noticeItem.title, noticeItem.createTime, iconRes, noticeItem.content))
                                .show();
                    } else if (StringUtil.equalsOne(noticeItem.tplCode, new String[] {
                            "adminReply"
                    })) {
                        Util.startFragment(MyFeedbackFragment.newInstance());
                    }
                }
            }
        });


        noticeListAdapter.setEnableLoadMore(true);
        noticeListAdapter.setOnLoadMoreListener(this, rvList);

        noticeListAdapter.setEmptyView(R.layout.layout_placeholder_no_app_message, rvList);
        rvList.setAdapter(noticeListAdapter);

        loadData(currPage + 1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_clear_all) {
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
                    }).asCustom(new TwConfirmPopup(_mActivity, "是否清除所有未讀數？", null, new OnConfirmCallback() {
                @Override
                public void onYes() {
                    SLog.info("onYes");
                    clearAllUnreadStatus();
                }

                @Override
                public void onNo() {
                    SLog.info("onNo");
                }
            })).show();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }


    private void clearAllUnreadStatus() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "tplClass", tplClass);

        SLog.info("params[%s]", params);

        Api.postUI(Api.PATH_MARK_ALL_MESSAGE_READ, params, new UICallback() {
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

                    for (NoticeItem noticeItem : noticeItemList) {
                        noticeItem.isRead = true;
                    }

                    ToastUtil.error(_mActivity, "清除未讀成功");

                    UnreadCount unreadCount = UnreadCount.processUnreadList(responseObj.getSafeArray("datas.unreadList"));
                    if (unreadCount != null) {
                        UnreadCount.save(unreadCount);
                    }

                    noticeListAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private void loadData(int page) {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "page", page,
                "tplClass", tplClass,
                "token", token);

        SLog.info("params[%s]", params);

        Api.getUI(Api.PATH_MESSAGE_LIST, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
                noticeListAdapter.loadMoreFail();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        noticeListAdapter.loadMoreFail();
                        return;
                    }

                    hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                    SLog.info("hasMore[%s]", hasMore);
                    if (!hasMore) {
                        noticeListAdapter.loadMoreEnd();
                        noticeListAdapter.setEnableLoadMore(false);
                    }

                    EasyJSONArray memberMessageList = responseObj.getSafeArray("datas.memberMessageList");
                    for (Object object : memberMessageList) {
                        EasyJSONObject message = (EasyJSONObject) object;

                        int messageId = message.getInt("messageId");


                        String tplCode = message.getSafeString("tplCode");
                        String addTime = message.getSafeString("addTime");
                        String imageUrl = message.getSafeString("imageUrl");
                        String content = message.getSafeString("messageContent");
                        int isRead = message.getInt("isRead");

                        String title = getMessageTitle(tplCode);

                        NoticeItem noticeItem = new NoticeItem(Constant.ITEM_TYPE_NORMAL, messageId, title, tplCode, addTime, imageUrl, content, isRead == 1);
                        if (message.exists("sn")) {
                            noticeItem.sn = message.getSafeString("sn");
                        }

                        noticeItemList.add(noticeItem);
                    }

                    if (!hasMore && noticeItemList.size() > 1) {
                        // 如果全部加載完畢，添加加載完畢的提示
                        NoticeItem item = new NoticeItem(Constant.ITEM_TYPE_LOAD_END_HINT);
                        noticeItemList.add(item);
                    }

                    noticeListAdapter.setNewData(noticeItemList);
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 上滑加載更多
     */
    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            noticeListAdapter.setEnableLoadMore(false);
            return;
        }
        loadData(currPage + 1);
    }

    private String getMessageTitle(String tplCode) {
        if (tplCode == null) {
            return null;
        }

        if (tplCode.equals("memberReturnAutoCancelNotice") || tplCode.equals("memberReturnUpdate")) {
            return "退貨提醒";
        } else if (tplCode.equals("memberOrdersCancel") || tplCode.equals("memberVirtualOrdersCancel")) {
            return "訂單取消提醒";
        } else if (tplCode.equals("memberOrdersPay") || tplCode.equals("memberVirtualOrdersPay")) {
            return "訂單支付提醒";
        } else if (tplCode.equals("memberOrdersSend")) {
            return "訂單發貨提醒";
        } else if (tplCode.equals("memberOrdersReceive")) {
            return "訂單收貨提醒";
        } else if (tplCode.equals("storeHr") || tplCode.equals("storeInfoUpdate")) {
            return "商店提醒";
        } else if (tplCode.equals("storeGoodsCommonUpdate")) {
            return "產品更新提醒";
        } else if (tplCode.equals("storeOpen")) {
            return "商店開店提醒";
        } else if (tplCode.equals("storeClose")) {
            return "商店關店提醒";
        } else if (tplCode.equals("storeGoodsCommonNew")) {
            return "商店消息";
        } else if (tplCode.equals("storeSalesPromotion")) {
            return "商店促銷提醒";
        }  else if (tplCode.equals("memberDiscountCoupon")) {
            return "商店優惠消息";
        } else if (tplCode.equals("memberWantCommentLike") || tplCode.equals("memberWantPostLike")) {
            return "讚想提醒";
        } else if (tplCode.equals("memberFriendsApply") || tplCode.equals("memberAgreeFriendsApply")) {
            return "好友邀請提醒";
        } else if (tplCode.equals("storeAnnouncement")) {
            return "商店公告提醒";
        } else if (tplCode.equals("memberStoreWantCommentReply") || tplCode.equals("memberGoodsWantCommentReply")) {
            return "說說提醒";
        } else if (tplCode.equals("memberFollowWantPost")) {
            return "關注提醒 ";
        }


        return "";
    }
}


