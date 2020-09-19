package com.ftofs.twant.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.ftofs.twant.R;
import com.ftofs.twant.TwantApplication;
import com.ftofs.twant.adapter.GoodsEvaluationAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.constant.EBMessageType;
import com.ftofs.twant.constant.RequestCode;
import com.ftofs.twant.entity.EBMessage;
import com.ftofs.twant.entity.EvaluationGoodsItem;
import com.gzp.lib_common.base.BaseFragment;
import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.task.TaskObservable;
import com.ftofs.twant.task.TaskObserver;
import com.ftofs.twant.util.FileUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SquareGridLayout;
import com.lxj.xpopup.core.BasePopupView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 訂單產品評價
 * @author zwm
 */
public class GoodsEvaluationFragment extends BaseFragment implements View.OnClickListener {
    SquareGridLayout sglImageContainer;
    ImageView btnAddImage;
    RelativeLayout rlButtonContainer;

    int ordersId;
    int storeId;
    String storeName;
    List<EvaluationGoodsItem> evaluationGoodsItemList;

    int currAddImagePosition; // 當前要添加圖片的評價項
    RecyclerView rvEvaluationList;
    GoodsEvaluationAdapter adapter;



    /**
     * 批量評論是否展開狀態
     */
    public static final int QUICK_COMMENT_COUNT = 3;
    TextView[] tvQuickCommentArr = new TextView[QUICK_COMMENT_COUNT];
    ImageView[] imgQuickCommentArr = new ImageView[QUICK_COMMENT_COUNT];
    boolean quickCommentContainerExpanded = false;
    ImageView imgQuickCommentButton;
    View llQuickCommentContainer;
    int selectedQuickCommentIndex = -1;  // 當前選中哪條批量評論，從0開始，-1表示未選中
    String[] quickCommentArr = new String[] {
            "產品包裝完好，物流送貨很及時。", "產品性價比高，商家態度很nice。", "產品不錯，下次還會再買。"
    };

    int twMediumGrey;
    int twBlack;

