package com.ftofs.twant.seller.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.CommonFragmentPagerAdapter;
import com.ftofs.twant.constant.CustomAction;
import com.ftofs.twant.constant.PopupType;
import com.ftofs.twant.entity.ListPopupItem;
import com.gzp.lib_common.base.BaseFragment;
import com.ftofs.twant.interfaces.OnSelectedListener;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.BlackDropdownMenu;
import com.ftofs.twant.widget.ListPopup;
import com.ftofs.twant.widget.SimpleTabButton;
import com.ftofs.twant.widget.SimpleTabManager;
import com.ftofs.twant.widget.UnscrollableViewPager;
import com.lxj.xpopup.XPopup;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

public class SellerGoodsListFragment extends BaseFragment implements View.OnClickListener, SimpleCallback, OnSelectedListener {
    SimpleTabButton[] tabButtons;

    UnscrollableViewPager viewPager;
    List<Fragment> fragmentList = new ArrayList<>();
    public static final int TAB_GOODS_IN_SALE = 0; // 出售中的商品
    public static final int TAB_GOODS_IN_STOCK = 1; // 倉庫中的商品

    int currTab;
    public static final int TAB_COUNT = 2;

    EditText etKeyword;

    public static SellerGoodsListFragment newInstance() {
        return newInstance(TAB_GOODS_IN_SALE);
    }

    public static SellerGoodsListFragment newInstance(int tab) {
        Bundle args = new Bundle();

        SellerGoodsListFragment fragment = new SellerGoodsListFragment();
        fragment.setArguments(args);
        fragment.currTab = tab;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_seller_goods_list, container, false);

        return view;
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        SLog.info("onSupportVisible");

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_publish_goods, this);
        Util.setOnClickListener(view, R.id.btn_more_menu, this);

        etKeyword = view.findViewById(R.id.et_keyword);
        etKeyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String keyword = textView.getText().toString().trim();
                    SellerGoodsListPageFragment sellerGoodsListPageFragment = (SellerGoodsListPageFragment) fragmentList.get(currTab);
                    if (sellerGoodsListPageFragment != null) {
                        sellerGoodsListPageFragment.setKeyword(keyword);
                    }

                    hideSoftInput();

                    return true;
                }
                return false;
            }
        });


        tabButtons = new SimpleTabButton[TAB_COUNT];

        SimpleTabManager tabManager = new SimpleTabManager(TAB_GOODS_IN_SALE) {
            @Override
            public void onClick(View v) {
                int id = v.getId();
                boolean isRepeat = onSelect(v);
                SLog.info("id[%d]", id);
                if (isRepeat) {
                    return;
                }

                if (id == R.id.tab_goods_in_sale) {
                    currTab = TAB_GOODS_IN_SALE;
                } else if (id == R.id.tab_goods_in_stock) {
                    currTab = TAB_GOODS_IN_STOCK;
                }

                SLog.info("currTab[%d]", currTab);
                viewPager.setCurrentItem(currTab);
            }
        };

        tabManager.add(view.findViewById(R.id.tab_goods_in_sale));
        tabManager.add(view.findViewById(R.id.tab_goods_in_stock));

        viewPager = view.findViewById(R.id.vp_page_list);
        viewPager.setCanScroll(false);
        viewPager.setOffscreenPageLimit(TAB_COUNT - 1);

        if (currTab != TAB_GOODS_IN_SALE) { // 如果不是默認的Tab, 選中對應的Tab
            viewPager.postDelayed(new Runnable() {
                @Override
                public void run() {
                    tabManager.performClick(currTab);
                }
            }, 500);
        }

        List<String> titleList = new ArrayList<>();
        titleList.add("出售中的商品");
        titleList.add("倉庫中的商品");

        fragmentList.add(SellerGoodsListPageFragment.newInstance(TAB_GOODS_IN_SALE, this));
        fragmentList.add(SellerGoodsListPageFragment.newInstance(TAB_GOODS_IN_STOCK, this));

        // 將getSupportFragmentManager()改為getChildFragmentManager(), 解決關閉登錄頁面后，重新打開后，
        // ViewPager中Fragment不回調onCreateView的問題
        CommonFragmentPagerAdapter adapter = new CommonFragmentPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        viewPager.setAdapter(adapter);
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
        } else if (id == R.id.btn_filter) {

        } else if (id == R.id.btn_publish_goods) {
            Util.startFragment(AddGoodsFragment.newInstance());
        } else if (id == R.id.btn_more_menu) {
            List<ListPopupItem> itemList = new ArrayList<>();
            itemList.add(new ListPopupItem(0, "商品管理", null));
            itemList.add(new ListPopupItem(1, "商品發布", null));
            itemList.add(new ListPopupItem(2, "商品規格", null));
            itemList.add(new ListPopupItem(3, "鎮店之寶", null));

            hideSoftInput();
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new ListPopup(_mActivity, "請選擇操作",
                            PopupType.MENU, itemList, -1, this)).show();
        }else if (id == R.id.btn_more) {
            new XPopup.Builder(_mActivity).moveUpToKeyboard(false)
                    .offsetX(-Util.dip2px(_mActivity, 11))
                    .offsetY(-Util.dip2px(_mActivity, 8))
                    .hasShadowBg(false)
                    .atView(v)
                    .asCustom(new BlackDropdownMenu(
                            _mActivity, this,
                            BlackDropdownMenu.TYPE_SELLER_GOODS))
                    .show();
        }
    }

    @Override
    public void onSimpleCall(Object data) {
        EasyJSONObject dataObj = (EasyJSONObject) data;

        try {
            int action = dataObj.getInt("action");
            if (CustomAction.CUSTOM_ACTION_RELOAD_DATA.ordinal() == action) {
                for (Fragment fragment : fragmentList) {
                    SellerGoodsListPageFragment sellerGoodsListPageFragment = (SellerGoodsListPageFragment) fragment;
                    sellerGoodsListPageFragment.reloadData();
                }
            }
        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public void onSelected(PopupType type, int id, Object extra) {
        SLog.info("onSelected, type[%s], id[%d], extra[%s]", type, id, extra);
        if (type == PopupType.MENU) {
            if (id == 1) {
                start(AddGoodsFragment.newInstance());
            } else if (id == 2) { // 商品規格
                start(SellerSpecFragment.newInstance());
            } else if (id == 3) {//鎮店之寶
                start(SellerFeaturesFragment.newInstance());
            }
        }
    }
}


