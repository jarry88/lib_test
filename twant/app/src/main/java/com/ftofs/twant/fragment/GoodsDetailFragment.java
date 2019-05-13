package com.ftofs.twant.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ftofs.twant.R;
import com.ftofs.twant.api.Api;
import com.ftofs.twant.api.UICallback;
import com.ftofs.twant.config.Config;
import com.ftofs.twant.entity.Spec;
import com.ftofs.twant.entity.SpecValue;
import com.ftofs.twant.log.SLog;
import com.ftofs.twant.util.StringUtil;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.User;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.SpecSelectPopup;
import com.lxj.xpopup.XPopup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.snailpad.easyjson.EasyJSONArray;
import cn.snailpad.easyjson.EasyJSONException;
import cn.snailpad.easyjson.EasyJSONObject;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 商品詳情頁面
 * @author zwm
 */
public class GoodsDetailFragment extends BaseFragment implements View.OnClickListener {
    // 商品Id
    int commonId;

    ImageView goodsImage;
    TextView tvGoodsName;
    TextView tvGoodsJingle;

    ImageView imgGoodsNationalFlag;
    TextView tvGoodsCountryName;

    List<Spec> specList = new ArrayList<>();
    // 從逗號連接的specValueId定位出goodsId的Map
    Map<String, Integer> specValueIdMap = new HashMap<>();

    public static GoodsDetailFragment newInstance(int commonId) {
        Bundle args = new Bundle();

        args.putInt("commonId", commonId);
        GoodsDetailFragment fragment = new GoodsDetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goods_detail, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle args = getArguments();
        commonId = args.getInt("commonId");
        SLog.info("commonId[%d]", commonId);

        goodsImage = view.findViewById(R.id.goods_image);
        tvGoodsName = view.findViewById(R.id.tv_goods_name);
        tvGoodsJingle = view.findViewById(R.id.tv_goods_jingle);

        imgGoodsNationalFlag = view.findViewById(R.id.img_goods_national_flag);
        tvGoodsCountryName = view.findViewById(R.id.tv_goods_country_name);

        Util.setOnClickListener(view, R.id.btn_add_to_cart, this);

        String token = User.getToken();
        if (!StringUtil.isEmpty(token)) {
            loadGoodsDetail(commonId, token);
        }
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_add_to_cart) {
            new XPopup.Builder(_mActivity)
                    // 如果不加这个，评论弹窗会移动到软键盘上面
                    .moveUpToKeyboard(false)
                    .asCustom(new SpecSelectPopup(_mActivity, specList, specValueIdMap))
                    .show();
        }
    }

    private void loadGoodsDetail(int commonId, String token) {
        String path = Api.PATH_GOODS_DETAIL + "/" + commonId;
        EasyJSONObject params = EasyJSONObject.generate("token", token);

        Api.postUI(path, params, new UICallback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseStr = response.body().string();
                EasyJSONObject responseObj = (EasyJSONObject) EasyJSONObject.parse(responseStr);

                if (ToastUtil.checkError(_mActivity, responseObj)) {
                    return;
                }

                try {
                    EasyJSONObject goodsDetail = responseObj.getObject("datas.goodsDetail");
                    SLog.info("goodsDetail[%s]", goodsDetail);

                    String goodsImageUrl = goodsDetail.getString("imageSrc");
                    Glide.with(GoodsDetailFragment.this).load(goodsImageUrl).into(goodsImage);

                    tvGoodsName.setText(goodsDetail.getString("goodsName"));
                    tvGoodsJingle.setText(goodsDetail.getString("jingle"));

                    String goodsNationalFlagUrl = Config.OSS_BASE_URL + "/" + responseObj.getString("datas.goodsCountry.nationalFlag");
                    Glide.with(GoodsDetailFragment.this).load(goodsNationalFlagUrl).into(imgGoodsNationalFlag);

                    tvGoodsCountryName.setText(responseObj.getString("datas.goodsCountry.countryCn"));

                    // 下面開始組裝規格數據列表
                    EasyJSONArray specJson = goodsDetail.getArray("specJson");
                    SLog.info("specJson[%s]", specJson);
                    for (Object object : specJson) {
                        EasyJSONObject specObj = (EasyJSONObject) object;

                        Spec specItem = new Spec();

                        specItem.specId = specObj.getInt("specId");
                        specItem.specName = specObj.getString("specName");

                        EasyJSONArray specValueList = specObj.getArray("specValueList");

                        for (Object object2 : specValueList) {
                            EasyJSONObject specValue = (EasyJSONObject) object2;
                            int specValueId = specValue.getInt("specValueId");
                            String specValueName = specValue.getString("specValueName");
                            String imageSrc = specValue.getString("imageSrc");

                            SpecValue specValueItem = new SpecValue(specValueId, specValueName, imageSrc);
                            specItem.specValueList.add(specValueItem);
                        }

                        specList.add(specItem);
                    }


                    String goodsSpecValues = goodsDetail.getString("goodsSpecValues");
                    EasyJSONArray goodsSpecValueArr = (EasyJSONArray) EasyJSONArray.parse(goodsSpecValues);
                    for (Object object : goodsSpecValueArr) {
                        EasyJSONObject mapItem = (EasyJSONObject) object;
                        specValueIdMap.put(mapItem.getString("specValueIds"), mapItem.getInt("goodsId"));
                    }

                    SLog.info("specList.size[%d]", specList.size());
                    for (Spec spec : specList) {
                        SLog.info("SPEC_DUMP[%s]", spec);
                    }

                } catch (EasyJSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
