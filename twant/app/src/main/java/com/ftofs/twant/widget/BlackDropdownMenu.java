package com.ftofs.twant.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.appserver.AppServiceImpl;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.fragment.AddFriendFragment;
import com.github.richardwrq.krouter.annotation.Inject;
import com.gzp.lib_common.base.BaseFragment;
import com.ftofs.twant.fragment.CartFragment;
import com.ftofs.twant.fragment.CategoryFragment;
import com.ftofs.twant.fragment.ChatFragment;
import com.ftofs.twant.fragment.CommitFeedbackFragment;
import com.ftofs.twant.fragment.ContactFragment;
import com.ftofs.twant.fragment.ENameCardFragment;
import com.ftofs.twant.fragment.GoodsDetailFragment;
import com.ftofs.twant.fragment.MainFragment;
import com.ftofs.twant.fragment.MemberInfoFragment;
import com.ftofs.twant.fragment.MessageFragment;
import com.ftofs.twant.fragment.PersonalInfoFragment;
import com.ftofs.twant.fragment.ShopCustomerServiceFragment;
import com.ftofs.twant.fragment.ShopMainFragment;
import com.ftofs.twant.seller.fragment.SellerFeaturesFragment;
import com.ftofs.twant.util.Util;
import com.gzp.lib_common.service.ConstantsPath;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.AttachPopupView;

/**
 * 通用的黑色彈出菜單
 * @author zwm
 */
public class BlackDropdownMenu extends AttachPopupView implements View.OnClickListener {
    /**
     * 消息菜單
     */
    public static final int TYPE_MESSAGE = 1;

    /**
     * 商店菜單
     */
    public static final int TYPE_STORE = 2;

    /**
     * 產品詳情菜單
     */
    public static final int TYPE_GOODS = 3;

    /**
     * 訂單菜單
     */
    public static final int TYPE_ORDER = 4;

    /**
     * 商城首頁+個人專頁
     */
    public static final int TYPE_HOME_AND_MY = 5;

    /**
     * 通訊錄菜單
     */
    public static final int TYPE_CONTACT = 6;
    /**
     * 聊天頁菜單
     */
    public static final int TYPE_CHAT=7;
    /**
     * 貼文詳情頁
     */
    public static final int TYPE_POST_DETAIL = 8;
    /**
     * 商家商品管理頁
     */
    public static final int TYPE_SELLER_GOODS = 9;


    Context context;
    BaseFragment baseFragment;

    @Inject(name = ConstantsPath.APP_SERVICE_PATH)
    AppServiceImpl appService;
    int type;
    public BlackDropdownMenu(@NonNull Context context, BaseFragment baseFragment, int type) {
        super(context);

        this.context = context;
        this.baseFragment = baseFragment;
        this.type = type;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.layout_black_menu;
    }


