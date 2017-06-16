package uk.ivanc.archi2017.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class User(
        val id: Long,
        val login: String,
        val url: String,
        @SerializedName("avatar_url") val avatarUrl: String,
        val name: String?,
        val email: String?,
        val location: String?
) : Parcelable {
    companion object {
        @JvmField val CREATOR: Parcelable.Creator<User> = object : Parcelable.Creator<User> {
            override fun createFromParcel(source: Parcel): User = User(source)
            override fun newArray(size: Int): Array<User?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(login)
        dest.writeString(url)
        dest.writeString(avatarUrl)
        dest.writeString(name)
        dest.writeString(email)
        dest.writeString(location)
    }
}