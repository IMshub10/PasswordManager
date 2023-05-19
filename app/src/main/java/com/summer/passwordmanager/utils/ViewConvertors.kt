package com.summer.passwordmanager.utils

import android.text.SpannableString
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.summer.passwordmanager.R

object ViewConvertors {
    @BindingAdapter("setSpannableText")
    @JvmStatic
    fun AppCompatTextView.setSpannableText(
        spannableString
        : SpannableString,
    ) {
        if (spannableString.toString().isEmpty()) this.gone() else this.visible()
        text = spannableString
    }

    @BindingAdapter("setRequired")
    @JvmStatic
    fun TextInputLayout.setRequired(isRequired: Boolean) {
        if (isRequired) makeRequired()
    }

    private fun TextInputLayout.makeRequired() {
        if (hint == null) return
        hint = buildSpannedString {
            append(hint)
            color(resources.getColor(R.color.error)) { append("*") }
        }
    }
    @BindingAdapter("setRequired")
    @JvmStatic
    fun AppCompatTextView.setRequired(isRequired: Boolean) {
        if (isRequired) makeRequired()
    }

    private fun AppCompatTextView.makeRequired() {
        if (hint == null) return
        hint = buildSpannedString {
            append(hint)
            color(resources.getColor(R.color.error)) { append("*") }
        }
    }
}