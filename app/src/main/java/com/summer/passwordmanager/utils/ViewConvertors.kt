package com.summer.passwordmanager.utils

import android.text.SpannableString
import androidx.appcompat.widget.AppCompatTextView
import androidx.databinding.BindingAdapter

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
}