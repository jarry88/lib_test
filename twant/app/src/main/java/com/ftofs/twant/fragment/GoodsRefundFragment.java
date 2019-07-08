package com.ftofs.twant.fragment;

import android.content.Intent;
import android.net.Uri;
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
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.IntentUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.SquareGridLayout;
import com.lxj.xpopup.XPopup;

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
    public static final int ACTION_REFUND = 1;
    public static final int ACTION_RETURN = 2;
    public static final int ACTION_COMPLAIN = 3;

    int action;
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
    EditText etRefundAmount;
    EditText etRefundDesc;

    ImageView btnAddImage;

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
        } catch (EasyJSONException e) {
            e.printStackTrace();
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

        switch (action) {
            case ACTION_REFUND:
                tvFragmentTitle.setText(getString(R.string.text_goods_refund));
                loadSingleGoodsRefundData();
                break;
            case ACTION_RETURN:
                tvFragmentTitle.setText(getString(R.string.text_goods_return));
                break;
            case ACTION_COMPLAIN:
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

            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, getString(R.string.text_refund_reason),
                            Constant.POPUP_TYPE_DEFAULT, reasonItemList, reasonIndex != -1 ? reasonIndex : 0, this))
                    .show();
        } else if (id == R.id.btn_add_image) {
            startActivityForResult(IntentUtil.makeOpenSystemAlbumIntent(), RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
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

                        float goodsPayAmount = (float) ordersGoodsVo.getDouble("goodsPayAmount");
                        tvMaxRefundAmount.setText(StringUtil.formatPrice(_mActivity, goodsPayAmount, 0));


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

        RelativeLayout imageWidget = (RelativeLayout) LayoutInflater.from(_mActivity).inflate(R.layout.refund_image_widget, sglImageContainer, false);
        ImageView imageView = imageWidget.findViewById(R.id.refund_image);

        Glide.with(_mActivity).load(imageAbsolutePath).centerCrop().into(imageView);
        int childCount = sglImageContainer.getChildCount();
        if (childCount > 0) {
            sglImageContainer.addView(imageWidget, childCount - 1);
        }
    }
}
