package de.twisted.examshare.ui.shared.widgets

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputLayout
import de.twisted.examshare.R

class ValidationTextInputLayout : TextInputLayout {

    private var textWatcher: TextWatcher? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(
        context: Context,
        attrs: AttributeSet,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        editText?.addTextChangedListener(textWatcher)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        editText?.removeTextChangedListener(textWatcher)
    }

    private fun init() {
        errorIconDrawable = null
        textWatcher = object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {}
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                validate()
            }
        }
    }

    fun validate(): Boolean {
        val valid: Boolean = editText?.text?.isNotBlank() == true
        if (!valid) {
            error = context.getString(R.string.missing_value)
        } else {
            error = null
            isErrorEnabled = false
        }

        return valid
    }
}
