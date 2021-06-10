package de.twisted.examshare.util.validation.rules

import android.util.Patterns
import de.twisted.examshare.R
import de.twisted.examshare.util.validation.ValidationException

class ValidationEmailRule : ValidationRule {

    override fun validate(text: String) {
        if (text.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(text).matches()) {
            throw EmailException()
        }
    }

    override fun equals(other: Any?): Boolean = other is ValidationEmailRule

    override fun hashCode(): Int = javaClass.hashCode()
}

class EmailException : ValidationException(R.string.missing_email)