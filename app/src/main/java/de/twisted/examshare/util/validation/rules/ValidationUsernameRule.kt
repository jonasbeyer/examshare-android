package de.twisted.examshare.util.validation.rules

import de.twisted.examshare.R
import de.twisted.examshare.util.validation.ValidationException

class ValidationUsernameRule : ValidationRule {

    override fun validate(text: String) {
        if (text.length < 4)
            throw ValidationException(R.string.username_too_short)

        if (!text.matches(Regex("^[a-zA-Z0-9_]+$")))
            throw ValidationException(R.string.username_only_specific_letters)

        if (!text.matches(Regex(".*?[a-zA-Z].*?.*?[a-zA-Z].*?")))
            throw ValidationException(R.string.username_two_letters_required)
    }

    override fun equals(other: Any?): Boolean = other is ValidationUsernameRule

    override fun hashCode(): Int = javaClass.hashCode()
}