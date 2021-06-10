package de.twisted.examshare.util.validation

import androidx.annotation.StringRes

open class ValidationException(
        @StringRes val resId: Int,
        vararg val arguments: Any?
) : Exception()