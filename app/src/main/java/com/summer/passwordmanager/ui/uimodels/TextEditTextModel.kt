package com.summer.passwordmanager.ui.uimodels

import android.text.InputFilter
import androidx.core.widget.addTextChangedListener
import androidx.databinding.BaseObservable
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputEditText
import com.summer.passwordmanager.utils.FilterUtils

data class TextEditTextModel(
    var fieldType: TextEditTextFieldType = TextEditTextFieldType.FULL_NAME,
    var isRequired: Boolean = false,
) :
    BaseObservable() {
    var editTextContent: String? = null

    fun validate(): Boolean {
        return when (fieldType) {
            TextEditTextFieldType.FULL_NAME -> {
                (editTextContent?.length ?: 0) > 2
            }

            TextEditTextFieldType.MOBILE_NUMBER -> {
                (editTextContent?.length ?: 0) > 10
            }
        }
    }

    companion object {
        @BindingAdapter("setUpTextEditText")
        @JvmStatic
        fun TextInputEditText.setUpTextEditText(model: TextEditTextModel) {
            //validations filter
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

            //listeners
            this.addTextChangedListener {
                model.editTextContent = it?.trim()?.toString()
                this.error = null
            }
        }
    }
}