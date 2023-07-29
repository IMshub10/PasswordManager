package com.summer.passwordmanager.utils

import android.text.SpannableString
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.res.ResourcesCompat
import androidx.core.text.buildSpannedString
import androidx.core.text.color
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout
import com.summer.passwordmanager.R


object ViewConvertors {
    @BindingAdapter("setSpannableText")
    @JvmStatic
    fun AppCompatTextView.setSpannableText(
        spannableString: SpannableString,
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
        if (text == null) return
        text = buildSpannedString {
            append(text)
            if (text[text.lastIndex] != '*')
                color(resources.getColor(R.color.error)) { append("*") }
        }
    }

    @BindingAdapter("setSelectableBackground")
    @JvmStatic
    fun View.setSelectableBackground(isSelected: Boolean) {
        this.setBackgroundResource(if (isSelected) R.drawable.ripple_rounded_rect_stroke_primary_radius_small else R.drawable.ripple_rounded_rect_color_label_small_corners)
    }

    @BindingAdapter("setSelectableTextColor")
    @JvmStatic
    fun AppCompatTextView.setSelectableTextColor(isSelected: Boolean) {
        if (isSelected) {
            this.setTextColor(ResourcesCompat.getColor(context.resources, R.color.blue_light, null))
        } else {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(R.attr.label, typedValue, true)
            @ColorInt val color: Int = typedValue.data
            this.setTextColor(color)
        }
    }

    @BindingAdapter("setIconTextColor")
    @JvmStatic
    fun AppCompatTextView.setIconTextColor(isRed: Boolean) {
        if (isRed) {
            this.setTextColor(ResourcesCompat.getColor(context.resources, R.color.red_light, null))
        } else {
            val typedValue = TypedValue()
            context.theme.resolveAttribute(R.attr.label, typedValue, true)
            @ColorInt val color: Int = typedValue.data
            this.setTextColor(color)
        }
    }


    @BindingAdapter("viewTagSelectableBackground")
    @JvmStatic
    fun View.viewTagSelectableBackground(isSelected: Boolean) {
        this.setBackgroundResource(if (isSelected) R.drawable.ripple_rounded_rect_primary_solid_medium_corners else R.drawable.ripple_rounded_rect_stroke_white_radius_medium)
    }
}