package com.gzp.lib_common.model

import android.os.Parcel
import android.os.Parcelable
import com.orhanobut.hawk.Hawk

data class User(
        val userId:Int=0,
        val token:String,
        val nickname:String,
        val lastLoginTime:Int=0,
        val memberToken:String
):Parcelable {
    constructor(parcel: Parcel) : this(
            parcel.readInt(),
            parcel.readString()!!,
            parcel.readString()!!,
            parcel.readInt(),
            parcel.readString()!!) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(userId)
        parcel.writeString(token)
        parcel.writeString(nickname)
        parcel.writeInt(lastLoginTime)
        parcel.writeString(memberToken)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        var userId=0
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }

        fun getUserInfo(fieldMemberName: String, nothing: String?): String? {
            return if (userId == 0) {
                nothing
            } else {
                Hawk.get(fieldMemberName,nothing)
            }
        }

        @JvmStatic
        fun logout() {
            // TODO: 2020/9/14 补充登出处理 、
        }
    }
}