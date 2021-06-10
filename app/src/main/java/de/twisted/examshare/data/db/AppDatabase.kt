package de.twisted.examshare.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import de.twisted.examshare.data.models.Subject
import de.twisted.examshare.data.models.Task
import de.twisted.examshare.data.subject.SubjectDao
import de.twisted.examshare.data.task.TaskDao
import de.twisted.examshare.util.room.Converters

@Database(
        entities = [Subject::class, Task::class],
        version = 3,
        exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase: RoomDatabase() {

    abstract fun subjectDao(): SubjectDao

    abstract fun taskDao(): TaskDao
}