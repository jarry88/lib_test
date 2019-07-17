package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.activity.MainActivity;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.orm.Emoji;
import com.ftofs.twant.util.Guid;
import com.ftofs.twant.util.Time;
import com.ftofs.twant.util.Util;
import com.macau.pay.sdk.MacauPaySdk;

import org.litepal.LitePal;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 測試用Fragment
 * @author zwm
 */
public class TestFragment extends BaseFragment implements View.OnClickListener {
    ImageView imageView;
    public static TestFragment newInstance() {
        Bundle args = new Bundle();

        TestFragment fragment = new TestFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_test, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = view.findViewById(R.id.image_view);
        Util.setOnClickListener(view, R.id.btn_test, this);
        Util.setOnClickListener(view, R.id.btn_test2, this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_test) {
            SLog.info("btn_test");
            // MainFragment mainFragment = MainFragment.getInstance();
            // mainFragment.start(ChatFragment.newInstance());

            String params = "{\n" +
                    "    \"postUrl\": \"http://202.86.151.170/payment/billCreatePc.do\",\n" +
                    "    \"charset\": \"UTF-8\",\n" +
                    "    \"biz_content\": \"{\\\"subject\\\":\\\"我是商品訂單\\\",\\\"body\\\":\\\"商品訂單詳情\\\",\\\"notify_url\\\":\\\"http://47.90.106.9/verifySign\\\",\\\"goods_detail\\\":\\\"[]\\\",\\\"out_trade_no\\\":\\\"M20190717154617\\\",\\\"total_amount\\\":0.1,\\\"trade_mode\\\":\\\"0002\\\",\\\"timeout_express\\\":\\\"30m\\\",\\\"return_url\\\":\\\"\\\"}\",\n" +
                    "    \"api_name\": \"preCreate\",\n" +
                    "    \"data_type\": \"Json\",\n" +
                    "    \"sign\": \"A05CD9C46078B5E50C5D2BE6BF48D6B06453E5CD3364BC9B9C0FB563E1150A58EA69AF8CBE28019F62E24D48CC69BA065595C47BE07B54AC0D2AFE0AA7FDD1F91D44CC7B213AA79D439965C097DB6450EF84526156BDB4BC1A9C2C1D5AACBDB398A90E0AE44015C223E6F2CB3EC8F8073EDCBB76180700035F362D10235F25754BDA0568CDD9EEBD421ABE6E6318230920132FCD0E8D95DF9B4042668960DB3BCF4B151FAE06913424073FC8A238F2DC618C8A0BF0287E20D208CFAAAE945076DF52891E203088AC54C77B086C08AC5B2EE6BD4488CBBDD56D063C75B6E1F1868791B7E7F8BD428A5C171AA372C4E230AF19A244AF9EE5B2C056715D6BD466E6\",\n" +
                    "    \"biz_api_code\": \"100020007\",\n" +
                    "    \"app_id\": \"00000000004414\",\n" +
                    "    \"sign_type\": \"RSA\",\n" +
                    "    \"version\": \"V1.0\",\n" +
                    "    \"timestamp\": \"2019-07-17 15:46:17\"\n" +
                    "}";

            // MacauPaySdk.setEnvironmentType(2);
            MacauPaySdk.macauPay(_mActivity, params, (MainActivity) _mActivity);
        } else if (id == R.id.btn_test2) {
            SLog.info("btn_test2");
            Emoji emoji = LitePal.where("emojiId = 21").findFirst(Emoji.class);
            if (emoji != null) {
                // Glide.with(_mActivity).load(emoji.image).into(imageView);
            } else {
                SLog.info("Error!find emoji failed");
            }
        }
    }

    @Override
    public boolean onBackPressedSupport() {
        SLog.info("onBackPressedSupport");
        pop();
        return true;
    }
}
