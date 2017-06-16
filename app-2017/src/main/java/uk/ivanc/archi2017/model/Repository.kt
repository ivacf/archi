package uk.ivanc.archi2017.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Repository(
        val id: Long,
        val name: String,
        val description: String?,
        val forks: Int,
        val watchers: Int,
        @SerializedName("stargazers_count") val stars: Int,
        val language: String?,
        val homepage: String?,
        val owner: User,
        @SerializedName("fork") val isFork: Boolean
) : Parcelable {

    companion object {
        @JvmField val CREATOR: Parcelable.Creator<Repository> = object : Parcelable.Creator<Repository> {
            override fun createFromParcel(source: Parcel): Repository = Repository(source)
            override fun newArray(size: Int): Array<Repository?> = arrayOfNulls(size)
        }
    }

    constructor(source: Parcel) : this(
            source.readLong(),
            source.readString(),
            source.readString(),
            source.readInt(),
            source.readInt(),
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readParcelable<User>(User::class.java.classLoader),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeLong(id)
        dest.writeString(name)
        dest.writeString(description)
        dest.writeInt(forks)
        dest.writeInt(watchers)
        dest.writeInt(stars)
        dest.writeString(language)
        dest.writeString(homepage)
        dest.writeParcelable(owner, 0)
        dest.writeInt((if (isFork) 1 else 0))
    }
}
