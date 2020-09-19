package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.ConfirmOrderGiftListAdapter;
import com.ftofs.twant.adapter.ViewGroupAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.CrossBorderStoreInfo;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.GiftItem;
import com.ftofs.twant.entity.cart.BaseStatus;
import com.ftofs.twant.entity.cart.SpuStatus;
import com.ftofs.twant.entity.cart.StoreStatus;
import com.ftofs.twant.entity.cart.TotalStatus;
import com.ftofs.twant.interfaces.OnConfirmCallback;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.util.LogUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.CartAdjustButton;
import com.ftofs.twant.widget.CartCrossBorderPopup;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.TwConfirmPopup;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

import static com.ftofs.twant.entity.cart.BaseStatus.PHRASE_BUBBLE;


/**
 * 購物袋
 * @author zwm
 */
public class CartFragment extends MainBaseFragment implements View.OnClickListener, OnSelectedListener {
    TextView tvFragmentTitle;
    LinearLayout cartStoreItemContainer;
    String textSettlement;
    TotalStatus totalStatus = new TotalStatus();

    LinearLayout llTotalOperationContainer;

    TextView btnDelete;
    LinearLayout llViewModeButtonGroup;
    TextView btnEdit;
    TextView btnSettlement;
    TextView tvTotalPrice;

    ScaledButton btnBack;

    Map<Integer, CrossBorderStoreInfo> crossBorderStoreMap = new LinkedHashMap<>();

    int totalCartItemCount; // 購物袋中的項數，用于顯示在主頁的底部工具欄中

    int mode = Constant.MODE_VIEW;
    boolean needReloadData = true;

    /**
     * 是否獨立的Fragment，還是依附于MainFragment
     */
    boolean isStandalone;

    ScrollView svItemContainer;
    int svItemContainerHeight = -1;

    public static CartFragment newInstance(boolean isStandalone) {
        Bundle args = new Bundle();

        args.putBoolean("isStandalone", isStandalone);
        CartFragment fragment = new CartFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        Bundle args = getArguments();
        isStandalone = args.getBoolean("isStandalone");

        textSettlement = getResources().getString(R.string.text_settlement);

        ScaledButton btnSelectAll = view.findViewById(R.id.btn_select_all);
        btnSelectAll.setTag(totalStatus);
        setCheckButtonOnClickListener(btnSelectAll);
        totalStatus.setRadio(btnSelectAll);

        btnEdit = view.findViewById(R.id.btn_edit);
        btnEdit.setOnClickListener(this);
        tvFragmentTitle = view.findViewById(R.id.tv_fragment_title);
        cartStoreItemContainer = view.findViewById(R.id.ll_cart_store_item_container);

        btnDelete = view.findViewById(R.id.btn_delete);
        btnDelete.setOnClickListener(this);
        llViewModeButtonGroup = view.findViewById(R.id.ll_view_mode_button_group);

        btnSettlement = view.findViewById(R.id.btn_settlement);
        btnSettlement.setOnClickListener(this);
        tvTotalPrice = view.findViewById(R.id.tv_total_price);

        svItemContainer = view.findViewById(R.id.sv_item_container);

        llTotalOperationContainer = view.findViewById(R.id.ll_total_operation_container);

        btnBack = view.findViewById(R.id.btn_back);
        if (isStandalone) {
            btnBack.setVisibility(View.VISIBLE);
            btnBack.setOnClickListener(this);

            // 調整llTotalOperationContainer的高度為原來一半
            ConstraintLayout.LayoutParams layoutParams = (ConstraintLayout.LayoutParams) llTotalOperationContainer.getLayoutParams();
            SLog.info("layoutParams: %s", layoutParams);
            layoutParams.height = Util.dip2px(_mActivity, 49);
            llTotalOperationContainer.setLayoutParams(layoutParams);
        }
    }

    private void displayCartItemCount(int totalUnreadCount) {
        MainFragment mainFragment = MainFragment.getInstance();
        if (mainFragment != null) {
            mainFragment.setCartItemCount(totalCartItemCount);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("onSupportVisible");

        updateMainSelectedFragment(MainFragment.CART_FRAGMENT);

        if (svItemContainerHeight == -1) {
            svItemContainerHeight = svItemContainer.getHeight();
        }

        int userId = User.getUserId();
        if (userId < 1) { // 用戶未登錄，顯示登錄頁面
            Util.showLoginFragment();
            return;
        }

        if (needReloadData) {
            reloadList();
            totalStatus.changeCheckStatus(false, BaseStatus.PHRASE_TARGET);
        }
    }

    /**
     * 重新加載購物袋
     */
    private void reloadList() {
        SLog.info("執行購物車重載");
        totalStatus.storeStatusList.clear();
        loadCartData();
        updateTotalData();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();


    }

    private void loadCartData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();

        String url = Api.PATH_CART_LIST;
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "clientType", Constant.CLIENT_TYPE_ANDROID);

