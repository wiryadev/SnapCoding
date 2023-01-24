package com.wiryadev.snapcoding.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.InputType
import android.text.method.PasswordTransformationMethod
import android.util.AttributeSet
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wiryadev.snapcoding.R

class SnapCodingPasswordEditText : TextInputEditText {

    private var errorDrawable: Drawable? = null
    private var parentInputLayout: TextInputLayout? = null

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        parentInputLayout = parent.parent as TextInputLayout
    }

    private fun init() {
        inputType = InputType.TYPE_TEXT_VARIATION_PASSWORD

        errorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_exclamation)

        transformationMethod = PasswordTransformationMethod.getInstance()

        doOnTextChanged { text, _, _, _ ->
            if (text.toString().length < 8) {
                showError()
            } else {
                hideError()
            }
        }
    }

    private fun showError() {
        parentInputLayout?.apply {
            error = resources.getString(R.string.error_field_length)
            errorIconDrawable = errorDrawable
        }
    }

    private fun hideError() {
        parentInputLayout?.apply {
            error = null
            errorIconDrawable = null
        }
    }
}