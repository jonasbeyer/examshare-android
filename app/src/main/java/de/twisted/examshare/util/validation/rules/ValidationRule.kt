package de.twisted.examshare.util.validation.rules

import de.twisted.examshare.util.validation.ValidationException

interface ValidationRule {

    @Throws(ValidationException::class)
    fun validate(text: String)
}