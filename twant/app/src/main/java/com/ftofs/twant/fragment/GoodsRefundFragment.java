package com.ftofs.twant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.entity.footprint.TotalStatus;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.IntentUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AdjustButton;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.SquareGridLayout;
import com.lxj.xpopup.XPopup;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商品退款、退貨、投訴Fragment
 * @author zwm
 */
public class GoodsRefundFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    int action;
    int ordersId;
    int ordersGoodsId;

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

    public static GoodsRefundFragment newInstance(String paramsIn) {
        Bundle args = new Bundle();

        args.putString("paramsIn", paramsIn);

        GoodsRefundFragment fragment = new GoodsRefundFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_refund, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        String paramsIn = args.getString("paramsIn");
        paramsInObj = (EasyJSONObject) EasyJSONObject.parse(paramsIn);
        try {
            action = paramsInObj.getInt("action");
            ordersId = paramsInObj.getInt("ordersId");
            ordersGoodsId = paramsInObj.getInt("ordersGoodsId");
        } catch (EasyJSONException e) {
            e.printStackTrace();
        }

        llWidgetContainer = view.findViewById(R.id.ll_widget_container);
        if (action == Constant.ACTION_REFUND) {
            LayoutInflater.from(_mActivity).inflate(R.layout.refund_widget, llWidgetContainer, true);
        } else if (action == Constant.ACTION_RETURN) {
            LayoutInflater.from(_mActivity).inflate(R.layout.return_widget, llWidgetContainer, true);
        } else if (action == Constant.ACTION_COMPLAIN) {

        }

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

        tvMaxRefundAmount = view.findViewById(R.id.tv_max_refund_amount);
        etRefundAmount = view.findViewById(R.id.et_refund_amount);
        etRefundDesc = view.findViewById(R.id.et_refund_desc);

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
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_select_refund_reason) {
            hideSoftInput();

            String title = "";
            if (action == Constant.ACTION_REFUND) {
                title = getString(R.string.text_refund_reason);
            } else if (action == Constant.ACTION_RETURN) {
                title = getString(R.string.text_return_reason);
            }


            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, title,
                            Constant.POPUP_TYPE_DEFAULT, reasonItemList, reasonIndex != -1 ? reasonIndex : 0, this))
                    .show();
        } else if (id == R.id.btn_add_image) {
            startActivityForResult(IntentUtil.makeOpenSystemAlbumIntent(), RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
        } else if (id == R.id.btn_commit) {
            commitRefundRequest();
        }
    }


    private void commitRefundRequest() {
        final String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        if (action == Constant.ACTION_REFUND) {
            if (reasonIndex == -1) {
                ToastUtil.show(_mActivity, getString(R.string.select_refund_reason_hint));
                return;
            }

            String refundAmountStr = etRefundAmount.getText().toString().trim();
            if (StringUtil.isEmpty(refundAmountStr)) {
                ToastUtil.show(_mActivity, getString(R.string.input_refund_amount_hint));
                return;
            }


            final float refundAmount = Float.valueOf(etRefundAmount.getText().toString().trim());
            SLog.info("refundAmount[%s], maxRefundAmount[%s], comp[%s]", refundAmount, maxRefundAmount, refundAmount > maxRefundAmount);
            if (refundAmount > maxRefundAmount) {
                ToastUtil.show(_mActivity, "退款金額不能超過最多可退金額");
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
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.show(_mActivity, "提交成功");
                    pop();
                }
            };

            TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
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
                            "picJson", picJson.toString(),
                            "buyerMessage", buyerMessage,
                            "ordersGoodsId", ordersGoodsId,
                            "reasonId", reasonItemList.get(reasonIndex).id,
                            "refundAmount", refundAmount);

                    SLog.info("params[%s]", params.toString());
                    String responseStr = Api.syncPost(Api.PATH_SINGLE_GOODS_REFUND_SAVE, params);
                    SLog.info("responseStr[%s]", responseStr);
                    return responseStr;
                }
            });
        } else if (action == Constant.ACTION_RETURN) {
            if (reasonIndex == -1) {
                ToastUtil.show(_mActivity, getString(R.string.select_return_reason_hint));
                return;
            }

            final int returnCount = abReturnCount.getValue();
            SLog.info("returnCount[%d], maxReturnCount[%d]", returnCount, maxReturnCount);
            if (returnCount > maxReturnCount) {
                ToastUtil.show(_mActivity, "退貨數量不能超過購買數量");
                return;
            }

            String refundAmountStr = etRefundAmount.getText().toString().trim();
            if (StringUtil.isEmpty(refundAmountStr)) {
                ToastUtil.show(_mActivity, getString(R.string.input_return_amount_hint));
                return;
            }

            final float refundAmount = Float.valueOf(etRefundAmount.getText().toString().trim());
            SLog.info("refundAmount[%s], maxRefundAmount[%s], comp[%s]", refundAmount, maxRefundAmount, refundAmount > maxRefundAmount);
            if (refundAmount > maxRefundAmount) {
                ToastUtil.show(_mActivity, "退貨金額不能超過最多可退金額");
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
                    EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    ToastUtil.show(_mActivity, "提交成功");
                    pop();
                }
            };

            TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
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
                            "refundAmount", refundAmount);

                    SLog.info("params[%s]", params.toString());
                    String responseStr = Api.syncPost(Api.PATH_SINGLE_GOODS_RETURN_SAVE, params);
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

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        String storeName = responseObj.getString("datas.ordersVo.storeName");
                        tvStoreName.setText(storeName);

                        EasyJSONObject ordersGoodsVo = responseObj.getObject("datas.ordersGoodsVo");

                        String goodsImageUrl = StringUtil.normalizeImageUrl(ordersGoodsVo.getString("goodsImage"));
                        Glide.with(_mActivity).load(goodsImageUrl).into(goodsImage);

                        String goodsName = ordersGoodsVo.getString("goodsName");
                        tvGoodsName.setText(goodsName);

                        String goodsFullSpecs = ordersGoodsVo.getString("goodsFullSpecs");
                        tvGoodsFullSpecs.setText(goodsFullSpecs);

                        float price = (float) ordersGoodsVo.getDouble("goodsPrice");
                        tvPrice.setText(StringUtil.formatPrice(_mActivity, price, 0));

                        int goodsNum = ordersGoodsVo.getInt("buyNum");
                        tvGoodsNum.setText(getString(R.string.times_sign) + " " + goodsNum);

                        EasyJSONArray refundReasonList = responseObj.getArray("datas.refundReasonList");
                        for (Object object : refundReasonList) {
                            EasyJSONObject reason = (EasyJSONObject) object;
                            reasonItemList.add(new ListPopupItem(
                                    reason.getInt("reasonId"),
                                    reason.getString("reasonInfo"),
                                    null));
                        }

                        maxRefundAmount = (float) ordersGoodsVo.getDouble("goodsPayAmount");
                        tvMaxRefundAmount.setText(StringUtil.formatPrice(_mActivity, maxRefundAmount, 0));
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

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

                        EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                        if (ToastUtil.checkError(_mActivity, responseObj)) {
                            return;
                        }

                        String storeName = responseObj.getString("datas.ordersVo.storeName");
                        tvStoreName.setText(storeName);

                        EasyJSONObject ordersGoodsVo = responseObj.getObject("datas.ordersGoodsVo");

                        String goodsImageUrl = StringUtil.normalizeImageUrl(ordersGoodsVo.getString("goodsImage"));
                        Glide.with(_mActivity).load(goodsImageUrl).into(goodsImage);

                        String goodsName = ordersGoodsVo.getString("goodsName");
                        tvGoodsName.setText(goodsName);

                        String goodsFullSpecs = ordersGoodsVo.getString("goodsFullSpecs");
                        tvGoodsFullSpecs.setText(goodsFullSpecs);

                        float price = (float) ordersGoodsVo.getDouble("goodsPrice");
                        tvPrice.setText(StringUtil.formatPrice(_mActivity, price, 0));

                        int goodsNum = ordersGoodsVo.getInt("buyNum");
                        maxReturnCount = ordersGoodsVo.getInt("buyNum");
                        tvGoodsNum.setText(getString(R.string.times_sign) + " " + goodsNum);
                        abReturnCount.setMinValue(1);
                        abReturnCount.setValue(goodsNum);

                        EasyJSONArray refundReasonList = responseObj.getArray("datas.refundReasonList");
                        for (Object object : refundReasonList) {
                            EasyJSONObject reason = (EasyJSONObject) object;
                            reasonItemList.add(new ListPopupItem(
                                    reason.getInt("reasonId"),
                                    reason.getString("reasonInfo"),
                                    null));
                        }

                        maxRefundAmount = (float) ordersGoodsVo.getDouble("goodsPayAmount");
                        tvMaxRefundAmount.setText(StringUtil.formatPrice(_mActivity, maxRefundAmount, 0));
                    } catch (Exception e) {

                    }
                }
            });
        } catch (Exception e) {

        }
    }

    @Override
    public void onSelected(int type, int id, Object extra) {
        SLog.info("type[%d], id[%d], extra[%s]", type, id, extra);
        if (type == Constant.POPUP_TYPE_DEFAULT) {
            reasonIndex = id;
            tvRefundReason.setText(reasonItemList.get(reasonIndex).title);
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
        pop();
        return true;
    }
}