        SLog.info("params[%s]", params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                ToastUtil.showNetworkError(_mActivity, e);
                loadingPopup.dismiss();
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                loadingPopup.dismiss();

                // responseStr = "{\"code\":200,\"datas\":{\"cartDbMaxCount\":50,\"cartStoreVoList\":[{\"cartItemVoList\":[],\"cartSpuVoList\":[{\"cartItemVoList\":[{\"cartId\":2395,\"goodsId\":4235,\"commonId\":3232,\"goodsName\":\"233\",\"goodsFullSpecs\":\"顔色：白色ddddddd；鞋碼：26；尺碼：XXS\",\"goodsPrice\":0.01,\"imageName\":\"image/19/e1/19e1b342d46e97749e4c8ed34085b867.jpg\",\"buyNum\":1,\"itemAmount\":0.01,\"goodsStorage\":9974,\"goodsStatus\":1,\"storeId\":85,\"storeName\":\"內測門店_測試改名\",\"storageStatus\":1,\"memberId\":247,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/19/e1/19e1b342d46e97749e4c8ed34085b867.jpg\",\"unitName\":\"盒\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":0.01,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":0.01,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":0.01,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/19/e1/19e1b342d46e97749e4c8ed34085b867.jpg\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionTypeText\":\"限時折扣\",\"promotionTitle\":\"233\",\"goodsPrice0\":0.01,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"promotionType\":1,\"isBook\":0,\"isGift\":0,\"giftVoList\":[],\"bundlingId\":0,\"buyBundlingItemVoList\":null,\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":0,\"chainId\":0,\"chainName\":null,\"realGoodsId\":0,\"realCommonId\":0,\"chainGoodsId\":0,\"isVirtual\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"isForeign\":0,\"isChain\":0,\"goodsType\":0,\"reserveStorage\":10,\"onlineStorage\":9964,\"jingle\":\"233\",\"batchPrice0\":123123.00,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null,\"tariffEnable\":1}],\"commonId\":3232,\"goodsName\":\"233\",\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/19/e1/19e1b342d46e97749e4c8ed34085b867.jpg\",\"goodsModal\":1,\"batchNum0\":1,\"batchNum1\":0,\"batchNum2\":0,\"batchNum0End\":0,\"batchNum1End\":0,\"goodsStatus\":1,\"isValid\":1,\"buyNum\":0,\"bundlingId\":0,\"chainId\":0,\"realCommonId\":0,\"goodsType\":0}],\"voucherTemplateVoList\":[],\"conformList\":[{\"conformId\":156,\"conformName\":\"333\",\"conformTitle\":null,\"conformTileFinal\":\"滿優惠\",\"startTime\":\"2019-12-24 19:11:38\",\"endTime\":\"2020-12-04 23:59:59\",\"conformState\":1,\"limitAmount\":300.00,\"conformPrice\":0.00,\"isFreeFreight\":0,\"ruleOutAreaIds\":\"\",\"ruleOutAreaNames\":\"\",\"templateId\":269,\"templatePrice\":10.00,\"isGift\":0,\"giftVoList\":[],\"storeId\":85,\"storeName\":null,\"conformStateText\":\"進行中\",\"shortRule\":\"送劵\",\"contentCartRule\":\"滿300元，送10元店鋪劵\"}],\"cartAmount\":0.01,\"storeName\":\"內測門店_測試改名\",\"storeId\":85,\"sellerId\":85,\"isOnline\":0,\"buyNum\":1,\"cartBundlingVoList\":[],\"chainId\":0,\"chainName\":null,\"serviceStaffList\":null},{\"cartItemVoList\":[],\"cartSpuVoList\":[{\"cartItemVoList\":[{\"cartId\":2097,\"goodsId\":4797,\"commonId\":3637,\"goodsName\":\"陽澄湖大閘蟹\",\"goodsFullSpecs\":\"種類：母蟹；個頭：2兩\",\"goodsPrice\":399.00,\"imageName\":\"image/c0/74/c074a4ecf50840f3ba714cb8ea3357cc.jpg\",\"buyNum\":3,\"itemAmount\":1197.00,\"goodsStorage\":100,\"goodsStatus\":1,\"storeId\":280,\"storeName\":\"螃蟹好吃\",\"storageStatus\":1,\"memberId\":247,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/c0/74/c074a4ecf50840f3ba714cb8ea3357cc.jpg\",\"unitName\":\"件\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":399.00,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":399.00,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":399.00,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/c0/74/c074a4ecf50840f3ba714cb8ea3357cc.jpg\",\"spuBuyNum\":4,\"joinBigSale\":1,\"promotionTypeText\":\"限時折扣\",\"promotionTitle\":\"複製折扣\",\"goodsPrice0\":399.00,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"promotionType\":1,\"isBook\":0,\"isGift\":0,\"giftVoList\":[],\"bundlingId\":0,\"buyBundlingItemVoList\":null,\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":0,\"chainId\":0,\"chainName\":null,\"realGoodsId\":0,\"realCommonId\":0,\"chainGoodsId\":0,\"isVirtual\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"isForeign\":0,\"isChain\":0,\"goodsType\":0,\"reserveStorage\":10,\"onlineStorage\":90,\"jingle\":\"蟹黃多\",\"batchPrice0\":1200.00,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null},{\"cartId\":2096,\"goodsId\":4796,\"commonId\":3637,\"goodsName\":\"陽澄湖大閘蟹\",\"goodsFullSpecs\":\"種類：公蟹；個頭：5兩\",\"goodsPrice\":499.00,\"imageName\":\"image/c0/74/c074a4ecf50840f3ba714cb8ea3357cc.jpg\",\"buyNum\":1,\"itemAmount\":499.00,\"goodsStorage\":48,\"goodsStatus\":1,\"storeId\":280,\"storeName\":\"螃蟹好吃\",\"storageStatus\":1,\"memberId\":247,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/c0/74/c074a4ecf50840f3ba714cb8ea3357cc.jpg\",\"unitName\":\"件\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":499.00,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":499.00,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":499.00,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/c0/74/c074a4ecf50840f3ba714cb8ea3357cc.jpg\",\"spuBuyNum\":4,\"joinBigSale\":1,\"promotionTypeText\":\"限時折扣\",\"promotionTitle\":\"複製折扣\",\"goodsPrice0\":499.00,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"promotionType\":1,\"isBook\":0,\"isGift\":0,\"giftVoList\":[],\"bundlingId\":0,\"buyBundlingItemVoList\":null,\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":0,\"chainId\":0,\"chainName\":null,\"realGoodsId\":0,\"realCommonId\":0,\"chainGoodsId\":0,\"isVirtual\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"isForeign\":0,\"isChain\":0,\"goodsType\":0,\"reserveStorage\":5,\"onlineStorage\":43,\"jingle\":\"蟹黃多\",\"batchPrice0\":1200.00,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null}],\"commonId\":3637,\"goodsName\":\"陽澄湖大閘蟹\",\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/c0/74/c074a4ecf50840f3ba714cb8ea3357cc.jpg\",\"goodsModal\":1,\"batchNum0\":1,\"batchNum1\":0,\"batchNum2\":0,\"batchNum0End\":0,\"batchNum1End\":0,\"goodsStatus\":1,\"isValid\":1,\"buyNum\":0,\"bundlingId\":0,\"chainId\":0,\"realCommonId\":0,\"goodsType\":0}],\"voucherTemplateVoList\":[],\"conformList\":[],\"cartAmount\":1696.00,\"storeName\":\"螃蟹好吃\",\"storeId\":280,\"sellerId\":280,\"isOnline\":0,\"buyNum\":2,\"cartBundlingVoList\":[],\"chainId\":0,\"chainName\":null,\"serviceStaffList\":null},{\"cartItemVoList\":[],\"cartSpuVoList\":[{\"cartItemVoList\":[{\"cartId\":1807,\"goodsId\":4725,\"commonId\":3597,\"goodsName\":\"高腰寬鬆牛仔褲\",\"goodsFullSpecs\":\"顔色：顏色隨機\",\"goodsPrice\":99.00,\"imageName\":\"image/32/98/3298e60059bf720abc4da9f3f2c79634.jpg\",\"buyNum\":1,\"itemAmount\":99.00,\"goodsStorage\":2,\"goodsStatus\":1,\"storeId\":263,\"storeName\":\"好運來\",\"storageStatus\":0,\"memberId\":247,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/32/98/3298e60059bf720abc4da9f3f2c79634.jpg\",\"unitName\":\"件\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":99.00,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":99.00,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":99.00,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/32/98/3298e60059bf720abc4da9f3f2c79634.jpg\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionTypeText\":\"\",\"promotionTitle\":\"\",\"goodsPrice0\":99.00,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"promotionType\":0,\"isBook\":0,\"isGift\":0,\"giftVoList\":[],\"bundlingId\":0,\"buyBundlingItemVoList\":null,\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":0,\"chainId\":0,\"chainName\":null,\"realGoodsId\":0,\"realCommonId\":0,\"chainGoodsId\":0,\"isVirtual\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"isForeign\":0,\"isChain\":0,\"goodsType\":0,\"reserveStorage\":2,\"onlineStorage\":0,\"jingle\":\"高腰 寬鬆 時尚\",\"batchPrice0\":99.00,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null}],\"commonId\":3597,\"goodsName\":\"高腰寬鬆牛仔褲\",\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/32/98/3298e60059bf720abc4da9f3f2c79634.jpg\",\"goodsModal\":1,\"batchNum0\":1,\"batchNum1\":0,\"batchNum2\":0,\"batchNum0End\":0,\"batchNum1End\":0,\"goodsStatus\":1,\"isValid\":1,\"buyNum\":0,\"bundlingId\":0,\"chainId\":0,\"realCommonId\":0,\"goodsType\":0}],\"voucherTemplateVoList\":[],\"conformList\":[],\"cartAmount\":99.00,\"storeName\":\"好運來\",\"storeId\":263,\"sellerId\":263,\"isOnline\":0,\"buyNum\":1,\"cartBundlingVoList\":[],\"chainId\":0,\"chainName\":null,\"serviceStaffList\":null},{\"cartItemVoList\":[],\"cartSpuVoList\":[{\"cartItemVoList\":[{\"cartId\":1665,\"goodsId\":4778,\"commonId\":3628,\"goodsName\":\"法國BEABA嬰兒360°訓練勺\",\"goodsFullSpecs\":\"顔色：橙色\",\"goodsPrice\":139.00,\"imageName\":\"image/06/52/0652065b2c549562ab7e181054d4b301.jpg\",\"buyNum\":1,\"itemAmount\":139.00,\"goodsStorage\":0,\"goodsStatus\":1,\"storeId\":274,\"storeName\":\"111\",\"storageStatus\":0,\"memberId\":247,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/06/52/0652065b2c549562ab7e181054d4b301.jpg\",\"unitName\":\"瓶\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":139.00,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":139.00,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":139.00,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/06/52/0652065b2c549562ab7e181054d4b301.jpg\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionTypeText\":\"\",\"promotionTitle\":\"\",\"goodsPrice0\":139.00,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"promotionType\":0,\"isBook\":0,\"isGift\":0,\"giftVoList\":[],\"bundlingId\":0,\"buyBundlingItemVoList\":null,\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":0,\"chainId\":0,\"chainName\":null,\"realGoodsId\":0,\"realCommonId\":0,\"chainGoodsId\":0,\"isVirtual\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"isForeign\":0,\"isChain\":0,\"goodsType\":0,\"reserveStorage\":0,\"onlineStorage\":0,\"jingle\":\"360可旋轉勺柄，不再怕BB灑出事物，鍛煉BB抓握能力\",\"batchPrice0\":139.00,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null}],\"commonId\":3628,\"goodsName\":\"法國BEABA嬰兒360°訓練勺\",\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/06/52/0652065b2c549562ab7e181054d4b301.jpg\",\"goodsModal\":1,\"batchNum0\":1,\"batchNum1\":0,\"batchNum2\":0,\"batchNum0End\":0,\"batchNum1End\":0,\"goodsStatus\":1,\"isValid\":1,\"buyNum\":0,\"bundlingId\":0,\"chainId\":0,\"realCommonId\":0,\"goodsType\":0}],\"voucherTemplateVoList\":[],\"conformList\":[],\"cartAmount\":139.00,\"storeName\":\"111\",\"storeId\":274,\"sellerId\":274,\"isOnline\":0,\"buyNum\":1,\"cartBundlingVoList\":[],\"chainId\":0,\"chainName\":null,\"serviceStaffList\":null},{\"cartItemVoList\":[],\"cartSpuVoList\":[{\"cartItemVoList\":[{\"cartId\":1580,\"goodsId\":4692,\"commonId\":3599,\"goodsName\":\"到貨通知測試法棍\",\"goodsFullSpecs\":\"顔色：白色\",\"goodsPrice\":0.13,\"imageName\":\"image/1f/24/1f2498c841aff1a88114222dbadc6541.jpg\",\"buyNum\":1,\"itemAmount\":0.13,\"goodsStorage\":3,\"goodsStatus\":1,\"storeId\":179,\"storeName\":\"結算日期測試8——心享店\",\"storageStatus\":0,\"memberId\":247,\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/1f/24/1f2498c841aff1a88114222dbadc6541.jpg\",\"unitName\":\"袋\",\"batchNumState\":1,\"batchNum0\":1,\"batchNum0End\":0,\"batchNum1\":0,\"batchNum1End\":0,\"batchNum2\":0,\"webPrice0\":0.13,\"webPrice1\":0.00,\"webPrice2\":0.00,\"webUsable\":0,\"appPrice0\":0.13,\"appPrice1\":0.00,\"appPrice2\":0.00,\"appUsable\":0,\"wechatPrice0\":0.13,\"wechatPrice1\":0.00,\"wechatPrice2\":0.00,\"wechatUsable\":0,\"promotionBeginTime\":null,\"promotionEndTime\":null,\"goodsModal\":1,\"spuImageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/1f/24/1f2498c841aff1a88114222dbadc6541.jpg\",\"spuBuyNum\":1,\"joinBigSale\":1,\"promotionTypeText\":\"\",\"promotionTitle\":\"\",\"goodsPrice0\":0.13,\"goodsPrice1\":0.00,\"goodsPrice2\":0.00,\"promotionType\":0,\"isBook\":0,\"isGift\":0,\"giftVoList\":[],\"bundlingId\":0,\"buyBundlingItemVoList\":null,\"contractItem1\":0,\"contractItem2\":0,\"contractItem3\":0,\"contractItem4\":0,\"contractItem5\":0,\"contractItem6\":0,\"contractItem7\":0,\"contractItem8\":0,\"contractItem9\":0,\"contractItem10\":0,\"goodsContractVoList\":[],\"limitAmount\":0,\"chainId\":0,\"chainName\":null,\"realGoodsId\":0,\"realCommonId\":0,\"chainGoodsId\":0,\"isVirtual\":0,\"isSecKill\":0,\"seckillGoodsId\":0,\"isForeign\":0,\"isChain\":0,\"goodsType\":0,\"reserveStorage\":3,\"onlineStorage\":0,\"jingle\":\"\",\"batchPrice0\":0.26,\"limitBuy\":0,\"limitBuyStartTime\":null,\"limitBuyEndTime\":null}],\"commonId\":3599,\"goodsName\":\"到貨通知測試法棍\",\"imageSrc\":\"https://ftofs-editor.oss-cn-shenzhen.aliyuncs.com/image/1f/24/1f2498c841aff1a88114222dbadc6541.jpg\",\"goodsModal\":1,\"batchNum0\":1,\"batchNum1\":0,\"batchNum2\":0,\"batchNum0End\":0,\"batchNum1End\":0,\"goodsStatus\":1,\"isValid\":1,\"buyNum\":0,\"bundlingId\":0,\"chainId\":0,\"realCommonId\":0,\"goodsType\":0}],\"voucherTemplateVoList\":[],\"conformList\":[],\"cartAmount\":0.13,\"storeName\":\"結算日期測試8——心享店\",\"storeId\":179,\"sellerId\":179,\"isOnline\":0,\"buyNum\":1,\"cartBundlingVoList\":[],\"chainId\":0,\"chainName\":null,\"serviceStaffList\":null}],\"skuCount\":6}}";
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                    return;
                }

