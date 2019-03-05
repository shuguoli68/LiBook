package com.li.libook.model.entity

import android.os.Parcel
import android.os.Parcelable

class CatBook(var id:String, var res:String, var title:String, var author:String, var retentionRatio:Double, var lastChapter:String,var shortIntro:String,var latelyFollower:Int,var tags:String

):Parcelable{
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(res)
        parcel.writeString(title)
        parcel.writeString(author)
        parcel.writeDouble(retentionRatio)
        parcel.writeString(lastChapter)
        parcel.writeString(shortIntro)
        parcel.writeInt(latelyFollower)
        parcel.writeString(tags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<CatBook> {
        override fun createFromParcel(parcel: Parcel): CatBook {
            return CatBook(parcel)
        }

        override fun newArray(size: Int): Array<CatBook?> {
            return arrayOfNulls(size)
        }
    }

}