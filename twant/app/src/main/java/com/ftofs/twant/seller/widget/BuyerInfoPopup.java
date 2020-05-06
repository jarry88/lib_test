package com.ftofs.twant.seller.widget;


import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.entity.MyFriendListItem;
import com.ftofs.twant.fragment.ChatFragment;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.FriendInfo;
import com.ftofs.twant.util.ChatUtil;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.lxj.xpopup.core.BottomPopupView;
import com.lxj.xpopup.util.XPopupUtils;

import java.io.IOException;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;

/**
 * 商家版買家信息彈窗
 * @author zwm
 */
public class BuyerInfoPopup extends BottomPopupView implements View.OnClickListener {
    Context context;
    Activity activity;

    String userPhoneNumber;
    String receiverPhoneNumber;

    String memberName;
    String nickname;

    String avatarUrl;

    int ordersId;

    public BuyerInfoPopup(@NonNull Context context, Activity activity, int ordersId, String memberName) {
        super(context);

        this.context = context;
        this.activity = activity;
        this.ordersId = ordersId;
        this.memberName = memberName;
    }


    @Override
    protected int getImplLayoutId() {
        return R.layout.buyer_info_popup;
    }

    @Override
    protected void onCreate() {
        super.onCreate();

        findViewById(R.id.btn_dismiss).setOnClickListener(this);
        findViewById(R.id.btn_chat_with_user).setOnClickListener(this);
        findViewById(R.id.btn_dial_user_phone).setOnClickListener(this);
        findViewById(R.id.btn_dial_receiver_phone).setOnClickListener(this);

        loadData();
    }

    private void loadData() {
        String token = User.getToken();
        if (StringUtil.isEmpty(token)) {
            return;
        }
        EasyJSONObject params = EasyJSONObject.generate(
                "token", token,
                "ordersId", ordersId,
                "memberName", memberName
        );

        SLog.info("params[%s]", params);
        Api.getUI(Api.PATH_SELLER_BUYER_INFO, params, new UICallback() {
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


                    /*

                    {
	"code": 200,
	"datas": {
		"memberInfo": {
			"memberId": 247,
			"memberName": "u_024615562560",
			"avatar": "image/10/55/1055e83585dd56d3d28c80e7b4f4fcb5.png",
			"nickName": "zwm672",
			"mobileAreaCode": "0086",
			"mobile": "13425038750"
		},
		"ordersBaseVo": {
			"ordersId": 6516,
			"ordersSn": 5700000000659400,
			"ordersSnStr": "5700000000659400",
			"ordersAmount": 17.50,
			"memberName": "u_024615562560",
			"receiverName": "周伟明",
			"receiverPhone": "0085366886688",
			"receiverAreaInfo": "广东 珠海市 香洲区",
			"receiverAddress": "zzz"
		}
	}
}
                     */
                    EasyJSONObject memberInfo = responseObj.getObject("datas.memberInfo");
                    avatarUrl = StringUtil.normalizeImageUrl(memberInfo.getSafeString("avatar"));

                    memberName = memberInfo.getSafeString("memberName");
                    ((TextView) findViewById(R.id.tv_buyer_member_name)).setText(memberName);

                    userPhoneNumber = memberInfo.getSafeString("mobileAreaCode") + memberInfo.getSafeString("mobile");
                    ((TextView) findViewById(R.id.tv_buyer_phone_number)).setText(userPhoneNumber);

                    ((TextView) findViewById(R.id.tv_buyer_nickname)).setText(memberInfo.getSafeString("nickName"));

                    EasyJSONObject ordersBaseVo = responseObj.getObject("datas.ordersBaseVo");
                    ((TextView) findViewById(R.id.tv_receiver_name)).setText(ordersBaseVo.getSafeString("receiverName"));

                    receiverPhoneNumber = ordersBaseVo.getSafeString("receiverPhone");
                    ((TextView) findViewById(R.id.tv_receiver_phone_number)).setText(receiverPhoneNumber);

                    ((TextView) findViewById(R.id.tv_receiver_address)).setText(ordersBaseVo.getSafeString("receiverAreaInfo") + " " + ordersBaseVo.getSafeString("receiverAddress"));
                } catch (Exception e){
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

    }

    @Override
    protected int getMaxHeight() {
        return (int) (XPopupUtils.getWindowHeight(getContext())*.85f);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_dismiss) {
            dismiss();
        } else if (id == R.id.btn_dial_user_phone || id == R.id.btn_dial_receiver_phone) {
            String phoneNumber;
            if (id == R.id.btn_dial_user_phone) {
                phoneNumber = userPhoneNumber;
            } else {
                phoneNumber = receiverPhoneNumber;
            }

            Util.dialPhone(activity, phoneNumber);
        } else if (id == R.id.btn_chat_with_user) {
            FriendInfo friendInfo = new FriendInfo();
            friendInfo.memberName = memberName;
            friendInfo.nickname = nickname;
            friendInfo.avatarUrl = avatarUrl;
            friendInfo.role = ChatUtil.ROLE_MEMBER;
            Util.startFragment(ChatFragment.newInstance(ChatUtil.getConversation(memberName, nickname, avatarUrl, ChatUtil.ROLE_MEMBER), friendInfo));

            dismiss();
        }
    }
}
