package de.twisted.examshare.data.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import kotlinx.android.parcel.IgnoredOnParcel
import kotlinx.android.parcel.Parcelize
import java.io.File
import java.util.*

@Parcelize
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey
    val id: String,
    val subject: String,
    val subjectId: String,
    val title: String,
    val keywords: List<String>,
    val author: String,
    val authorId: String,
    var grade: Int,
    var creator: String?,
    var schoolForm: String,
    var federalState: String,
    val taskImagesCount: Int,
    val solutionImagesCount: Int,
    val rating: Float,
    val ratingCount: Int,
    val isFavorite: Boolean,
    val isRated: Boolean,
    val createdAt: Date,
    val updatedAt: Date
) : Parcelable {

//        fun rate(rating: Double) {
//        setRated(true)
//        setRatingCount(getRatingCount() + 1)
//        setRating(rating)
//    }

    constructor(subjectId: String, title: String, keywords: List<String>) :
        this("", "", subjectId, title, keywords, "", "", 0, "", "", "", 0, 0, 0F, 0, false, false, Date(), Date())

    @Ignore
    @IgnoredOnParcel
    var taskFiles: List<File>? = null

    @Ignore
    @IgnoredOnParcel
    var solutionFiles: List<File>? = null

    val keywordsString: String
        get() {
            val keywordsString = keywords.toString().trim { it <= ' ' }
            return keywordsString.substring(1, keywordsString.length - 1)
        }

    fun isSubmittedBy(userId: String?): Boolean = authorId == userId
}