package com.ftofs.twant.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 單據信息
 * @author zwm
 */
public class Receipt implements Parcelable {
    /**
     * 單據抬頭
     */
    public String header;
    /**
     * 單據內容
     */
    public String content;
    /**
     * 納稅人識別號
     */
    public String taxPayerId;

    public Receipt() {
    }

    protected Receipt(Parcel in) {
        header = in.readString();
        content = in.readString();
        taxPayerId = in.readString();
    }

    public static final Creator<Receipt> CREATOR = new Creator<Receipt>() {
        @Override
        public Receipt createFromParcel(Parcel in) {
            return new Receipt(in);
        }

        @Override
        public Receipt[] newArray(int size) {
            return new Receipt[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(header);
        dest.writeString(content);
        dest.writeString(taxPayerId);
    }
}
