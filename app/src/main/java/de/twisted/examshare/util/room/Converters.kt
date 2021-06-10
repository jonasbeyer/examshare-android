package de.twisted.examshare.util.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.*

class Converters {

    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun toTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun fromStringList(list: List<String?>?): String? {
        return gson.toJson(list)
    }

    @TypeConverter
    fun toStringList(listOfString: String?): List<String?>? {
        return gson.fromJson(listOfString, object : TypeToken<List<String?>?>() {}.type)
    }
}