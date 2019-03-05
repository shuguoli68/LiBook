package com.li.libook.model.entity

import android.os.Parcel
import android.os.Parcelable

data class LocalBean(var name:String?,var path:String,var isSelect:Boolean,var isExist:Boolean) : Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(path)
        parcel.writeByte(if (isSelect) 1 else 0)
        parcel.writeByte(if (isExist) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<LocalBean> {
        override fun createFromParcel(parcel: Parcel): LocalBean {
            return LocalBean(parcel)
        }

        override fun newArray(size: Int): Array<LocalBean?> {
            return arrayOfNulls(size)
        }
    }
}