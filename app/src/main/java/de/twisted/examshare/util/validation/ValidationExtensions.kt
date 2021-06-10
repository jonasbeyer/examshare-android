package de.twisted.examshare.util.validation

import androidx.lifecycle.MutableLiveData

fun areFieldsValid(vararg fields: MutableLiveData<Validation>): Boolean {
    for (field in fields) {
        if (field.value?.result?.isValid != true)
            return false
    }

    return true
}

fun validateFormFields(
        errorMode: MutableLiveData<ErrorMode>,
        vararg fields: MutableLiveData<Validation>
): Boolean {
    val isFormValid = !fields.any { field -> field.value?.result?.isValid == false }
    if (!isFormValid) {
        errorMode.value = ErrorMode.Once(ErrorMode.OnUserInput)
    }

    return isFormValid
}