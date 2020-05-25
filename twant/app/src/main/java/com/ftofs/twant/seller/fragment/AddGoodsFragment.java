package com.ftofs.twant.seller.fragment;

import android.database.DataSetObserver;
import android.location.Address;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.ftofs.twant.R;
import com.ftofs.twant.adapter.SimpleViewPagerAdapter;
import com.ftofs.twant.fragment.BaseFragment;
import com.ftofs.twant.util.ToastUtil;
import com.ftofs.twant.util.Util;
import com.ftofs.twant.widget.ScaledButton;

import org.litepal.util.LitePalLog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.yokeyword.fragmentation.ISupportFragment;
import permissions.dispatcher.OnNeverAskAgain;

public class AddGoodsFragment extends BaseFragment implements View.OnClickListener {
    private Unbinder unbinder;
    private SimpleViewPagerAdapter mPagerAdapter;
    private List<View> mViews =new ArrayList<>();

    public static AddGoodsFragment newInstance() {
        return new AddGoodsFragment();
    }

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.sb_check_notice)
    ScaledButton sbNotice;
    @BindView(R.id.vp_seller_good_add)
    ViewPager vpAddGood;

    @OnClick(R.id.btn_back)
    void back() {
        hideSoftInputPop();
    }
    @OnClick(R.id.rl_guide_container)
    void hideGuide(){
        getView().findViewById(R.id.rl_guide_container).setVisibility(View.GONE);
        getView().findViewById(R.id.vp_seller_good_add).setVisibility(View.VISIBLE);
        vpAddGood.setCurrentItem(0);
    }

    @OnClick(R.id.sb_check_notice)
    void checkNotice() {
        sbNotice.setChecked(!sbNotice.isChecked());
        sbNotice.setIconResource(sbNotice.isChecked()?R.drawable.icon_checked:R.drawable.icon_cart_item_unchecked);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_seller_goods_publish, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        loadDate();
    }

    private void initView() {
        sbNotice.setIconResource(R.drawable.icon_cart_item_unchecked);
        sbNotice.setChecked(false);
        mViews.add(primaryView());
        mViews.add(basicView());
        mViews.add(specView());
        mViews.add(detailView());
        mViews.add(freightView());
        mViews.add(othersView());
        mPagerAdapter = new SimpleViewPagerAdapter(_mActivity,mViews);
        vpAddGood.setAdapter(mPagerAdapter);
        vpAddGood.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                setTitle(position);
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void setTitle(int position) {
        switch (position) {
            case 0:
                tvTitle.setText("基本信息");
                break;
            case 1:
                tvTitle.setText("交易信息");
                break;
            case 2:
                tvTitle.setText("規格與圖片");
                break;
            case 3:
                tvTitle.setText("詳情描述");
                break;
            case 4:
                tvTitle.setText("物流信息");
                break;
            case 5:
                tvTitle.setText("其它信息");
                break;
            default:
                break;
        }
    }

    private View othersView() {
        View view =LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_others_widget, vpAddGood, false);
        Util.setOnClickListener(view,R.id.btn_others_prev,this);
        Util.setOnClickListener(view,R.id.btn_others_next,this);
        ScaledButton sbInstancePublish = view.findViewById(R.id.sb_instance_publish);
        ScaledButton sbAddHub = view.findViewById(R.id.sb_add_hub);
        sbInstancePublish.setButtonCheckedBlue();
        sbAddHub.setButtonCheckedBlue();
        sbInstancePublish.setText("立即發佈");
        sbAddHub.setText("放入倉庫");
        return view;
    }

    private View freightView() {
        View view =LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_freight_widget, vpAddGood, false);
        Util.setOnClickListener(view,R.id.btn_freight_prev,this);
        Util.setOnClickListener(view,R.id.btn_freight_next,this);
        return view;
    }
    private View detailView() {
        View view =LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_detail_widget, vpAddGood, false);
        Util.setOnClickListener(view,R.id.btn_detail_next,this);
        Util.setOnClickListener(view,R.id.btn_detail_prev,this);
        Util.setOnClickListener(view,R.id.btn_add_address,this);
        return view;
    }

    private View specView() {
        View view =LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_spec_widget, vpAddGood, false);
        Util.setOnClickListener(view,R.id.btn_spec_next,this);
        Util.setOnClickListener(view,R.id.btn_spec_prev,this);
        return view;
    }

    private View basicView() {
        View view =LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_basic_widget, vpAddGood, false);
        Util.setOnClickListener(view,R.id.btn_basic_next,this);
        Util.setOnClickListener(view,R.id.btn_basic_prev,this);
        ScaledButton sbRetail = view.findViewById(R.id.sb_retail);
        ScaledButton sbVirtual = view.findViewById(R.id.sb_virtual);
        ScaledButton sbAcross = view.findViewById(R.id.sb_across);
        sbRetail.setText("零售型");
        sbVirtual.setText("虛擬型");
        sbAcross.setText("跨城型");
        return view;
    }

    private View primaryView() {

        View view =LayoutInflater.from(getContext()).inflate(R.layout.seller_add_good_primary_widget, vpAddGood, false);
        Util.setOnClickListener(view,R.id.btn_primary_next,this);
        Spinner spGoodLogo = view.findViewById(R.id.sp_add_good_logo);
        Spinner spGoodLocation = view.findViewById(R.id.sp_add_good_location);
        List<String> listLogo = new ArrayList<>();
        List<String> listLocation = new ArrayList<>();
        listLogo.add("s");
        listLogo.add("s");
        listLogo.add("s");
        listLocation.addAll(listLogo);


        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private void loadDate() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.btn_primary_next:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem()+1);
                break;
            case R.id.btn_basic_next:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem()+1);

                break;
            case R.id.btn_basic_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem()-1);

                break;
            case R.id.btn_spec_next:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem()+1);

                break;
            case R.id.btn_spec_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem()-1);

                break;
            case R.id.btn_add_address:
                ToastUtil.success(_mActivity, "添加商品描述");
                break;
            case R.id.btn_detail_next:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem()+1);

                tvTitle.setText("詳情描述");
                break;
            case R.id.btn_detail_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem()-1);

                tvTitle.setText("規格與圖片");
                break;
            case R.id.btn_freight_next:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem()+1);

                tvTitle.setText("物流信息");
                break;
            case R.id.btn_freight_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem()-1);

                tvTitle.setText("詳情描述");
                break;
            case R.id.btn_others_next:
                commitGoodsInfo();
                break;
            case R.id.btn_others_prev:
                vpAddGood.setCurrentItem(vpAddGood.getCurrentItem()-1);
                tvTitle.setText("物流信息");
                break;
            default:
                break;
        }

    }

    private void commitGoodsInfo() {

    }
}
