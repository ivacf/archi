package com.ivancarballo.archi.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Repository implements Parcelable {
    public long id;
    public String name;
    public String description;
    public int forks;
    public int watchers;
    @SerializedName("stargazers_count")
    public int starts;
    public String language;
    public String homepage;
    public Owner owner;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.id);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.forks);
        dest.writeInt(this.watchers);
        dest.writeInt(this.starts);
        dest.writeString(this.language);
        dest.writeString(this.homepage);
        dest.writeParcelable(this.owner, 0);
    }

    public Repository() {
    }

    protected Repository(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.description = in.readString();
        this.forks = in.readInt();
        this.watchers = in.readInt();
        this.starts = in.readInt();
        this.language = in.readString();
        this.homepage = in.readString();
        this.owner = in.readParcelable(Owner.class.getClassLoader());
    }

    public static final Parcelable.Creator<Repository> CREATOR = new Parcelable.Creator<Repository>() {
        public Repository createFromParcel(Parcel source) {
            return new Repository(source);
        }

        public Repository[] newArray(int size) {
            return new Repository[size];
        }
    };
}
