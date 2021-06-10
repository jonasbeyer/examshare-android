package de.twisted.examshare.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import de.twisted.examshare.R
import de.twisted.examshare.data.models.Subject
import de.twisted.examshare.data.subject.SubjectRepository
import de.twisted.examshare.ui.shared.base.BaseViewModel
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val subjectRepository: SubjectRepository
) : BaseViewModel() {

    private val _categories = MediatorLiveData<List<String>>()
    val categories: LiveData<List<String>>
        get() = _categories

    private val _dataLoadingError = MutableLiveData<Int>()
    val dataLoadingError: LiveData<Int>
        get() = _dataLoadingError

    init {
        fetchSubjects()
    }

    private fun fetchSubjects() {
        compositeDisposable.add(subjectRepository.getSubjects()
            .doOnSubscribe { setLoading(true) }
            .doAfterTerminate { setLoading(false) }
            .subscribe(::onFetchSubjectsSuccess, ::onFetchSubjectsError))
    }

    private fun onFetchSubjectsSuccess(subjectList: List<Subject>) {
        _categories.value = subjectList
            .map { subject -> subject.category }
            .distinct()
    }

    private fun onFetchSubjectsError(throwable: Throwable) {
        _dataLoadingError.value = R.string.connection_error
    }
}