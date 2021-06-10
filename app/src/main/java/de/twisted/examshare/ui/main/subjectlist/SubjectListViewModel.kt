package de.twisted.examshare.ui.main.subjectlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Subject
import de.twisted.examshare.data.subject.SubjectRepository
import de.twisted.examshare.ui.shared.base.BaseViewModel
import de.twisted.examshare.util.extensions.setValueIfNew
import de.twisted.examshare.util.result.Event
import io.reactivex.rxkotlin.subscribeBy
import timber.log.Timber
import javax.inject.Inject

class SubjectListViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
) : BaseViewModel(), SubjectActions {

    private val _category = MutableLiveData<String>()

    private val _subjectList = MediatorLiveData<List<Subject>>()
    val subjectList: LiveData<List<Subject>>
        get() = _subjectList

    private val _navigateToSubjectDetails = MutableLiveData<Event<Subject>>()
    val navigateToSubjectDetails: LiveData<Event<Subject>>
        get() = _navigateToSubjectDetails

    private val _notificationUpdated = MutableLiveData<Event<Int>>()
    val notificationUpdated: LiveData<Event<Int>>
        get() = _notificationUpdated

    init {
        // Observe subject list
        _subjectList.addSource(_category) { category ->
            Timber.d("Observing subjects for category $category")
            observeSubjects(category)
        }
    }

    fun setCategory(category: String) {
        _category.setValueIfNew(category)
    }

    private fun observeSubjects(category: String) {
        compositeDisposable.add(subjectRepository
            .observeSubjects(category)
            .subscribe(_subjectList::setValue))
    }

    override fun openSubjectDetails(subject: Subject) {
        _navigateToSubjectDetails.value = Event(subject)
    }

    override fun updateNotificationPreference(subject: Subject) {
        val newNotificationState = !subject.notificationsEnabled
        val stringResId = if (newNotificationState) {
            R.string.notifications_enabled
        } else {
            R.string.notifications_disabled
        }

        compositeDisposable.add(subjectRepository.updateNotificationPreference(
            subject.copy(notificationsEnabled = newNotificationState)
        ).subscribeBy(
            onComplete = { emitSnackbarMessage(stringResId) },
            onError = { emitSnackbarMessage(R.string.an_error_occurred) }
        ))
    }

    private fun emitSnackbarMessage(resId: Int) {
        _notificationUpdated.value = Event(resId)
    }
}