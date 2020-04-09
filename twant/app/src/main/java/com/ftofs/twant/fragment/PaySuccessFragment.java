package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.PaySuccessItemAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.PaySuccessStoreInfoItem;
import com.ftofs.twant.entity.PaySuccessSummaryItem;
import com.ftofs.twant.entity.StoreMapInfo;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.AmapPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;


/**
 * 訂單支付成功Fragment
 * @author zwm
 */
public class PaySuccessFragment extends BaseFragment implements View.OnClickListener {
    int payId;

    RecyclerView rvList;
    PaySuccessItemAdapter adapter;
    List<MultiItemEntity> dataList = new ArrayList<>();
    public static PaySuccessFragment newInstance(int payId) {
        Bundle args = new Bundle();

        args.putInt("payId", payId);
        PaySuccessFragment fragment = new PaySuccessFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_success, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();

        payId = args.getInt("payId");

        Util.setOnClickListener(view, R.id.btn_goto_home, this);
        Util.setOnClickListener(view, R.id.btn_view_order, this);

        rvList = view.findViewById(R.id.rv_list);
        adapter = new PaySuccessItemAdapter(_mActivity, dataList);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();

                MultiItemEntity itemEntity = dataList.get(position);
                int itemViewType = itemEntity.getItemType();

                if (itemViewType == Constant.ITEM_VIEW_TYPE_SUMMARY) {
                    if (id == R.id.btn_view_order) {
                        // 看下一層的Fragment是什么，如果是訂單列表Fragment，則只需要pop，不需要start
                        Fragment fragment = Util.getFragmentByLayer(_mActivity, 2);
                        if (fragment != null) {
                            boolean needStartFragment = !OrderFragment.class.equals(fragment.getClass());
                            SLog.info("needStartFragment[%s]", needStartFragment);
                            popTo(OrderFragment.class,false);
                            // 轉去訂單列表，已跟進能確認過
                            if (!needStartFragment) {
                                hideSoftInputPop();
                                pop();
                            }
                            Util.startFragment(OrderFragment.newInstance(Constant.ORDER_STATUS_TO_BE_SHIPPED, OrderFragment.USAGE_LIST));

                        } else {
                            hideSoftInputPop();
                            Util.startFragment(OrderFragment.newInstance(Constant.ORDER_STATUS_TO_BE_SHIPPED, OrderFragment.USAGE_LIST));

                        }


                    } else if (id == R.id.btn_goto_home) {
                        MainFragment.getInstance().showHideFragment(MainFragment.HOME_FRAGMENT);
                        popTo(MainFragment.class, false);
                    }
                } else if (itemViewType == Constant.ITEM_VIEW_TYPE_COMMON) {
                    PaySuccessStoreInfoItem paySuccessStoreInfoItem = (PaySuccessStoreInfoItem) itemEntity;

                    if (id == R.id.btn_shop_map) {
                        StoreMapInfo storeMapInfo = new StoreMapInfo(paySuccessStoreInfoItem.storeLongitude, paySuccessStoreInfoItem.storeLatitude, paySuccessStoreInfoItem.storeDistance, 0, 0,
                                paySuccessStoreInfoItem.storeName, paySuccessStoreInfoItem.storeAddress, paySuccessStoreInfoItem.storePhone,paySuccessStoreInfoItem.storeBusInfo);
                        new XPopup.Builder(_mActivity)
                                // 如果不加这个，评论弹窗会移动到软键盘上面
                                .moveUpToKeyboard(false)
                                .asCustom(new AmapPopup(_mActivity, storeMapInfo))
                                .show();
                    }
                }
            }
        });
        rvList.setAdapter(adapter);

        loadData();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }

    private void loadData() {
        //                 "token", "542ebd30f1724316b0e0121013906959",
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "payId", payId
        );
        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_MPAY, params, new UICallback() {
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

                    PaySuccessSummaryItem paySuccessSummaryItem = new PaySuccessSummaryItem();
                    int couponPrice = 0;
                    SLog.info("HERE");
                    if (responseObj.exists("datas.couponPrice") && !Util.isJsonNull(responseObj.get("datas.couponPrice"))) {
                        couponPrice = (int) responseObj.getDouble("datas.couponPrice");
                        SLog.info("HERE");
                        if (couponPrice > 0) {
                            paySuccessSummaryItem.isMPayActivity = true;
                            paySuccessSummaryItem.mpayActivityDesc = String.format("獲得%s元優惠券一張", StringUtil.formatPrice(_mActivity, couponPrice, 0));
                            SLog.info("HERE");
                        }
                    }

                    paySuccessSummaryItem.itemViewType = Constant.ITEM_VIEW_TYPE_SUMMARY;
                    paySuccessSummaryItem.couponPrice = couponPrice;
                    dataList.add(paySuccessSummaryItem);

                    loadStoreData();

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    /**
     * 支付成功，加載店鋪列表頁
     */
    private void loadStoreData() {
        //                 "token", "542ebd30f1724316b0e0121013906959",
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "payId", payId
        );
        SLog.info("params[%s]", params);
        Api.postUI(Api.PATH_BUY_ORDERS, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    // responseStr = "{\"code\":200,\"datas\":{\"storeVoList\":[{\"storeId\":85,\"storeName\":\"內測門店_測試改名\",\"classId\":0,\"className\":null,\"storeAddress\":null,\"storeLogo\":\"\",\"storeAvatar\":\"image/25/2d/252d317015c4c7d8dba4c132d81ed743.png\",\"storeAvatarUrl\":\"\",\"storeZy\":\"\",\"isOwnShop\":0,\"state\":1,\"storeWorkingtime\":\"\",\"companyName\":\"\",\"companyArea\":\"\",\"companyAreaId\":0,\"storeFigureImage\":\"image/a7/b2/a7b2e2d3bcb81d875c5bc7eaee6e18c4.png\",\"storeFigureImageUrl\":null,\"storePresales\":\"\",\"storeAftersales\":\"\",\"chainAreaInfo\":\"北京 北京市 西城区\",\"chainAddress\":\"枯井\",\"chainPhone\":\"\",\"chainOpeningTime\":\"\",\"allVisitCount\":null,\"collectCount\":null,\"likeCount\":null,\"isOpen\":1,\"videoUrl\":\"\",\"storePresalesList\":null,\"shopDay\":null,\"storeSignature\":null,\"storeIntroduce\":null,\"isFavorite\":0,\"isLike\":0,\"lng\":113.55,\"lngString\":\"\",\"lat\":22.2,\"latString\":\"\",\"storeFigureImageInner\":null,\"storeFigureImageOuter\":null,\"distance\":\"0\",\"storeServiceStaffVoList\":null,\"storePreServiceStaffVoList\":null,\"storeAfterServiceStaffVoList\":null,\"storeReply\":0,\"sellerName\":null,\"contactsPhone\":\"65478912\",\"weekDayStart\":\"星期日\",\"weekDayStartTime\":\"10:30\",\"weekDayEnd\":\"星期二\",\"weekDayEndTime\":\"18:30\",\"restDayStart\":\"星期三\",\"restDayStartTime\":\"09:30\",\"restDayEnd\":\"星期三\",\"restDayEndTime\":\"21:30\",\"storeLogoUrl\":null,\"goodsCommonCount\":0,\"isForbidShow\":0,\"startBusiness\":null,\"expireTime\":null,\"chainTrafficLine\":\"10B\",\"rateExpireState\":0,\"ordersGoods\":{\"ordersGoodsId\":6165,\"ordersId\":5338,\"goodsId\":4235,\"commonId\":3232,\"goodsName\":\"233\",\"basePrice\":0.01,\"goodsPrice\":0.01,\"goodsPayAmount\":0.01,\"buyNum\":1,\"goodsImage\":\"image/19/e1/19e1b342d46e97749e4c8ed34085b867.jpg\",\"goodsType\":0,\"storeId\":85,\"memberId\":119,\"commissionRate\":10,\"categoryId\":484,\"categoryId1\":470,\"categoryId2\":471,\"categoryId3\":484,\"goodsFullSpecs\":\"顔色：白色ddddddd；鞋碼：26；尺碼：XXS\",\"unitName\":\"盒\",\"promotionTitle\":\"\",\"isGift\":0,\"bundlingId\":0,\"distributionOrdersId\":0,\"refundPrice\":0,\"goodsSerial\":\"\",\"trysApplyId\":0,\"commissionAmount\":0,\"complainId\":0,\"contract\":\"[]\",\"seckillGoodsId\":0,\"bargainOpenId\":0,\"taxTate\":0,\"taxAmount\":0,\"joinBigSale\":1,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/19/e1/19e1b342d46e97749e4c8ed34085b867.jpg\"},\"storeAlias\":\"top1\",\"takeCode\":905568},{\"storeId\":305,\"storeName\":\"憂鬱臘味店\",\"classId\":0,\"className\":null,\"storeAddress\":null,\"storeLogo\":\"\",\"storeAvatar\":\"\",\"storeAvatarUrl\":\"\",\"storeZy\":\"\",\"isOwnShop\":0,\"state\":1,\"storeWorkingtime\":\"\",\"companyName\":\"\",\"companyArea\":\"\",\"companyAreaId\":0,\"storeFigureImage\":null,\"storeFigureImageUrl\":null,\"storePresales\":\"\",\"storeAftersales\":\"\",\"chainAreaInfo\":\"澳門 路氹\",\"chainAddress\":\"第三方而無法\",\"chainPhone\":\"\",\"chainOpeningTime\":\"\",\"allVisitCount\":null,\"collectCount\":null,\"likeCount\":null,\"isOpen\":0,\"videoUrl\":\"\",\"storePresalesList\":null,\"shopDay\":null,\"storeSignature\":null,\"storeIntroduce\":null,\"isFavorite\":0,\"isLike\":0,\"lng\":113.55,\"lngString\":\"\",\"lat\":22.2,\"latString\":\"\",\"storeFigureImageInner\":null,\"storeFigureImageOuter\":null,\"distance\":\"0\",\"storeServiceStaffVoList\":null,\"storePreServiceStaffVoList\":null,\"storeAfterServiceStaffVoList\":null,\"storeReply\":0,\"sellerName\":null,\"contactsPhone\":\"18578276310\",\"weekDayStart\":\"\",\"weekDayStartTime\":null,\"weekDayEnd\":\"\",\"weekDayEndTime\":null,\"restDayStart\":\"\",\"restDayStartTime\":null,\"restDayEnd\":\"\",\"restDayEndTime\":null,\"storeLogoUrl\":null,\"goodsCommonCount\":0,\"isForbidShow\":0,\"startBusiness\":null,\"expireTime\":null,\"chainTrafficLine\":null,\"rateExpireState\":0,\"ordersGoods\":{\"ordersGoodsId\":6164,\"ordersId\":5337,\"goodsId\":4981,\"commonId\":3694,\"goodsName\":\"泡麵叛侶\",\"basePrice\":12,\"goodsPrice\":12,\"goodsPayAmount\":12,\"buyNum\":1,\"goodsImage\":\"image/07/ad/07ad90da8fd8a6510ebdb42e7980accd.jpg\",\"goodsType\":0,\"storeId\":305,\"memberId\":119,\"commissionRate\":15,\"categoryId\":645,\"categoryId1\":593,\"categoryId2\":600,\"categoryId3\":645,\"goodsFullSpecs\":\"\",\"unitName\":\"瓶\",\"promotionTitle\":\"\",\"isGift\":0,\"bundlingId\":0,\"distributionOrdersId\":0,\"refundPrice\":0,\"goodsSerial\":\"\",\"trysApplyId\":0,\"commissionAmount\":1.8,\"complainId\":0,\"contract\":\"[]\",\"seckillGoodsId\":0,\"bargainOpenId\":0,\"taxTate\":0,\"taxAmount\":0,\"joinBigSale\":1,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/07/ad/07ad90da8fd8a6510ebdb42e7980accd.jpg\"},\"storeAlias\":null,\"takeCode\":221395}]}}";
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }


                    int storeCouponCount = 0; // 收到的店铺券数量
                    EasyJSONArray storeVoList = responseObj.getSafeArray("datas.storeVoList");
                    for (Object object : storeVoList) {
                        EasyJSONObject storeVo = (EasyJSONObject) object;

                        PaySuccessStoreInfoItem storeInfoItem = new PaySuccessStoreInfoItem();
                        storeInfoItem.itemViewType = Constant.ITEM_VIEW_TYPE_COMMON;

                        storeInfoItem.storeAvatar = storeVo.getSafeString("storeAvatar");
                        storeInfoItem.storeName = storeVo.getSafeString("storeName");
                        storeInfoItem.storeBizStatus = storeVo.getInt("isOpen");

                        EasyJSONObject ordersGoods = storeVo.getSafeObject("ordersGoods");
                        storeInfoItem.goodsImageUrl = ordersGoods.getSafeString("goodsImage");
                        storeInfoItem.goodsName = ordersGoods.getSafeString("goodsName");
                        storeInfoItem.goodsSpec = ordersGoods.getSafeString("goodsFullSpecs");
                        storeInfoItem.selfTakeCode = String.valueOf(storeVo.getInt("takeCode"));

                        storeInfoItem.storePhone = storeVo.getSafeString("contactsPhone");
                        storeInfoItem.storeBusInfo = storeVo.getSafeString("chainTrafficLine");
                        storeInfoItem.storeAddress = storeVo.getSafeString("chainAreaInfo") + " " + storeVo.getSafeString("chainAddress");
                        storeInfoItem.storeLatitude = storeVo.getDouble("lat");
                        storeInfoItem.storeLongitude = storeVo.getDouble("lng");
                        storeInfoItem.storeDistance = Double.valueOf(storeVo.get("distance").toString());
                        storeInfoItem.businessTimeWorkingDay = String.format("%s 至 %s %s - %s",
                                storeVo.getSafeString("weekDayStart"), storeVo.getSafeString("weekDayEnd"),
                                storeVo.getSafeString("weekDayStartTime"), storeVo.getSafeString("weekDayEndTime"));
                        storeInfoItem.businessTimeWeekend = String.format("%s 至 %s %s - %s",
                                storeVo.getSafeString("restDayStart"), storeVo.getSafeString("restDayEnd"),
                                storeVo.getSafeString("restDayStartTime"), storeVo.getSafeString("restDayEndTime"));

                        storeInfoItem.transportInstruction = storeVo.getSafeString("chainTrafficLine");

                        if (storeVo.exists("conform.templatePrice")) {
                            storeInfoItem.templatePrice =  storeVo.getDouble("conform.templatePrice");
                            SLog.info("storeInfoItem.templatePrice[%s]", storeInfoItem.templatePrice);
                            // storeInfoItem.templatePrice = 999;

                            if (storeInfoItem.templatePrice > 0) {
                                storeCouponCount++;
                            }
                        }

                        dataList.add(storeInfoItem);
                    }

                    SLog.info("storeCouponCount[%d]", storeCouponCount);
                    if (dataList.size() > 0) {
                        MultiItemEntity itemEntity = dataList.get(0);
                        if (itemEntity instanceof PaySuccessSummaryItem) {
                            PaySuccessSummaryItem summaryItem = (PaySuccessSummaryItem) itemEntity;
                            summaryItem.storeCouponCount = storeCouponCount;
                        }
                    }

                    adapter.setNewData(dataList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();


    }
}

