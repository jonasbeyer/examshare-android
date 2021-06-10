package de.twisted.examshare.data.task

import androidx.room.*
import de.twisted.examshare.data.models.Task
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(tasks: List<Task>): Completable

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOne(task: Task): Completable

    @Update
    fun updateOne(task: Task): Completable

    @Query("DELETE FROM tasks WHERE id = :taskId")
    fun deleteOne(taskId: String)

    @Query("SELECT * FROM tasks WHERE id = :taskId")
    fun observeOne(taskId: String): Observable<Task>

    @Query("SELECT * FROM tasks WHERE subjectId = :subjectId")
    fun observeAll(subjectId: String): Observable<List<Task>>
}