                try {
                    String cartText = _mActivity.getResources().getString(R.string.text_cart);
                    int cartItemCount = responseObj.getInt("datas.skuCount");
                    cartText = String.format(cartText + "(%d/50)", cartItemCount);
                    tvFragmentTitle.setText(cartText);

                    EasyJSONArray cartStoreVoList = responseObj.getSafeArray("datas.cartStoreVoList");

                    int storeCount = 0;
                    totalCartItemCount = 0;
                    cartStoreItemContainer.removeAllViews();
                    for (Object object : cartStoreVoList) { // store LOOP
                        StoreStatus storeStatus = new StoreStatus();
                        storeStatus.parent = totalStatus;
                        EasyJSONObject cartStoreVo = (EasyJSONObject) object;

                        View cartStoreItem = LayoutInflater.from(_mActivity).inflate(R.layout.cart_store_item, null, false);
                        TextView tvStoreName = cartStoreItem.findViewById(R.id.tv_store_name);
                        ScaledButton btnCheckStore = cartStoreItem.findViewById(R.id.btn_check_store);
                        btnCheckStore.setTag(storeStatus);
                        setCheckButtonOnClickListener(btnCheckStore);
                        storeStatus.setRadio(btnCheckStore);

                        // 點擊商店標題，跳轉到具體的商店
                        final int storeId = cartStoreVo.getInt("storeId");
                        cartStoreItem.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Util.startFragment(ShopMainFragment.newInstance(storeId));
                            }
                        });
                        tvStoreName.setText(cartStoreVo.getSafeString("storeName"));

                        EasyJSONArray cartSpuVoList = cartStoreVo.getSafeArray("cartSpuVoList");
                        int spuCount=0;
                        for (Object object2 : cartSpuVoList) { // spu LOOP
                            EasyJSONObject cartSpuVo = (EasyJSONObject) object2;

                            EasyJSONArray cartItemVoList = cartSpuVo.getSafeArray("cartItemVoList");
                            int skuCount=0;
                            for (Object object3 : cartItemVoList) { // sku LOOP
                                LinearLayout cartSpuItemContainer = cartStoreItem.findViewById(R.id.ll_cart_spu_item_container);
                                SpuStatus spuStatus = new SpuStatus();

                                spuStatus.parent = storeStatus;
                                View cartSpuItem = LayoutInflater.from(_mActivity).inflate(R.layout.cart_spu_item, null, false);
                                SLog.info("spu%d spulenth%d,sku%d,skulenth%d,",spuCount,cartSpuVoList.length(),skuCount,cartItemVoList.length());
                                skuCount++;

                                LinearLayout cartSkuItemContainer = cartSpuItem.findViewById(R.id.ll_cart_sku_item_container);
                                View cartSkuItem = LayoutInflater.from(_mActivity).inflate(R.layout.cart_sku_item, cartSkuItemContainer, false);

                                ImageView goodsImage = cartSpuItem.findViewById(R.id.goods_image);
                                ScaledButton btnCheckSpu = cartSpuItem.findViewById(R.id.btn_check_spu);

                                btnCheckSpu.setTag(spuStatus);
                                setCheckButtonOnClickListener(btnCheckSpu);
                                spuStatus.setRadio(btnCheckSpu);

                                // 點擊Spu，跳轉到對應的產品詳情頁面
                                final int commonId = cartSpuVo.getInt("commonId");
                                cartSpuItem.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Util.startFragment(GoodsDetailFragment.newInstance(commonId, 0));
                                    }
                                });
                                TextView tvGoodsName = cartSkuItem.findViewById(R.id.tv_goods_name);
                                tvGoodsName.setText(cartSpuVo.getSafeString("goodsName"));

                                ++totalCartItemCount;
