package de.twisted.examshare.util.validation

import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.databinding.InverseBindingListener
import com.google.android.material.textfield.TextInputEditText
import de.twisted.examshare.R
import de.twisted.examshare.util.extensions.addTextChangeListener
import de.twisted.examshare.util.validation.rules.ValidationEqualRule
import de.twisted.examshare.util.validation.rules.ValidationPasswordRule

@BindingAdapter("validation")
fun TextInputEditText.setValidation(validation: Validation) {
    setValidator(validation)
}


@InverseBindingAdapter(attribute = "validation", event = "texAttributeChange")
fun TextInputEditText.getResult(): Validation {
    val validator = getOrCreateValidator()
    return validator.validation
}

//@InverseBindingAdapter(attribute = "validationText", event = "texAttributeChange")
//fun TextInputEditText.getTextResult(): String {
//    val validator = getOrCreateValidator()
//    return validator.validationResult.text
//}

@BindingAdapter("texAttributeChange")
fun TextInputEditText.setInverseBindingListener(listener: InverseBindingListener?) {
    val validator = getOrCreateValidator()
    if (listener == null) {
        validator.callback = null
    } else {
        validator.callback = object : OnValidation {
            override fun onValidation(validateResult: ValidationResult) {
                if (getTag(R.id.validation_tag_result) != validateResult) {
                    setTag(R.id.validation_tag_result, validateResult)
                    listener.onChange()
                }
            }
        }
    }
}

@BindingAdapter("validationErrorMode")
fun TextInputEditText.setMode(mode: ErrorMode) {
    val validator = getOrCreateValidator()
    validator.errorMode = mode
    validator.validateInput()
}

//@BindingAdapter("showErrorMode")
//fun TextInputEditText.setMode(mode: ErrorModeConstant) {
//    val validator = getOrCreateValidator()
//    validator.errorMode = mode.mode
//    validator.validateInput()
//}

//@BindingAdapter(value = ["validationLengthMin", "validationLengthMax"], requireAll = false)
//fun TextInputEditText.setValidtionLength(min: Int?, max: Int?) {
//    val validator = getOrCreateValidator()
//    validator.putRule(ValidationPasswordRule(min, max))
//}

//@BindingAdapter("validationRule")
//fun TextInputEditText.setCustomRule(rule: ValidationRule) {
//    val validator = getOrCreateValidator()
//    validator.putRule(rule)
//    validator.validateInput()
//}

@BindingAdapter(value = ["validateEqual", "validateEqualOnBoth"], requireAll = false)
fun TextInputEditText.setValidateEqual(view: TextView, showErrorOnBoth: Boolean = false) {
    val validator = getOrCreateValidator()
    validator.putRule(ValidationEqualRule(view))

    if (view is EditText) {
        view.addTextChangeListener { validator.validateInput(showErrorOnBoth) }
    }
}

fun TextInputEditText.setValidator(
        validation: Validation
) {
    val validator = getOrCreateValidator()
    validator.setRules(validation.rules)

    if (!TextUtils.equals(text, validation.result?.text))
        setText(validation.result?.text)
}


fun TextInputEditText.validate(showResult: Boolean = true): ValidationResult {
    val validator = getOrCreateValidator()
    validator.validateInput(this, showResult)
    return validator.validationResult
}

@Synchronized
fun TextInputEditText.getOrCreateValidator(): Validator {
    var validator = getTag(R.id.validation_tag_validator) as? Validator
    if (validator == null) {
        validator = Validator(this)
        setTag(R.id.validation_tag_validator, validator)
    }

    return validator
}