package com.ivancarballo.archi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Owner implements Parcelable {
    public long id;
    public String login;
    @SerializedName("avatar_url")
    public String avatarUrl;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.login);
        dest.writeString(this.avatarUrl);
    }

    public Owner() {
    }

    protected Owner(Parcel in) {
        this.id = in.readLong();
        this.login = in.readString();
        this.avatarUrl = in.readString();
    }

    public static final Parcelable.Creator<Owner> CREATOR = new Parcelable.Creator<Owner>() {
        public Owner createFromParcel(Parcel source) {
            return new Owner(source);
        }

        public Owner[] newArray(int size) {
            return new Owner[size];
        }
    };
}
