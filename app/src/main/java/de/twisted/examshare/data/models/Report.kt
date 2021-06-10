package de.twisted.examshare.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class Report(
    val id: String,
    val author: String,
    val reason: String,
    val createdAt: Date,
    val updatedAt: Date
) : Parcelable