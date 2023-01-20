package com.wiryadev.snapcoding.ui.components

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Patterns
import android.widget.EditText
import androidx.core.content.ContextCompat
import androidx.core.widget.doOnTextChanged
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.wiryadev.snapcoding.R

class SnapCodingEmailEditText : TextInputEditText {

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
        parentInputLayout = findViewById<EditText>(id).parent.parent as TextInputLayout
    }

    private fun init() {
        errorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_exclamation)
        doOnTextChanged { text, _, _, _ ->
            if (isValidEmail(text.toString())) {
                hideError()
            } else {
                showError()
            }
        }
    }

    private fun isValidEmail(email: String): Boolean {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showError() {
        parentInputLayout?.apply {
            error = resources.getString(R.string.error_email_invalid)
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