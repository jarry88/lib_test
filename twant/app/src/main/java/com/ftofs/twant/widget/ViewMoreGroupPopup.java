package com.ftofs.twant.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.BuildConfig;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.AppGuideActivity;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.activity.SplashActivity;
import com.ftofs.twant.adapter.GroupListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.CustomAction;
import com.ftofs.twant.constant.SPField;
import com.ftofs.twant.entity.CustomActionData;
import com.ftofs.twant.entity.GroupListItem;
import com.ftofs.twant.fragment.ConfirmOrderFragment;
import com.ftofs.twant.interfaces.SimpleCallback;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.Jarbon;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.XPopupUtils;
import com.orhanobut.hawk.Hawk;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 查看更多拼團信息彈窗
 * @author zwm
 */
public class ViewMoreGroupPopup extends CenterPopupView implements View.OnClickListener {
    Context context;
    int commonId;
    SimpleCallback simpleCallback;

    RecyclerView rvList;
    GroupListAdapter adapter;
    List<GroupListItem> groupList = new ArrayList<>();

    public ViewMoreGroupPopup(@NonNull Context context, int commonId, SimpleCallback simpleCallback) {
        super(context);

        this.context = context;
        this.commonId = commonId;
        this.simpleCallback = simpleCallback;
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.view_more_group_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_close).setOnClickListener(this);

        rvList = findViewById(R.id.rv_list);
        rvList.setLayoutManager(new LinearLayoutManager(context));
        adapter = new GroupListAdapter(context, R.layout.group_list_item, groupList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_join_group) {
                    int goId = groupList.get(position).goId;

                    if (simpleCallback != null) {
                        CustomActionData customActionData = new CustomActionData();
                        customActionData.action = CustomAction.CUSTOM_ACTION_SELECT_JOIN_GROUP;
                        customActionData.data = EasyJSONObject.generate(
                                "goId", goId
                        );
                        simpleCallback.onSimpleCall(customActionData);
                    }

                    dismiss();
                }
            }
        });
        rvList.setAdapter(adapter);

        EasyJSONObject params = EasyJSONObject.generate(
                "commonId", commonId
        );
        SLog.info("url[%s], params[%s]", Api.PATH_GROUP_LIST, params);
        Api.getUI(Api.PATH_GROUP_LIST, params, new UICallback() {
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

                    EasyJSONArray groupLogOpenVoList = responseObj.getSafeArray("datas.groupLogOpenVoList");
                    for (Object object : groupLogOpenVoList) {
                        EasyJSONObject groupLogOpenVo = (EasyJSONObject) object;

                        GroupListItem groupListItem = new GroupListItem(
                                groupLogOpenVo.getInt("goId"),
                                groupLogOpenVo.getLong("endTime"),
                                groupLogOpenVo.getSafeString("memberAvatar"),
                                groupLogOpenVo.getInt("requireNum"),
                                groupLogOpenVo.getInt("joinedNum")
                        );

                        groupList.add(groupListItem);
                    }

                    adapter.setNewData(groupList);
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }

    //完全可见执行
    @Override
    protected void onShow() {
        super.onShow();
    }

    //完全消失执行
    @Override
    protected void onDismiss() {
        // 關閉時，清除計時器
        adapter.cancelAllTimers();
    }

    @Override
    protected int getMaxWidth() {
        return (int) (XPopupUtils.getWindowWidth(getContext()) * 0.8f);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.btn_close) {
            dismiss();
        }
    }
}


