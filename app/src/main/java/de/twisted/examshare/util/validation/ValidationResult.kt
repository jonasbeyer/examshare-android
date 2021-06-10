package de.twisted.examshare.util.validation

import de.twisted.examshare.util.validation.rules.ValidationRule

data class ValidationResult(
    val text: String,
    val errorMessage: String? = null,
    val isValid: Boolean = false
)

data class Validation(
    val rules: List<ValidationRule>,
    val result: ValidationResult? = null
)