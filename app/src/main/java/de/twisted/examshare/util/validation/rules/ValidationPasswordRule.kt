package de.twisted.examshare.util.validation.rules

import de.twisted.examshare.R
import de.twisted.examshare.util.validation.ValidationException

class ValidationPasswordRule : ValidationRule {

    override fun validate(text: String) {
        if (text.length < 6) {
            throw ValidationException(R.string.password_too_short)
        }
    }

    override fun equals(other: Any?): Boolean = other is ValidationPasswordRule

    override fun hashCode(): Int = javaClass.hashCode()
}