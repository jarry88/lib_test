package com.ftofs.twant.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.gzp.lib_common.utils.SLog;
import com.ftofs.twant.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

import cn.snailpad.easyjson.EasyJSONObject;

/**
 * 收貨地址數據結構
 * @author zwm
 */
public class AddrItem implements Parcelable {
    public AddrItem(int addressId, String realName, List<Integer> areaIdList, int areaId, String areaInfo,
                    String address, String mobileAreaCode, String mobPhone, int isDefault) {
        this.addressId = addressId;
        this.realName = realName;
        this.areaIdList = areaIdList;
        this.areaId = areaId;
        this.areaInfo = areaInfo;
        this.address = address;
        this.mobileAreaCode = mobileAreaCode;
        this.mobPhone = mobPhone;
        this.isDefault = isDefault;
    }

    protected AddrItem(Parcel in) {
        addressId = in.readInt();
        realName = in.readString();
        in.readList(areaIdList, Integer.class.getClassLoader());
        areaId = in.readInt();
        areaInfo = in.readString();
        address = in.readString();
        mobileAreaCode = in.readString();
        mobPhone = in.readString();
        isDefault = in.readInt();
    }

    @NonNull
    @Override
    public String toString() {
        String areaIdListStr = StringUtil.implode(",", areaIdList);
        return String.format("addressId[%d], realName[%s], areaIdList[%s], areaId[%d], areaInfo[%s], address[%s], mobileAreaCode[%s], mobPhone[%s], isDefault[%d]",
                addressId, realName, areaIdListStr, areaId, areaInfo, address, mobileAreaCode, mobPhone, isDefault);
    }

    public static final Creator<AddrItem> CREATOR = new Creator<AddrItem>() {
        @Override
        public AddrItem createFromParcel(Parcel in) {
            return new AddrItem(in);
        }

        @Override
        public AddrItem[] newArray(int size) {
            return new AddrItem[size];
        }
    };

    public EasyJSONObject toEasyJSONObject() {
        return EasyJSONObject.generate(
                "addressId", addressId,
                "realName", realName,
                "areaId1", areaIdList.get(0),
                "areaId2", areaIdList.get(1),
                "areaId3", areaIdList.get(2),
                "areaId4", areaIdList.get(3),
                "areaId", areaId,
                "areaInfo", areaInfo,
                "address", address,
                "mobPhone", mobileAreaCode + mobPhone,
                "isDefault", isDefault);
    }

    public AddrItem(EasyJSONObject easyJSONObject) {
        try {
            addressId = easyJSONObject.getInt("addressId");
            realName = easyJSONObject.getSafeString("realName");
            areaIdList = new ArrayList<>();
            areaIdList.add(easyJSONObject.getInt("areaId1"));
            areaIdList.add(easyJSONObject.getInt("areaId2"));
            areaIdList.add(easyJSONObject.getInt("areaId3"));
            areaIdList.add(easyJSONObject.getInt("areaId4"));

            areaId = easyJSONObject.getInt("areaId");
            areaInfo = easyJSONObject.getSafeString("areaInfo");
            address = easyJSONObject.getSafeString("address");
            mobileAreaCode = easyJSONObject.getSafeString("mobileAreaCode");
            mobPhone = easyJSONObject.getSafeString("mobPhone");
            isDefault = easyJSONObject.getInt("isDefault");

        } catch (Exception e) {
            SLog.info("Error!message[%s], trace[%s]", e.getMessage(), Log.getStackTraceString(e));
        }
    }

    public int addressId;
    public String realName;
    public List<Integer> areaIdList;
    public int areaId;
    public String areaInfo;
    public String address;
    public String mobileAreaCode;
    public String mobPhone;
    public int isDefault;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(addressId);
        dest.writeString(realName);
        dest.writeList(areaIdList);
        dest.writeInt(areaId);
        dest.writeString(areaInfo);
        dest.writeString(address);
        dest.writeString(mobileAreaCode);
        dest.writeString(mobPhone);
        dest.writeInt(isDefault);
    }
}
