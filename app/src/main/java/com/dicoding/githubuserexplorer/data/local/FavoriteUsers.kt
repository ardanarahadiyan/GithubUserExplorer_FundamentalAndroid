package com.dicoding.githubuserexplorer.data.local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class FavoriteUsers (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "username")
    var username : String = "",

    @ColumnInfo(name = "avatarUrl")
    var avatarUrl : String? = "",

    @ColumnInfo(name = "githubUrl")
    var githubUrl : String? = ""
): Parcelable