package com.ftofs.twant.entity;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

public class CommentItem implements Parcelable {
    public int commentId;
    /**
     * 評論類型 1全部 2視頻 3文本
     */
    public int commentType;
    public int commentChannel; // 貼文渠道 1全部 2店鋪 3商品 4貼文 5推文

    public String content;
    public int isLike; // 是否點讚
    public int commentLike; // 點讚數
    public int commentReply; // 回復數

    public String commenterAvatar;
    public String memberName;
    public String nickname; // 評論人的昵稱
    public String commentTime;
    public String imageUrl; // 評論的圖片

    public int relateCommonId;  // 相關的商品Id
    public int relateStoreId;   // 相關的店鋪Id
    public int replyCommentId;  // 回復評論Id
    public int relatePostId;    // 相關的貼文Id
    public int parentCommentId; // 一級評論ID

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
        memberName = in.readString();
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
        dest.writeString(memberName);
        dest.writeString(nickname);
        dest.writeString(commentTime);
        dest.writeString(imageUrl);

        dest.writeInt(relateCommonId);
        dest.writeInt(relateStoreId);
        dest.writeInt(replyCommentId);
        dest.writeInt(relatePostId);
        dest.writeInt(parentCommentId);
    }

    @NonNull
    @Override
    public String toString() {
        return String.format("commentId[%s], commentType[%s], commentChannel[%s], content[%s], isLike[%s], commentLike[%s], commentReply[%s], commenterAvatar[%s], memberName[%s], nickname[%s], commentTime[%s], imageUrl[%s], relateCommonId[%s], relateStoreId[%s], replyCommentId[%s], relatePostId[%s], parentCommentId[%s]",
                commentId, commentType, commentChannel, content, isLike, commentLike, commentReply, commenterAvatar, memberName, nickname, commentTime, imageUrl, relateCommonId, relateStoreId, replyCommentId, relatePostId, parentCommentId);
    }
}