//                                SkuStatus skuStatus = new SkuStatus();
//                                skuStatus.parent = spuStatus;
                                TextView tvGoodsFullSpecs = cartSkuItem.findViewById(R.id.tv_goods_full_specs);
                                TextView tvPriceSum = cartSkuItem.findViewById(R.id.tv_price_sum);
//                                ScaledButton btnCheckSku = cartSkuItem.findViewById(R.id.btn_check_sku);
//                                btnCheckSku.setTag(skuStatus);

                                // 購買數量調節按鈕
                                CartAdjustButton abQuantity = cartSkuItem.findViewById(R.id.ab_quantity);
                                abQuantity.setMinValue(1, null);  // 調節數量不能小于1

                                abQuantity.setSpuStatus(spuStatus);
                                setCheckButtonOnClickListener(btnCheckSpu);
                                spuStatus.setRadio(btnCheckSpu);

                                EasyJSONObject cartSkuVo = (EasyJSONObject) object3;
                                Glide.with(CartFragment.this).load(cartSkuVo.getSafeString("imageSrc")).centerCrop().into(goodsImage);
                                int limitBuy = cartSkuVo.getInt("limitBuy");
                                //如果有限購限制增加數量
                                if (limitBuy < 0) {
                                    abQuantity.setMaxValue(1, () -> {
                                        ToastUtil.error(_mActivity,getString(R.string.out_of_buy_limit));
                                    });
                                }

                                spuStatus.setStoreId(cartSkuVo.getInt("storeId"));
                                spuStatus.setStoreName(cartSkuVo.getString("storeName"));
                                spuStatus.setGoodsId(cartSkuVo.getInt("goodsId"));
                                spuStatus.setCartId(cartSkuVo.getInt("cartId"));
                                tvGoodsFullSpecs.setText(cartSkuVo.getSafeString("goodsFullSpecs"));
                                float goodsPrice = (float) cartSkuVo.getDouble("goodsPrice");

                                // 跨境購功能
                                boolean tariffEnable = false;
                                if (cartSkuVo.exists("tariffEnable")) {
                                    tariffEnable = cartSkuVo.getInt("tariffEnable") == Constant.TRUE_INT;
                                    spuStatus.setCrossBorder(tariffEnable);
                                }
                                cartSkuItem.findViewById(R.id.cross_border_indicator).setVisibility(tariffEnable ? View.VISIBLE: View.GONE);

                                int buyNum = cartSkuVo.getInt("buyNum");

                                SLog.info("buyNum %d,limitNum %d ，tariffEnable %s", buyNum, limitBuy, tariffEnable);
                                tvPriceSum.setText(StringUtil.formatPrice(_mActivity, goodsPrice, 0,2));
                                abQuantity.setValue(buyNum);

                                spuStatus.setPrice(goodsPrice);
                                spuStatus.setCount(buyNum);
                                spuStatus.setLimitState(limitBuy);

                                // 贈品列表
                                EasyJSONArray giftVoList = cartSkuVo.getSafeArray("giftVoList");
                                if (giftVoList.length() > 0) {
                                    LinearLayout llGiftListContainer = cartSpuItem.findViewById(R.id.ll_gift_list_container);
                                    ConfirmOrderGiftListAdapter adapter = new ConfirmOrderGiftListAdapter(_mActivity, llGiftListContainer, R.layout.cart_gift_item);
                                    List<GiftItem> giftItemList = new ArrayList<>();
                                    for (Object object4 : giftVoList) {
                                        GiftItem giftItem = (GiftItem) EasyJSONObject.jsonDecode(GiftItem.class, object4.toString());
                                        giftItemList.add(giftItem);
                                    }
                                    adapter.setItemClickListener(new ViewGroupAdapter.OnItemClickListener() {
                                        @Override
                                        public void onClick(ViewGroupAdapter adapter, View view, int position) {
                                            GiftItem giftItem = giftItemList.get(position);
                                            Util.startFragment(GoodsDetailFragment.newInstance(giftItem.commonId, giftItem.goodsId));
                                        }
                                    });
                                    adapter.setData(giftItemList);

                                    llGiftListContainer.setVisibility(View.VISIBLE);
                                }

