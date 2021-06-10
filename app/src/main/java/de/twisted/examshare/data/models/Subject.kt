package de.twisted.examshare.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "subjects")
data class Subject(
    @PrimaryKey
    val id: String,
    val name: String,
    val category: String,
    val notificationsEnabled: Boolean
) : Parcelable