package com.ftofs.twant.widget;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.tangram.NewShoppingSpecialFragment;
import com.ftofs.twant.util.ClipboardUtils;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import cn.snailpad.easyjson.EasyJSONObject;


/**
 * 新版口令彈窗
 * @author zwm
 */
public class NewWordPopup extends CenterPopupView implements View.OnClickListener {
    public static final int COMMAND_TYPE_UNKNOWN = 0; // 未知类型口令
    public static final int COMMAND_TYPE_STORE = 1; // 店铺类型口令
    public static final int COMMAND_TYPE_GOODS = 2; // 商品类型口令
    public static final int COMMAND_TYPE_SHOPPING = 3; // 购物专场类型口令

    Context context;
    EasyJSONObject extra;

    String commandType;
    int commandTypeInt;

    public NewWordPopup(@NonNull Context context, String commandType, int commandTypeInt, EasyJSONObject extra) {
        super(context);

        this.context = context;
        this.commandType = commandType;
        this.commandTypeInt = commandTypeInt;
        this.extra = extra;

        SLog.info("commandType[%s], commandTypeInt[%d]", commandType, commandTypeInt);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.new_word_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        ImageView imageView = findViewById(R.id.image_view);
        TextView tvDesc = findViewById(R.id.tv_desc);
        LinearLayout llMainContentContainer = findViewById(R.id.ll_main_content_container);
        llMainContentContainer.setBackground(BackgroundDrawable.create(Color.WHITE, Util.dip2px(context, 8)));

        findViewById(R.id.btn_ok).setOnClickListener(this);
        findViewById(R.id.btn_close).setOnClickListener(this);

        /*
        "zoneId":52,"commandType":"shoppingZone","zoneName":"測試生成分享口令","appLogo":"image/8e/37/8e370775524a29f9a86d4a214be49424.jpg"
         */
        if (commandTypeInt == COMMAND_TYPE_SHOPPING) {  // 如果是跳轉到購物賣場
            switchContainer(false);
            String appLogo = extra.optString("appLogo");
            Glide.with(context).load(StringUtil.normalizeImageUrl(appLogo, "?x-oss-process=image/resize,w_600")).centerCrop().into(imageView);
            tvDesc.setText(extra.optString("zoneName"));
        } else if (commandTypeInt == COMMAND_TYPE_GOODS) {
            switchContainer(true);

            ImageView goodsImage = findViewById(R.id.goods_image);
            String imageName = extra.optString("goodsCommon.imageName");
            Glide.with(context).load(StringUtil.normalizeImageUrl(imageName, "?x-oss-process=image/resize,w_600")).centerCrop().into(goodsImage);

            TextView tvGoodsName = findViewById(R.id.tv_goods_name);
            tvGoodsName.setText(extra.optString("goodsCommon.goodsName"));

            TextView tvJingle = findViewById(R.id.tv_goods_jingle);
            tvJingle.setText(extra.optString("goodsCommon.jingle"));

            double goodsPrice = extra.optDouble("goodsCommon.appPrice0");
            TextView tvGoodsPrice = findViewById(R.id.tv_goods_price);
            tvGoodsPrice.setText(StringUtil.formatPrice(context, goodsPrice, 0));

            if ("goods".equals(commandType)) { // 如果是普通商品，没有折扣，则隐藏原价
                findViewById(R.id.ll_original_price_container).setVisibility(GONE);
            } else { // 如果是活动商品，则设置原价
                double originalPrice = extra.optDouble("appPriceMax");
                TextView tvOriginalPrice = findViewById(R.id.tv_original_price);
                tvOriginalPrice.setText(StringUtil.formatFloat(originalPrice));
            }
        } else if (commandTypeInt == COMMAND_TYPE_STORE) {
            switchContainer(false);
            String storeFigureImage = extra.optString("storeFigureImage");
            SLog.info("storeFigureImage[%s]", storeFigureImage);
            Glide.with(context).load(StringUtil.normalizeImageUrl(storeFigureImage, "?x-oss-process=image/resize,w_600")).centerCrop().into(imageView);
            tvDesc.setText(extra.optString("storeName"));
        }
    }

    /**
     * 切换容器的可见显示
     * @param showGoods 是否显示商品
     */
    private void switchContainer(boolean showGoods) {
        if (showGoods) {
            findViewById(R.id.ll_store_container).setVisibility(GONE);
            findViewById(R.id.ll_goods_container).setVisibility(VISIBLE);
        } else {
            findViewById(R.id.ll_store_container).setVisibility(VISIBLE);
            findViewById(R.id.ll_goods_container).setVisibility(GONE);
        }
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
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 0.8f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_ok) {
            if (commandTypeInt == COMMAND_TYPE_SHOPPING) {
                Util.startFragment(NewShoppingSpecialFragment.newInstance(extra.optInt("zoneId")));
            } else if (commandTypeInt == COMMAND_TYPE_STORE) {
                Util.startFragment(ShopMainFragment.newInstance(extra.optInt("storeId")));
            } else if (commandTypeInt == COMMAND_TYPE_GOODS) {
                Util.startFragment(GoodsDetailFragment.newInstance(extra.optInt("goodsCommon.commonId"), extra.optInt("goodsCommon.goodsId")));
            }
            // ClipboardUtils.copyText(context, ""); // 清空剪貼板
            dismiss();
        } else if (id == R.id.btn_close) {
            // ClipboardUtils.copyText(context, ""); // 清空剪貼板
            dismiss();
        }
    }
}
