package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.ftofs.twant.R;
import com.ftofs.twant.constant.SearchType;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Util;

/**
 * 搜索Fragment
 * @author zwm
 */
public class SearchFragment extends BaseFragment implements View.OnClickListener {
    EditText etKeyword;
    SearchType searchType = SearchType.GOODS;

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

        etKeyword = view.findViewById(R.id.et_keyword);

        TabLayout tabLayout = view.findViewById(R.id.search_tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.search_tab_title_goods)).setTag(SearchType.GOODS));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.search_tab_title_store)).setTag(SearchType.STORE));
        tabLayout.addTab(tabLayout.newTab().setText(getResources().getText(R.string.search_tab_title_article)).setTag(SearchType.ARTICLE));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                searchType = (SearchType) tab.getTag();
                SLog.info("searchType[%s]", searchType);
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
        if (id == R.id.btn_back) {
            pop();
        } else if (id == R.id.btn_clear_all) {
            // 清空搜索關鍵字
            etKeyword.setText("");
        } else if (id == R.id.btn_search) {
            String keyword = etKeyword.getText().toString();
            MainFragment mainFragment = MainFragment.getInstance();
            mainFragment.start(SearchResultFragment.newInstance(searchType.name()  , keyword));
        }
    }


    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
