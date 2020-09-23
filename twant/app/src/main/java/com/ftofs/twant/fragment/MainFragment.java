package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.ftofs.twant.login.UserManager;
import com.ftofs.twant.R;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.login.service.LoginServiceImpl;
import com.github.richardwrq.krouter.annotation.Inject;
import com.github.richardwrq.krouter.api.core.KRouter;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.service.ConstantsPath;
import com.gzp.lib_common.service.login.LoginService;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.tangram.NewShoppingSpecialFragment;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BasePopupView;
import com.orhanobut.hawk.Hawk;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.ISupportFragment;
import me.yokeyword.fragmentation.SupportFragment;

/**
 * 主Fragment容器
 * @author zwm
 */
public class MainFragment extends BaseFragment implements View.OnClickListener {
    /** 首頁 */
    public static final int HOME_FRAGMENT = 0;
    /** 消息 */
    public static final int MESSAGE_FRAGMENT = 1;
    /** 想要圈 */
    public static final int CIRCLE_FRAGMENT = 2;
    /** 購物袋 */
    public static final int CART_FRAGMENT = 3;
    /** 專頁 */
    public static final int MY_FRAGMENT = 4;
    BasePopupView mLoading;
    TextView tvMessageItemCount; // 顯示未讀消息條數的紅點
    TextView tvCartItemCount;    // 顯示購物袋中產品數的紅點

    private SupportFragment[] mFragments = new SupportFragment[5];
    private int[] bottomBarButtonIds = new int[] {R.id.btn_home, R.id.btn_message, R.id.btn_circle,
                                                    R.id.btn_cart, R.id.btn_my};
    private ImageView[] bottomBarIcons = new ImageView[5];
    private int[] bottomBarIconResources = new int[] {R.drawable.icon__bottom_bar_home, R.drawable.icon__bottom_bar_message, R.drawable.icon__bottom_bar_want,
                                                        R.drawable.icon__bottom_bar_cart, R.drawable.icon__bottom_bar_my};
    private int[] bottomBarSelIconResources = new int[] {R.drawable.icon__bottom_bar_home_sel, R.drawable.icon__bottom_bar_message_sel, R.drawable.icon__bottom_bar_want_sel,
            R.drawable.icon__bottom_bar_cart_sel, R.drawable.icon__bottom_bar_my_sel};

    /**
     * 當前正在顯示的Fragment的下標
     */
    public int selectedFragmentIndex = HOME_FRAGMENT;

    private static MainFragment instance;