//                                spuStatus.skuStatusList.add(skuStatus);
                                cartSkuItemContainer.addView(cartSkuItem);

                                int goodsStatus = cartSkuVo.getInt("goodsStatus");
                                int onlineStorage = cartSkuVo.getInt("onlineStorage");
                                ImageView maskImage = cartSpuItem.findViewById(R.id.mask_image);

                                if (goodsStatus == 0) { // 產品下架
                                    Glide.with(_mActivity).load(R.drawable.icon_take_off).into(maskImage);
//                                    btnCheckSpu.setEnabled(false);
                                    ((SpuStatus)btnCheckSpu.getTag()).changeCheckableStatus(false,PHRASE_BUBBLE);
                                    btnCheckSpu.setIconResource(R.drawable.icon_disable_check);
                                } else if (onlineStorage == 0) { // 售罄
                                    Glide.with(_mActivity).load(R.drawable.icon_no_storage).into(maskImage);
//                                    btnCheckSpu.setEnabled(false);
                                    ((SpuStatus)btnCheckSpu.getTag()).changeCheckableStatus(false,PHRASE_BUBBLE);


                                    btnCheckSpu.setIconResource(R.drawable.icon_disable_check);
                                }else if (onlineStorage <= 2) { // 庫存緊張
                                    ((SpuStatus)btnCheckSpu.getTag()).changeCheckableStatus(true,PHRASE_BUBBLE);

                                    Glide.with(_mActivity).load(R.drawable.icon_less_storage).into(maskImage);
                                }else {
                                    ((SpuStatus)btnCheckSpu.getTag()).changeCheckableStatus(true,PHRASE_BUBBLE);

                                }

                                if (spuCount == cartSpuVoList.length()-1&&spuCount>0) {
                                    cartSpuItem.findViewById(R.id.line).setVisibility(View.GONE);
                                } else if (cartSpuVoList.length() == 1) {
                                    cartSpuItem.findViewById(R.id.line).setVisibility(View.GONE);

                                }

                                storeStatus.spuStatusList.add(spuStatus);
                                cartSpuItemContainer.addView(cartSpuItem);
                            } // END OF sku LOOP
                            spuCount++;

                        } // END OF spu LOOP


                        totalStatus.storeStatusList.add(storeStatus);
                        cartStoreItemContainer.addView(cartStoreItem);

                        storeCount++;
                    } // END OF store LOOP

                    if (storeCount == 0 && svItemContainerHeight != -1) { // 如果購物車內沒東西，則顯示空白頁面的占位符
                        View placeholderCartEmpty = LayoutInflater.from(_mActivity).inflate(R.layout.layout_placeholder_cart_empty, null, false);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, svItemContainerHeight);
                        cartStoreItemContainer.addView(placeholderCartEmpty, layoutParams);
                    }

                    displayCartItemCount(cartItemCount);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                hideSoftInputPop();
                break;
            case R.id.btn_edit:
                switchMode();
                break;
            case R.id.btn_settlement:
                EasyJSONArray buyData = totalStatus.getBuyDataNew();
                if (buyData.length() < 1) {
                    // 如果沒有勾選什么數據，返回
                    return;
                }

                if (totalStatus.getTotalDataNew().first >= 20000) {
                    new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                            // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                            .asCustom(new TwConfirmPopup(_mActivity, "每次交易總金額不得超過$20,000，請調整購物數量再提交", null, new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("onYes");
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    })).show();

                    return;
                }

                /*
                校驗是否符合跨境購結算條件
                a.  屬於跨城購的商品不可和非跨城購商品合併生成訂單；
                b.  屬於跨城購的商品不可和其他店鋪的商品合併生成訂單；
                 */
                if (!checkCrossBorderCondition(buyData)) {
                    new XPopup.Builder(_mActivity)
                            // 如果不加这个，评论弹窗会移动到软键盘上面
                            .moveUpToKeyboard(false)
                            .asCustom(new CartCrossBorderPopup(_mActivity, crossBorderStoreMap, this))
                            .show();
                    return;
                }
                Util.startFragmentForResult(NewConfirmOrderFragment.newInstance(1, buyData.toString()), RequestCode.CONFIRM_ORDER.ordinal());
                break;
            case R.id.btn_delete:
                buyData = totalStatus.getBuyDataNew();
                if (buyData.length() < 1) {
                    // 如果沒有勾選什么數據，返回
                    SLog.info("如果沒有勾選什么數據，返回");
                    return;
                }


                new XPopup.Builder(_mActivity)
