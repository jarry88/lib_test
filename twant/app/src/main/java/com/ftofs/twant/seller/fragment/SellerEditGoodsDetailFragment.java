package com.ftofs.twant.seller.fragment;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.domain.Format;
import com.ftofs.twant.domain.goods.Category;
import com.ftofs.twant.domain.store.StoreLabel;
import com.ftofs.twant.entity.ListPopupItem;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.seller.widget.StoreLabelPopup;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ListPopup;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

public class SellerEditGoodsDetailFragment extends BaseFragment implements View.OnClickListener, OnSelectedListener {
    TextView tvTitle;
    private EasyJSONObject publishGoodsInfo= new EasyJSONObject();
    private int commonId;
    SellerGoodsDetailFragment parent;
    private int formatBottomIndex;
    private int formatTopIndex;
    private Integer formatTop;
    private Integer formatBottom;
    private EditText  etVideos;
    private int storeLabelId;
    private List<Category> selectCategoryList=new ArrayList<>();
    private EasyJSONArray storeLabelIdList=new EasyJSONArray();
    private String storeLabelNames;
    private TextView tvCategory;
    private TextView  tvFormatTop;
    private TextView  tvFormatBottom;
    private TextView tvAddGoodsBody;


    public static SellerEditGoodsDetailFragment newInstance(SellerGoodsDetailFragment parent) {
        SellerEditGoodsDetailFragment fragment= new SellerEditGoodsDetailFragment();
        fragment.parent = parent;
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_goods_detail_edit, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        View view = getView();
        tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("編輯詳情信息");

        etVideos = view.findViewById(R.id.et_goods_video_url);
        tvCategory = view.findViewById(R.id.tv_select_store_category);
        view.findViewById(R.id.ll_bottom_container).setVisibility(View.GONE);
        view.findViewById(R.id.btn_ok).setVisibility(View.VISIBLE);

        Util.setOnClickListener(view, R.id.btn_ok, this);
        Util.setOnClickListener(view, R.id.btn_back, this);

        Util.setOnClickListener(view, R.id.btn_select_store_category_id, this);
        tvAddGoodsBody =view.findViewById(R.id.btn_add_app_description);
        tvFormatTop =view.findViewById(R.id.tv_format_top);
        tvFormatBottom =view.findViewById(R.id.tv_format_bottom);
//        tvAddGoodsBody.setOnClickListener(v -> {
//            rlDetailBody.setVisibility(View.VISIBLE);
//        });
        loadDetailDate();
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (parent.goodsVo == null) {
            loadData();
        } else {
            explainData();
        }
    }

    private void explainData() {
        try{
            storeLabelNames = parent.storeLabelNames;
            if (!StringUtil.isEmpty(parent.formatBottomName)) {
                tvFormatBottom.setText(parent.formatBottomName);
            }
            if (!StringUtil.isEmpty(parent.formatTopName)) {
                tvFormatTop.setText(parent.formatTopName);
            }
            if (!StringUtil.isEmpty(parent.detailVideo)) {
                etVideos.setText(parent.detailVideo);
            }
            updateView();
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    private void updateView() {
        tvCategory.setText(storeLabelNames);
    }


    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String url = Api.PATH_SELLER_GOODS_DETAIL + "/" + parent.commonId;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );
        SLog.info("url[%s], params[%s]", url, params);
        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        loadingPopup.show();
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                loadingPopup.dismiss();
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();
                try {
                    SLog.info("responseStr[%s]", responseStr);
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    View contentView = getView();
                    if (contentView == null) {
                        return;
                    }

                    EasyJSONObject goodsVo = responseObj.getSafeObject("datas.GoodsVo");
                    parent.goodsVo=goodsVo;
                    parent.commonId = goodsVo.getInt("commonId");
                    parent.formatTopName = goodsVo.getSafeString("formatTopName");
                    parent.formatBottomName = goodsVo.getSafeString("formatBottomName");
                    explainData();
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    private boolean checkDetailInfo() {
        String detailVideo = "";
        if (etVideos.getText() != null) {
            detailVideo = etVideos.getText().toString();
        }
//        if (storeLabelId <=0) {
//            ToastUtil.error(_mActivity,"請選擇店内分類");
//            return false;
//        }

        try{
            publishGoodsInfo.set("commonId", parent.commonId);
            publishGoodsInfo.set("editType", 4);
            if (storeLabelId >0) {
                publishGoodsInfo.set("storeLabelIdList", storeLabelIdList);
            }

            publishGoodsInfo.set("detailVideo", detailVideo);
            if (formatBottom > 0) {
                publishGoodsInfo.set("formatBottom", formatBottom);
            }
            if (formatTop > 0) {
                publishGoodsInfo.set("formatTop", formatTop);
            }
//            goodsMobileBodyVoList
        }catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        return true;
    }
    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        }
        if (id == R.id.btn_ok) {
            if (checkDetailInfo()) {
                parent.saveGoodsInfo(publishGoodsInfo, new SimpleCallback() {
                    @Override
                    public void onSimpleCall(Object data) {
                        ToastUtil.success(_mActivity,data.toString());
                        hideSoftInputPop();
                    }
                });
            }

        }

    }

