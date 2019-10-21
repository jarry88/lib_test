package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.enums.PopupAnimation;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    /** 購物籃 */
    public static final int CART_FRAGMENT = 3;
    /** 專頁 */
    public static final int MY_FRAGMENT = 4;

    TextView tvMessageItemCount; // 顯示未讀消息條數的紅點
    TextView tvCartItemCount;    // 顯示購物籃中商品數的紅點

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
    private int selectedFragmentIndex = HOME_FRAGMENT;

    private static MainFragment instance;


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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        instance = this;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

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
                // 如果是查看【消息】、【購物籃】或【我的】，先檢查是否已經登錄
                if (!User.isLogin()) {
                    Util.showLoginFragment();
                    return;
                }
            }

            showHideFragment(index);
        }
    }

    public void showHideFragment(int index) {
        showHideFragment(mFragments[index], mFragments[selectedFragmentIndex]);

        // 切換未選中圖標
        bottomBarIcons[selectedFragmentIndex].setImageResource(bottomBarIconResources[selectedFragmentIndex]);

        selectedFragmentIndex = index;

        // 切換選中圖標
        bottomBarIcons[selectedFragmentIndex].setImageResource(bottomBarSelIconResources[selectedFragmentIndex]);

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
            setCartItemCount(0); // 購物籃商品數置0
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_SHOW_FRAGMENT) {
            int fragmentIndex = (int) message.data;
            if (HOME_FRAGMENT <= fragmentIndex && fragmentIndex <= MY_FRAGMENT) {
                showHideFragment(fragmentIndex);
            }
        } else if (message.messageType == EBMessageType.MESSAGE_TYPE_WALLET_PAY_SUCCESS) {
            SLog.info("EBMessageType.MESSAGE_TYPE_WALLET_PAY_SUCCESS");
            tvMessageItemCount.postDelayed(new Runnable() {
                @Override
                public void run() {
                    start(PaySuccessFragment.newInstance(""));
                }
            }, 250);
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
}