    public static GoodsEvaluationFragment newInstance(int ordersId, int storeId, String storeName, List<EvaluationGoodsItem> evaluationGoodsItemList) {
        Bundle args = new Bundle();

        GoodsEvaluationFragment fragment = new GoodsEvaluationFragment();
        fragment.setArguments(args);

        fragment.setOrdersId(ordersId);
        fragment.setStoreId(storeId);
        fragment.setStoreName(storeName);
        fragment.setEvaluationGoodsItemList(evaluationGoodsItemList);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NotNull @NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_evaluation, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EventBus.getDefault().register(this);

        sglImageContainer = view.findViewById(R.id.sgl_image_container);
        rlButtonContainer = view.findViewById(R.id.rl_button_container);
        btnAddImage = view.findViewById(R.id.btn_add_image);

        imgQuickCommentButton = view.findViewById(R.id.img_quick_comment_button);
        llQuickCommentContainer = view.findViewById(R.id.ll_quick_comment_container);

        tvQuickCommentArr[0] = view.findViewById(R.id.tv_quick_comment_0);
        tvQuickCommentArr[1] = view.findViewById(R.id.tv_quick_comment_1);
        tvQuickCommentArr[2] = view.findViewById(R.id.tv_quick_comment_2);

        for (int i = 0; i < QUICK_COMMENT_COUNT; i++) {
            tvQuickCommentArr[i].setText(quickCommentArr[i]);
        }

        imgQuickCommentArr[0] = view.findViewById(R.id.img_quick_comment_0);
        imgQuickCommentArr[1] = view.findViewById(R.id.img_quick_comment_1);
        imgQuickCommentArr[2] = view.findViewById(R.id.img_quick_comment_2);

        Util.setOnClickListener(view, R.id.btn_quick_comment_0, this);
        Util.setOnClickListener(view, R.id.btn_quick_comment_1, this);
        Util.setOnClickListener(view, R.id.btn_quick_comment_2, this);

        twMediumGrey = getResources().getColor(R.color.tw_medium_grey, null);
        twBlack = getResources().getColor(R.color.tw_black, null);

        Util.setOnClickListener(view, R.id.btn_back, this);
        Util.setOnClickListener(view, R.id.btn_commit, this);

        Util.setOnClickListener(view, R.id.btn_switch_quick_comment, this);

        rvEvaluationList = view.findViewById(R.id.rv_evaluation_list);
        adapter = new GoodsEvaluationAdapter(R.layout.goods_evaluation_item, storeId, storeName, evaluationGoodsItemList);
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
                hideSoftInputPop();
                break;
            case R.id.btn_commit:
                commitComment();
                break;
            case R.id.btn_switch_quick_comment:
                quickCommentContainerExpanded = !quickCommentContainerExpanded;
                llQuickCommentContainer.setVisibility(quickCommentContainerExpanded ? View.VISIBLE : View.GONE);
                imgQuickCommentButton.setImageResource(quickCommentContainerExpanded ? R.drawable.icon_collapse_20 : R.drawable.icon_expand_20);
                break;
            case R.id.btn_quick_comment_0:
                selectQuickComment(0);
                break;
            case R.id.btn_quick_comment_1:
                selectQuickComment(1);
                break;
            case R.id.btn_quick_comment_2:
                selectQuickComment(2);
                break;
            default:
                break;
        }
    }

    private void selectQuickComment(int index) {
        for (EvaluationGoodsItem item : evaluationGoodsItemList) {
            item.content = quickCommentArr[index];
        }
        adapter.notifyDataSetChanged();

        if (selectedQuickCommentIndex == index) {
            return;
        }

        // 取消上一個選中狀態
        if (selectedQuickCommentIndex != -1) {
            tvQuickCommentArr[selectedQuickCommentIndex].setTextColor(twMediumGrey);
            imgQuickCommentArr[selectedQuickCommentIndex].setVisibility(View.GONE);
        }

        selectedQuickCommentIndex = index;

        // 設置當前選中狀態
        tvQuickCommentArr[selectedQuickCommentIndex].setTextColor(twBlack);
        imgQuickCommentArr[selectedQuickCommentIndex].setVisibility(View.VISIBLE);
    }

    private void commitComment() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        for (EvaluationGoodsItem evaluationGoodsItem : evaluationGoodsItemList) {
            if (StringUtil.isEmpty(evaluationGoodsItem.content)) {
                ToastUtil.error(_mActivity, getString(R.string.text_comment_content_can_not_be_empty));
                return;
            }
        }

        final BasePopupView loadingPopup = Util.createLoadingPopup(_mActivity).show();
        TaskObserver taskObserver = new TaskObserver() {
            @Override
            public void onMessage() {
                loadingPopup.dismiss();

                String responseStr = (String) message;
                EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);
                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                // 如果提交成功，訂單詳情和訂單列表頁面都要刷新一下
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_DETAIL, null);
                EBMessage.postMessage(EBMessageType.MESSAGE_TYPE_RELOAD_DATA_ORDER_LIST, null);

                ToastUtil.success(_mActivity, "提交成功");
                hideSoftInputPop();
            }
        };

        TwantApplication.Companion.getThreadPool().execute(new TaskObservable(taskObserver) {
            @Override
            public Object doWork() {
                EasyJSONArray wantCommentVoList = EasyJSONArray.generate();
                // 處理每一項Sku
                for (EvaluationGoodsItem evaluationGoodsItem : evaluationGoodsItemList) {
                    /*
                    {
                        "deep": "1",
                        "content": "ordersComment111-1",
                        "relateCommonId": "1693",
                        "images": [
                        {
                            "imageUrl": "image/07/8d/078d90ea33ae01417aa3d230f97849b1.png"
                        }
                        ],
                        "commentChannel": "3"
                    }
                     */

                    EasyJSONArray images = EasyJSONArray.generate();

                    // 上傳圖片文件
                    for (String imagePath : evaluationGoodsItem.imageList) {
                        String name = Api.syncUploadFile(new File(imagePath));
                        SLog.info("name[%s]", name);
                        if (StringUtil.isEmpty(name)) {
                            continue;
                        }

                        images.append(EasyJSONObject.generate("imageUrl", name));
                    }

                    EasyJSONObject wantCommentVo = EasyJSONObject.generate(
                            "deep", "1",
                            "content", evaluationGoodsItem.content,
                            "relateCommonId", String.valueOf(evaluationGoodsItem.commonId),
                            "images", images,
                            "commentChannel", "3");
                    wantCommentVoList.append(wantCommentVo);
                }


                EasyJSONObject params = EasyJSONObject.generate(
                        "ordersId", ordersId,
                        "ordersType", "1",
                        "wantCommentVoList", wantCommentVoList);

                SLog.info("params[%s]", params.toString());

                String path = Api.PATH_GOODS_COMMENT + Api.makeQueryString(EasyJSONObject.generate("token", token));
                String responseStr = Api.syncPostJson(path, params.toString());
                SLog.info("responseStr[%s]", responseStr);
                return responseStr;
            }
        });
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
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

        EvaluationGoodsItem evaluationGoodsItem = evaluationGoodsItemList.get(currAddImagePosition);
        evaluationGoodsItem.imageList.add(imageAbsolutePath);
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

    public void setOrdersId(int ordersId) {
        this.ordersId = ordersId;
    }

    public void setStoreId(int storeId) {
        this.storeId = storeId;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public void setEvaluationGoodsItemList(List<EvaluationGoodsItem> evaluationGoodsItemList) {
        this.evaluationGoodsItemList = evaluationGoodsItemList;
    }
}


