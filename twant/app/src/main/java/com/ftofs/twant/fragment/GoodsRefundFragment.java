package com.ftofs.twant.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.RefundGoodsListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.gzp.lib_common.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.lib_common_ui.entity.ListPopupItem;
import com.ftofs.twant.entity.RefundGoodsItem;
import com.gzp.lib_common.base.callback.OnSelectedListener;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.gzp.lib_common.task.TaskObservable;
import com.gzp.lib_common.task.TaskObserver;
import com.gzp.lib_common.utils.FileUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AdjustButton;
import com.ftofs.lib_common_ui.popup.ListPopup;
import com.ftofs.twant.widget.RefundWayPopup;
import com.ftofs.twant.widget.SquareGridLayout;
import com.lxj.xpopup.XPopup;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 產品退款、退貨、投訴Fragment
 * @author zwm
 */
public class GoodsRefundFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    int action;
    int ordersId;
    int ordersGoodsId;

    LinearLayout llGoodsOuterContainer;
    LinearLayout llGoodsListContainer;

    LinearLayout llWidgetContainer;

    EasyJSONObject paramsInObj;

    SquareGridLayout sglImageContainer;

    TextView tvFragmentTitle;
    TextView tvStoreName;
    ImageView goodsImage;
    TextView tvGoodsName;
    TextView tvGoodsFullSpecs;
    TextView tvPrice;
    TextView tvGoodsNum;

    TextView tvRefundReason;
    TextView tvMaxRefundAmount;
    float maxRefundAmount;
    float refundAmount;
    // 最多可退貨數量
    int maxReturnCount;
    EditText etRefundAmount;
    EditText etRefundDesc;

    ImageView btnAddImage;

    AdjustButton abReturnCount;

    // 退货原因列表
    List<ListPopupItem> reasonItemList = new ArrayList<>();

    LinearLayout btnSelectRefundReason;
    int reasonIndex = -1;

    TextView tvRefundWay;

    /**
     * 當前選中的退款方式
     */
    int refundWay = RefundWayPopup.REFUND_WAY_UNKNOWN;

    RefundGoodsListAdapter adapter;

    public static GoodsRefundFragment newInstance(String paramsIn) {
        Bundle args = new Bundle();

        args.putString("paramsIn", paramsIn);
        SLog.info("paramsIn[%s]", paramsIn);

        GoodsRefundFragment fragment = new GoodsRefundFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_refund, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String paramsIn = args.getString("paramsIn");
        SLog.info("paramsIn[%s]", paramsIn);

        paramsInObj = EasyJSONObject.parse(paramsIn);
        try {
            action = paramsInObj.getInt("action");
            ordersId = paramsInObj.getInt("ordersId");
            ordersGoodsId = paramsInObj.getInt("ordersGoodsId");
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        // 讀取上一次記錄的退款方式
        int userId = User.getUserId();
        if (userId != 0) {
            String refundWayKey = String.format(SPField.FIELD_USER_REFUND_WAY, userId);
            refundWay = Hawk.get(refundWayKey, RefundWayPopup.REFUND_WAY_UNKNOWN);
        }

        llGoodsOuterContainer = view.findViewById(R.id.ll_goods_outer_container);
        llWidgetContainer = view.findViewById(R.id.ll_widget_container);
        if (action == Constant.ACTION_REFUND || action == Constant.ACTION_REFUND_ALL) {
            View vwWidget = LayoutInflater.from(_mActivity).inflate(R.layout.refund_widget, llWidgetContainer, true);
            if (action == Constant.ACTION_REFUND_ALL) {
                vwWidget.findViewById(R.id.ll_refund_reason_container).setVisibility(View.GONE);
                vwWidget.findViewById(R.id.rl_refund_amount_container).setVisibility(View.GONE);
            }
        } else if (action == Constant.ACTION_RETURN) {
            LayoutInflater.from(_mActivity).inflate(R.layout.return_widget, llWidgetContainer, true);
        } else if (action == Constant.ACTION_COMPLAIN) {
            LayoutInflater.from(_mActivity).inflate(R.layout.complain_widget, llWidgetContainer, true);
            // 如果是投訴，隱藏底部的線條
            llGoodsOuterContainer.setBackground(null);
        }

        llGoodsListContainer = view.findViewById(R.id.ll_goods_list_container);
        adapter = new RefundGoodsListAdapter(_mActivity, llGoodsListContainer, R.layout.refund_goods_list_item);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);

        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        tvStoreName = view.findViewById(R.id.tv_store_name);
        goodsImage = view.findViewById(R.id.goods_image);
        tvGoodsName = view.findViewById(R.id.tv_goods_name);
        tvGoodsFullSpecs = view.findViewById(R.id.tv_goods_full_specs);
        tvPrice = view.findViewById(R.id.tv_price);
        tvGoodsNum = view.findViewById(R.id.tv_goods_num);
        btnSelectRefundReason = view.findViewById(R.id.btn_select_refund_reason);
        btnSelectRefundReason.setOnClickListener(this);
        tvRefundReason = view.findViewById(R.id.tv_refund_reason);

        tvRefundWay = view.findViewById(R.id.tv_refund_way);
        updateRefundWayDesc();
        Util.setOnClickListener(view, R.id.btn_select_refund_way, this);

        tvMaxRefundAmount = view.findViewById(R.id.tv_max_refund_amount);
        etRefundAmount = view.findViewById(R.id.et_refund_amount);
        etRefundDesc = view.findViewById(R.id.et_refund_desc);
        if (action == Constant.ACTION_REFUND || action == Constant.ACTION_REFUND_ALL) {
            etRefundDesc.setHint(getString(R.string.input_refund_desc_hint));
        } else if (action == Constant.ACTION_RETURN) {
            etRefundDesc.setHint(getString(R.string.input_return_desc_hint));
        }

        sglImageContainer = view.findViewById(R.id.sgl_image_container);
        btnAddImage = view.findViewById(R.id.btn_add_image);
        btnAddImage.setOnClickListener(this);

        abReturnCount = view.findViewById(R.id.ab_return_amount);

        switch (action) {
            case Constant.ACTION_REFUND:
                tvFragmentTitle.setText(getString(R.string.text_goods_refund));
                loadSingleGoodsRefundData();
                break;
            case Constant.ACTION_RETURN:
                tvFragmentTitle.setText(getString(R.string.text_goods_return));
                loadSingleGoodsReturnData();
                break;
            case Constant.ACTION_COMPLAIN:
                tvFragmentTitle.setText(getString(R.string.text_goods_complain));
                loadComplainData();
                break;
            case Constant.ACTION_REFUND_ALL:
                // 全部退款
                tvFragmentTitle.setText(getString(R.string.text_goods_refund));
                loadRefundAllData();
                break;
            default:
                break;
        }
    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_select_refund_reason) {
            hideSoftInput();

            String title = "";
            if (action == Constant.ACTION_REFUND) {
                title = getString(R.string.text_refund_reason);
            } else if (action == Constant.ACTION_RETURN) {
                title = getString(R.string.text_return_reason);
            } else {
                title = getString(R.string.text_complain_subject);
            }


            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, title,
                            PopupType.DEFAULT, reasonItemList, reasonIndex != -1 ? reasonIndex : 0, this))
                    .show();
        } else if (id == R.id.btn_select_refund_way) {
            hideSoftInput();
            showRefundWayPopup();
        } else if (id == R.id.btn_add_image) {
            openSystemAlbumIntent(RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
        } else if (id == R.id.btn_commit) {
            commitRefundRequest();
        }
    }

    private void showRefundWayPopup() {
        new XPopup.Builder(_mActivity)
                // 如果不加这个，评论弹窗会移动到软键盘上面
                .moveUpToKeyboard(false)
                .asCustom(new RefundWayPopup(_mActivity, this, refundWay))
                .show();
    }

    private void commitRefundRequest() {
        final String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        // 如果是退款、全部退款、退貨，校驗是否有選擇【退款方式】
        if (action == Constant.ACTION_REFUND || action == Constant.ACTION_REFUND_ALL || action == Constant.ACTION_RETURN) {
            if (refundWay == RefundWayPopup.REFUND_WAY_UNKNOWN) {
                ToastUtil.error(getContext(), "請選擇退款方式");
                showRefundWayPopup();
                return;
            }
        }

        if (action == Constant.ACTION_REFUND || action == Constant.ACTION_REFUND_ALL) {
            if (action == Constant.ACTION_REFUND) {
                if (reasonIndex == -1) {
                    ToastUtil.error(_mActivity, getString(R.string.select_refund_reason_hint));
                    return;
                }

                String refundAmountStr = etRefundAmount.getText().toString().trim();
                if (StringUtil.isEmpty(refundAmountStr)) {
                    ToastUtil.error(_mActivity, getString(R.string.input_refund_amount_hint));
                    return;
                }

                refundAmount = Float.valueOf(etRefundAmount.getText().toString().trim());
                SLog.info("refundAmount[%s], maxRefundAmount[%s], comp[%s]", refundAmount, maxRefundAmount, refundAmount > maxRefundAmount);
                if (refundAmount > maxRefundAmount) {
                    ToastUtil.error(_mActivity, "退款金額不能超過最多可退金額");
                    return;
                }
            }



            final String buyerMessage = etRefundDesc.getText().toString().trim();
            if (StringUtil.isEmpty(buyerMessage)) {
                ToastUtil.error(_mActivity, "請填寫退款說明");
                return;
            }

            final List<String> imagePathList = new ArrayList<>();
            int childCount = sglImageContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = sglImageContainer.getChildAt(i);
                if (view instanceof ImageView) {  // 如果是添加按鈕，則跳過
                    continue;
                }
                imagePathList.add((String) view.getTag());
            }


            TaskObserver taskObserver = new TaskObserver() {
                @Override
                public void onMessage() {
                    String responseStr = (String) message;
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }


                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_DETAIL, null);
                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST, null);
                    ToastUtil.success(_mActivity, "提交成功");
                    hideSoftInputPop();
                }
            };

            TwantApplication.Companion.getThreadPool().execute(new TaskObservable(taskObserver) {
                @Override
                public Object doWork() {
                    StringBuilder picJson = new StringBuilder();
                    // 上傳圖片文件
                    for (String imagePath : imagePathList) {
                        String result = Api.syncUploadFile(new File(imagePath));
                        SLog.info("result[%s]", result);
                        if (StringUtil.isEmpty(result)) {
                            return null;
                        }

                        if (picJson.length() > 0) {
                            picJson.append(",");
                        }
                        picJson.append(result);
                    }

                    EasyJSONObject params;
                    String path;
                    if (action == Constant.ACTION_REFUND) {
                        path = Api.PATH_SINGLE_GOODS_REFUND_SAVE;
                        params = EasyJSONObject.generate(
                                "token", token,
                                "ordersId", ordersId,
                                "picJson", picJson.toString(),
                                "buyerMessage", buyerMessage,
                                "ordersGoodsId", ordersGoodsId,
                                "reasonId", reasonItemList.get(reasonIndex).id,
                                "refundAmount", refundAmount,
                                "refundWay", refundWay);
                    } else {
                        path = Api.PATH_REFUND_ALL_SAVE;
                        params = EasyJSONObject.generate(
                                "token", token,
                                "ordersId", ordersId,
                                "picJson", picJson.toString(),
                                "buyerMessage", buyerMessage,
                                "refundWay", refundWay);
                    }

                    SLog.info("params[%s]", params.toString());

                    String responseStr = Api.syncPost(path, params);
                    SLog.info("responseStr[%s]", responseStr);
                    return responseStr;
                }
            });
        } else if (action == Constant.ACTION_RETURN) {
            if (reasonIndex == -1) {
                ToastUtil.error(_mActivity, getString(R.string.select_return_reason_hint));
                return;
            }

            final int returnCount = abReturnCount.getValue();
            SLog.info("returnCount[%d], maxReturnCount[%d]", returnCount, maxReturnCount);
            if (returnCount > maxReturnCount) {
                ToastUtil.error(_mActivity, "退貨數量不能超過購買數量");
                return;
            }

            String refundAmountStr = etRefundAmount.getText().toString().trim();
            if (StringUtil.isEmpty(refundAmountStr)) {
                ToastUtil.error(_mActivity, getString(R.string.input_return_amount_hint));
                return;
            }

            refundAmount = Float.valueOf(etRefundAmount.getText().toString().trim());
            SLog.info("refundAmount[%s], maxRefundAmount[%s], comp[%s]", refundAmount, maxRefundAmount, refundAmount > maxRefundAmount);
            if (refundAmount > maxRefundAmount) {
                ToastUtil.error(_mActivity, "退貨金額不能超過最多可退金額");
                return;
            }

            final String buyerMessage = etRefundDesc.getText().toString().trim();

            final List<String> imagePathList = new ArrayList<>();
            int childCount = sglImageContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = sglImageContainer.getChildAt(i);
                if (view instanceof ImageView) {  // 如果是添加按鈕，則跳過
                    continue;
                }
                imagePathList.add((String) view.getTag());
            }


            TaskObserver taskObserver = new TaskObserver() {
                @Override
                public void onMessage() {
                    String responseStr = (String) message;
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }


                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_DETAIL, null);
                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST, null);
                    ToastUtil.success(_mActivity, "提交成功");
                    hideSoftInputPop();
                }
            };

            TwantApplication.Companion.getThreadPool().execute(new TaskObservable(taskObserver) {
                @Override
                public Object doWork() {
                    StringBuilder picJson = new StringBuilder();
                    // 上傳圖片文件
                    for (String imagePath : imagePathList) {
                        String result = Api.syncUploadFile(new File(imagePath));
                        SLog.info("result[%s]", result);
                        if (StringUtil.isEmpty(result)) {
                            return null;
                        }

                        if (picJson.length() > 0) {
                            picJson.append(",");
                        }
                        picJson.append(result);
                    }

                    EasyJSONObject params = EasyJSONObject.generate(
                            "token", token,
                            "ordersId", ordersId,
                            "goodsNum", returnCount,
                            "picJson", picJson.toString(),
                            "buyerMessage", buyerMessage,
                            "ordersGoodsId", ordersGoodsId,
                            "reasonId", reasonItemList.get(reasonIndex).id,
                            "refundAmount", refundAmount,
                            "refundWay", refundWay);

                    SLog.info("params[%s]", params.toString());
                    String responseStr = Api.syncPost(Api.PATH_SINGLE_GOODS_RETURN_SAVE, params);
                    SLog.info("responseStr[%s]", responseStr);
                    return responseStr;
                }
            });
        } else { // 產品投訴
            if (reasonIndex == -1) {
                ToastUtil.error(_mActivity, getString(R.string.select_complain_subject_hint));
                return;
            }

            final String buyerMessage = etRefundDesc.getText().toString().trim();
            if (StringUtil.isEmpty(buyerMessage)) {
                ToastUtil.error(_mActivity, getString(R.string.input_complain_content_hint));
                return;
            }

            // 憑證圖片收集
            final List<String> imagePathList = new ArrayList<>();
            int childCount = sglImageContainer.getChildCount();
            for (int i = 0; i < childCount; i++) {
                View view = sglImageContainer.getChildAt(i);
                if (view instanceof ImageView) {  // 如果是添加按鈕，則跳過
                    continue;
                }
                imagePathList.add((String) view.getTag());
            }

            TaskObserver taskObserver = new TaskObserver() {
                @Override
                public void onMessage() {
                    String responseStr = (String) message;
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }


                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_DETAIL, null);
                    EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST, null);
                    ToastUtil.success(_mActivity, "提交成功");
                    hideSoftInputPop();
                }
            };

            TwantApplication.Companion.getThreadPool().execute(new TaskObservable(taskObserver) {
                @Override
                public Object doWork() {
                    StringBuilder picJson = new StringBuilder();
                    // 上傳圖片文件
                    for (String imagePath : imagePathList) {
                        String result = Api.syncUploadFile(new File(imagePath));
                        SLog.info("result[%s]", result);
                        if (StringUtil.isEmpty(result)) {
                            return null;
                        }

                        if (picJson.length() > 0) {
                            picJson.append(",");
                        }
                        picJson.append(result);
                    }

                    EasyJSONObject params = EasyJSONObject.generate(
                            "token", token,
                            "ordersId", ordersId,
                            "ordersGoodsId", ordersGoodsId,
                            "subjectId", reasonItemList.get(reasonIndex).id,
                            "accuserImages", picJson.toString(),
                            "accuserContent", buyerMessage);

                    SLog.info("params[%s]", params.toString());
                    String responseStr = Api.syncPost(Api.PATH_COMPLAIN_SAVE, params);
                    SLog.info("responseStr[%s]", responseStr);
                    return responseStr;
                }
            });
        }
    }

    private void loadSingleGoodsRefundData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        try {
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "ordersId", paramsInObj.getInt("ordersId"),
                    "ordersGoodsId", paramsInObj.getInt("ordersGoodsId"));

            SLog.info("params[%s]", params);

            Api.postUI(Api.PATH_SINGLE_GOODS_REFUND, params, new UICallback() {
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

                        String storeName = responseObj.getSafeString("datas.ordersVo.storeName");
                        tvStoreName.setText(storeName);

                        EasyJSONObject ordersGoodsVo = responseObj.getSafeObject("datas.ordersGoodsVo");


                        List<RefundGoodsItem> refundGoodsItemList = new ArrayList<>();

                        RefundGoodsItem refundGoodsItem = new RefundGoodsItem();
                        refundGoodsItem.goodsImageUrl = StringUtil.normalizeImageUrl(ordersGoodsVo.getSafeString("goodsImage"));
                        refundGoodsItem.goodsName = ordersGoodsVo.getSafeString("goodsName");
                        refundGoodsItem.goodsFullSpecs = ordersGoodsVo.getSafeString("goodsFullSpecs");
                        refundGoodsItem.price =  ordersGoodsVo.getDouble("goodsPrice");
                        refundGoodsItem.goodsNum = ordersGoodsVo.getInt("buyNum");
                        refundGoodsItemList.add(refundGoodsItem);
                        adapter.setData(refundGoodsItemList);

                        EasyJSONArray refundReasonList = responseObj.getSafeArray("datas.refundReasonList");
                        for (Object object : refundReasonList) {
                            EasyJSONObject reason = (EasyJSONObject) object;
                            reasonItemList.add(new ListPopupItem(
                                    reason.getInt("reasonId"),
                                    reason.getSafeString("reasonInfo"),
                                    null));
                        }

                        maxRefundAmount = (float) ordersGoodsVo.getDouble("goodsPayAmount");
                        tvMaxRefundAmount.setText(StringUtil.formatPrice(_mActivity, maxRefundAmount, 0));
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void loadRefundAllData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        try {
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "ordersId", paramsInObj.getInt("ordersId"));

            SLog.info("params[%s]", params);

            Api.postUI(Api.PATH_REFUND_ALL, params, new UICallback() {
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

                        String storeName = responseObj.getSafeString("datas.ordersVo.storeName");
                        tvStoreName.setText(storeName);

                        EasyJSONArray ordersGoodsVoList = responseObj.getSafeArray("datas.ordersVo.ordersGoodsVoList");
                        List<RefundGoodsItem> refundGoodsItemList = new ArrayList<>();
                        for (Object object : ordersGoodsVoList) {
                            EasyJSONObject ordersGoodsVo = (EasyJSONObject) object;

                            RefundGoodsItem refundGoodsItem = new RefundGoodsItem();
                            refundGoodsItem.goodsImageUrl = StringUtil.normalizeImageUrl(ordersGoodsVo.getSafeString("goodsImage"));
                            refundGoodsItem.goodsName = ordersGoodsVo.getSafeString("goodsName");
                            refundGoodsItem.goodsFullSpecs = ordersGoodsVo.getSafeString("goodsFullSpecs");
                            refundGoodsItem.price =  ordersGoodsVo.getDouble("goodsPrice");
                            refundGoodsItem.goodsNum = ordersGoodsVo.getInt("buyNum");
                            refundGoodsItemList.add(refundGoodsItem);
                        }
                        adapter.setData(refundGoodsItemList);

                        /*
                        EasyJSONArray refundReasonList = responseObj.getSafeArray("datas.refundReasonList");
                        for (Object object : refundReasonList) {
                            EasyJSONObject reason = (EasyJSONObject) object;
                            reasonItemList.add(new ListPopupItem(
                                    reason.getInt("reasonId"),
                                    reason.getString("reasonInfo"),
                                    null));
                        }
                        */

                        maxRefundAmount = (float) responseObj.getDouble("datas.ordersVo.ordersAmount");
                        tvMaxRefundAmount.setText(StringUtil.formatPrice(_mActivity, maxRefundAmount, 0));
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void loadSingleGoodsReturnData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        try {
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "ordersId", paramsInObj.getInt("ordersId"),
                    "ordersGoodsId", paramsInObj.getInt("ordersGoodsId"));

            SLog.info("params[%s]", params);

            Api.postUI(Api.PATH_SINGLE_GOODS_RETURN, params, new UICallback() {
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

                        String storeName = responseObj.getSafeString("datas.ordersVo.storeName");
                        tvStoreName.setText(storeName);

                        EasyJSONObject ordersGoodsVo = responseObj.getSafeObject("datas.ordersGoodsVo");

                        List<RefundGoodsItem> refundGoodsItemList = new ArrayList<>();

                        RefundGoodsItem refundGoodsItem = new RefundGoodsItem();
                        refundGoodsItem.goodsImageUrl = StringUtil.normalizeImageUrl(ordersGoodsVo.getSafeString("goodsImage"));
                        refundGoodsItem.goodsName = ordersGoodsVo.getSafeString("goodsName");
                        refundGoodsItem.goodsFullSpecs = ordersGoodsVo.getSafeString("goodsFullSpecs");
                        refundGoodsItem.price = ordersGoodsVo.getDouble("goodsPrice");
                        int goodsNum = ordersGoodsVo.getInt("buyNum");
                        refundGoodsItem.goodsNum = goodsNum;
                        refundGoodsItemList.add(refundGoodsItem);
                        adapter.setData(refundGoodsItemList);
                        maxReturnCount = ordersGoodsVo.getInt("buyNum");
                        // tvGoodsNum.setText(getString(R.string.times_sign) + " " + goodsNum);
                        abReturnCount.setMinValue(1, null);
                        abReturnCount.setMaxValue(maxReturnCount, null);
                        abReturnCount.setValue(goodsNum);

                        EasyJSONArray refundReasonList = responseObj.getSafeArray("datas.refundReasonList");
                        for (Object object : refundReasonList) {
                            EasyJSONObject reason = (EasyJSONObject) object;
                            reasonItemList.add(new ListPopupItem(
                                    reason.getInt("reasonId"),
                                    reason.getSafeString("reasonInfo"),
                                    null));
                        }

                        maxRefundAmount = (float) ordersGoodsVo.getDouble("goodsPayAmount");
                        etRefundAmount.setText(StringUtil.formatFloat(maxRefundAmount));

                        tvMaxRefundAmount.setText(StringUtil.formatPrice(_mActivity, maxRefundAmount, 0));
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }


    private void loadComplainData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        try {
            EasyJSONObject params = EasyJSONObject.generate(
                    "token", token,
                    "ordersId", paramsInObj.getInt("ordersId"),
                    "ordersGoodsId", paramsInObj.getInt("ordersGoodsId"));

            SLog.info("params[%s]", params);

            Api.postUI(Api.PATH_COMPLAIN, params, new UICallback() {
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

                        String storeName = responseObj.getSafeString("datas.ordersVo.storeName");
                        tvStoreName.setText(storeName);

                        EasyJSONObject ordersGoodsVo = responseObj.getSafeObject("datas.ordersGoodsVo");

                        List<RefundGoodsItem> refundGoodsItemList = new ArrayList<>();

                        RefundGoodsItem refundGoodsItem = new RefundGoodsItem();
                        refundGoodsItem.goodsImageUrl = StringUtil.normalizeImageUrl(ordersGoodsVo.getSafeString("goodsImage"));
                        refundGoodsItem.goodsName = ordersGoodsVo.getSafeString("goodsName");
                        refundGoodsItem.goodsFullSpecs = ordersGoodsVo.getSafeString("goodsFullSpecs");
                        refundGoodsItem.price = (float) ordersGoodsVo.getDouble("goodsPrice");
                        int goodsNum = ordersGoodsVo.getInt("buyNum");
                        refundGoodsItem.goodsNum = goodsNum;
                        refundGoodsItemList.add(refundGoodsItem);
                        adapter.setData(refundGoodsItemList);

                        maxReturnCount = ordersGoodsVo.getInt("buyNum");
                        // tvGoodsNum.setText(getString(R.string.times_sign) + " " + goodsNum);

                        EasyJSONArray refundReasonList = responseObj.getSafeArray("datas.complainSubjectList");
                        for (Object object : refundReasonList) {
                            EasyJSONObject reason = (EasyJSONObject) object;
                            reasonItemList.add(new ListPopupItem(
                                    reason.getInt("subjectId"),
                                    reason.getSafeString("title"),
                                    null));
                        }

                        maxRefundAmount = (float) ordersGoodsVo.getDouble("goodsPayAmount");
                        tvMaxRefundAmount.setText(StringUtil.formatPrice(_mActivity, maxRefundAmount, 0));
                    } catch (Exception e) {
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("type[%s], id[%d], extra[%s]", type, id, extra);
        if (type == PopupType.DEFAULT) {
            reasonIndex = id;
            tvRefundReason.setText(reasonItemList.get(reasonIndex).title);
        } else if (type == PopupType.SELECT_REFUND_WAY) {
            refundWay = id;
            updateRefundWayDesc();
        }
    }

    private void updateRefundWayDesc() {
        if (tvRefundWay == null) {
            return;
        }
        tvRefundWay.setTextColor(getResources().getColor(R.color.tw_black,null));
        if (refundWay == RefundWayPopup.REFUND_WAY_UNKNOWN) {
            tvRefundWay.setText("請選擇");
            tvRefundWay.setTextColor(Color.parseColor("#A8A8A8"));
        } else if (refundWay == RefundWayPopup.REFUND_WAY_WALLET) {
            tvRefundWay.setText("退至預存款");
        } else if (refundWay == RefundWayPopup.REFUND_WAY_ORIGINAL) {
            tvRefundWay.setText("原路退回");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SLog.info("resultCode[%d]", resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }

        Uri uri = data.getData();
        String imageAbsolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
        SLog.info("imageAbsolutePath[%s]", imageAbsolutePath);

        final View imageWidget = LayoutInflater.from(_mActivity).inflate(R.layout.refund_image_widget, sglImageContainer, false);
        ImageView imageView = imageWidget.findViewById(R.id.refund_image);
        imageWidget.findViewById(R.id.btn_remove_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sglImageContainer.removeView(imageWidget);
                btnAddImage.setVisibility(View.VISIBLE);
            }
        });

        imageWidget.setTag(imageAbsolutePath);

        Glide.with(_mActivity).load(imageAbsolutePath).centerCrop().into(imageView);
        int childCount = sglImageContainer.getChildCount();
        if (childCount > 0) {
            if (childCount == 3) { // 最多3張圖片，如果原本已經有2張 加1個添加按鈕，則隱藏添加按鈕
                btnAddImage.setVisibility(View.GONE);
            }
            sglImageContainer.addView(imageWidget, childCount - 1);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}
