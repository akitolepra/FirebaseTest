package com.example.firebasetest

import android.os.Parcel
import android.os.Parcelable

class MyRect() : Parcelable {

    var top: Int = 10
    private var bottom: Int = 20
    private var right: Int = 30
    private var left: Int = 40

    companion object CREATOR :Parcelable.Creator<MyRect>{

        override fun createFromParcel(source: Parcel): MyRect {
            return MyRect(source)
        }

        override fun newArray(size: Int): Array<MyRect> {
            return Array(size){MyRect()}
        }
    }

    private constructor(inParcel:Parcel):this(){
        readFromParcel(inParcel)
    }

    private fun readFromParcel(inParcel: Parcel){
        top = inParcel.readInt()
        bottom = inParcel.readInt()
        right = inParcel.readInt()
        left = inParcel.readInt()
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(top)
        dest.writeInt(bottom)
        dest.writeInt(right)
        dest.writeInt(left)
    }

    override fun describeContents(): Int {
        return 0
    }
}