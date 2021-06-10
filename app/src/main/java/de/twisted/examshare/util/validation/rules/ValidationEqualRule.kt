package de.twisted.examshare.util.validation.rules

import android.text.TextUtils
import android.widget.TextView
import de.twisted.examshare.R
import de.twisted.examshare.util.validation.ValidationException

class ValidationEqualRule(private val view: TextView) : ValidationRule {

    override fun validate(text: String) {
        if (!TextUtils.equals(text, view.text)) throw EqualException()
    }

    override fun equals(other: Any?): Boolean = other is ValidationEqualRule

    override fun hashCode(): Int = javaClass.hashCode()
}

class EqualException : ValidationException(R.string.passwords_not_equal)