    @Inject(name = ConstantsPath.LOGIN_SERVICE_PATH)
    LoginServiceImpl loginService;
    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);

        return fragment;
    }


    public static MainFragment getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        instance = this;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);
        KRouter.INSTANCE.inject(this);
        tvMessageItemCount = view.findViewById(R.id.tv_message_item_count);
        tvCartItemCount = view.findViewById(R.id.tv_cart_item_count);

        tvMessageItemCount.postDelayed(new Runnable() {
            @Override
            public void run() {
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_UPDATE_TOOLBAR_RED_BUBBLE, null);
            }
        }, 500);

        for (int id : bottomBarButtonIds) {
            Util.setOnClickListener(view, id, this);
        }

        bottomBarIcons[HOME_FRAGMENT] = view.findViewById(R.id.icon_home);
        bottomBarIcons[MESSAGE_FRAGMENT] = view.findViewById(R.id.icon_message);
        bottomBarIcons[CIRCLE_FRAGMENT] = view.findViewById(R.id.icon_circle);
        bottomBarIcons[CART_FRAGMENT] = view.findViewById(R.id.icon_cart);
        bottomBarIcons[MY_FRAGMENT] = view.findViewById(R.id.icon_my);
    }
    public HomeFragment getHomeFragment() {
        HomeFragment homeFragment = (HomeFragment) mFragments[HOME_FRAGMENT];
        return homeFragment;
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportFragment homeFragment = findChildFragment(HomeFragment.class);

        if (homeFragment == null) {
            mFragments[HOME_FRAGMENT] = HomeFragment.newInstance();
            mFragments[MESSAGE_FRAGMENT] = MessageFragment.newInstance(false);
            mFragments[CIRCLE_FRAGMENT] = CircleFragment.newInstance(false, null);
            mFragments[CART_FRAGMENT] = CartFragment.newInstance(false);
            mFragments[MY_FRAGMENT] = MyFragment.newInstance();


            loadMultipleRootFragment(R.id.fl_tab_container, selectedFragmentIndex,
                    mFragments[HOME_FRAGMENT],
                    mFragments[MESSAGE_FRAGMENT],
                    mFragments[CIRCLE_FRAGMENT],
                    mFragments[CART_FRAGMENT],
                    mFragments[MY_FRAGMENT]);
        } else {
            mFragments[HOME_FRAGMENT] = homeFragment;
            mFragments[MESSAGE_FRAGMENT] = findChildFragment(MessageFragment.class);
            mFragments[CIRCLE_FRAGMENT] = findChildFragment(CircleFragment.class);
            mFragments[CART_FRAGMENT] = findChildFragment(CartFragment.class);
            mFragments[MY_FRAGMENT] = findChildFragment(MyFragment.class);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        int len = bottomBarButtonIds.length;
        // 想要选中的Fragment的下标
        int index = -1;
        for (int i = 0; i < len; i++) {
            if (id == bottomBarButtonIds[i]) {
                index = i;
            }
        }

        SLog.info("index[%d], selectedFragmentIndex[%d]", index, selectedFragmentIndex);
        // 如果index不等于-1，表示是按下BottomBar的按鈕
        if (index != -1) {
            if (index == selectedFragmentIndex) {
                // 已經是當前Fragment，返回
                return;
            }

            if (index == MESSAGE_FRAGMENT || index == CART_FRAGMENT || index == MY_FRAGMENT) {
                // 如果是查看【消息】、【購物袋】或【我的】，先檢查是否已經登錄
                if (User.getUserId()<1) {
                    Util.showLoginFragment(requireContext());
                    SLog.info("前往啓動頁");
//                    UserManager.INSTANCE.start(getContext());
                    return;
                }
            }

            showHideFragment(index);
        }
    }

    public void showHideFragment(int index) {
        if (!isAdded()) {
            return;
        }
        SLog.info("切換【%s】",index);
        showHideFragment(mFragments[index], mFragments[selectedFragmentIndex]);

        // 切換未選中圖標
        bottomBarIcons[selectedFragmentIndex].setImageResource(bottomBarIconResources[selectedFragmentIndex]);

        // 切換選中圖標
        bottomBarIcons[index].setImageResource(bottomBarSelIconResources[index]);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        SLog.info("onFragmentResult, requestCode[%d], resultCode[%d]", requestCode, resultCode);
        super.onFragmentResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        // 如果退出登錄，顯示主頁
        if (message.messageType == EBMessageType.MESSAGE_TYPE_LOGOUT_SUCCESS) {
            showHideFragment(HOME_FRAGMENT);
            setMessageItemCount(0); // 未讀消息數置0
            setCartItemCount(0); // 購物袋產品數置0
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT) {
            int fragmentIndex = (int) message.data;
            if (HOME_FRAGMENT <= fragmentIndex && fragmentIndex <= MY_FRAGMENT) {
                showHideFragment(fragmentIndex);
            }
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_WALLET_PAY_SUCCESS) {
            int payId = (int) message.data;
            SLog.info("EBMessageType.MESSAGE_TYPE_WALLET_PAY_SUCCESS, payId[%d]", payId);
            tvMessageItemCount.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start(PaySuccessFragment.newInstance(payId));
                }
            }, 250);
        } else if (message.messageType == EBMessageType.LOADING_POPUP_DISMISS) {
            if (mLoading != null) {
                mLoading.dismiss();
            }
        }else if(message.messageType==EBMessageType.SHOW_LOADING){
            if (mLoading == null) {
                mLoading = Util.createLoadingPopup(getContext());
            }
            mLoading.show();
        }
    }

    public void setMessageItemCount(int count) {
        SLog.info("count[%d]", count);
        if (count == 0) { // 如果沒有數據，則隱藏
            tvMessageItemCount.setVisibility(View.GONE);
        } else {
            tvMessageItemCount.setText(String.valueOf(count));
            tvMessageItemCount.setVisibility(View.VISIBLE);
        }
    }

    public void setCartItemCount(int count) {
        SLog.info("count[%d]", count);
        if (count == 0) { // 如果沒有數據，則隱藏
            tvCartItemCount.setVisibility(View.GONE);
        } else {
            tvCartItemCount.setText(String.valueOf(count));
            tvCartItemCount.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        // 回到前臺後，看是否有未處理的友盟自定義消息
        handleUmengCustomAction();
    }

    @Override
    public void onSupportInvisible() {
        super.onSupportInvisible();
    }

    /**
     * 處理友盟自定義動作消息
     */
    public void handleUmengCustomAction() {
        // 根據msgId獲取消息數據
        String extraDataStr = Hawk.get(SPField.FIELD_UNHANDLED_UMENG_MESSAGE_PARAMS);
        SLog.info("extraDataStr[%s]", extraDataStr);

        // 讀取完成後，就可以刪除記錄
        Hawk.delete(SPField.FIELD_UNHANDLED_UMENG_MESSAGE_PARAMS);

        EasyJSONObject extraDataObj = EasyJSONObject.parse(extraDataStr);
        if (extraDataObj == null) {
            return;
        }

        /*
        描述 key	value	value   樣式
店鋪詳情頁	store_id    具體的店鋪ID	1
商品詳情頁	common_id 具體的商品SPU編碼	1234
主題活動	shoppingzone_id 具體的專場ID	25
想要圈列表	wantpost wantpost  wantpost
想要圈貼文	wantpost_id 具體的貼文ID	2290
店鋪優惠列表	storeDiscounts_id	具體的店鋪ID	23
個人專頁優惠券頁	mineDiscounts	mineDiscounts mineDiscounts
         */
        try {
            if (extraDataObj.exists("store_id")) { // 店鋪詳情頁
                String val = extraDataObj.getSafeString("store_id");
                int storeId = Integer.parseInt(val);
                start(ShopMainFragment.newInstance(storeId));
            } else if (extraDataObj.exists("common_id")) { // 商品詳情頁
                String val = extraDataObj.getSafeString("common_id");
                int commonId = Integer.parseInt(val);
                start(GoodsDetailFragment.newInstance(commonId, 0));
            } else if (extraDataObj.exists("shoppingzone_id")) { // 主題活動
                String val = extraDataObj.getSafeString("shoppingzone_id");
                int zoneId = Integer.parseInt(val);
                start(NewShoppingSpecialFragment.newInstance(zoneId));
            } else if (extraDataObj.exists("wantpost")) { // 想要圈列表
                Util.popToMainFragment(_mActivity);
                showHideFragment(CIRCLE_FRAGMENT);
            } else if (extraDataObj.exists("wantpost_id")) { // 想要圈貼文
                String val = extraDataObj.getSafeString("wantpost_id");
                int postId = Integer.parseInt(val);
                start(PostDetailFragment.newInstance(postId));
            } else if (extraDataObj.exists("storeDiscounts_id")) { // 店鋪優惠列表
                String val = extraDataObj.getSafeString("storeDiscounts_id");
                int storeId = Integer.parseInt(val);
                start(ShopMainFragment.newInstance(storeId, ShopMainFragment.ACTIVITY_FRAGMENT));
            } else if (extraDataObj.exists("mineDiscounts")) { // 個人專頁優惠券頁
                if (User.isLogin()) {
                    start(CouponFragment.newInstance());
                } else {
                    Util.showLoginFragment(requireContext());
                }
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public void goLogin(ISupportFragment fragment) {
        loginService.start(requireContext());
    }
}


