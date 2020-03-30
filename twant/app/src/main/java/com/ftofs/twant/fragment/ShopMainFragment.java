package com.ftofs.twant.fragment;


import android.app.Notification;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.CustomerServiceStaff;
import com.ftofs.twant.entity.StoreNavigationItem;
import com.ftofs.twant.interfaces.NestedScrollingCallback;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.orm.ImNameMap;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.ftofs.twant.widget.ScaledButton;
import com.ftofs.twant.widget.SimpleTabManager;
import com.ftofs.twant.widget.StoreCustomerServicePopup;
import com.ftofs.twant.widget.WhiteDropdownMenu;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.material.tabs.TabLayout;
import com.hyphenate.chat.EMConversation;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONBase;
import cn.snailpad.easyjson.EasyJSONObject;
import de.hdodenhof.circleimageview.CircleImageView;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;


/**
 * 商店主Fragment容器
 * @author zwm
 */
public class ShopMainFragment extends BaseFragment implements View.OnClickListener, SimpleCallback, NestedScrollingCallback {
    // 商店Id
    int storeId;
    String storeFigure;
    LinearLayout toolbar;
    RelativeLayout preToolbar;

    TextView tvShopTitle;

    // 商店頭像圓形按鈕
    ImageView imgBottomBarShopAvatar;

    List<StoreNavigationItem> storeNavigationItemList = new ArrayList<>();

    // 商店名稱
    String storeName = "";

    FloatingActionMenu btnCustomerMenu;
    public ScaledButton btnSearch;
    LinearLayout llTabButtonContainer;

    SimpleTabManager simpleTabManager;

    /** 首頁 */
    public static final int HOME_FRAGMENT = 0;
    /** 產品 */
    public static final int COMMODITY_FRAGMENT = 1;
    /** 活動 */
    public static final int ACTIVITY_FRAGMENT = 2;
    /** 招聘 */
    public static final int RECRUITMENT_FRAGMENT = 3;
    /** 更多 */
    public static final int MORE_FRAGMENT = 4;

    private SupportFragment[] mFragments = new SupportFragment[4];
    private ImageView[] mCustomers = new ImageView[4];
    private int[] bottomBarButtonIds = new int[] {R.id.btn_home, R.id.btn_commodity,
            R.id.btn_activity, R.id.btn_recruitment, R.id.btn_more};
    private int[] bottomBarIconIds = new int[] {0, R.id.icon_shop_bottom_bar_commodity, R.id.icon_shop_bottom_bar_activity,
            R.id.icon_shop_bottom_bar_recruitment, R.id.icon_shop_bottom_bar_more};
    private ImageView[] bottomBarIcons = new ImageView[5];
    private int[] bottomBarIconResources = new int[] {0, R.drawable.icon_shop_bottom_bar_commodity, R.drawable.icon_shop_bottom_bar_activity,
             R.drawable.icon_shop_bottom_bar_recruitment, R.drawable.icon_shop_bottom_bar_more};
    private int[] bottomBarSelIconResources = new int[] {0, R.drawable.icon_shop_bottom_bar_commodity_sel, R.drawable.icon_shop_bottom_bar_activity_sel,
            R.drawable.icon_shop_bottom_bar_recruitment_sel, R.drawable.icon_shop_bottom_bar_more_sel};
    /*
    用于記錄滑動狀態，以處理浮動按鈕的顯示與隱藏
     */
    private static final int FLOAT_BUTTON_SCROLLING_EFFECT_DELAY = 800; // 浮動按鈕滑動顯示與隱藏效果的延遲時間(毫秒)
    boolean isScrolling = false; // 是否在滑動狀態
    long lastScrollingTimestamp = 0;  // 最近一次滑動的時間戳（毫秒）
    boolean floatButtonShown = true;  // 浮動按鈕是否有顯示
    LinearLayout llFloatButtonContainer;
    /**
     * 當前正在顯示的Fragment的下標
     */
    private int selectedFragmentIndex = HOME_FRAGMENT;
    private int commentChannel= Constant.COMMENT_CHANNEL_STORE;
    private List<ImageView> customerList =new ArrayList<>();
    private boolean customerListLoaded =false;
    private float serversAvatarSize =36;
    private LinearLayout llFloatButtonList;
    private ImageView customer1;
    private ImageView customer2;
    private ImageView customer3;
    private ImageView customerMore;
    private ImageView btnCustomer;
    private boolean customerExpanded =false;
    private int customerCount;
    private ImageView btnCart;
    private ImageView btnComment;
    RelativeLayout tool;

