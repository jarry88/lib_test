package com.ftofs.twant.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class FriendItem implements Parcelable {
    public int memberId;
    public String memberName;
    public String nickname;
    public int gender;
    public String avatarUrl;

    public FriendItem() {

    }

    protected FriendItem(Parcel in) {
        memberId = in.readInt();
        memberName = in.readString();
        nickname = in.readString();
        gender = in.readInt();
        avatarUrl = in.readString();
    }

    public static final Creator<FriendItem> CREATOR = new Creator<FriendItem>() {
        @Override
        public FriendItem createFromParcel(Parcel in) {
            return new FriendItem(in);
        }

        @Override
        public FriendItem[] newArray(int size) {
            return new FriendItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(memberId);
        dest.writeString(memberName);
        dest.writeString(nickname);
        dest.writeInt(gender);
        dest.writeString(avatarUrl);
    }
}