    @Override
    public View getPopupImplView() {
        View view = super.getPopupImplView();
        view.setBackground(getPopupBackground());
        return view;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        if (type == TYPE_MESSAGE) {
            ((ImageView) findViewById(R.id.icon_item_1)).setImageResource(R.drawable.icon_contact_white);
            ((TextView) findViewById(R.id.tv_item_1)).setText(R.string.text_contact);
            ((ImageView) findViewById(R.id.icon_item_2)).setImageResource(R.drawable.icon_scan_qr_code);
            ((TextView) findViewById(R.id.tv_item_2)).setText(R.string.text_scan_qr_code);
            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_add_friend);
            ((TextView) findViewById(R.id.tv_item_3)).setText(R.string.text_add_friend);
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_setting_white);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.text_setting);
        } else if (type == TYPE_STORE || type == TYPE_GOODS) {
            ((ImageView) findViewById(R.id.icon_item_1)).setImageResource(R.drawable.icon_black_menu_home);
            ((TextView) findViewById(R.id.tv_item_1)).setText(R.string.menu_item_shop_home_home);
            ((ImageView) findViewById(R.id.icon_item_2)).setImageResource(R.drawable.icon_black_menu_search);
            ((TextView) findViewById(R.id.tv_item_2)).setText(R.string.menu_item_shop_home_search);
            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_black_menu_message_custom);
            ((TextView) findViewById(R.id.tv_item_3)).setText(R.string.menu_item_shop_home_customer_service);
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_black_menu_message);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.menu_item_shop_home_message);
            ((ImageView) findViewById(R.id.icon_item_5)).setImageResource(R.drawable.icon_black_menu_message_share);
            ((TextView) findViewById(R.id.tv_item_5)).setText(R.string.text_share);
        } else if (type == TYPE_ORDER) {
            ((ImageView) findViewById(R.id.icon_item_1)).setImageResource(R.drawable.icon_black_menu_home);
            ((TextView) findViewById(R.id.tv_item_1)).setText(R.string.menu_item_shop_home_home);
            ((ImageView) findViewById(R.id.icon_item_2)).setImageResource(R.drawable.icon_meun_bag);
            ((TextView) findViewById(R.id.tv_item_2)).setText(R.string.text_cart);
            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_black_menu_my);
            ((TextView) findViewById(R.id.tv_item_3)).setText(R.string.text_my_page);
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_black_menu_message);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.menu_item_shop_home_message);
        } else if (type == TYPE_HOME_AND_MY) { // 只有2項，商城首頁 + 個人專頁
            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_black_menu_home);
            ((TextView) findViewById(R.id.tv_item_3)).setText(R.string.menu_item_shop_home_home);
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_black_menu_my);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.text_my_page);
        } else if (type == TYPE_CONTACT) {
            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_scan_qr_code);
            ((TextView) findViewById(R.id.tv_item_3)).setText(R.string.text_scan_qr_code);
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_add_friend);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.text_add_friend);
        }else if (type == TYPE_SELLER_GOODS) {
//            ((ImageView) findViewById(R.id.icon_item_1)).setImageResource(R.drawable.icon_black_menu_home);
            ((TextView) findViewById(R.id.tv_item_1)).setText(R.string.text_goods_mannager);
//            ((ImageView) findViewById(R.id.icon_item_2)).setImageResource(R.drawable.icon_meun_bag);
            ((TextView) findViewById(R.id.tv_item_2)).setText("商品發佈");
//            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_black_menu_my);
            ((TextView) findViewById(R.id.tv_item_3)).setText("商品規格");
//            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_black_menu_message);
            ((TextView) findViewById(R.id.tv_item_4)).setText("鎮店之寶");

        } else if (type == TYPE_POST_DETAIL) {
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_report);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.text_report);
        }else if (type == TYPE_CHAT) {
            ((ImageView) findViewById(R.id.icon_item_2)).setImageResource(R.drawable.icon_goto_store_info);
            ((TextView) findViewById(R.id.tv_item_2)).setText(R.string.text_store_enc);
            ((ImageView) findViewById(R.id.icon_item_3)).setImageResource(R.drawable.icon_goto_member_info);
            ((TextView) findViewById(R.id.tv_item_3)).setText(R.string.text_goto_member_info);
            ((ImageView) findViewById(R.id.icon_item_4)).setImageResource(R.drawable.icon_enc_mini);
            ((TextView) findViewById(R.id.tv_item_4)).setText(R.string.text_goto_enc);

        }

        // 在這里可以做一些findViewById等查找控件，進行自定義操作
        if (type == TYPE_HOME_AND_MY || type == TYPE_CONTACT||type==TYPE_POST_DETAIL) {
            // 只有2項，不需要對前2項設置事件處理
            findViewById(R.id.btn_item_1).setVisibility(GONE);
            findViewById(R.id.btn_item_2).setVisibility(GONE);
            if (type == TYPE_POST_DETAIL) {
                findViewById(R.id.btn_item_3).setVisibility(GONE);
                View btnItem4 = findViewById(R.id.btn_item_4);
                ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) btnItem4.getLayoutParams();
                layoutParams.topMargin = Util.dip2px(context, 15);
            } else {
                View btnItem3 = findViewById(R.id.btn_item_3);
                ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) btnItem3.getLayoutParams();
                layoutParams.topMargin = Util.dip2px(context, 15);
            }
            //暫時只做店鋪
        }else if(type==TYPE_CHAT){
            findViewById(R.id.btn_item_1).setVisibility(View.GONE);
            if (baseFragment == null) {
                return;
            }
            ChatFragment chatFragment = (ChatFragment) baseFragment;
            int storeId = chatFragment.getStoreId();
            boolean showCard = chatFragment.getCard();
            findViewById(R.id.btn_item_4).setVisibility(showCard?VISIBLE:GONE);
            View btnItem3 = findViewById(R.id.btn_item_3);
            View btnItem2 = findViewById(R.id.btn_item_2);
            int count = 1;
            if (storeId > 0) {
                count++;
            }
            if (showCard) {
                count++;
            }else {
                btnItem3.setBackgroundColor(getResources().getColor(R.color.tw_no_color));
            }

            btnItem2.setVisibility(storeId>0?VISIBLE:GONE);
            ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) btnItem3.getLayoutParams();
            if (storeId > 0) {
                layoutParams = (MarginLayoutParams) btnItem2.getLayoutParams();
            }
            if (count > 2) {
                layoutParams.topMargin = Util.dip2px(context, 30);

            } else if (count == 2) {
                layoutParams.topMargin = Util.dip2px(context, 15);
            }else {
                layoutParams.topMargin = Util.dip2px(context, 15);

            }
            findViewById(R.id.btn_item_2).setOnClickListener(this);

        } else if(type==TYPE_STORE){
            View btnItem5 = findViewById(R.id.btn_item_5);
            View btnItem4 = findViewById(R.id.btn_item_4);
            btnItem4.setBackgroundResource(R.drawable.black_dropdown_menu_separator);
            btnItem5.setVisibility(VISIBLE);
            findViewById(R.id.btn_item_1).setOnClickListener(this);
            findViewById(R.id.btn_item_2).setOnClickListener(this);


//            ViewGroup.MarginLayoutParams layoutParams = (MarginLayoutParams) btnItem5.getLayoutParams();
//            layoutParams.topMargin = Util.dip2px(context, 15);
        }else {
            findViewById(R.id.btn_item_1).setOnClickListener(this);
            findViewById(R.id.btn_item_2).setOnClickListener(this);
        }

        findViewById(R.id.btn_item_3).setOnClickListener(this);
        findViewById(R.id.btn_item_4).setOnClickListener(this);
        findViewById(R.id.btn_item_5).setOnClickListener(this);
    }

    // 如果要自定义弹窗的背景，不要给布局设置背景图片，重写这个方法返回一个Drawable即可
    @SuppressLint("UseCompatLoadingForDrawables")
    protected Drawable getPopupBackground() {
        if (type == TYPE_HOME_AND_MY || type == TYPE_CONTACT) {
            return getResources().getDrawable(R.drawable.black_menu_bg_small, null);
        } else if(type==TYPE_POST_DETAIL||type==TYPE_CHAT){

           return getResources().getDrawable(R.drawable.black_menu_bg_mini, null);
        } else{
            return getResources().getDrawable(R.drawable.black_menu_bg, null);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_item_1) {
            handleItem1Clicked();
        } else if (id == R.id.btn_item_2) {
            handleItem2Clicked();
        } else if (id == R.id.btn_item_3) {
            handleItem3Clicked();
        } else if (id == R.id.btn_item_4) {
            handleItem4Clicked();
        } else if (id == R.id.btn_item_5) {
            handleItem5Clicked();
        }

        dismiss();
    }

    private void handleItem5Clicked() {
        switch (type) {
            case TYPE_STORE:
                ((ShopMainFragment) baseFragment).getHomeFragment().pullShare();
                break;
            case TYPE_GOODS:
                // 商品分享
                ((GoodsDetailFragment)baseFragment).pullShare();
                break;
            default:break;
    }
    }

    private void handleItem1Clicked() {
        switch (type) {
            case TYPE_MESSAGE:
                // 通訊錄
                Util.startFragment(ContactFragment.newInstance());
                break;
            case TYPE_STORE:
            case TYPE_GOODS:
                // 商城首頁
                baseFragment.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.HOME_FRAGMENT);
                break;
            case TYPE_ORDER:
                // 商城首頁
                baseFragment.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.HOME_FRAGMENT);
                break;
            case TYPE_SELLER_GOODS:
                //商品管理
            default:
                break;
        }
    }

    private void handleItem2Clicked() {
        switch (type) {
            case TYPE_MESSAGE:
                // 掃一掃
                baseFragment.startCaptureActivity(AppServiceImpl.Companion.getCaptureIntent());
                break;
            case TYPE_STORE:
            case TYPE_GOODS:
                // 全站搜索
                baseFragment.start(CategoryFragment.newInstance(SearchType.GOODS, null));
                break;
            case TYPE_ORDER:
                // 購物袋
                baseFragment.start(CartFragment.newInstance(true));
                break;
            case TYPE_CHAT:
                int storeId = ((ChatFragment) baseFragment).getStoreId();
                new XPopup.Builder(context)
                        // 如果不加这个，评论弹窗会移动到软键盘上面
                        .moveUpToKeyboard(false)
                        .asCustom(new StoreCardPopup(context, storeId))
                        .show();
                break;
            default:
                break;
        }
    }


    private void handleItem3Clicked() {
        switch (type) {
            case TYPE_MESSAGE:
                // 添加朋友
                Util.startFragment(AddFriendFragment.newInstance());
                break;
            case TYPE_STORE:
                // 咨詢客服
                Util.startFragment(ShopCustomerServiceFragment.newInstance(((ShopMainFragment) baseFragment).getStoreId(), ((ShopMainFragment) baseFragment).getStoreFigure()));
                break;
            case TYPE_GOODS:
                ((GoodsDetailFragment) baseFragment).showStoreCustomerService();
                break;
            case TYPE_ORDER:
                // 個人專頁
                baseFragment.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.MY_FRAGMENT);
                break;
            case TYPE_HOME_AND_MY:
                // 商城首頁
                baseFragment.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.HOME_FRAGMENT);
                break;
            case TYPE_CONTACT:
                // 掃一掃
                baseFragment.startCaptureActivity(AppServiceImpl.Companion.getCaptureIntent());
                break;
            case TYPE_CHAT:
                // 查看資料 個人信息頁
                ChatFragment chatFragment = (ChatFragment) baseFragment;

                Util.startFragment(MemberInfoFragment.newInstance(chatFragment.getYourMemberName()));

                break;

            default:
                break;
        }
    }


    private void handleItem4Clicked() {
        switch (type) {
            case TYPE_MESSAGE:
                Util.startFragment(PersonalInfoFragment.newInstance());
                break;
            case TYPE_STORE:
            case TYPE_GOODS:
                // 消息
                Util.startFragment(MessageFragment.newInstance(true));
                break;
            case TYPE_ORDER:
                // 消息
                Util.startFragment(MessageFragment.newInstance(true));
                break;
            case TYPE_HOME_AND_MY:
                // 個人專頁
                baseFragment.popTo(MainFragment.class, false);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT, MainFragment.MY_FRAGMENT);
                break;
            case TYPE_CONTACT:
                // 添加朋友
                Util.startFragment(AddFriendFragment.newInstance());
                break;
            case TYPE_CHAT:
                ChatFragment chatFragment = (ChatFragment) baseFragment;
                //顯示電子名片
                Util.startFragment(ENameCardFragment.newInstance(chatFragment.getYourMemberName()));

                break;
            case TYPE_POST_DETAIL:
                Util.startFragment(CommitFeedbackFragment.newInstance());
                break;
            case TYPE_SELLER_GOODS:
                Util.startFragment(SellerFeaturesFragment.newInstance());
                break;
            default:
                break;
        }
    }
}
