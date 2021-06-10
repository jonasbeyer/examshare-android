package de.twisted.examshare.ui.addedittask

import de.twisted.examshare.ui.account.AccountViewModelDelegate
import de.twisted.examshare.ui.shared.base.BaseViewModel
import javax.inject.Inject

class AddEditViewModel @Inject constructor(
        private val accountViewModelDelegate: AccountViewModelDelegate
): BaseViewModel(), AccountViewModelDelegate by accountViewModelDelegate {

}