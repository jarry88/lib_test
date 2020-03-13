package com.ftofs.twant.entity;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;

public class CommentItem implements Parcelable {
    public int commentId;
    /**
     * 說說類型 1全部 2視頻 3文本
     */
    public static final int COMMENT_ROLE_MEMBER=0;//會員
    public static final int COMMENT_ROLE_CS=3;//客服
    public static final int COMMENT_ROLE_BOSS=4;//老闆
    public int commentType;
    public int commentChannel; // 想要帖渠道 1全部 2商店 3產品 4想要帖 5推文

    public String content;
    public int isLike; // 是否讚想
    public int commentLike; // 讚想數
    public int commentReply; // 回覆數

    public String commenterAvatar;
    public String memberName;
    public String nickname; // 說說人的昵稱
    public String commentTime;
    public String imageUrl; // 說說的圖片
    public String [] images;

    public int relateCommonId;  // 相關的產品Id
    public int relateStoreId;   // 相關的商店Id
    public int replyCommentId;  // 回覆說說Id
    public int relatePostId;    // 相關的想要帖Id
    public String configurePostUrl;//想要帖封面圖
    public String postContent; //想要帖內容
    public String postTitle;
    public String postAuthorAvatar;
    public String postAuthorName;
    public int parentCommentId; // 一級說說ID
    public long date;         //說說發表時間
    public final  static int TYPE_POST = 3;
    public final static int TYPE_STORE = 2;
    public final static int TYPE_GOOD = 1;
    public int commentRole;

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
