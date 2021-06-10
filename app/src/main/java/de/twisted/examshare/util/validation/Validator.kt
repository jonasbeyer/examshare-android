package de.twisted.examshare.util.validation

import android.text.Editable
import android.text.TextWatcher
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import de.twisted.examshare.util.validation.rules.ValidationRule
import java.lang.ref.WeakReference
import java.util.concurrent.CopyOnWriteArraySet

class Validator(
        editText: TextInputEditText,
        mode: ErrorMode = ErrorMode.None
) : TextWatcher {

    private val editTextWeek: WeakReference<TextInputEditText> = WeakReference(editText)
    private val rules = CopyOnWriteArraySet<ValidationRule>()

    lateinit var validationResult: ValidationResult

    var callback: OnValidation? = null
    var errorMode: ErrorMode = mode

    val validation: Validation
        get() = Validation(rules.toList(), validationResult)

    init {
        editText.addTextChangedListener(this)
        validateInput(editText, mode == ErrorMode.Always)
    }

    fun setRules(validationRules: List<ValidationRule>) {
        rules.clear()
        rules.addAll(validationRules)
    }

    fun validateInput(ignoreFocus: Boolean = false) {
        editTextWeek.get()?.let { edit ->
            validateInput(edit, when (errorMode) {
                ErrorMode.None -> false
                ErrorMode.OnUserInput -> edit.hasFocus() || ignoreFocus
                ErrorMode.Always -> true
                is ErrorMode.Once -> true
            })
        }
    }

    fun validateInput(editText: TextInputEditText, shouldShowError: Boolean = false) {
        hideError()
        var errorText: String? = null
        val resources = editText.context.resources
        val text = editText.text.toString()
        var isValidText = true

        rules.forEach { rule ->

            try {
                rule.validate(text)
            } catch (e: ValidationException) {
                isValidText = false
                errorText = resources.getString(e.resId)
                return@forEach
            }
        }

        showErrorOnView(errorText, shouldShowError)


        if (errorMode is ErrorMode.Once) {
            errorMode = (errorMode as ErrorMode.Once).nextMode
        }

        validationResult = ValidationResult(text, errorText, isValidText)
        callback?.onValidation(validationResult)
    }

    private fun showErrorOnView(errorText: String?, shouldShowError: Boolean) =
            editTextWeek.get()?.let { editText ->
                if (!shouldShowError) return@let

                if (editText.parent?.parent is TextInputLayout) {
                    (editText.parent?.parent as TextInputLayout).error = errorText
                } else {
                    editText.error = errorText
                }
            }


    private fun hideError() = editTextWeek.get()?.let { editText ->
        if (editText.parent?.parent is TextInputLayout) {
            val textInputLayout = editText.parent?.parent as TextInputLayout

            textInputLayout.error = null
            textInputLayout.isErrorEnabled = false
        } else {
            editText.error = null
        }
    }

    override fun afterTextChanged(s: Editable) {
        validateInput()
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
    }

    fun putRule(rule: ValidationRule) {
        rules.add(rule)
    }
}

interface OnValidation {
    fun onValidation(validateResult: ValidationResult)
}