    public static ShopMainFragment newInstance(int shopId) {
        Bundle args = new Bundle();

        args.putInt("shopId", shopId);
        ShopMainFragment fragment = new ShopMainFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_main, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        storeId = args.getInt("shopId");
        llFloatButtonContainer = view.findViewById(R.id.ll_float_button_container);
        llFloatButtonList = view.findViewById(R.id.btn_customer_list);
        Util.setOnClickListener(view,R.id.btn_comment,this);
        tool = view.findViewById(R.id.tool_bar);
        toolbar = view.findViewById(R.id.tool_bar_main);
        preToolbar = view.findViewById(R.id.rv_pre_tool_bar);
        tvShopTitle = view.findViewById(R.id.tv_shop_title);
        imgBottomBarShopAvatar = view.findViewById(R.id.img_bottom_bar_shop_avatar);

        llTabButtonContainer = view.findViewById(R.id.ll_tab_button_container);
        Util.setOnClickListener(view,R.id.btn_menu,this);
        Util.setOnClickListener(view,R.id.btn_menu_round,this);
        Util.setOnClickListener(view,R.id.btn_cart,this);
        btnComment = view.findViewById(R.id.btn_comment);
        btnCart = view.findViewById(R.id.btn_cart);
        btnCart.setOnClickListener((v -> {
            if (User.getUserId() > 0) {
                Util.startFragment(CartFragment.newInstance(true));
            } else {
                Util.showLoginFragment();
            }
        }));
        initCustomerList(view);
        loadCustomerData(getContext());

        for (int id : bottomBarButtonIds) {
            Util.setOnClickListener(view, id, this);
        }

        for (int i = 0; i < 5; i++) {
            if (i == HOME_FRAGMENT) {
                continue;
            }
            bottomBarIcons[i] = view.findViewById(bottomBarIconIds[i]);
        }


        btnSearch = view.findViewById(R.id.btn_search);
        btnSearch.setOnClickListener(this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_back_round, this);
        Util.setOnClickListener(view, R.id.btn_search_round, this);

        simpleTabManager = new SimpleTabManager(0) {
            @Override
            public void onClick(View v) {
                boolean isRepeat = onSelect(v);
                int id = v.getId();
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }

                ShopCommodityFragment shopCommodityFragment = (ShopCommodityFragment) mFragments[COMMODITY_FRAGMENT];
                if (shopCommodityFragment != null) {
                    shopCommodityFragment.switchTabPage();
                }
            }
        };

        simpleTabManager.add(view.findViewById(R.id.stb_all));
        simpleTabManager.add(view.findViewById(R.id.stb_want_see));

        showGoodsFragment(false);

