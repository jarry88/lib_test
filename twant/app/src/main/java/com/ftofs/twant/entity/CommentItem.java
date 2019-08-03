package com.ftofs.twant.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class CommentItem implements Parcelable {
    public int commentId;
    /**
     * 評論類型 1全部 2視頻 3文本
     */
    public int commentType;
    public int commentChannel; // 貼文渠道 1全部 2店鋪 3商品 4貼文 5推文

    public String content;
    public int isLike; // 是否點贊
    public int commentLike; // 點贊數
    public int commentReply; // 回復數

    public String commenterAvatar;
    public String nickname;
    public String commentTime;
    public String imageUrl;

    public int relateCommonId;
    public int relateStoreId;
    public int replyCommentId;
    public int relatePostId;
    public int parentCommentId;

    public CommentItem() {

    }

    protected CommentItem(Parcel in) {
        commentId = in.readInt();
        commentType = in.readInt();
        commentChannel = in.readInt();
        content = in.readString();
        isLike = in.readInt();
        commentLike = in.readInt();
        commentReply = in.readInt();
        commenterAvatar = in.readString();
        nickname = in.readString();
        commentTime = in.readString();
        imageUrl = in.readString();

        relateCommonId = in.readInt();
        relateStoreId = in.readInt();
        replyCommentId = in.readInt();
        relatePostId = in.readInt();
        parentCommentId = in.readInt();
    }

    public static final Creator<CommentItem> CREATOR = new Creator<CommentItem>() {
        @Override
        public CommentItem createFromParcel(Parcel in) {
            return new CommentItem(in);
        }

        @Override
        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(commentId);
        dest.writeInt(commentType);
        dest.writeInt(commentChannel);
        dest.writeString(content);
        dest.writeInt(isLike);
        dest.writeInt(commentLike);
        dest.writeInt(commentReply);
        dest.writeString(commenterAvatar);
        dest.writeString(nickname);
        dest.writeString(commentTime);
        dest.writeString(imageUrl);

        dest.writeInt(relateCommonId);
        dest.writeInt(relateStoreId);
        dest.writeInt(replyCommentId);
        dest.writeInt(relatePostId);
        dest.writeInt(parentCommentId);
    }
}
