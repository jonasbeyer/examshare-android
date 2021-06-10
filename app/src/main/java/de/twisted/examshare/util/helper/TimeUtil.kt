package de.twisted.examshare.util.helper

import android.content.Context
import android.text.format.DateUtils
import de.twisted.examshare.R
import java.util.*

object TimeUtil {

    private const val SECOND_MILLIS = 1000

    @JvmStatic
    fun getTimeAgo(context: Context, date: Date): String {
        val now = System.currentTimeMillis()
        val diff = now - date.time
        val message = context.getString(R.string.just_now)

        return if (diff < SECOND_MILLIS) {
            message
        } else {
            DateUtils.getRelativeTimeSpanString(
                date.time,
                now,
                DateUtils.SECOND_IN_MILLIS,
                DateUtils.FORMAT_ABBREV_RELATIVE
            ).toString()
        }
    }
}