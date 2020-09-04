package com.ftofs.twant.fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SecKillGoodsListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.entity.Goods;
import com.ftofs.twant.entity.SecKillGoodsListItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.tangram.SloganView;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BackgroundDrawable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 秒殺商品列表Fragment
 * @author zwm
 */
public class SecKillGoodsListFragment extends BaseFragment implements BaseQuickAdapter.RequestLoadMoreListener {
    int scheduleId;

    // 當前要加載第幾頁(從1開始）
    int currPage = 0;
    boolean hasMore;

    SecKillGoodsListAdapter adapter;
    List<SecKillGoodsListItem> goodsItemList = new ArrayList<>();

    public static SecKillGoodsListFragment newInstance(int scheduleId) {
        Bundle args = new Bundle();

        SecKillGoodsListFragment fragment = new SecKillGoodsListFragment();
        fragment.setArguments(args);
        fragment.scheduleId = scheduleId;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sec_kill_goods_list, container, false);
        SLog.info("onCreateView[%d]", scheduleId);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SLog.info("onViewCreated[%d]", scheduleId);

        RecyclerView rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter = new SecKillGoodsListAdapter(_mActivity, R.layout.sec_kill_list_normal_item, goodsItemList);
//        adapter.setMLoadMoreModule$com_github_CymChad_brvah(true);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                SecKillGoodsListItem item = goodsItemList.get(position);
                SLog.info("seckillCommonId[%d], seckillGoodsId[%d]", item.seckillCommonId, item.seckillGoodsId);
                Util.startFragment(GoodsDetailFragment.newInstance(item.commonId, item.goodsId));
                // Util.startFragment(GoodsDetailFragment.newInstance(item.seckillCommonId, item.seckillGoodsId));
            }
        });

        /*
        adapter.setChildListRes(R.id.goods_image,R.id.tv_versatile_text);
        adapter.setOnItemChildClickListener((a,v,p)->{
            int id = v.getId();
            if (id == R.id.goods_image) {
                SLog.info("5555");

                ToastUtil.success(_mActivity,"sss");
            }
            if (id == R.id.tv_versatile_text) {
                SLog.info("5555");

                ToastUtil.success(_mActivity,"s00000s");
            }
            SLog.info("s211");
        });
         */
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(this, rvList);
        rvList.setAdapter(adapter);

        currPage = 0;  // ViewPager銷毀Fragment，再重建時，會調用onViewCreated()方法
        loadData(currPage + 1);
    }

    private void loadData(int page) {
        EasyJSONObject params = EasyJSONObject.generate(
                "scheduleId", scheduleId,
                "page", page
        );

        String url = Api.PATH_SEC_KILL_GOODS_LIST;
        SLog.info("url[%s], params[%s]", url, params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
                adapter.loadMoreFail();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                if (Config.USE_DEVELOPER_TEST_DATA) {
                    // responseStr = "{\"code\":200,\"datas\":{\"pageEntity\":{\"hasMore\":false,\"totalPage\":1,\"total\":2},\"seckillGoodsCommonList\":[{\"seckillCommonId\":143,\"seckillGoodsId\":0,\"goodsName\":\"Y&I's\",\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"goodsId\":0,\"commonId\":3728,\"goodsPrice\":11.90,\"seckillGoodsPrice\":1.00,\"goodsStorage\":1,\"limitAmount\":0,\"scheduleId\":2749,\"startTime\":\"2020-09-04 18:00:00\",\"endTime\":\"2020-09-05 18:00:00\",\"scheduleState\":0,\"verifyRemark\":null,\"scheduleStateText\":\"即將開場\",\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"scheduleName\":null,\"goodsSaleNum\":0,\"salesPercentage\":0,\"isNotice\":0,\"goodsCountry\":16,\"nationalFlagImage\":null,\"storeAvatar\":\"image/01/88/018848f237e5e254f0d209fba2733b9a.jpg\",\"membersAvatars\":[],\"goodsClick\":25261,\"storeId\":303,\"goodsImageList\":null,\"goodsFreight\":0.00,\"freightTemplateId\":0,\"storeServiceStaffVoList\":null},{\"seckillCommonId\":140,\"seckillGoodsId\":0,\"goodsName\":\"测试商品\",\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/81/93/8193edf58a1f1bb656b8836a3f1127ef.jpg\",\"goodsId\":0,\"commonId\":4363,\"goodsPrice\":15.00,\"seckillGoodsPrice\":0.01,\"goodsStorage\":1,\"limitAmount\":0,\"scheduleId\":2749,\"startTime\":\"2020-09-04 18:00:00\",\"endTime\":\"2020-09-05 18:00:00\",\"scheduleState\":0,\"verifyRemark\":null,\"scheduleStateText\":\"即將開場\",\"storeName\":\"科研室\",\"scheduleName\":null,\"goodsSaleNum\":0,\"salesPercentage\":0,\"isNotice\":0,\"goodsCountry\":20,\"nationalFlagImage\":null,\"storeAvatar\":\"image/3d/bb/3dbb082f98537a7b5cccba729db31aad.png\",\"membersAvatars\":[\"image/50/14/50146f14535fc62cba145cc92de25218.jpg\",\"image/25/2d/252d317015c4c7d8dba4c132d81ed743.png\",\"image/9d/6c/9d6c65390dbea105747a17ae02f7c6d3.png\"],\"goodsClick\":13216,\"storeId\":281,\"goodsImageList\":null,\"goodsFreight\":10.00,\"freightTemplateId\":0,\"storeServiceStaffVoList\":null}]}}";
                    // responseStr = "{\"code\":200,\"datas\":{\"pageEntity\":{\"hasMore\":false,\"totalPage\":1,\"total\":2},\"seckillGoodsCommonList\":[{\"seckillCommonId\":143,\"seckillGoodsId\":0,\"goodsName\":\"Y&I's\",\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"goodsId\":0,\"commonId\":3728,\"goodsPrice\":11.90,\"seckillGoodsPrice\":1.00,\"goodsStorage\":1,\"limitAmount\":0,\"scheduleId\":2749,\"startTime\":\"2020-09-04 18:00:00\",\"endTime\":\"2020-09-05 18:00:00\",\"scheduleState\":0,\"verifyRemark\":null,\"scheduleStateText\":\"即將開場\",\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"scheduleName\":null,\"goodsSaleNum\":0,\"salesPercentage\":0,\"isNotice\":0,\"goodsCountry\":16,\"nationalFlagImage\":null,\"storeAvatar\":\"image/01/88/018848f237e5e254f0d209fba2733b9a.jpg\",\"membersAvatars\":[],\"goodsClick\":25261,\"storeId\":303,\"goodsImageList\":null,\"goodsFreight\":0.00,\"freightTemplateId\":0,\"storeServiceStaffVoList\":null},{\"seckillCommonId\":140,\"seckillGoodsId\":0,\"goodsName\":\"测试商品\",\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/81/93/8193edf58a1f1bb656b8836a3f1127ef.jpg\",\"goodsId\":0,\"commonId\":4363,\"goodsPrice\":15.00,\"seckillGoodsPrice\":0.01,\"goodsStorage\":125,\"limitAmount\":0,\"scheduleId\":2749,\"startTime\":\"2020-09-04 18:00:00\",\"endTime\":\"2020-09-05 18:00:00\",\"scheduleState\":0,\"verifyRemark\":null,\"scheduleStateText\":\"即將開場\",\"storeName\":\"科研室\",\"scheduleName\":null,\"goodsSaleNum\":0,\"salesPercentage\":80,\"isNotice\":0,\"goodsCountry\":20,\"nationalFlagImage\":null,\"storeAvatar\":\"image/3d/bb/3dbb082f98537a7b5cccba729db31aad.png\",\"membersAvatars\":[\"image/50/14/50146f14535fc62cba145cc92de25218.jpg\",\"image/25/2d/252d317015c4c7d8dba4c132d81ed743.png\"],\"goodsClick\":13216,\"storeId\":281,\"goodsImageList\":null,\"goodsFreight\":10.00,\"freightTemplateId\":0,\"storeServiceStaffVoList\":null}]}}";
                    // responseStr = "{\"code\":200,\"datas\":{\"pageEntity\":{\"hasMore\":false,\"totalPage\":1,\"total\":2},\"seckillGoodsCommonList\":[{\"seckillCommonId\":143,\"seckillGoodsId\":0,\"goodsName\":\"Y&I's\",\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/4a/23/4a23ac7fc80daf87e9b0e86aa8c467d2.jpg\",\"goodsId\":0,\"commonId\":3728,\"goodsPrice\":11.90,\"seckillGoodsPrice\":1.00,\"goodsStorage\":1,\"limitAmount\":0,\"scheduleId\":2749,\"startTime\":\"2020-09-04 18:00:00\",\"endTime\":\"2020-09-05 18:00:00\",\"scheduleState\":0,\"verifyRemark\":null,\"scheduleStateText\":\"即將開場\",\"storeName\":\"迪高 (DE'COR) 專業美髮用品\",\"scheduleName\":null,\"goodsSaleNum\":0,\"salesPercentage\":0,\"isNotice\":0,\"goodsCountry\":16,\"nationalFlagImage\":null,\"storeAvatar\":\"image/01/88/018848f237e5e254f0d209fba2733b9a.jpg\",\"membersAvatars\":[],\"goodsClick\":25261,\"storeId\":303,\"goodsImageList\":null,\"goodsFreight\":0.00,\"freightTemplateId\":0,\"storeServiceStaffVoList\":null},{\"seckillCommonId\":140,\"seckillGoodsId\":0,\"goodsName\":\"测试商品\",\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/81/93/8193edf58a1f1bb656b8836a3f1127ef.jpg\",\"goodsId\":0,\"commonId\":4363,\"goodsPrice\":15.00,\"seckillGoodsPrice\":0.01,\"goodsStorage\":125,\"limitAmount\":0,\"scheduleId\":2749,\"startTime\":\"2020-09-04 18:00:00\",\"endTime\":\"2020-09-05 18:00:00\",\"scheduleState\":0,\"verifyRemark\":null,\"scheduleStateText\":\"即將開場\",\"storeName\":\"科研室\",\"scheduleName\":null,\"goodsSaleNum\":0,\"salesPercentage\":80,\"isNotice\":0,\"goodsCountry\":20,\"nationalFlagImage\":null,\"storeAvatar\":\"image/3d/bb/3dbb082f98537a7b5cccba729db31aad.png\",\"membersAvatars\":[\"image/50/14/50146f14535fc62cba145cc92de25218.jpg\"],\"goodsClick\":13216,\"storeId\":281,\"goodsImageList\":null,\"goodsFreight\":10.00,\"freightTemplateId\":0,\"storeServiceStaffVoList\":null}]}}";
                }

                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                    adapter.loadMoreFail();
                    return;
                }

                if (page == 1) {
                    goodsItemList.clear();
                }

                try {
                    hasMore = responseObj.getBoolean("datas.pageEntity.hasMore");
                    SLog.info("hasMore[%s]", hasMore);
                    if (!hasMore) {
                        adapter.loadMoreEnd();
                        adapter.setEnableLoadMore(false);
                    }

                    EasyJSONArray goodsCommonList = responseObj.getSafeArray("datas.seckillGoodsCommonList");
                    for (Object object : goodsCommonList) {
                        EasyJSONObject goodsCommon = (EasyJSONObject) object;

                        SecKillGoodsListItem item = new SecKillGoodsListItem();
                        item.seckillCommonId = goodsCommon.optInt("seckillCommonId");
                        item.seckillGoodsId = goodsCommon.optInt("seckillGoodsId");
                        item.commonId = goodsCommon.optInt("commonId");
                        item.goodsId = goodsCommon.optInt("goodsId");
                        item.goodsName = goodsCommon.optString("goodsName");
                        item.imageSrc = goodsCommon.optString("imageSrc");
                        item.scheduleState = goodsCommon.optInt("scheduleState");
                        item.scheduleStateText = goodsCommon.optString("scheduleStateText");
                        item.originalPrice = goodsCommon.optDouble("goodsPrice");
                        item.secKillPrice = goodsCommon.optDouble("seckillGoodsPrice");
                        item.goodsStorage = goodsCommon.optInt("goodsStorage");
                        item.salesPercentage = goodsCommon.optInt("salesPercentage");

                        // 取秒殺榜的前3個頭像
                        int counter = 0;
                        EasyJSONArray membersAvatars = goodsCommon.getSafeArray("membersAvatars");
                        for (Object object2 : membersAvatars) {
                            String avatarUrl = (String) object2;
                            item.membersAvatars.add(avatarUrl);
                            counter++;
                            if (counter >= 3) {
                                break;
                            }
                        }

                        goodsItemList.add(item);
                    }

                    adapter.loadMoreComplete();
                    adapter.setNewData(goodsItemList);
                    currPage++;
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onLoadMoreRequested() {
        SLog.info("onLoadMoreRequested");

        if (!hasMore) {
            adapter.setEnableLoadMore(false);
            return;
        }
        loadData(currPage + 1);
    }
}



