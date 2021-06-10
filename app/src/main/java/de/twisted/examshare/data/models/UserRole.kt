package de.twisted.examshare.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserRole(
    val id: Int,
    val name: String
) : Parcelable {

    val type: RoleType
        get() = RoleType.values().find { it.id == id } ?: RoleType.USER
}

enum class RoleType(val id: Int) {
    USER(0),
    MODERATOR(1),
    ADMIN(2)
}