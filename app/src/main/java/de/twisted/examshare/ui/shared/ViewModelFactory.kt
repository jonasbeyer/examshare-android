package de.twisted.examshare.ui.shared

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Provider

/**
 * This class is a view model factory to inject view models` constructor dependencies
 *
 * @param T                 the view model's type
 * @param viewModelProvider the view model provider injected by dagger
 */
class ViewModelFactory<T : ViewModel> @Inject constructor(private val viewModelProvider: Provider<T>) :
    ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return viewModelProvider.get() as T
    }
}