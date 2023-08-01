package com.summer.passwordmanager.ui.dialogs

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.RelativeLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.summer.passwordmanager.R
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.databinding.DialogCreateTagBinding
import com.summer.passwordmanager.ui.uimodels.TextEditTextFieldType
import com.summer.passwordmanager.ui.uimodels.TextEditTextModel
import com.summer.passwordmanager.utils.AppUtils
import com.summer.passwordmanager.utils.UiUtils

class CreateTagDialog(
    private val dismissListener: DismissLister,
    private val tagEntity: TagEntity? = null
) : DialogFragment() {

    private var tagNameEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.TAG_NAME, isRequired = true)
    private var tagDescriptionEditTextModel =
        TextEditTextModel(fieldType = TextEditTextFieldType.TAG_DESCRIPTION, isRequired = false)

    private var mBinding: DialogCreateTagBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        isCancelable = false
        mBinding = DialogCreateTagBinding.inflate(layoutInflater)
        setUpInputModels()
        mBinding?.tagNameModel = tagNameEditTextModel
        mBinding?.tagDescriptionModel = tagDescriptionEditTextModel
        return mBinding?.root
    }

    private fun setUpInputModels() {
        tagNameEditTextModel.editTextContent = tagEntity?.name ?: ""
        tagDescriptionEditTextModel.editTextContent = tagEntity?.description ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
    }

    private fun listeners() {
        mBinding?.run {
            tvDialogCreateTagCancelButton.setOnClickListener {
                dismiss()
            }
            tvDialogCreateTagConfirmButton.setOnClickListener {
                if (validate()) {
                    dismiss()
                    dismissListener.onSave(
                        tagEntity?.apply {
                            name = tagNameEditTextModel.editTextContent ?: ""
                            description = tagDescriptionModel?.editTextContent
                            updatedAtApp = AppUtils.getCurrentTimeSecs()
                        }
                            ?: TagEntity(
                                name = tagNameEditTextModel.editTextContent ?: "",
                                description = tagDescriptionModel?.editTextContent
                            ).apply {
                                createdAtApp = AppUtils.getCurrentTimeSecs()
                                updatedAtApp = AppUtils.getCurrentTimeSecs()
                            }
                    )
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (!tagNameEditTextModel.validate()) {
            mBinding?.etDialogCreateTagTagName?.tilEditTextEdit?.error =
                getString(R.string.invalid_input)
            return false
        }
        if (!tagDescriptionEditTextModel.validate()) {
            mBinding?.etDialogCreateTagTagDescription?.tilEditTextEdit?.error =
                getString(R.string.invalid_input)
            return false
        }
        return true
    }

    interface DismissLister {
        fun onSave(tagEntity: TagEntity)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?) = Dialog(requireActivity()).apply {
        window?.attributes?.windowAnimations = R.style.SpannableDialogAnimation
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(RelativeLayout(requireActivity()).apply {
            setBackgroundColor(
                ContextCompat.getColor(
                    requireContext(), android.R.color.transparent
                )
            )
            layoutParams = ViewGroup.LayoutParams(
                UiUtils.getScreenWidthIntDp(requireActivity()) - 160,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        })
        window?.setBackgroundDrawableResource(android.R.color.transparent)
        window?.setLayout(
            UiUtils.getScreenWidthIntDp(requireActivity()) - 128,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}