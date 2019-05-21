package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.SearchHistoryUtil;
import com.ftofs.twant.util.Util;
import com.nex3z.flowlayout.FlowLayout;
import com.orhanobut.hawk.Hawk;

import org.litepal.LitePal;

import java.util.Set;

import cn.snailpad.easyjson.EasyJSONArray;


/**
 * 搜索Fragment
 * @author zwm
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener {
    EditText etKeyword;
    SearchType searchType = SearchType.GOODS;

    FlowLayout flSearchHistoryContainer;

    public static SearchFragment newInstance() {
        Bundle args = new Bundle();

        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_clear_all, this);
        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_search, this);
        Util.setOnClickListener(view, R.id.btn_clear_search_history, this);

        etKeyword = view.findViewById(R.id.et_keyword);
        flSearchHistoryContainer = view.findViewById(R.id.fl_search_history_container);

        TabLayout tabLayout = view.findViewById(R.id.search_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.search_tab_title_goods)).setTag(SearchType.GOODS));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.search_tab_title_store)).setTag(SearchType.STORE));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.search_tab_title_article)).setTag(SearchType.ARTICLE));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchType = (SearchType) tab.getTag();
                SLog.info("searchType[%s]", searchType);
                loadSearchHistory();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_clear_all:
                // 清空搜索關鍵字
                etKeyword.setText("");
                break;
            case R.id.btn_clear_search_history:
                SearchHistoryUtil.clearSearchHistory(searchType.ordinal());
                flSearchHistoryContainer.removeAllViews();
                break;
            case R.id.btn_search:
                String keyword = etKeyword.getText().toString().trim();
                MainFragment mainFragment = MainFragment.getInstance();
                mainFragment.start(SearchResultFragment.newInstance(searchType.name(), keyword));
                break;
            default:
                break;
        }
    }

    private void loadSearchHistory() {
        flSearchHistoryContainer.removeAllViews();

        Set<String> keywordSet = SearchHistoryUtil.loadSearchHistory(searchType.ordinal());

        for (String keyword : keywordSet) {
            addSearchHistoryItemView(keyword);
        }
    }

    private void addSearchHistoryItemView(String keyword) {
        TextView textView = new TextView(_mActivity);

        textView.setPadding(Util.dip2px(_mActivity, 16), Util.dip2px(_mActivity, 6),
                Util.dip2px(_mActivity, 16), Util.dip2px(_mActivity, 6));
        textView.setText(keyword);
        textView.setTextSize(14);
        textView.setTextColor(getResources().getColor(R.color.tw_black, null));
        textView.setBackgroundResource(R.drawable.search_item_bg);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView tv = (TextView) v;
                String keyword = tv.getText().toString();
                MainFragment mainFragment = MainFragment.getInstance();
                mainFragment.start(SearchResultFragment.newInstance(searchType.name(), keyword));
            }
        });

        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        flSearchHistoryContainer.addView(textView, layoutParams);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();

        loadSearchHistory();
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
