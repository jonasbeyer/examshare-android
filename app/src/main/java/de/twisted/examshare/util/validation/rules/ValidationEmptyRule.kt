package de.twisted.examshare.util.validation.rules

import de.twisted.examshare.R
import de.twisted.examshare.util.validation.ValidationException

class ValidationEmptyRule : ValidationRule {

    override fun validate(text: String) {
        if (text.isBlank()) {
            throw EmptyException()
        }
    }

    override fun equals(other: Any?): Boolean = other is ValidationEmailRule

    override fun hashCode(): Int = javaClass.hashCode()
}

class EmptyException : ValidationException(R.string.missing_value)