package de.twisted.examshare.data.subject

import de.twisted.examshare.data.models.Subject
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SubjectRepository @Inject constructor(
    private val subjectService: RestSubjectService,
    private val subjectDao: SubjectDao
) {

    fun getSubjects(): Single<List<Subject>> {
        return subjectService.getSubjects()
            .flatMap { subjects ->
                subjectDao
                    .insertSubjects(subjects)
                    .andThen(Single.just(subjects))
            }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun updateNotificationPreference(subject: Subject): Completable {
        return subjectService
            .updateSubjectNotificationsEnabled(subject.id, subject.notificationsEnabled)
            .andThen(subjectDao.updateSubject(subject))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
    }

    fun observeSubjects(category: String): Observable<List<Subject>> {
        return subjectDao
            .observeSubjects(category)
            .observeOn(AndroidSchedulers.mainThread())
    }
}