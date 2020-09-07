package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PostGoodsAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SimpleTabManager;
import com.lxj.xpopup.core.BasePopupView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 發表想要時，添加想要帖的產品
 * @author zwm
 */
public class AddPostGoodsFragment extends BaseFragment implements View.OnClickListener, BaseQuickAdapter.OnItemClickListener {
    private static final int DATA_TYPE_CART = 0; // 購物袋
    private static final int DATA_TYPE_BUY = 1;  // 最近購買
    private static final int DATA_TYPE_VISIT = 2;  // 瀏覽記憶

    private int currDataType = DATA_TYPE_CART;
    private boolean[] dataLoaded = new boolean[] {false, false, false};

    private PostGoodsAdapter[] postGoodsAdapterArr = new PostGoodsAdapter[3];

    private List<List<Goods>> dataList = new ArrayList<>();


    RecyclerView rvList;

    public static AddPostGoodsFragment newInstance() {
        Bundle args = new Bundle();

        AddPostGoodsFragment fragment = new AddPostGoodsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_post_goods, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化
        for (int i = 0; i < dataLoaded.length; i++) {
            List<Goods> data = new ArrayList<>();
            dataList.add(data);

            postGoodsAdapterArr[i] = new PostGoodsAdapter(R.layout.post_goods_item, data);
            postGoodsAdapterArr[i].setOnItemClickListener(this);
        }

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_search, this);

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new GridLayoutManager(_mActivity, 3));


        SimpleTabManager tabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }

                if (id == R.id.tab_cart) { // 購物車
                    SLog.info("購物車");
                    setData(DATA_TYPE_CART);
                } else if (id == R.id.tab_buy) { // 最近購買
                    SLog.info("最近購買");
                    setData(DATA_TYPE_BUY);
                } else if (id == R.id.tab_visit) { // 瀏覽記憶
                    SLog.info("瀏覽記憶");
                    setData(DATA_TYPE_VISIT);
                }
            }
        };

        tabManager.add(view.findViewById(R.id.tab_cart));
        tabManager.add(view.findViewById(R.id.tab_buy));
        tabManager.add(view.findViewById(R.id.tab_visit));


        setData(DATA_TYPE_CART);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_search) {
            Util.startFragment(CategoryFragment.newInstance(SearchType.GOODS, getClass().getName()));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }


    private void setData(int dataType) {
        currDataType = dataType;
        if (dataLoaded[dataType]) {
            rvList.setAdapter(postGoodsAdapterArr[dataType]);
            return;
        }


        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        String token = User.getToken();
        String url;

        if (dataType == DATA_TYPE_CART) {
            url = Api.PATH_POST_GOODS_CART;
        } else if (dataType == DATA_TYPE_BUY) {
            url = Api.PATH_POST_GOODS_BUY;
        } else {
            url = Api.PATH_POST_GOODS_VISIT;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token
        );

        SLog.info("url[%s], params[%s]", url, params);
        Api.getUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();

                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    String path;
                    if (dataType == DATA_TYPE_CART) {
                        path = "datas.cartItemVoList";
                    } else if (dataType == DATA_TYPE_BUY) {
                        path = "datas.ordersGoodsList";
                    } else {
                        path = "datas.goodsBrowseList";
                    }


                    List<Goods> goodsList = dataList.get(dataType);
                    EasyJSONArray goodsArray = responseObj.getSafeArray(path);
                    for (Object object : goodsArray) {
                        Goods goods = new Goods();
                        EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                        goods.id = easyJSONObject.getInt("commonId");
                        goods.name = easyJSONObject.getSafeString("goodsName");
                        if (easyJSONObject.exists("batchPrice0")) {
                            goods.price = easyJSONObject.getDouble("batchPrice0");
                        }
                        if (dataType == DATA_TYPE_CART) {
                            goods.imageUrl = easyJSONObject.getSafeString("imageName");
                        } else {
                            goods.imageUrl = easyJSONObject.getSafeString("goodsImage");
                        }
                        goods.goodsModal = StringUtil.safeModel(easyJSONObject);

                        goodsList.add(goods);
                    }

                    dataLoaded[dataType] = true;

                    postGoodsAdapterArr[dataType].setNewData(goodsList);
                    rvList.setAdapter(postGoodsAdapterArr[dataType]);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        SLog.info("onItemClick, position[%d]", position);

        Goods goods = dataList.get(currDataType).get(position);

        Bundle bundle = new Bundle();
        bundle.putString("from", AddAddressFragment.class.getName());
        bundle.putInt("commonId", goods.id);
        bundle.putString("goodsName", goods.name);
        bundle.putDouble("price", goods.price);
        bundle.putString("imageUrl", goods.imageUrl);
        bundle.putInt("goodsModal", goods.goodsModal);
        setFragmentResult(RESULT_OK, bundle);

        hideSoftInputPop();
    }
}
