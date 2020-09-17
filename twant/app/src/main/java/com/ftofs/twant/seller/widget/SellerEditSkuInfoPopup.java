package com.ftofs.twant.seller.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.CustomAction;
import com.ftofs.twant.entity.CustomActionData;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.seller.entity.SellerSpecPermutation;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import cn.snailpad.easyjson.EasyJSONObject;

public class SellerEditSkuInfoPopup extends CenterPopupView implements View.OnClickListener {
    Context context;

    EditText etPrice;
    EditText etStockStorage;
    EditText etReservedStorage;
    EditText etGoodsSN;

    int position;
    SellerSpecPermutation sellerSpecPermutation;
    SimpleCallback simpleCallback;

    public SellerEditSkuInfoPopup(@NonNull Context context, int position, SellerSpecPermutation sellerSpecPermutation, SimpleCallback simpleCallback) {
        super(context);

        this.context = context;
        this.position = position;
        this.sellerSpecPermutation = sellerSpecPermutation;
        this.simpleCallback = simpleCallback;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.seller_edit_sku_info_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        etPrice = findViewById(R.id.et_price);
        etPrice.setText(StringUtil.formatFloat(sellerSpecPermutation.price));

        etStockStorage = findViewById(R.id.et_stock_storage);
        etStockStorage.setText(String.valueOf(sellerSpecPermutation.storage));

        etReservedStorage = findViewById(R.id.et_reserved_storage);
        etReservedStorage.setText(String.valueOf(sellerSpecPermutation.reserved));

        etGoodsSN = findViewById(R.id.et_goods_sn);
        etGoodsSN.setText(sellerSpecPermutation.goodsSN);

        findViewById(R.id.btn_commit).setOnClickListener(this);
        findViewById(R.id.btn_cancel).setOnClickListener(this);
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {

    }

    @Override
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 1f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cancel) {
            dismiss();
        } else if (id == R.id.btn_commit) {
            try {
                double price = Double.parseDouble(etPrice.getText().toString().trim());
                int stockStorage = Integer.parseInt(etStockStorage.getText().toString().trim());
                int reservedStorage = Integer.parseInt(etReservedStorage.getText().toString().trim());
                String goodsSN = etGoodsSN.getText().toString().trim();

                if (simpleCallback != null) {
                    CustomActionData customActionData = new CustomActionData();
                    customActionData.action = CustomAction.CUSTOM_ACTION_SELLER_EDIT_SKU_INFO.ordinal();
                    customActionData.data = EasyJSONObject.generate(
                            "position", position,
                            "price", price,
                            "stockStorage", stockStorage,
                            "reservedStorage", reservedStorage,
                            "goodsSN", goodsSN
                    );
                    simpleCallback.onSimpleCall(customActionData);
                }

                dismiss();
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));

                ToastUtil.error(context, "請輸入有效的SKU信息");
                return;
            }
        }
    }
}
