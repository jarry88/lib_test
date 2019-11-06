package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.AppGuideActivity;
import com.ftofs.twant.adapter.NoticeListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.NoticeItem;
import com.ftofs.twant.entity.PostItem;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.XPopupCallback;

import java.io.IOException;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
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
                                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                                        return;
                                    }

                                    noticeItemList.remove(position);
                                    adapter.notifyItemRemoved(position);
                                }
                            });
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    }))
                            .show();
                }
            }
        });
        noticeListAdapter.setEnableLoadMore(true);
        noticeListAdapter.setOnLoadMoreListener(this, rvList);

        // 設置空頁面
        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.no_result_empty_view, null, false);
        // 設置空頁面的提示語
        TextView tvEmptyHint = emptyView.findViewById(R.id.tv_empty_hint);
        tvEmptyHint.setText(R.string.no_data_hint);
        noticeListAdapter.setEmptyView(emptyView);
        rvList.setAdapter(noticeListAdapter);

        loadData(currPage + 1);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
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

                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
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

                    EasyJSONArray memberMessageList = responseObj.getArray("datas.memberMessageList");
                    for (Object object : memberMessageList) {
                        EasyJSONObject message = (EasyJSONObject) object;

                        int messageId = message.getInt("messageId");


                        String tplCode = message.getString("tplCode");
                        String addTime = message.getString("addTime");
                        String imageUrl = message.getString("imageUrl");
                        String content = message.getString("messageContent");

                        String title = getMessageTitle(tplCode);

                        NoticeItem noticeItem = new NoticeItem(Constant.ITEM_TYPE_NORMAL, messageId, title, tplCode, addTime, imageUrl, content);
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
                    SLog.info("Error!%s", e.getMessage());
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
            return "店鋪提醒";
        } else if (tplCode.equals("storeGoodsCommonUpdate")) {
            return "商品更新提醒";
        } else if (tplCode.equals("storeOpen")) {
            return "店鋪開店提醒";
        } else if (tplCode.equals("storeClose")) {
            return "店鋪關店提醒";
        } else if (tplCode.equals("storeGoodsCommonNew")) {
            return "店鋪消息";
        } else if (tplCode.equals("storeSalesPromotion")) {
            return "店鋪促銷提醒";
        }  else if (tplCode.equals("memberDiscountCoupon")) {
            return "店鋪優惠消息";
        } else if (tplCode.equals("memberWantCommentLike") || tplCode.equals("memberWantPostLike")) {
            return "點讚提醒";
        } else if (tplCode.equals("memberFriendsApply") || tplCode.equals("memberAgreeFriendsApply")) {
            return "好友邀請提醒";
        } else if (tplCode.equals("storeAnnouncement")) {
            return "店鋪公告提醒";
        } else if (tplCode.equals("memberStoreWantCommentReply") || tplCode.equals("memberGoodsWantCommentReply")) {
            return "評論提醒";
        } else if (tplCode.equals("memberFollowWantPost")) {
            return "關注提醒 ";
        }


        return "";
    }
}


