package com.ftofs.twant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.GoodsEvaluationAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.order.OrderDetailGoodsItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SquareGridLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

public class GoodsEvaluationFragment extends BaseFragment implements View.OnClickListener {
    SquareGridLayout sglImageContainer;
    ImageView btnAddImage;
    RelativeLayout rlButtonContainer;
    EditText etContent;

    int storeId;
    String storeName;
    List<OrderDetailGoodsItem> orderDetailGoodsItemList;

    int currAddImagePosition; // 當前要添加圖片的評價項
    RecyclerView rvEvaluationList;
    GoodsEvaluationAdapter adapter;

    public static GoodsEvaluationFragment newInstance(int storeId, String storeName, List<OrderDetailGoodsItem> orderDetailGoodsItemList) {
        Bundle args = new Bundle();

        GoodsEvaluationFragment fragment = new GoodsEvaluationFragment();
        fragment.setArguments(args);

        fragment.setStoreId(storeId);
        fragment.setStoreName(storeName);
        fragment.setOrderDetailGoodsItemList(orderDetailGoodsItemList);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_evaluation, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        etContent = view.findViewById(R.id.et_content);
        sglImageContainer = view.findViewById(R.id.sgl_image_container);
        rlButtonContainer = view.findViewById(R.id.rl_button_container);
        btnAddImage = view.findViewById(R.id.btn_add_image);


        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);

        rvEvaluationList = view.findViewById(R.id.rv_evaluation_list);
        adapter = new GoodsEvaluationAdapter(R.layout.goods_evaluation_item, storeId, storeName, orderDetailGoodsItemList);
        adapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                int id = view.getId();
                if (id == R.id.btn_add_image) {
                    currAddImagePosition = position;
                    openSystemAlbumIntent(RequestCode.OPEN_ALBUM.ordinal()); // 打开相册
                }
            }
        });
        rvEvaluationList.setLayoutManager(new LinearLayoutManager(_mActivity));
        rvEvaluationList.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_back:
                pop();
                break;
            case R.id.btn_commit:
                commitComment();
                break;
            default:
                break;
        }
    }

    private void commitComment() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        String content = etContent.getText().toString().trim();
        if (StringUtil.isEmpty(content)) {
            ToastUtil.error(_mActivity, getString(R.string.text_comment_content_can_not_be_empty));
            return;
        }

        // 收集圖片列表
        List<String> imagePathList = new ArrayList<>();
        int childCount = sglImageContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View view = sglImageContainer.getChildAt(i);
            if (view instanceof ImageView) {  // 如果是添加按鈕，則跳過
                continue;
            }
            imagePathList.add((String) view.getTag());
        }

        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                String responseStr = (String) message;
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                ToastUtil.success(_mActivity, "提交成功");
                pop();
            }
        };

        TwantApplication.getThreadPool().execute(new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                EasyJSONArray imageList = EasyJSONArray.generate();

                // 上傳圖片文件
                for (String imagePath : imagePathList) {
                    String name = Api.syncUploadFile(new File(imagePath));
                    SLog.info("name[%s]", name);
                    if (StringUtil.isEmpty(name)) {
                        continue;
                    }

                    imageList.append(EasyJSONObject.generate("imageUrl", name));
                }

                EasyJSONObject params = EasyJSONObject.generate(
                        "suggestContent", content,
                        "imageList", imageList);

                SLog.info("params[%s]", params.toString());

                String path = Api.PATH_COMMIT_FEEDBACK + Api.makeQueryString(EasyJSONObject.generate("token", token));
                String responseStr = Api.syncPostJson(path, params.toString());
                SLog.info("responseStr[%s]", responseStr);
                return responseStr;
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        SLog.info("resultCode[%d]", resultCode);

        if (resultCode != RESULT_OK) {
            return;
        }

        Uri uri = data.getData();
        String imageAbsolutePath = FileUtil.getRealFilePath(getActivity(), uri);  // 相册文件的源路径
        SLog.info("imageAbsolutePath[%s]", imageAbsolutePath);

        OrderDetailGoodsItem orderDetailGoodsItem = orderDetailGoodsItemList.get(currAddImagePosition);
        if (orderDetailGoodsItem.evaluationImageList == null) {
            orderDetailGoodsItem.evaluationImageList = new ArrayList<>();
        }
        orderDetailGoodsItem.evaluationImageList.add(imageAbsolutePath);
        adapter.notifyItemChanged(currAddImagePosition);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEBMessage(EBMessage message) {
        if (message.messageType == EBMessageType.MESSAGE_TYPE_KEYBOARD_STATE_CHANGED) {
            int keyboardState = (int) message.data;
            SLog.info("keyboardState[%d]", keyboardState);
            if (keyboardState == Constant.KEYBOARD_SHOWN) {
                rlButtonContainer.setVisibility(View.GONE);
            } else {
                rlButtonContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        rlButtonContainer.setVisibility(View.VISIBLE);
                    }
                }, 150);
            }
        }
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setOrderDetailGoodsItemList(List<OrderDetailGoodsItem> orderDetailGoodsItemList) {
        this.orderDetailGoodsItemList = orderDetailGoodsItemList;
    }
}
