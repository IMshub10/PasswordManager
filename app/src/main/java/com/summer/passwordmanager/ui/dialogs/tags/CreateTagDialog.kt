package com.summer.passwordmanager.ui.dialogs.tags

import android.os.Bundle
import android.view.View
import com.summer.passwordmanager.R
import com.summer.passwordmanager.base.ui.BaseDialogFragment
import com.summer.passwordmanager.database.entities.TagEntity
import com.summer.passwordmanager.databinding.DialogCreateTagBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CreateTagDialog(
    private val dismissListener: DismissLister,
    private val tagEntity: TagEntity? = null
) : BaseDialogFragment<DialogCreateTagBinding>() {

    override val layoutResId: Int
        get() = R.layout.dialog_create_tag

    private val viewModel: CreateTagViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.tagEntity = tagEntity
        viewModel.setUpInputModels()
    }

    override fun onFragmentReady(instanceState: Bundle?) {
        mBinding.model = viewModel
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listeners()
    }

    private fun listeners() {
        with(mBinding) {
            tvDialogCreateTagCancelButton.setOnClickListener {
                dismiss()
            }
            tvDialogCreateTagConfirmButton.setOnClickListener {
                if (validate()) {
                    dismiss()
                    dismissListener.onSave(
                        viewModel.toSaveTagEntity()
                    )
                }
            }
        }
    }

    private fun validate(): Boolean {
        if (!viewModel.tagNameEditTextModel.validate()) {
            mBinding.etDialogCreateTagTagName.tilEditTextEdit.error =
                getString(R.string.invalid_input)
            return false
        }
        if (!viewModel.tagDescriptionEditTextModel.validate()) {
            mBinding.etDialogCreateTagTagDescription.tilEditTextEdit.error =
                getString(R.string.invalid_input)
            return false
        }
        return true
    }

    interface DismissLister {
        fun onSave(tagEntity: TagEntity)
    }
}