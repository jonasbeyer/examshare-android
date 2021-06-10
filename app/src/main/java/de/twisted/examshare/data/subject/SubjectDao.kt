package de.twisted.examshare.data.subject

import androidx.room.*
import de.twisted.examshare.data.models.Subject
import io.reactivex.Completable
import io.reactivex.Observable

@Dao
interface SubjectDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSubjects(subjects: List<Subject>): Completable

    @Update
    fun updateSubject(subject: Subject): Completable

    @Query("SELECT * FROM subjects WHERE category = :category")
    fun observeSubjects(category: String): Observable<List<Subject>>
}