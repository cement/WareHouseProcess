package com.changqing.warehouse.base;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/6/23 0023.
 */

public class Per implements Parcelable,Serializable{
    String name;
    String age;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.age);
    }

    public Per() {
    }

    protected Per(Parcel in) {
        this.name = in.readString();
        this.age = in.readString();
    }

    public static final Parcelable.Creator<Per> CREATOR = new Parcelable.Creator<Per>() {
        @Override
        public Per createFromParcel(Parcel source) {
            return new Per(source);
        }

        @Override
        public Per[] newArray(int size) {
            return new Per[size];
        }
    };
}