        String url = Api.PATH_STORE_NAVIGATION + "/" + storeId;
        SLog.info("url[%s]", url);
        Api.getUI(url, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                SLog.info("responseStr[%s]", responseStr);
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONArray navigationList = responseObj.getSafeArray("datas.navigationList");
                    for (Object object : navigationList) {
                        StoreNavigationItem item = (StoreNavigationItem) EasyJSONBase.jsonDecode(StoreNavigationItem.class, object.toString());
                        storeNavigationItemList.add(item);
                    }
                } catch (Exception e) {

                }
            }
        });
        setStoreNavigationItem();
    }

    private void initCustomerList(View view) {
        Util.setOnClickListener(view,R.id.btn_customer,this);
        Util.setOnClickListener(view,R.id.btn_customer1,this);
        Util.setOnClickListener(view,R.id.btn_customer2,this);
        Util.setOnClickListener(view,R.id.btn_customer3,this);
        Util.setOnClickListener(view,R.id.btn_customer_more,this);
        btnCustomer = view.findViewById(R.id.btn_customer);
        customer1 = view.findViewById(R.id.btn_customer1);
        customer2 = view.findViewById(R.id.btn_customer2);
        customer3 = view.findViewById(R.id.btn_customer3);
        customerMore = view.findViewById(R.id.btn_customer_more);
        mCustomers[0] = customer1;
        mCustomers[1] = customer2;
        mCustomers[2] = customer3;
        mCustomers[3] = customerMore;
    }

    /**
     * 初始化【更多】菜單按鈕
     *
     */
    private void setStoreNavigationItem() {
        // 添加兩個固定在頂部的菜單
        StoreNavigationItem storeNavigationItem = new StoreNavigationItem();
        storeNavigationItem.id = -1;
        storeNavigationItem.title = "產品想看";
        storeNavigationItemList.add(storeNavigationItem);

        storeNavigationItem = new StoreNavigationItem();
        storeNavigationItem.id = -2;
        storeNavigationItem.title = "聯繫我們";
        storeNavigationItemList.add(storeNavigationItem);
    }

    /**
     * 是否切換到顯示產品Fragment
     * @param show
     */
    private void showGoodsFragment(boolean show) {
        if (show) {
            tvShopTitle.setVisibility(View.GONE);
            btnCustomer.setVisibility(View.VISIBLE);
            btnCart.setVisibility(View.VISIBLE);
            btnComment.setVisibility(View.GONE);
            btnSearch.setVisibility(View.GONE);
            llTabButtonContainer.setVisibility(View.VISIBLE);
        } else {
            tvShopTitle.setVisibility(View.VISIBLE);
            btnCart.setVisibility(View.GONE);
            btnComment.setVisibility(View.VISIBLE);
            llTabButtonContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SupportFragment homeFragment = findChildFragment(ShopHomeFragment.class);

        if (homeFragment == null) {
            mFragments[HOME_FRAGMENT] = ShopHomeFragment.newInstance();
            mFragments[COMMODITY_FRAGMENT] = ShopCommodityFragment.newInstance(false, EasyJSONObject.generate("storeId", storeId).toString());
            mFragments[ACTIVITY_FRAGMENT] = ShopActivityFragment.newInstance();
            mFragments[RECRUITMENT_FRAGMENT] = ShopRecruitmentFragment.newInstance();


            loadMultipleRootFragment(R.id.fl_tab_container, selectedFragmentIndex,
                    mFragments[HOME_FRAGMENT],
                    mFragments[COMMODITY_FRAGMENT],
                    mFragments[ACTIVITY_FRAGMENT],
                    mFragments[RECRUITMENT_FRAGMENT]);
        } else {
            mFragments[HOME_FRAGMENT] = homeFragment;
            mFragments[COMMODITY_FRAGMENT] = findChildFragment(ShopCommodityFragment.class);
            mFragments[ACTIVITY_FRAGMENT] = findChildFragment(ShopActivityFragment.class);
            mFragments[RECRUITMENT_FRAGMENT] = findChildFragment(ShopRecruitmentFragment.class);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back||id==R.id.btn_back_round) {
            hideSoftInputPop();
        } else if (id == R.id.btn_menu||id==R.id.btn_menu_round) {
            SLog.info("here");
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity, 15))
                    .offsetY(-Util.dip2px(_mActivity, 9))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(v)
                    .asCustom(new BlackDropdownMenu(_mActivity, this, BlackDropdownMenu.TYPE_STORE))
                    .show();
        } else if (id == R.id.btn_search||id==R.id.btn_search_round) {
            start(ShopSearchFragment.newInstance(storeId, null));
        } else if(id==R.id.btn_customer){
            customerListShow();
        } else if(id==R.id.btn_comment){
            int userId = User.getUserId();
            if (userId == 0) {
                Util.showLoginFragment();
                return;
            }
            Util.startFragmentForResult(AddCommentFragment.newInstance(storeId, commentChannel), RequestCode.ADD_COMMENT.ordinal());
        }else { // 點擊底部導航欄
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
                onBottomBarClick(index);
            }
        }
    }

    private void customerListShow() {

        if (customerExpanded) {
            //收起
//            llFloatButtonList.getBackground().setAlpha(0);
            Glide.with(_mActivity).load(R.drawable.icon_customer_without_shadow).centerCrop().into(btnCustomer);
            for (int i=0;i<3;i++) {
                mCustomers[i].setVisibility(View.GONE);
            }
            customerMore.setVisibility(View.GONE);
        }else {
            //展開
//            btnCustomer.(R.drawable.icon_red_customer);

            if (customerCount== 1) {
                customer1.performClick();
                return;
            } else if (customerCount == 0) {
                ToastUtil.error(_mActivity,"當前店鋪未設置客服");
                return;
            }
            for (int i=0;i<3&&i<customerCount;i++) {
                mCustomers[i].setVisibility(View.VISIBLE);
            }
            SLog.info("展開顯示了%d個圖標",customerCount);
//            llFloatButtonList.getBackground().setAlpha(1);
            Glide.with(_mActivity).load(R.drawable.icon_red_customer).centerCrop().into(btnCustomer);
            if (customerCount > 3) {
                customerMore.setVisibility(View.VISIBLE);
            } else {
                customerMore.setVisibility(View.GONE);
            }
        }
        customerExpanded = !customerExpanded;
    }

    /**
     * 底部工具欄的點擊處理
     * @param index
     */
    public void onBottomBarClick(int index) {
        if (index == selectedFragmentIndex) {
            // 已經是當前Fragment，返回
            return;
        }

        if (index == ACTIVITY_FRAGMENT) {
            // 如果是點擊【商店活動】的按鈕，檢查用戶是否已經登錄
            if (!User.isLogin()) {
                Util.showLoginFragment();
                return;
            }
        }

        if (index == COMMODITY_FRAGMENT) { // 如果切換到產品Tab，頂部工具欄隱藏分隔線
            toolbar.setBackgroundColor(getResources().getColor(android.R.color.white, null));
            showGoodsFragment(true);
        } else { // 如果切換到其它Tab，恢復背景
            toolbar.setBackgroundResource(R.drawable.border_type_d);
            showGoodsFragment(false);
        }

        if (index == MORE_FRAGMENT) {
            ImageView imgIcon = bottomBarIcons[MORE_FRAGMENT];
            new XPopup.Builder(_mActivity)
                    .offsetX(-Util.dip2px(_mActivity, 45))
                    .offsetY(-Util.dip2px(_mActivity, 6))
//                        .popupPosition(PopupPosition.Right) //手动指定位置，有可能被遮盖
                    .hasShadowBg(false) // 去掉半透明背景
                    .atView(imgIcon)
                    .asCustom(new WhiteDropdownMenu(_mActivity, storeId, storeFigure, storeNavigationItemList, this))
                    .show();

            tmpSwitchSelectedIcon(false);
            return;
        }

        // 切換底部工具欄圖標的選中狀態
        if (selectedFragmentIndex != HOME_FRAGMENT) {
            ImageView imgIcon = bottomBarIcons[selectedFragmentIndex];
            imgIcon.setImageResource(bottomBarIconResources[selectedFragmentIndex]);
        }

        if (index != HOME_FRAGMENT) {
            ImageView imgIcon = bottomBarIcons[index];
            imgIcon.setImageResource(bottomBarSelIconResources[index]);
            toolbar.setBackgroundResource(R.drawable.border_type_d);
            hideTitle(true);
            getView().findViewById(R.id.line_title).setVisibility(View.INVISIBLE);
            adjustFlHeight(false);
            toolbar.setAlpha(1.0f);
            preToolbar.setAlpha(0);
        } else {
            toolbar.setBackgroundColor(getResources().getColor(R.color.tw_white,null));
            adjustFlHeight(true);
        }



        tvShopTitle.setText(storeName);

        SLog.info("index[%d], selectedFragmentIndex[%d]", index, selectedFragmentIndex);
        showHideFragment(mFragments[index], mFragments[selectedFragmentIndex]);
        selectedFragmentIndex = index;
    }


    public void setImgBottomBarShopAvatar(String url) {
        if (StringUtil.isEmpty(url)) {
            Glide.with(this).load(R.drawable.default_store_avatar).into(imgBottomBarShopAvatar);
        } else {
            Glide.with(this).load(url).into(imgBottomBarShopAvatar);
        }
    }

    public void setShopName(String storeName) {
        this.storeName = storeName;
        setFragmentTitle(storeName);
    }

    public void setFragmentTitle(String title) {
        tvShopTitle.setText(title);
    }

    public int getStoreId() {
        return storeId;
    }
    public void setStoreFigure(String storeFigure){this.storeFigure = storeFigure;}
    public String getStoreFigure(){ return storeFigure; }

    /**
     * 彈出【更多】菜單時，臨時高亮【更多】icon，不高亮原先的icon
     * 当【更多】菜单关闭时，恢复原先状态
     * @param isRestore 是否為恢復狀態
     */
    public void tmpSwitchSelectedIcon(boolean isRestore) {
        if (isRestore) {
            bottomBarIcons[MORE_FRAGMENT].setImageResource(R.drawable.icon_shop_bottom_bar_more);

            if (selectedFragmentIndex != HOME_FRAGMENT) {
                bottomBarIcons[selectedFragmentIndex].setImageResource(bottomBarSelIconResources[selectedFragmentIndex]);
            }
        } else {
            bottomBarIcons[MORE_FRAGMENT].setImageResource(R.drawable.icon_shop_bottom_bar_more_sel);

            if (selectedFragmentIndex != HOME_FRAGMENT) {
                bottomBarIcons[selectedFragmentIndex].setImageResource(bottomBarIconResources[selectedFragmentIndex]);
            }
        }
    }

    @Override
    public void onSimpleCall(Object data) {
        int btnId = (int) data;
        if (btnId == -1) { // 切換到商店想看視頻
            onBottomBarClick(COMMODITY_FRAGMENT);
            simpleTabManager.performClick(1);
        }
        // 彈出菜單關閉，恢復原先icon的高亮狀態
        tmpSwitchSelectedIcon(true);
    }


    @Override
    public void onCbStartNestedScroll() {
        isScrolling = true;
        lastScrollingTimestamp = System.currentTimeMillis();
        hideFloatButton();
    }

    private void showFloatButton() {
        if (floatButtonShown ){
            return;
        }
        llFloatButtonContainer.setTranslationX(0);
        floatButtonShown = true;
    }

    private void hideFloatButton() {
        if (floatButtonShown) {
            llFloatButtonContainer.setTranslationX(Util.dip2px(_mActivity, 25));
            floatButtonShown = false;
        }

    }

    @Override
    public void onCbStopNestedScroll() {
        if(!isScrolling){
            return;
        }

        isScrolling = false;
        llFloatButtonContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isScrolling) {
                    return;
                }

                showFloatButton();
            }
        }, FLOAT_BUTTON_SCROLLING_EFFECT_DELAY);

    }

    /**
     * 加載商店客服頭像數據
     */
    private void loadCustomerData(Context context) {
        String path = Api.PATH_STORE_CUSTOMER_SERVICE + "/" + storeId;
        SLog.info("path[%s]", path);

        Api.getUI(path, null, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(context, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                    if (ToastUtil.checkError(context, responseObj)) {
                        return;
                    }

                    EasyJSONArray serviceStaffList = responseObj.getSafeArray("datas.serviceStaffList");
                    customerList.clear();
                    if (serviceStaffList == null || serviceStaffList.length() == 0) {
                        btnCustomer.setOnClickListener(v ->
                                ToastUtil.error(context,"當前店鋪未設置客服"));
                        btnCustomerMenu.getMenuIconView().setOnClickListener(v ->
                                ToastUtil.error(context,"當前店鋪未設置客服"));
                        return;
                    }
                    customerCount = serviceStaffList.length();
                    if (serviceStaffList.length() > 3) {

                        customerMore.setOnClickListener(v -> {
                            new XPopup.Builder(_mActivity)
                                    // 如果不加这个，评论弹窗会移动到软键盘上面
                                    .moveUpToKeyboard(false)
                                    .asCustom(new StoreCustomerServicePopup(_mActivity, storeId,null))
                                    .show();
                        });
//                        customerMore.setVisibility(View.VISIBLE);
                    }

                    for (int i=0;i<serviceStaffList.length()&&i<3;i++) {
                        Object object = serviceStaffList.getObject(i);
                        EasyJSONObject serviceStaff = (EasyJSONObject) object;

                        CustomerServiceStaff staff = new CustomerServiceStaff();
                        Util.packStaffInfo(staff, serviceStaff);
//                        FloatingActionButton floatingActionButton = new FloatingActionButton(context);
//                        floatingActionButton.setButtonSize(FloatingActionButton.SIZE_MINI);
//                        CircleImageView view =new CircleImageView(context);
//                        view.setMaxWidth(25);
//                        view.setMaxHeight(25);


//                        floatingActionButton.setColorNormal(getResources().getColor(R.color.tw_white));
                        SLog.info("staff.avatar %s",staff.avatar);
                        boolean defaultAvatar = "img/default_avatar.png".equals(staff.avatar);
                        if (!defaultAvatar) {
                            Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(staff.avatar)).centerCrop().into(mCustomers[i]);
                        }
                        mCustomers[i].setOnClickListener(v ->{
                                    String memberName = staff.memberName;
                                    String imName = staff.imName;
                                    ImNameMap.saveMap(imName, memberName, storeId);

                                    FriendInfo friendInfo = new FriendInfo();
                                    friendInfo.memberName = memberName;
                                    friendInfo.nickname = staff.staffName;
                                    friendInfo.avatarUrl = staff.avatar;
                                    friendInfo.role = ChatUtil.ROLE_CS_AVAILABLE;
                                    if (StringUtil.isEmpty(imName)) {
                                        //存在客服im为空的情况
                                        imName = memberName;
                                    }
                                    imName = StringUtil.getPureMemberName(imName);
                                    FriendInfo.upsertFriendInfo(imName, staff.staffName, staff.avatar, ChatUtil.ROLE_CS_AVAILABLE,ChatUtil.ROLE_CS_AVAILABLE,storeName,storeId);
                                    EMConversation conversation = ChatUtil.getConversation(imName, staff.staffName, staff.avatar, ChatUtil.ROLE_CS_AVAILABLE);
                            if (conversation == null) {
                                SLog.info("创建会话失败，memberName %s", memberName);
                                ToastUtil.success(_mActivity, "暂时无法于此客服创建会话");
                            } else {
                                Util.startFragment(ChatFragment.newInstance(conversation, friendInfo));
                            }
                                });
//                        customerList.add(floatingActionButton);
                    }
                    customerListLoaded=true;

                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    public ShopHomeFragment getHomeFragment() {
        return (ShopHomeFragment)mFragments[HOME_FRAGMENT];
    }

    public void hideTitle(boolean isVisible) {
        View view = getView();
        if (view == null) {
            return;
        }
        view.findViewById(R.id.btn_back).setVisibility(isVisible?View.VISIBLE:View.INVISIBLE);
        view.findViewById(R.id.btn_menu).setVisibility(isVisible?View.VISIBLE:View.INVISIBLE);
        view.findViewById(R.id.btn_search).setVisibility(isVisible?View.VISIBLE:View.INVISIBLE);
        view.findViewById(R.id.line_title).setVisibility(isVisible?View.VISIBLE:View.INVISIBLE);
    }

    public void adjustFlHeight(boolean isTop) {
        ConstraintLayout layout= getView().findViewById(R.id.constraint_container);
        ConstraintSet set = new ConstraintSet();
        set.clone(layout);
        if (isTop) {
            set.connect(R.id.fl_tab_container,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
            set.connect(R.id.tool_bar,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
            set.applyTo(layout);
        } else {
            set.connect(R.id.fl_tab_container,ConstraintSet.TOP,R.id.tool_bar,ConstraintSet.BOTTOM);
            set.connect(R.id.tool_bar,ConstraintSet.TOP,ConstraintSet.PARENT_ID,ConstraintSet.TOP);
            set.applyTo(layout);
        }
    }
}
