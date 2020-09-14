package com.ftofs.twant.widget;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ImStoreGoodsListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ImStoreGoodsItem;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * IM聊天發送產品選擇(暫時不做分類)
 * @author zwm
 */
public class ImStoreGoodsPopup extends BottomPopupView implements View.OnClickListener {
    Context context;

    int storeId;
    OnSelectedListener onSelectedListener;
    String imName;
    String keyword;

    ImStoreGoodsListAdapter adapter;
    List<ImStoreGoodsItem> imStoreGoodsItemList = new ArrayList<>();

    EditText etKeyword;

    public ImStoreGoodsPopup(@NonNull Context context, int storeId, String imName, String keyword, OnSelectedListener onSelectedListener) {
        super(context);
        this.context = context;

        this.storeId = storeId;
        this.imName = imName;
        this.keyword = keyword;
        this.onSelectedListener = onSelectedListener;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.im_store_goods_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);

        etKeyword = findViewById(R.id.et_keyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    keyword = etKeyword.getText().toString().trim();
                    Util.hideSoftInput(context, etKeyword);

                    loadData();
                    return true;
                }
                return false;
            }
        });

        RecyclerView rvList = findViewById(R.id.rv_list);
        GridLayoutManager layoutManager = new GridLayoutManager(context, 4, GridLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        adapter = new ImStoreGoodsListAdapter(imStoreGoodsItemList);
        adapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                ImStoreGoodsItem item = imStoreGoodsItemList.get(position);
                if (item.getItemType() == ImStoreGoodsItem.ITEM_TYPE_HEADER) {
                    return 4;
                } else {
                    return 1;
                }
            }
        });
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ImStoreGoodsItem item = imStoreGoodsItemList.get(position);
                if (item.getItemType() == ImStoreGoodsItem.ITEM_TYPE_ITEM) {

                    EasyJSONObject goodsInfo = EasyJSONObject.generate(
                            "goodsName", item.goodsName,
                            "commonId", item.commonId,
                            "goodsImage", item.goodsImg);

                    SLog.info("goodsInfo[%s]", goodsInfo.toString());
                    onSelectedListener.onSelected(PopupType.IM_CHAT_SEND_GOODS, 0, goodsInfo);
                    dismiss();
                }
            }
        });
        rvList.setAdapter(adapter);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "imName", imName,
                "storeId", storeId);

        if (!StringUtil.isEmpty(keyword)) {
            try {
                params.set("keyword", keyword);
            } catch (Exception e) {
                SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
            }
        }

        String url = Api.PATH_IM_STORE_GOODS_LIST;
        SLog.info("params[%s]", params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(context, responseObj)) {
                    LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                    return;
                }

                try {
                    imStoreGoodsItemList.clear();

                    EasyJSONArray goodsList = responseObj.getSafeArray("datas.goodsList");

                    for (Object object : goodsList) {
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;

                        // 分兩類 1.分類名  2.產品列表
                        ImStoreGoodsItem item = new ImStoreGoodsItem(ImStoreGoodsItem.ITEM_TYPE_HEADER);
                        item.goodsName = easyJSONObject.getSafeString("categoryName");
                        imStoreGoodsItemList.add(item);

                        EasyJSONArray goods = easyJSONObject.getSafeArray("goods");
                        for (Object object2 : goods) {
                            EasyJSONObject easyJSONObject2 = (EasyJSONObject) object2;

                            item = new ImStoreGoodsItem(ImStoreGoodsItem.ITEM_TYPE_ITEM);

                            item.commonId = easyJSONObject2.getInt("commonId");
                            item.goodsImg = easyJSONObject2.getSafeString("goodsImg");
                            item.goodsName = easyJSONObject2.getSafeString("goodsName");
                            imStoreGoodsItemList.add(item);
                        }
                    }
                    adapter.setNewData(imStoreGoodsItemList);
                } catch (Exception e) {

                }
            }
        });
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
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_dismiss) {
            dismiss();
        }
    }
}
