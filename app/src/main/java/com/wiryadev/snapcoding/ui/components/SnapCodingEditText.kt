package com.wiryadev.snapcoding.ui.components

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import com.wiryadev.snapcoding.R

class SnapCodingEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.appcompat.R.attr.editTextStyle
) : AppCompatEditText(context, attrs, defStyleAttr) {

    private var errorDrawable: Drawable? = null

    init {
        errorDrawable = ContextCompat.getDrawable(context, R.drawable.ic_round_error_24)
        addTextChangedListener(
            onTextChanged = { _, _, _, count ->
                if (count < 6) {
                    showError()
                }
            },
            afterTextChanged = {
                if (it.toString().length > 6) {
                    hideError()
                }
            }
        )
    }

    override fun setError(error: CharSequence?, icon: Drawable?) {
        super.setError(error, icon)
        setCompoundDrawablesWithIntrinsicBounds(null, null, icon, null)
    }

    private fun showError() {
        setError(
            resources.getString(R.string.error_field_length),
            errorDrawable
        )
    }

    private fun hideError() {
        setError(null, null)
    }
}