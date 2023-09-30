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
        if (!isRequired) {
            return true
        }
        return when (fieldType) {
            TextEditTextFieldType.FULL_NAME -> {
                (editTextContent?.length ?: 0) > 2
            }

            TextEditTextFieldType.MOBILE_NUMBER -> {
                (editTextContent?.length ?: 0) > 9 && editTextContent?.contains("+") ?: false
            }

            TextEditTextFieldType.VAULT_NAME -> {
                (editTextContent?.length ?: 0) > 2
            }

            TextEditTextFieldType.WEBSITE_ADDRESS -> {
                (editTextContent?.length ?: 0) > 5
            }

            TextEditTextFieldType.USERNAME_MOBILE -> {
                (editTextContent?.length ?: 0) > 5
            }

            TextEditTextFieldType.PASSWORD -> {
                (editTextContent?.length ?: 0) > 3
            }

            TextEditTextFieldType.NOTES -> {
                (editTextContent?.length ?: 0) > 2
            }

            TextEditTextFieldType.TAG_NAME -> {
                (editTextContent?.length ?: 0) > 2
            }

            TextEditTextFieldType.TAG_DESCRIPTION -> {
                (editTextContent?.length ?: 0) > 5
            }

            TextEditTextFieldType.EXPORT_FILE_NAME -> {
                (editTextContent?.length ?: 0) > 2
            }

            TextEditTextFieldType.EXPORT_KEY -> {
                (editTextContent?.length ?: 0) == 32
            }
        }
    }

    fun setModel(content: String) {
        editTextContent = content
        notifyChange()
    }

    fun reset() {
        editTextContent = ""
        notifyChange()
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

                TextEditTextFieldType.VAULT_NAME -> {
                    this.filters = arrayOf(
                        FilterUtils.alphaNumericWhiteSpaceFullStopFilter,
                        InputFilter.LengthFilter(40)
                    )
                }

                TextEditTextFieldType.WEBSITE_ADDRESS -> {
                    this.filters = arrayOf(
                        InputFilter.LengthFilter(200)
                    )
                }

                TextEditTextFieldType.USERNAME_MOBILE -> {
                    this.filters = arrayOf(
                        InputFilter.LengthFilter(200)
                    )
                }

                TextEditTextFieldType.PASSWORD -> {
                    this.filters = arrayOf(
                        InputFilter.LengthFilter(128)
                    )
                }

                TextEditTextFieldType.NOTES -> {
                    this.filters = arrayOf(
                        InputFilter.LengthFilter(300)
                    )
                }

                TextEditTextFieldType.TAG_NAME -> {
                    this.filters = arrayOf(
                        FilterUtils.alphaNumericWhiteSpaceFullStopFilter,
                        InputFilter.LengthFilter(25)
                    )
                }

                TextEditTextFieldType.TAG_DESCRIPTION -> {
                    this.filters = arrayOf(
                        FilterUtils.alphaNumericWhiteSpaceFullStopFilter,
                        InputFilter.LengthFilter(100)
                    )
                }

                TextEditTextFieldType.EXPORT_FILE_NAME -> {
                    this.filters = arrayOf(
                        FilterUtils.alphaNumericSpaceUnderscoreHyphenFilter,
                        InputFilter.LengthFilter(30)
                    )
                }

                TextEditTextFieldType.EXPORT_KEY -> {
                    this.filters = arrayOf(
                        InputFilter.LengthFilter(32)
                    )
                }
            }
            this.setText(model.editTextContent.orEmpty())
            //listeners
            this.addTextChangedListener {
                model.editTextContent = it?.trim()?.toString()
                this.error = null
            }
        }
    }
}