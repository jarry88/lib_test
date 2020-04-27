package com.ftofs.twant.util;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class UiUtil {

    public static void toPriceUI(TextView leftTextView,int firstSize) {
        toPriceUI(leftTextView,firstSize,0,1);
    }
    public static void toPriceUI(TextView leftTextView,int firstSize,int start,int end) {
        if (leftTextView == null) {
            return;
        }
        if (firstSize == 0) {
            firstSize = 11;
        }
        SpannableStringBuilder spannable = new SpannableStringBuilder(leftTextView.getText().toString());
//        //设置字体颜色为红色
//        ForegroundColorSpan good_red = new ForegroundColorSpan(getResources().getColor(R.color.goods_red));
//        //设置字体颜色为灰色
//        ForegroundColorSpan good_gray = new ForegroundColorSpan(getResources().getColor(R.color.col909090));
//        //改变第0-3个字体颜色
//        spannable.setSpan(good_red, 0, 4, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //改变第4-之后所有的字体颜色（这里一定要注意范围，否则会造成越界）
//        spannable.setSpan(good_gray, 4, test.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        //改变第0个字体大小
        spannable.setSpan(new AbsoluteSizeSpan(firstSize,true), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        leftTextView.setText(spannable);
    }
}