//                         .dismissOnTouchOutside(false)
                        // 设置弹窗显示和隐藏的回调监听
//                         .autoDismiss(false)
                       .asCustom(new TwConfirmPopup(_mActivity, "確定要刪除嗎？", null, new OnConfirmCallback() {
                        @Override
                        public void onYes() {
                            SLog.info("onYes");
                            deleteCartItem(buyData);
                        }

                        @Override
                        public void onNo() {
                            SLog.info("onNo");
                        }
                    })).show();
                break;
            default:
                break;
        }
    }

    /**
     * 檢查是否符合購境條件
     * @param buyData
     * @return true -- 不需要拆單
     *         false -- 需要拆單
     */
    private boolean checkCrossBorderCondition(EasyJSONArray buyData) {
        crossBorderStoreMap.clear();

        try {
            SLog.info("buyData[%s]", buyData);
            for (Object object : buyData) {
                EasyJSONObject item = (EasyJSONObject) object;

                int storeId = item.getInt("storeId");
                String storeName = item.getString("storeName");
                boolean isCrossBorder = item.getBoolean("isCrossBorder");
                int buyNum = item.getInt("buyNum");
                int goodsId = item.getInt("goodsId");
                int cartId = item.getInt("cartId");

                int key;
                if (isCrossBorder) { // 如果是購境購商品，按店鋪切分
                    key = storeId;
                } else { // key為0表示非跨境購商品
                    key = 0;
                }
                CrossBorderStoreInfo crossBorderStoreInfo = crossBorderStoreMap.get(key);
                if (crossBorderStoreInfo == null) {
                    crossBorderStoreInfo = new CrossBorderStoreInfo(storeId, storeName, isCrossBorder);
                }
                crossBorderStoreInfo.productCount += buyNum;
                crossBorderStoreInfo.buyData.append(
                        EasyJSONObject.generate(
                                "buyNum", buyNum,
                                "goodsId", goodsId,
                                "cartId", cartId)
                );
                crossBorderStoreMap.put(key, crossBorderStoreInfo);
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }

        SLog.info("crossBorderStoreMap[%s]", crossBorderStoreMap);

        return crossBorderStoreMap.size() <= 1;
    }

    private void deleteCartItem(EasyJSONArray buyData) {
        try {
            boolean first = true;
            StringBuilder cartId = new StringBuilder();
            for (Object object : buyData) {
                EasyJSONObject easyJSONObject = (EasyJSONObject) object;
                if (!first) {
                    cartId.append(",");
                }
                cartId.append(easyJSONObject.getInt("cartId"));
                first = false;
            }

            String token = User.getToken();
            if (StringUtil.isEmpty(token)) {
                return;
            }

            String url = Api.PATH_DELETE_CART;
            EasyJSONObject params = EasyJSONObject.generate("token", token, "cartId", cartId.toString());
            SLog.info("params[%s]", params);
            Api.postUI(url, params, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    LogUtil.uploadAppLog(url, params.toString(), "", e.getMessage());
                    ToastUtil.showNetworkError(_mActivity, e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        LogUtil.uploadAppLog(url, params.toString(), responseStr, "");
                        return;
                    }

                    ToastUtil.success(_mActivity, "刪除成功");
                    reloadList();
                }
            });
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    /**
     * 切換購物袋的模式
     * 查看模式 還是 編輯模式
     */
    private void switchMode() {
        if (mode == Constant.MODE_VIEW) {
            btnDelete.setVisibility(View.VISIBLE);
            llViewModeButtonGroup.setVisibility(View.GONE);

            btnEdit.setText(getResources().getString(R.string.text_finish));
            btnEdit.setTextColor(getResources().getColor(R.color.tw_red, null));
            totalStatus.resetCheckable();
            mode = Constant.MODE_EDIT;
        } else {
            btnDelete.setVisibility(View.GONE);
            llViewModeButtonGroup.setVisibility(View.VISIBLE);

            btnEdit.setText(getResources().getString(R.string.text_edit));
            btnEdit.setTextColor(getResources().getColor(R.color.tw_black, null));
            mode = Constant.MODE_VIEW;
            reloadList();
            totalStatus.changeCheckStatus(false, BaseStatus.PHRASE_TARGET);
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        if (mode == Constant.MODE_EDIT) {
            switchMode();
            return true;
        }

        if (isStandalone) {
            hideSoftInputPop();
            return true;
        }
        return false;
    }

    private void setCheckButtonOnClickListener(View checkButton) {
        checkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScaledButton btnCheck = (ScaledButton) v;
                BaseStatus status = (BaseStatus) btnCheck.getTag();
//                SLog.info("checkable %s  %s",status.isCheckable(),status.isChecked());
                status.changeCheckStatus(!status.isChecked(), BaseStatus.PHRASE_TARGET);
                updateTotalData();
            }
        });
    }

    /**
     * 更新合計數據
     */
    private void updateTotalData() {
        Pair<Double, Integer> totalData = totalStatus.getTotalDataNew();

        double totalPrice = totalData.first;  // 總價錢
        int totalCount = totalData.second;  // 總件數
        tvTotalPrice.setText(StringUtil.formatPrice(_mActivity, totalPrice, 0,2));
        String btnSettlementText = textSettlement;

        if (totalCount > 0) {
            btnSettlementText += "(" + totalCount + ")";
        }
        btnSettlement.setText(btnSettlementText);
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_ADD_CART) {
            updateTotalData();
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_UPDATE_TOOLBAR_RED_BUBBLE ||
                    message.messageType == EBMessageType.MESSAGE_TYPE_LOGIN_SUCCESS) {
            reloadList();
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        if (type == PopupType.SELECT_SPLIT_CROSS_BORDER) {

        }
    }
}
