package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.adapter.BargainHelpListAdapter;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.constant.Constant;
import com.ftofs.twant.entity.BargainHelpListItem;
import com.ftofs.twant.entity.BargainItem;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SharePopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 砍價詳情頁
 * @author zwm
 */
public class BargainDetailFragment extends BaseFragment implements View.OnClickListener {
    int openId;
    int isOwner;

    BargainHelpListAdapter adapter;

    // 分享數據
    String shareUrl;
    String shareTitle;
    String shareDescription;
    String shareCoverUrl;

    public static BargainDetailFragment newInstance(int openId) {
        Bundle args = new Bundle();

        BargainDetailFragment fragment = new BargainDetailFragment();
        fragment.setArguments(args);
        fragment.openId = openId;

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bargain_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Util.setOnClickListener(view, R.id.btn_invite_friend, this);
        Util.setOnClickListener(view, R.id.btn_buy_now, this);
        Util.setOnClickListener(view, R.id.btn_back, this);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }

        View contentView = getView();
        if (contentView == null) {
            return;
        }

        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "openId", openId);

        String url = Api.PATH_BARGAIN_SHARE;
        SLog.info("url[%s], params[%s]", url, params);
        Api.postUI(url, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ToastUtil.showNetworkError(_mActivity, e);
            }

            @Override
            public void onResponse(Call call, String responseStr) throws IOException {
                try {
                    SLog.info("responseStr[%s]", responseStr);

                    EasyJSONObject responseObj = EasyJSONObject.parse(responseStr);

                    if (ToastUtil.checkError(_mActivity, responseObj)) {
                        return;
                    }

                    isOwner = responseObj.getInt("datas.isOwner");
                    EasyJSONObject bargainOpenDetailVo = responseObj.getSafeObject("datas.bargainOpenDetailVo");

                    ImageView goodsImage = contentView.findViewById(R.id.goods_image);
                    String imageSrc = bargainOpenDetailVo.getSafeString("imageSrc");
                    Glide.with(_mActivity).load(StringUtil.normalizeImageUrl(imageSrc)).centerCrop().into(goodsImage);

                    ((TextView) contentView.findViewById(R.id.tv_goods_name)).setText(bargainOpenDetailVo.getSafeString("goodsName"));
                    ((TextView) contentView.findViewById(R.id.tv_goods_spec)).setText(bargainOpenDetailVo.getSafeString("goodsSpecString"));

                    int bargainTimes = bargainOpenDetailVo.getInt("bargainTimes");
                    double bargainPrice = bargainOpenDetailVo.getDouble("bargainPrice");
                    String bargainStateDesc = String.format("%d人幫忙砍，已砍掉%s", bargainTimes, StringUtil.formatPrice(_mActivity, bargainPrice, 0));
                    ((TextView) contentView.findViewById(R.id.tv_bargain_state_desc)).setText(bargainStateDesc);

                    ((TextView) contentView.findViewById(R.id.tv_price))
                            .setText(StringUtil.formatPrice(_mActivity, bargainOpenDetailVo.getDouble("openPrice"), 0));

                    ((TextView) contentView.findViewById(R.id.tv_bottom_price))
                            .setText("底價：" + StringUtil.formatPrice(_mActivity, bargainOpenDetailVo.getDouble("bottomPrice"), 0));

                    EasyJSONArray bargainOpenLogList = bargainOpenDetailVo.getSafeArray("bargainOpenLogList");

                    LinearLayout llHelpList = contentView.findViewById(R.id.ll_help_list);
                    adapter = new BargainHelpListAdapter(_mActivity, llHelpList, R.layout.bargain_help_list_item);
                    List<BargainHelpListItem> bargainHelpList = new ArrayList<>();
                    for (Object object : bargainOpenLogList) {
                        EasyJSONObject bargainOpenLog = (EasyJSONObject) object;

                        BargainHelpListItem item = new BargainHelpListItem();
                        item.avatar = bargainOpenLog.getSafeString("avatar");
                        item.nickname = bargainOpenLog.getSafeString("memberName");
                        item.createTime = bargainOpenLog.getSafeString("createTime");
                        item.bargainPrice = bargainOpenLog.getDouble("bargainPrice");
                        bargainHelpList.add(item);
                    }

                    adapter.setData(bargainHelpList);

                    EasyJSONObject shareVo = responseObj.getSafeObject("datas.shareVo");
                    shareTitle = shareVo.getSafeString("title");
                    shareDescription = shareVo.getSafeString("content");
                    shareUrl = shareVo.getSafeString("shareUrl");
                    shareCoverUrl = shareVo.getSafeString("image");
                } catch (Exception e) {
                    SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_back) {
            hideSoftInputPop();
        } else if (id == R.id.btn_invite_friend) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new SharePopup(_mActivity, shareUrl, shareTitle, shareDescription, shareCoverUrl, null))
                    .show();

        } else if (id == R.id.btn_buy_now) {
            if (isOwner != Constant.TRUE_INT) {
                ToastUtil.error(_mActivity, "只有砍價發起人才有購買資格");
                return;
            }
            Util.startFragment(ConfirmOrderFragment.newInstance(Constant.FALSE_INT, null));
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        hideSoftInputPop();
        return true;
    }
}

