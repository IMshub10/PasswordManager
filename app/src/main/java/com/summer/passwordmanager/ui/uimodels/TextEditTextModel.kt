package com.summer.passwordmanager.ui.uimodels

import android.text.InputFilter
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.summer.passwordmanager.utils.FilterUtils

data class TextEditTextModel(
    var fieldType: TextEditTextFieldType = TextEditTextFieldType.FULL_NAME,
    var isRequired: Boolean =false
) :
    BaseObservable() {
    companion object {
        @BindingAdapter("setUpFilter")
        @JvmStatic
        fun TextInputEditText.setUpFilter(model: TextEditTextModel) {
            when (model.fieldType) {
                TextEditTextFieldType.FULL_NAME -> {
                    this.filters = arrayOf(
                        FilterUtils.alphaNumericWhiteSpaceFilter,
                        InputFilter.LengthFilter(20)
                    )
                }

                TextEditTextFieldType.MOBILE_NUMBER -> {
                    this.filters =
                        arrayOf(FilterUtils.universalPhoneFilter, InputFilter.LengthFilter(20))
                }
            }
        }
    }
}