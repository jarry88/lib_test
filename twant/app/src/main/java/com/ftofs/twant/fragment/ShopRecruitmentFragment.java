package com.ftofs.twant.fragment;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.StoreJobAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.WantedPostItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import me.yokeyword.fragmentation.SupportFragment;
import okhttp3.Call;

public class ShopRecruitmentFragment extends BaseFragment {
    RecyclerView rvList;
    StoreJobAdapter storeJobAdapter;
    ShopMainFragment parentFragment;

    List<WantedPostItem> wantedPostItemList = new ArrayList<>();
    private boolean dataLoaded;

    public static ShopRecruitmentFragment newInstance() {
        Bundle args = new Bundle();

        ShopRecruitmentFragment fragment = new ShopRecruitmentFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop_recruitment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        parentFragment = (ShopMainFragment) getParentFragment();

        rvList = view.findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(_mActivity));
        storeJobAdapter = new StoreJobAdapter(R.layout.store_job_item, wantedPostItemList);
        storeJobAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WantedPostItem item = wantedPostItemList.get(position);
                Util.startFragment(JobDetailFragment.newInstance(item));
            }
        });

        // 設置空頁面
        View emptyView = LayoutInflater.from(_mActivity).inflate(R.layout.layout_placeholder_no_data, null, false);
        // 設置空頁面的提示語
        TextView tvEmptyHint = emptyView.findViewById(R.id.tv_empty_hint);
        tvEmptyHint.setText(R.string.no_data_hint);
        storeJobAdapter.setEmptyView(emptyView);

        rvList.setAdapter(storeJobAdapter);

    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        if (!dataLoaded) {
            loadData();
        }
    }

    private void loadData(){
        try{
            SLog.info("here__");
            String path = Api.PATH_STORE_HRPOST+"/"+parentFragment.getStoreId();
            EasyJSONObject parse = EasyJSONObject.generate();
            Api.getUI(path, parse, new UICallback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    ToastUtil.showNetworkError(_mActivity,e);
                }

                @Override
                public void onResponse(Call call, String responseStr) throws IOException {
                    try {
                        EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                        EasyJSONArray hrPostList = responseObj.getSafeArray("datas.hrPostList");
                        if (hrPostList != null && hrPostList.length() > 0) {
                            SLog.info("hrPostList.length[%d]", hrPostList.length());

                            for (Object object : hrPostList) {
                                EasyJSONObject hrPost = (EasyJSONObject) object;

                                WantedPostItem item = new WantedPostItem();
                                item.postId = hrPost.getInt("postId");
                                item.postTitle = hrPost.getSafeString("postTitle");
                                item.postType = hrPost.getSafeString("postType");
                                item.postArea = hrPost.getSafeString("postArea");
                                item.salaryType = hrPost.getSafeString("salaryType");
                                item.salaryRange = hrPost.getSafeString("salaryRange");
                                item.currency = hrPost.getSafeString("currency");
                                item.postDescription = hrPost.getSafeString("postDescription");
                                item.mailbox = hrPost.getSafeString("mailbox");
                                item.isFavor = hrPost.getInt("isFavor");

                                wantedPostItemList.add(item);
                            }
                            SLog.info("wantedPostItemList.size[%d]", wantedPostItemList.size());
                            storeJobAdapter.setNewData(wantedPostItemList);
                            dataLoaded = true;
                        }
                    }catch (Exception e){
                        SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                    }
                }
            });
        } catch (Exception e){
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        ((SupportFragment) getParentFragment()).pop();
        return true;
    }
}
