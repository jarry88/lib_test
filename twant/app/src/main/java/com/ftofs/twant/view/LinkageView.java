package com.ftofs.twant.view;
/*
 * Copyright (c) 2018-2019. KunMinX
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;


import com.ftofs.twant.R;
import com.ftofs.twant.adapter.TwantLinkageSecondaryAdapter;
import com.ftofs.twant.entity.ElemeGroupedItem;
import com.kunminx.linkage.adapter.LinkageSecondaryAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Create by Gzp at 20/5/6
 */
public class LinkageView<T extends ElemeGroupedItem.ItemInfo> extends RelativeLayout {

    private static final int DEFAULT_SPAN_COUNT = 1;
    private static final int SCROLL_OFFSET = 0;

    private Context mContext;

    private RecyclerView mRvPrimary;
    private RecyclerView mRvSecondary;
    private LinearLayout mLinkageLayout;


    private TextView mTvHeader;
    private FrameLayout mHeaderContainer;

    private List<String> mInitGroupNames;
    private List<ElemeGroupedItem> mInitItems;

    private List<Integer> mHeaderPositions = new ArrayList<>();
    private int mTitleHeight;
    private int mFirstVisiblePosition;
    private String mLastGroupName;
    private LinearLayoutManager mSecondaryLayoutManager;
    private LinearLayoutManager mPrimaryLayoutManager;

    private boolean mScrollSmoothly = true;

    public LinkageView(Context context) {
        super(context);
    }

    public LinkageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public LinkageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        this.mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.layout_linkage_view, this);
        mRvPrimary = (RecyclerView) view.findViewById(R.id.rv_primary);
        mRvSecondary = (RecyclerView) view.findViewById(R.id.rv_secondary);
        mHeaderContainer = (FrameLayout) view.findViewById(R.id.header_container);
        mLinkageLayout = (LinearLayout) view.findViewById(R.id.linkage_layout);
    }


 



}