    private void loadDetailDate() {
        EasyJSONObject params = EasyJSONObject.generate("token", User.getToken(),"isPublish",0);
        String url = Api.PATH_SELLER_GOODS_PUBLISH_PAGE;

        SLog.info("url[%s], params[%s]", url, params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }


            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    EasyJSONObject data = responseObj.getObject("datas");
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        hideSoftInput();
                        return;
                    }
                    updateDetailView(data);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }
    //    更新商品详情页数据
    private void updateDetailView(EasyJSONObject data) throws Exception{
        EasyJSONArray storeLabelList = data.getArray("storeLabelList");
        if (storeLabelList != null) {
            List<StoreLabel> list = new ArrayList<>();
            for (Object o : storeLabelList) {
                list.add(StoreLabel.parse(((EasyJSONObject) o)));
            }
            TextView tvSelect=getView().findViewById(R.id.btn_select_store_category_id);
            tvSelect.setOnClickListener(v ->{
                hideSoftInput();
                if (list != null && list.size() != 0) {
                    new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(
                            new StoreLabelPopup(_mActivity, PopupType.STORE_CATEGORY, list, this)
                    ).show();
                } else {
                    ToastUtil.error(_mActivity,"當前店鋪沒有分類數據");
                    storeLabelId = 0;
                }
            });
        }

        EasyJSONArray formatBottomList = data.getArray("formatBottomList");

        if (formatBottomList != null) {

            List<ListPopupItem> list = new ArrayList<>();
            list.add(new ListPopupItem(-1, "取消選擇", null));
            for (Object o : formatBottomList) {
                Format format = Format.parse(((EasyJSONObject) o));
                ListPopupItem item = new ListPopupItem(format.getFormatId(),format.getFormatName(),format);
                list.add(item);
            }
            TextView tvBottom=getView().findViewById(R.id.tv_format_bottom);
            OnSelectedListener listener = this;
            tvBottom.setOnClickListener(v ->{
                new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(
                        new ListPopup(_mActivity,"底部版式", PopupType.SELLER_FORMAT_BOTTOM, list, formatBottomIndex, this)
                ).show();
            });
        }
        EasyJSONArray formatTopList = data.getArray("formatTopList");


        if (formatTopList != null) {
            List<ListPopupItem> list = new ArrayList<>();
            list.add(new ListPopupItem(-1, "取消選擇", null));
            for (Object o : formatTopList) {
                Format format = Format.parse(((EasyJSONObject) o));
                ListPopupItem item = new ListPopupItem(format.getFormatId(),format.getFormatName(),format);

                list.add(item);
            }
            TextView tvFormatTop=getView().findViewById(R.id.tv_format_top);
            OnSelectedListener listener = this;
            tvFormatTop.setOnClickListener(v ->{
                new XPopup.Builder(_mActivity).moveUpToKeyboard(false).asCustom(
                        new ListPopup(_mActivity, "頂部版式",PopupType.SELLER_FORMAT_TOP, list,formatTopIndex,listener)
                ).show();
            });
        }
    }
    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.STORE_LABEL) {
            selectCategoryList = (List<Category>) extra;
            Category categoryLast = new Category();
            StringBuilder selectCategoryName = new StringBuilder();
            for (Category category : selectCategoryList) {
                categoryLast = category;
                selectCategoryName.append(category.getCategoryName()).append(" -- ");
            }
            if (!StringUtil.isEmpty(selectCategoryName.toString())) {
                selectCategoryName.delete(selectCategoryName.length() - 4, selectCategoryName.length() - 1);
                    ((TextView) getView().findViewById(R.id.tv_select_store_category)).setText(selectCategoryName.toString());
                    storeLabelId = categoryLast.getCategoryId();
                    EasyJSONArray array = new EasyJSONArray();
                    for (Category category : selectCategoryList) {
                        array.append(category.getCategoryId());
                    }
                    storeLabelIdList = array;
            }

        }
        if (type == PopupType.SELLER_FORMAT_TOP) {
            TextView tvFormatTop = getView().findViewById(R.id.tv_format_top);

            if (extra == null||id<0) {
                formatTop = 0;
                formatTopIndex = 0;
                tvFormatTop.setText("請選擇");

            } else {
                tvFormatTop.setText(((Format) extra).getFormatName());

                formatTopIndex = id;
                formatTop = ((Format) extra).getFormatId();
            }
        } else if (type == PopupType.SELLER_FORMAT_BOTTOM) {
            TextView tvFormatBottom =getView().findViewById(R.id.tv_format_bottom);
            if (extra == null) {
                formatBottom = 0;
                formatBottomIndex = 0;
                tvFormatBottom.setText("請選擇");

            } else {
                formatBottomIndex = id;
                formatBottom = ((Format) extra).getFormatId();
                tvFormatBottom.setText(((Format) extra).getFormatName());

            }
        }
    }
}
